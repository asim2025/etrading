package ats.algo;

import java.io.Serializable;

/*
 * Basic Order instruction
 * 
 * The object is immuatable and can't be modified after creation.
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public final class Order implements Serializable {
	final private static long serialVersionUID = 1L;
	
	final private String symbol;		// ticker
	final private int shares;			// original shares on the order
	final private double limitPrice;	// order price for limit orders
	final private Side side;
	final private OrderType orderType;
	
	enum Side {
		BUY,
		SELL
	}
	
	enum OrderType {
		MARKET,
		LIMIT
	}
	
	public Order(String symbol, int shares, Side side, OrderType type) {
		this(symbol, shares, side, type, 0D);
	}
	
	public Order(String symbol, int shares, Side side, OrderType type, double price) {
		this.symbol = symbol;
		this.shares = shares;
		this.side = side;
		this.orderType = type;
		this.limitPrice = price;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public Side getSide() {
		return side;
	}
	
	public int getShares() {
		return shares;
	}
	
	public double getPrice() {
		return limitPrice;
	}
	
	public OrderType getOrderType() {
		return orderType;
	}
	
	@Override
	public String toString() {
		return "Order [symbol=" + symbol + ", shares=" + shares + ", price="
				+ limitPrice + ", side=" + side + ", orderType=" + orderType + "]";
	}
	
}
