package ats.algo;

import java.util.HashMap;
import java.util.Map;

import common.Logger;

/*
 * Main program that invokes algorithms that generate orders for execution.
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class AlgoService {
	private static Logger log = Logger.getInstance(AlgoService.class);
	
	@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
	private final static Map<String, Algo> algos = new HashMap() {{
		put("TimeSlice", new TimeSlice());
		put("VWAP", new VWAP());
	}};
	
	
	public static void main(String[] args) throws Exception {
		AlgoService service = new AlgoService();
		
		AlgoParameter param = new AlgoParameter();
		
		/* Move to JUNIT -- 
		 * execute a MSFT 6000 shares buy order in 1 minute 
		 * executing 1000 shares every 10 seconds 
		 */
		param.setSymbol("MSFT");
		param.setSide(Order.Side.BUY);
		param.setTotalShares(6000);
		param.setTotalTime(60 * 1000); // 1 min
		param.setFrequency(10 * 1000); // 10 seconds
		service.schedule("TimeSlice", param);
	}
	
	
	public void schedule(String algoName, AlgoParameter param) throws Exception {
		Algo algo = algos.get(algoName);
		AlgoRunner runner = new AlgoRunner(algo, param);
		Thread t = new Thread(runner);
		t.start();
		log.info("runner isDone:" + runner.isDone() + ", exception: " + runner.getE());
	}	
	
	private class AlgoRunner implements Runnable {
		private Algo algo;
		private AlgoParameter param;
		private Exception e;
		private boolean done;
		
		public AlgoRunner(Algo algo, AlgoParameter param) {
			this.algo = algo;
			this.param = param;
			done = false;
		}
		
		@Override
		public void run() {
			try {
				algo.execute(param);
			} catch (Exception e) {
				this.e = e;
			} finally {
				done = true;
			}			
		}
		
		public Exception getE() { return e; }
		public boolean isDone() { return done; }
	}
	
}
