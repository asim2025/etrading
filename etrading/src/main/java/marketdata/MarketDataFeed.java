package marketdata;

import java.text.DecimalFormat;
import java.util.Random;

import common.Logger;

/*
 * Provide market data (randomly generated).
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class MarketDataFeed {
	private Logger logger = Logger.getInstance(MarketDataFeed.class);
	
	private Random random = new Random();
	
	public double getPrice(String ticker, int minPrice, int maxPrice) {
		int num = random.nextInt(maxPrice - 1 - minPrice + 1) + minPrice;
		double decimals = random.nextDouble();
		logger.debug("ticker:" + ticker + ", num:" + num + ", decimal:" + decimals);
		
		DecimalFormat df = new DecimalFormat("###.##");
		double price = Double.parseDouble( df.format( (double) num + decimals ) );		
		return price;
	}
}
