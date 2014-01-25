package ats.algo;

/*
 * Algo input parameter container class
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class AlgoParameter {
	String symbol;				// ticker
	Order.Side side;			// buy or sell
	int totalShares;			// total shares
	long totalTime;				// total time to complete order in milliseconds
	long frequency; 			// how often order is placed in milliseconds

}
