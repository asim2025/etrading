package ats.algo;

import java.util.concurrent.TimeUnit;

/*
 * Algo configuration container class
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public final class AlgoConfig {
	private final String algoName;
	private final long initialDelay;
	private final long period;
	private final TimeUnit timeUnit;
	
	public AlgoConfig(String algoName, long initialDelay, long period, TimeUnit timeUnit) {
		this.algoName = algoName;
		this.initialDelay = initialDelay;
		this.period = period;
		this.timeUnit = timeUnit;
	}
	
	public String getAlgoName() { return algoName; }
	public long getInitialDelay() { return initialDelay; }
	public long getPeriod() { return period; }
	public TimeUnit getTimeUnit() { return timeUnit; }
}
