package ats.algo;

import java.util.Date;

/*
 * Algo input parameter container class.
 * 
 * Not all parameters are used by every strategy.
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class AlgoParameter {
	
	/*
	 * standard parameters
	 */
	private String symbol;				// ticker
	private Order.Side side;			// buy or sell
	private int totalShares;			// total shares
	private long totalTime;				// total time to complete order in milliseconds
	private long frequency; 			// how often order is placed in milliseconds
	
	/*
	 * VWAP specific
	 */
	private int[] histVolume;			// historical volume
	private double[] histPrice;			// historical price for traded volume
	private Date[] histStartDate;		// historical date range for volume/price
	private Date[] histEndDate;			// historical date range for volume/price
	
	
	
	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public Order.Side getSide() {
		return side;
	}
	
	public void setSide(Order.Side side) {
		this.side = side;
	}
	
	public int getTotalShares() {
		return totalShares;
	}
	
	public void setTotalShares(int totalShares) {
		this.totalShares = totalShares;
	}
	
	public long getTotalTime() {
		return totalTime;
	}
	
	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}
	
	public long getFrequency() {
		return frequency;
	}
	
	public void setFrequency(long frequency) {
		this.frequency = frequency;
	}

	public int[] getHistVolume() {
		return histVolume;
	}

	public void setHistVolume(int[] histVolume) {
		this.histVolume = histVolume;
	}

	public double[] getHistPrice() {
		return histPrice;
	}

	public void setHistPrice(double[] histPrice) {
		this.histPrice = histPrice;
	}

	public Date[] getHistStartDate() {
		return histStartDate;
	}

	public void setHistStartDate(Date[] histStartDate) {
		this.histStartDate = histStartDate;
	}

	public Date[] getHistEndDate() {
		return histEndDate;
	}

	public void setHistEndDate(Date[] histEndDate) {
		this.histEndDate = histEndDate;
	}

	
}
