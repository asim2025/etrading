package ats.algo;


/*
 * All algo strategies should implement this interface
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public interface Algo {
	
	public void execute(AlgoParameter param) throws Exception; // run strategy and send orders to market
	public void cancel() throws Exception;  // cancel currently executing strategy 
	
}
