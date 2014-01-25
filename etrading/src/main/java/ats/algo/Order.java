package ats.algo;

import java.io.Serializable;

/*
 * Basic Order object  
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String symbol;		// ticker
	private int shares;			// original shares on the order
	private double limitPrice;	// order price for limit orders
	private Side side;
	private OrderType orderType;
	
	enum Side {
		BUY,
		SELL
	}
	
	enum OrderType {
		MARKET,
		LIMIT
	}
	
	public Order(String symbol, int shares, Side side, OrderType type) {
		this.symbol = symbol;
		this.shares = shares;
		this.side = side;
		this.orderType = type;
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
	
	public void setPrice(double price) {
		this.limitPrice = price;
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
