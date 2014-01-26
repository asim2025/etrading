package ats.algo;

import org.junit.Test;

/*
 * A Test harness for VWAP strategy 
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class VWAPTest {
	
	@Test
	public void testStrategy() throws Exception {
		AlgoParameter param = new AlgoParameter();
		
		/* execute a MSFT 6000 shares buy order in 1 minute 
		 * executing 1000 shares every 10 seconds 
		 * using price calculated from VWAP
		 */
		int[] volume = { 100, 100, 200, 300, 450 };
		double[] price = { 100.1, 100.20, 100.45, 99.59, 99.99 };
		param.setHistVolume(volume);
		param.setHistPrice(price);
		
		param.setSymbol("MSFT");
		param.setSide(Order.Side.BUY);
		param.setTotalShares(6000);
		param.setTotalTime(60 * 1000); // 1 min
		param.setFrequency(10 * 1000); // 10 seconds
		Algo algo = new VWAP();
		algo.execute(param);
	}

}
