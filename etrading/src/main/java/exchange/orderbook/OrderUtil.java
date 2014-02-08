package exchange.orderbook;

/*
 * Order Utility Functions
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class OrderUtil {
	
	public static int getSide(OrderSide orderSide) {
		int side = 0;
		switch (orderSide) {
		case BUY : side = 1; break;
		case SELL : side = 2; break;
		default: new RuntimeException("invalid side");
		}
		return side;
	}
	

	public static int getOrderType(OrderType orderType) {
		int type = 0;
		switch (orderType) {
		case Market: type = 1; break;
		case Limit: type = 2; break;
		default: new RuntimeException("invalid order type");
		}
		return type;
	}
	

}
