package orders;

import java.util.LinkedList;
import java.util.List;

/* 
 * limit stored in a binary tree data structure 
 * 
 */
public class Limit {
	private int limitPrice;
	//private int size;
	//private int totalVolume;
	
	private Limit left;
	private Limit right;
	
	//orders sorted by FIFO
	private List<Order> orders = new LinkedList<Order>();

	public Limit(int limitPrice, Limit left, Limit right) {
		this.limitPrice = limitPrice;
		this.left = left;
		this.right = right;
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
}
