package ats.algo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import common.Logger;

/*
 * Main program that spawns various algorithms that generate orders for execution.
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class AlgoEngine {
	private static Logger log = Logger.getInstance(AlgoEngine.class);
	
	public static void main(String[] args) throws Exception {
		AlgoEngine engine = new AlgoEngine();
		engine.execute();
	}
	
	public void execute() throws Exception {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		
		AbstractAlgo timeSlice = getTimeSlice();
		
		scheduler.scheduleAtFixedRate(timeSlice,
					timeSlice.getConfig().getInitialDelay(),
					timeSlice.getConfig().getPeriod(),
					timeSlice.getConfig().getTimeUnit()
					);
		
		Thread.sleep(5000); // let scheduler start
		scheduler.shutdown();
	}	
	
	private AbstractAlgo getTimeSlice() {
		AbstractAlgo algo = new TimeSlice();
		
		// TODO: generalize it
		AlgoParameter param = new AlgoParameter();
		param.size = 10000;
		algo.setConfig(new AlgoConfig("TimeSlice", 1, 60, TimeUnit.SECONDS));		
		return algo;
	}
}
