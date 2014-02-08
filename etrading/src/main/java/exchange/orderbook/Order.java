package exchange.orderbook;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * Basic order / trade object
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ticker;		// ticker
	private int id;				// unique order id
	private int side;			// buy = 1, sell = 2 (FIX protocol tag 54)
	private int shares;			// original shares on the order
	private int adjShares;		// adjusted shares if order is partially filled.
	private int limitPrice;		// limit price - divisable by 100 e.g. 123.10 is 12310
	private long entryTime;		// order placement time
	private long updateTime;	// last updated time
	private int orderType;		// 1 = market, limit = 2 (FIX protocol tag 40)

	private static AtomicInteger GOID = new AtomicInteger(0);	// unique order id num - enhance for multiple jvms
	
	public Order(String ticker, int orderType, int side, int shares, int limitPrice, long entryTime) {
		this.id= GOID.incrementAndGet();
		this.ticker = ticker;
		this.side = side;
		this.orderType = orderType;
		this.shares = shares;
		this.adjShares = shares;
		this.limitPrice = limitPrice;
		this.entryTime = entryTime;
		this.updateTime = System.currentTimeMillis();
	}
	
	
	public int getId() { return id; }
	public int getSide() { return side; }
	public int getShares() { return shares; }
	public int getAdjShares() { return adjShares; }
	public int getLimitPrice() { return limitPrice; }
	public long getEntryTime() { return entryTime; }
	public long getUpdateTime() { return updateTime; }
	public String getTicker() { return ticker; }
	public int getOrderType() { return orderType; }

	// the caller ensures that adjusted shares can't result in a negative number
	public void adjustShares(int shares) {		
		this.adjShares = this.shares - shares; 
		updateTime = System.currentTimeMillis();
	}
	
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Order)) return false;
		return this.getId() == ((Order)o).getId();
	}

	public int hashCode() {
		return getId();
	}
	
	public String toString() {
		return "OrderId:" + getId();
	}
}
