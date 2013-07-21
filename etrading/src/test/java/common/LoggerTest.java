package common;

import org.junit.Test;

public class LoggerTest {

	@Test
	public void logInfo() {
		Logger logger = Logger.getInstance(LoggerTest.class);
		logger.info("testing...");
	}
	
	
	@Test
	public void logInfoSlow() {
		Logger logger = Logger.getInstance(LoggerTest.class);
		logger.showReadableTime(true);
		logger.info("slow logger testing...");
	}
}
