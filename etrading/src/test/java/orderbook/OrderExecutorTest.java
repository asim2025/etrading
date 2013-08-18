package orderbook;

import static org.junit.Assert.*;

import java.util.Random;

import orderbook.OrderExecutor;
import orderbook.OrderSide;
import orderbook.OrderType;

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
	public void addSingleLimitOrder() throws InterruptedException {
		String ticker = "IBM";
		int shares = 100;
		double limitPrice = 123.10;	
		long entryTime = System.nanoTime();	
		int orderId = executor.addOrder(ticker, OrderType.Limit, OrderSide.BUY, shares, 
				limitPrice, entryTime);
		logger.info("orderId:" + orderId);
		assertTrue(orderId != -1);
		Thread.sleep(100); // add delay
		executor.printOrderBook(ticker, OrderSide.BUY);
	}

	@Test
	public void matchTwoLimitOrders() throws InterruptedException {
		int orderId1 = executor.addOrder("IBM", OrderType.Limit, OrderSide.BUY, 300, 25, System.nanoTime());
		logger.info("orderId:" + orderId1);
		int orderId2 = executor.addOrder("IBM", OrderType.Limit, OrderSide.SELL, 300, 25, System.nanoTime());
		logger.info("orderId:" + orderId2);
		Thread.sleep(1000);
		executor.printOrderBook("IBM", OrderSide.BUY);
		executor.printOrderBook("IBM", OrderSide.SELL);
	}
	
	@Test
	public void matchLimitMarketOrders() throws InterruptedException {
		int orderId1 = executor.addOrder("MSFT", OrderType.Limit, OrderSide.BUY, 300, 25, System.nanoTime());
		logger.info("orderId:" + orderId1);
		int orderId2 = executor.addOrder("MSFT", OrderType.Market, OrderSide.SELL, 300, 25, System.nanoTime());
		logger.info("orderId:" + orderId2);
		Thread.sleep(1000);
		executor.printOrderBook("MSFT", OrderSide.BUY);
		executor.printOrderBook("MSFT", OrderSide.SELL);
	}
	
	
	@Test
	public void buildOrderBook() throws InterruptedException {
		int orderId1 = executor.addOrder("IBM", OrderType.Limit, OrderSide.BUY, 300, 25, System.nanoTime());
		logger.info("orderId:" + orderId1);
		int orderId2 = executor.addOrder("IBM", OrderType.Limit, OrderSide.SELL, 400, 25.25, System.nanoTime());
		logger.info("orderId:" + orderId2);
		int orderId3 = executor.addOrder("IBM", OrderType.Limit, OrderSide.BUY, 200, 25, System.nanoTime());
		logger.info("orderId:" + orderId3);
		int orderId4 = executor.addOrder("IBM", OrderType.Market, OrderSide.SELL, 100, 25, System.nanoTime());
		logger.info("orderId:" + orderId4);
		int orderId5 = executor.addOrder("IBM", OrderType.Limit, OrderSide.SELL, 200, 25.5, System.nanoTime());
		logger.info("orderId:" + orderId5);
		int orderId6 = executor.addOrder("IBM", OrderType.Market, OrderSide.BUY, 600, 25, System.nanoTime());
		logger.info("orderId:" + orderId6);
		int orderId7 = executor.addOrder("IBM", OrderType.Limit, OrderSide.SELL, 400, 25, System.nanoTime());
		logger.info("orderId:" + orderId7);
		Thread.sleep(100); // add delay
		executor.printOrderBook("IBM", OrderSide.BUY);
		executor.printOrderBook("IBM", OrderSide.SELL);
	}
	
	

	@Test
	public void addMultipleLimitOrder() throws InterruptedException {
		
		Random random = new Random();
		logger.setDebug(false);
		
		for (int i = 0; i < 10000; i++) {
			int orderId = executor.addOrder (
					"IBM", 
					(Boolean.TRUE == random.nextBoolean()) ? OrderType.Market : OrderType.Limit,
					(Boolean.TRUE == random.nextBoolean()) ? OrderSide.BUY : OrderSide.SELL, 
					random.nextInt(500 - 10 + 1) + 10, // max=500, min=10
					random.nextDouble() * 100, 
					System.nanoTime());
			logger.info("orderId:" + orderId);
			assertTrue(orderId != -1);			
		}
	}
	

}
