package orders;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import common.Logger;

public class OrderExecutor {
	private final static Logger logger = Logger.getInstance(OrderExecutor.class);
	
	private final static Map<String, OrderBook> orderbooks = new ConcurrentHashMap<>(); // ticker+side to order book mapping 

	
	/*
	 * Process new order entered by the trader. 
	 * Call execution if this order crosses existing orders.
	 * Otherwise, queue up with other orders.
	 * @return orderId
	 */
	public int addLimitOrder(String ticker, boolean buyOrSell, int shares, double limitPrice, long entryTime) {
		logger.info("processLimitOrder: ticker:" + ticker + ",buyOrSell:" + buyOrSell + ",shares:" + shares +
				",limitPrice:" + limitPrice + ",entryTime:" + entryTime);
		

		OrderBook book = getOrderBook(ticker, buyOrSell);
		int limitPriceInt = (int) limitPrice * 100;
		Limit limit = book.insert(limitPriceInt);
		int orderId = limit.addOrder(buyOrSell, shares, limitPriceInt, entryTime);
		return orderId;
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
}
