package orders;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class OrderMatchingEngine {
	private Map<String, OrderBook> orderbooks = new HashMap(); // ticker+side to order book mapping 
	
	private ArrayBlockingQueue<Order> buyOrders = new ArrayBlockingQueue(1000, true);
	
	public OrderMatchingEngine() {
	}
	
	/*
	 * Receive order instruction.
	 * Handle synchronization/multi-threaded.
	 */
	public void messageReceived(Object o) {
		String ticker = "IBM";
		String side = "B";
		int quantity = 100;
		int price = 10;
		OrderType type = OrderType.Limit;
		
	}
	
	
	/*
	 * Process new order entered by the trader. 
	 * Call execution if this order crosses existing orders.
	 * Otherwise, queue up with the other orders.
	 * @return orderId
	 */
	public int limit(OrderBook book) {
		return -1;
	}
	
	/*
	 * Remove order from the queue, identified by the order id.
	 * Check if the order is partially executed.
	 * 
	 * @return orderId of cxl order, -1 if order not found.
	 */

	public int cancel(String ticker, Order order) {
		return -1;
	}

	/*
	 * Execute a trade.  Called twice when two orders cross
	 * one for buyer side and other with seller side.
	 * 
	 * @return number of shares executed
	 */

	public int execution(String ticker, Order order) {
		return -1;
	}
	
	
}
