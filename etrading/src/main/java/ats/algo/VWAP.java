package ats.algo;

import common.Logger;

/*
 * VWAP Algo - Volume Weighted Average Price
 * 
 * Split large order to smaller orders and send it over a period of time using VWAP  
 * 
 * inputs:
 *	a. historical profile
 * 	b. total quantity to be traded
 *	c. total trade window time
 *	d. frequency / interval / time slice
 *
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class VWAP implements Algo {
	private final static Logger log = Logger.getInstance(VWAP.class);
	
	private boolean cancel = false;
	
	@Override
	public void execute(AlgoParameter param) throws Exception {
		
		// step 1 - calculate target shares and price using T-1 historical profile
		// future TODO : calculate price that match traded intervals
		double price = vwap(param.getHistVolume(), param.getHistPrice());
		
		int totalShares = param.getTotalShares();
		long targetShares = (totalShares * param.getFrequency()) / param.getTotalTime();
		int tradedShares = 0;
		
		while (!cancel && tradedShares != totalShares) {
			// TODO: step 2 - compare target price with market price, use the one that is lower
			double targetPrice = price; 
			
			// step 3 - send order to market
			sendOrder(param.getSymbol(),
					param.getSide(),
					Order.OrderType.LIMIT,
					(int) targetShares,
					targetPrice
				);
			tradedShares += targetShares;
			log.info("shares left: " + (totalShares-tradedShares));
			Thread.sleep(param.getFrequency());
		}

	}

	@Override
	public void cancel() throws Exception {
		cancel = true;		
	}
	
	
	/*
	 * calculate and return vwap
	 */
	private double vwap(int[] volume, double[] price) {		
		double numenator = 0d;
		double denominator = 0d;
		
		for (int i = 0; i < volume.length; i++) {
			numenator += (volume[i] * price[i]);
			denominator += volume[i];
		}

		double vwap = numenator / denominator;
		return vwap;
	}
	
	
	/*
	 * send order to OMS to route to market
	 */
	private void sendOrder (String symbol, Order.Side side, Order.OrderType type, int shares, double price) {
		Order order = new Order(symbol, shares, side, type, price);
		log.info("sendOrder:" + order);
	}

}
