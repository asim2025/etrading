package exchange.orderbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import common.Logger;

/*
 * Process and manage orders received from upstream.
 * Maintain a ticker level limit order book.
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class OrderExecutor {
	private final static Logger logger = Logger.getInstance(OrderExecutor.class);
	
	private final static Map<String, OrderBookEntry> orderbooks = new ConcurrentHashMap<>(); // ticker level order book 
	private BlockingQueue<Order> queue = new ArrayBlockingQueue<>(10000);
	
	private Thread executor;
	
	public OrderExecutor() {
		executor = new Thread(new BookThread(queue), "order-executor");
		executor.start();
	}
	
	/*
	 * Process new order entered by the trader. 
	 * 
	 * @return orderId
	 */
	public int addOrder(String ticker, OrderType orderType, OrderSide orderSide, int shares, 
			double limitPrice, long entryTime) throws InterruptedException {
		
		int side = getSide(orderSide);
		int type = getOrderType(orderType);
		
		if (logger.isDebug()) {
			logger.debug("addOrder: ticker:" + ticker + ",side:" + side + ",type:" + type +
					",shares:" + shares + ",limitPrice:" + limitPrice + ",entryTime:" + entryTime);
		}
		
		Order order = new Order(ticker, type, side, shares, (int) limitPrice * 100, entryTime);
		logger.debug("orderId:" + order.getId());
		
		queue.put(order); 
		return order.getId();
	}

	
	/*
	 * Remove order from the queue, identified by the order id.
	 * Check if the order is partially executed.
	 * 
	 * @return orderId of cxl order, -1 if order not found.
	 */
	/*public int cancel(String ticker, Order order) {
		return -1;
	}
	 */
	

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
			logger.info("orderBook for other side not found for ticker.side:" + ticker + side);
			return 0;
		}
		
		// find the order with best price and match and then remove order
		int shares = executeOrder(order, otherBook); // market or limit
		return shares;
	}
	
	
	private int executeOrder(Order order, OrderBook otherBook) {
		// find order in the otherbook
		Limit otherLimit = otherBook.search(order.getLimitPrice());
		
		if (otherLimit == null) {
			logger.info("otherBook does not contain limitPrice:" + order.getLimitPrice());
			return 0;
		}
		
		if (order.getShares() > otherLimit.getSize()) {
			logger.info("order size exceeds number of shares in the orderBook.  orderSize:" + 
					order.getShares() + ", orderBookSize:" + otherLimit.getSize());
			return 0;
		}
		
		List<Order> orders = new ArrayList<>();
		
		int shares = order.getShares();
		for (Order o : otherLimit.getOrders()) {
			int oshares = o.getShares();
			logger.info("processing order:" + o.getId() + ", shares:" + oshares);
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
			logger.info("removed order. status:" + status);
		}
		
		if (otherLimit.getOrders().size() == 0) {
			otherBook.delete(order.getLimitPrice());
		}
		return order.getShares();
	}
	
	
	public void printOrderBook(String ticker, OrderSide side) {
		OrderBook book = getOrderBook(ticker, getSide(side), false);
		if (book == null) {
			logger.error("book not found");
		} else {
			logger.info("order book:");
			book.printOrderBook();
		}
	}
	

	private OrderBook getOrderBook(String ticker, int side, boolean createIt) {
		OrderBookEntry entry = orderbooks.get(ticker);
		if (createIt && entry == null) {
			entry = new OrderBookEntry();
			orderbooks.put(ticker, entry);
			logger.info("created orderBook for key:" + ticker);
		}
		
		OrderBook book = (side == 1) ? entry.buy : entry.sell;
		return book; 
	}
	
	
	private int getSide(OrderSide orderSide) {
		int side = 0;
		switch (orderSide) {
		case BUY : side = 1; break;
		case SELL : side = 2; break;
		default: new RuntimeException("invalid side");
		}
		return side;
	}
	

	private int getOrderType(OrderType orderType) {
		int type = 0;
		switch (orderType) {
		case Market: type = 1; break;
		case Limit: type = 2; break;
		default: new RuntimeException("invalid order type");
		}
		return type;
	}

	
	private class BookThread implements Runnable {
		private BlockingQueue<Order> queue;
		
		public BookThread(BlockingQueue<Order> queue) {
			this.queue = queue;
		}
		
		public void run() {
			while (true) {
				try {
					Order order = queue.take();
					int orderId = order.getId();
					logger.info("processing orderId:" + orderId);
					
					OrderBook book = getOrderBook(order.getTicker(), order.getSide(), true);
					logger.info("matching orderId:" + orderId);
					if (execution(order, book) == order.getShares()) {
						logger.info("matched orderId:" + orderId);
						continue;
					}
					
					logger.info("adding to lob, orderId:" + orderId);
					Limit limit = book.insert(order.getLimitPrice());
					limit.setOrder(order);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
