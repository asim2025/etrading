package orders;

/*
 * Basic order / trade object
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class Order {
	private int id;				// unique order id
	private boolean buyOrSell;	// buy = true, sell = false
	private int shares;			// original shares on the order
	private int adjShares;		// adjusted shares if order is partially filled.
	private int limitPrice;		// limit price - divisable by 100 e.g. 123.10 is 12310
	private long entryTime;		// order placement time
	private long updateTime;	// last updated time

	private volatile static int GOID = 1;	// unique order id num - enhance for multiple jvms
	
	public Order(int id, boolean buyOrSell, int shares, int limitPrice, 
			long entryTime) {
		this.id= (id == -1) ? GOID++ : id;
		this.buyOrSell = buyOrSell;
		this.shares = shares;
		this.adjShares = shares;
		this.limitPrice = limitPrice;
		this.entryTime = entryTime;
		this.updateTime = System.currentTimeMillis();
	}
	
	
	public int getId() { return id; }
	public boolean getBuyOrSell() { return buyOrSell; }
	public int getShares() { return shares; }
	public int getAdjShares() { return adjShares; }
	public int getLimitPrice() { return limitPrice; }
	public long getEntryTime() { return entryTime; }
	public long getUpdateTime() { return updateTime; }
	

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
