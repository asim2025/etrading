package common;

import org.junit.Test;

/*
 * Test harness for Logger
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class LoggerTest {

	@Test
	public void logInfo() {
		Logger logger = Logger.getInstance(LoggerTest.class);
		logger.info("testing...");
		logger.debug("should not see this.");
	}
	
	
	@Test
	public void logInfoSlow() {
		Logger logger = Logger.getInstance(LoggerTest.class);
		logger.showReadableTime(true);
		logger.info("slow logger testing...");
		logger.debug("should not see this.");
	}
	
	@Test
	public void logDebug() {
		Logger logger = Logger.getInstance(LoggerTest.class);
		logger.setDebug(true);
		logger.debug("debug on");
		logger.info("should this.");
	}
}
