package orders;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import common.Logger;


/*
 * Test harness for OrderExecutor.
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class OrderExecutorTest {
	private Logger logger = Logger.getInstance(OrderExecutorTest.class);
	private OrderExecutor executor;
	
	@Before
	public void init() {
		executor = new OrderExecutor();
		logger.showReadableTime(true);
		logger.setDebug(true);
	}
	
	@Test
	public void addSingleLimitOrder() {
		String ticker = "IBM";
		boolean buyOrSell = true; //buy order
		int shares = 100;
		double limitPrice = 123.10;	
		long entryTime = System.nanoTime();	
		int orderId = executor.addLimitOrder(ticker, buyOrSell, shares, limitPrice, entryTime);
		logger.info("orderId:" + orderId);
		assertTrue(orderId != -1);
	}

	@Test
	public void addMultipleLimitOrder() {
		
		Random random = new Random();
		logger.setDebug(false);
		
		for (int i = 0; i < 10000; i++) {
			int orderId = executor.addLimitOrder(
					"IBM", 
					random.nextBoolean(), 
					random.nextInt(500 - 10 + 1) + 10, // max=500, min=10
					random.nextDouble() * 100, 
					System.nanoTime());
			logger.info("orderId:" + orderId);
			assertTrue(orderId != -1);			
		}
	}


}
