package orders;

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
	
	private final static Map<String, OrderBook> orderbooks = new ConcurrentHashMap<>(); // ticker+side to order book mapping 
	private BlockingQueue<Order> buyQueue = new ArrayBlockingQueue<>(1000);
	private BlockingQueue<Order> sellQueue = new ArrayBlockingQueue<>(1000);
	
	private Thread buyBookThread;
	private Thread sellBookThread;
	
	public OrderExecutor() {
		buyBookThread = new Thread(new BookThread(buyQueue));
		sellBookThread = new Thread(new BookThread(sellQueue));
		buyBookThread.start();
		sellBookThread.start();
	}
	
	/*
	 * Process new order entered by the trader. 
	 * Call execution if this order crosses existing orders.
	 * Otherwise, queue up with other orders.
	 * @return orderId
	 */
	public int addLimitOrder(String ticker, OrderSide side, int shares, double limitPrice, long entryTime) throws InterruptedException {
		boolean buyOrSell = (side == OrderSide.BUY) ? true : false;
		
		if (logger.isDebug()) {
			logger.debug("processLimitOrder: ticker:" + ticker + ",buyOrSell:" + buyOrSell + ",shares:" + shares +
				",limitPrice:" + limitPrice + ",entryTime:" + entryTime);
		}
		
		Order order = new Order(ticker, buyOrSell, shares, (int) limitPrice * 100, entryTime);
		logger.debug("orderId:" + order.getId());
		
		if (buyOrSell) {
			buyQueue.put(order); 
		} else {
			sellQueue.put(order);
		}
		return order.getId();
	}
	
	public void printOrderBook(String ticker, OrderSide side) {
		boolean buyOrSell = (side == OrderSide.BUY) ? true : false;
		OrderBook book = getOrderBook(ticker, buyOrSell);
		book.printOrderBook();
	}
	
	/*
	 * Remove order from the queue, identified by the order id.
	 * Check if the order is partially executed.
	 * 
	 * @return orderId of cxl order, -1 if order not found.
	 */

	//private int cancel(String ticker, Order order) {
	//	return -1;
	//}

	/*
	 * Execute a trade.  Called twice when two orders cross
	 * one for buyer side and other with seller side.
	 * 
	 * @return number of shares executed
	 */

	//private int execution(String ticker, Order order) {
	//	return -1;
	//}
	
	private OrderBook getOrderBook(String ticker, boolean buyOrSell) {
		String key = ticker+buyOrSell;
		OrderBook book = orderbooks.get(key);
		if (book == null) {
			book = new OrderBook();
			orderbooks.put(key, book);
			logger.info("created orderBook for key:" + key);
		}
		return book;
	}
	
	private class BookThread implements Runnable {
		private BlockingQueue<Order> queue;
		
		public BookThread(BlockingQueue<Order> queue) {
			this.queue = queue;
		}
		
		public void run() {
			while (true) {
				Order order;
				try {
					order = queue.take();
					logger.info("add to orderBook orderId:" + order.getId());
					OrderBook book = getOrderBook(order.getTicker(), order.getBuyOrSell());
					Limit limit = book.insert(order.getLimitPrice());
					limit.setOrder(order);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

}
