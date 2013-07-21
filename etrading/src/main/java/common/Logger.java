package common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * Simple log writer mirrored after famous open source logging library.
 * Avoid unnecessary object creations and keep implementation to bare minimum.
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class Logger {
	private static Map<String, Logger> instances = new HashMap<>();

	private static boolean readableTime;  // e.g. MM/dd/yyyy HH:mm:ss.SS otherwise System.nanos()
	private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SS");
	private static boolean debug;
	
	private String id;	
	
	
	// not thread safe but it's okay for our purpose
	public static Logger getInstance(Class<?> className) {
		String instanceName = className.toString();
		Logger logger = instances.get(instanceName);
		if (logger == null) {
			logger = new Logger(instanceName);
			instances.put(instanceName, logger);
		}
		return logger;
	}
	
	protected Logger(String id) {
		this.id = id;
	}
	
	public void showReadableTime(boolean flag) {
		readableTime = flag;
	}
	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean flag) {
		debug = flag;
	}

	public void debug(String msg) {
		if (isDebug()) {
			info(msg);
		}
	}
	
	public void info(String msg) {
		System.out.println(((readableTime) ? formatDateTime() : System.nanoTime()) + " [" + id + "] " + msg);
	}

	private String formatDateTime() {
		synchronized(sdf) {
			return sdf.format(new Date());
		}
	}
}
