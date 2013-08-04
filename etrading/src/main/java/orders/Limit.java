package orders;

import java.util.LinkedList;
import java.util.List;

/* 
 * limit object is a node in the binary tree.
 * Each limit contains 1+ orders. Orders are placed in FIFO order.
 *  
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class Limit {
	private int limitPrice;
	private int size;
	private int totalVolume;
	
	private Limit left;
	private Limit right;
	
	//orders 
	private List<Order> orders = new LinkedList<Order>();

	public Limit(int limitPrice) {
		this.limitPrice = limitPrice;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(int totalVolume) {
		this.totalVolume = totalVolume;
	}

	public Limit getLeft() {
		return left;
	}
	
	public void setLeft(Limit left) {
		this.left = left;
	}
	
	public Limit getRight() {
		return right;
	}
	
	public void setRight(Limit right) {
		this.right = right;
	}
	
	public int getLimitPrice() {
		return limitPrice;
	}
	
	public void setLimitPrice(int limitPrice) {
		this.limitPrice = limitPrice;
	}

	public Order getOrder() {
		return orders.get(0);
	}
	
	public void setOrder(Order o) {
		orders.add(o);		
	}
	
	public List<Order> getOrders() {
		return orders;
	}
}
