package exchange.orderbook;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import common.Logger;
import common.messaging.MessageConsumer;
import common.messaging.MessageListener;
import common.messaging.MessageProducer;

/*
 * Process and manage orders received from FIX Gateway Server.
 * Maintain a ticker level limit order book.
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class OrderMatchingService {
	private final static Logger log = Logger.getInstance(OrderMatchingService.class);
	private final static String ORDER_QUEUE = "order_queue";	// orders from FIX gateway
	private final static String DB_QUEUE = "db_queue";			// async db persistance queue 
	private final static String EXEC_QUEUE = "exec_queue";		// executed orders
	/*
	 * limit order books by ticker. 
	 * each order book consist of buy and sell sub-book.
	 */
	private final static Map<String, OrderBookEntry> orderbooks = new ConcurrentHashMap<>();
	
	/*
	 * All orders from upstream are placed in local queue and processed in time receive priority. 
	 */
	private ConcurrentLinkedQueue<Order> queue = new ConcurrentLinkedQueue<>();
	
	private Thread executor;
	private MessageConsumer orderConsumer;
	private MessageProducer dbProducer;
	private MessageProducer execProducer;
	
	public static void main(String[] args) throws Exception {
		OrderMatchingService executor = new OrderMatchingService();
		log.info("started...");
		executor.start();
	}
	
	
	public void start() throws IOException {
		dbProducer = new MessageProducer(DB_QUEUE);
		execProducer = new MessageProducer(EXEC_QUEUE);
		
		executor = new Thread(new OrderBookRunner(queue), "OrderBookRunner");
		executor.start();
		
		orderConsumer = new MessageConsumer(ORDER_QUEUE);
		orderConsumer.addListener(new MessageListener() {

			@Override
			public void onMessage(Object o) {
				addOrder( (Order) o );
			}
		});
	}
	
	/*
	 * Add order to internal queue.
	 */
	public void addOrder(Order order) {
		log.info("order recevied:" + order);
		queue.offer(order);
	}
	
	
	
	/*
	 * Execute a trade.  
	 * Handle limit and market orders.
	 * Support partial order matching.
	 * 
	 * @return number of shares executed
	 */
	private int execution(Order order, OrderBook book) {
		int side = order.getSide();
		String ticker = order.getTicker();
		
		// find other side order book
		OrderBook otherBook = getOrderBook(ticker, side == 1 ? 2 : 1, false);
		if (otherBook == null) {
			log.info("orderBook for other side not found for ticker.side:" + ticker + side);
			return 0;
		}
		
		// find the order with best price and match and then remove order
		int shares = executeOrder(order, otherBook); // market or limit
		return shares;
	}
	
	
	/*
	 * Match Buy and Sell orders for the same ticker.
	 * Return number of shares filled.
	 */
	private int executeOrder(Order order, OrderBook otherBook) {
		// find order in the otherbook
		Limit otherLimit = otherBook.search(order.getLimitPrice());
		
		if (otherLimit == null) {
			log.info("otherBook does not contain limitPrice:" + order.getLimitPrice());
			return 0;
		}
		
		if (order.getShares() > otherLimit.getSize()) {
			log.info("order size exceeds number of shares in the orderBook.  orderSize:" + 
					order.getShares() + ", orderBookSize:" + otherLimit.getSize());
			return 0;
		}
		
		List<Order> orders = new LinkedList<>();
		
		int shares = order.getShares();
		for (Order o : otherLimit.getOrders()) {
			int oshares = o.getShares();
			log.info("processing order:" + o.getId() + ", shares:" + oshares);
			if (shares <= oshares) {
				o.adjustShares(shares);
			} else {
				o.adjustShares(oshares);
			}
			if (o.getAdjShares()<=0) orders.add(o);
			shares = shares - oshares;
		}

		for (Order o : orders) {
			boolean status = otherLimit.removeOrder(o);
			log.info("removed order from LOB. status:" + status);
			execProducer.send(o);  // send to exec service
		}
		
		if (otherLimit.getOrders().size() == 0) {
			otherBook.delete(order.getLimitPrice());
		}
		return order.getShares();
	}
	
	
	/*
	 * Return Order Book for ticker/side combination
	 */
	private OrderBook getOrderBook(String ticker, int side, boolean createIt) {
		OrderBook book = null;
		OrderBookEntry entry = orderbooks.get(ticker);
		if (createIt && entry == null) {
			entry = new OrderBookEntry();
			orderbooks.put(ticker, entry);
			log.info("created orderBook for key:" + ticker);
		}
		
		book = (side == 1) ? entry.buy : entry.sell;
		return book; 
	}
	
	/*
	 * Print order book entries on std out
	 */
	public void printOrderBook(String ticker, OrderSide side) {
		OrderBook book = getOrderBook(ticker, OrderUtil.getSide(side), false);
		if (book == null) {
			log.error("book not found");
		} else {
			log.info("order book:");
			book.printOrderBook();
		}
	}



	
	private class OrderBookRunner implements Runnable {
		private ConcurrentLinkedQueue<Order> queue;
		
		public OrderBookRunner(ConcurrentLinkedQueue<Order> queue) {
			this.queue = queue;
		}
		
		public void run() {
			while (true) {
				Order order = queue.poll();
				if (order == null) continue; //spin
				
				int orderId = order.getId();
				log.info("processing orderId:" + orderId);
				dbProducer.send(order);
				
				OrderBook book = getOrderBook(order.getTicker(), order.getSide(), true);
				log.info("matching orderId:" + orderId);
				if (execution(order, book) == order.getShares()) {
					log.info("matched orderId:" + orderId);
					continue;
				}
				
				log.info("adding to lob, orderId:" + orderId);
				Limit limit = book.insert(order.getLimitPrice());
				limit.setOrder(order);
			}
		}
	}
	
	public static class OrderBookEntry {
		private OrderBook buy;
		private OrderBook sell;
		
		public OrderBookEntry() {
			buy = new OrderBook();
			sell = new OrderBook();
		}
	}
}
