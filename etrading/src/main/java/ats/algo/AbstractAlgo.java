package ats.algo;

import java.util.List;

import common.Logger;

/*
 * Algos base class.  
 * 
 * All algos should extend this base class
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public abstract class AbstractAlgo implements Runnable {
	private final static Logger log = Logger.getInstance(AbstractAlgo.class);
	
	private AlgoParameter parameter;		// input parameters
	private AlgoConfig config;				// configuration
	private List<?> orders;					// orders that need to be send to market
	
	/*
	 * Execute and return list of Orders.
	 */
	public abstract List<?> execute(AlgoParameter param) throws Exception;
	
	
	@Override
	public final void run() {
		try {
			log.info("executing algo: " + config.getAlgoName());
			orders = execute(parameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public List<?> getOrders() {
		return orders;
	}
	
	public AlgoParameter getParameter() {
		return parameter;
	}


	public void setParameter(AlgoParameter parameter) {
		this.parameter = parameter;
	}


	public AlgoConfig getConfig() {
		return config;
	}


	public void setConfig(AlgoConfig config) {
		this.config = config;
	}

}
