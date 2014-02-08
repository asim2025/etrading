package ats.algo;


/*
 * All algo strategies should implement this interface
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public interface Algo {
	
	// run strategy and send orders to market
	public void execute(AlgoParameter param) throws Exception;
	
	// cancel currently executing strategy
	public void cancel() throws Exception;  
	
}
