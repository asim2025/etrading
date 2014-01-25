package ats.algo;

import common.Logger;


/*
 * Time Slice Algo
 * 
 * Split large order to smaller orders and send it over a period of time  
 * 
 * inputs:
 * 	a. total quantity to be traded
 *	b. total trade window time
 *	c. frequency / interval / time slice
 *
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class TimeSlice implements Algo {
	private final static Logger log = Logger.getInstance(TimeSlice.class);

	@Override
	public void execute(AlgoParameter param) throws Exception {
		int shares = param.totalShares;
		long targetShares = (shares * param.frequency) / param.totalTime;
		int tradedShares = 0;
		
		while (tradedShares != shares) {
			// send to Market targetShares
			sendOrder(param.symbol,
					param.side,
					Order.OrderType.MARKET,
					(int) targetShares
				);
					
			log.info("market order sent for qty:" + targetShares);
			tradedShares += targetShares;
			Thread.sleep(param.frequency);
		}		
	}

	
	/*
	 * send order to OMS to route to market
	 */
	private void sendOrder (String symbol, Order.Side side, Order.OrderType type, int shares) {
		Order order = new Order(symbol, shares, side, type);
		log.info("sendOrder:" + order);
	}


}