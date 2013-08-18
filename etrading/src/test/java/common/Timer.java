package common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Timer {

	private final static Map<String, Long> timers = new ConcurrentHashMap<>();
	
	public static void start(String token) {
		timers.put(token, System.nanoTime());
	}
	
	public static long endNanos(String token) {
		long nanos = System.nanoTime() - timers.get(token);
		timers.remove(token);
		return nanos;
	}
	
	public static double endMicros(String token) {
		return endNanos(token) / 1000D;
	}
	
	public static double endMillis(String token) {
		return endMicros(token) / 1000D;
	}
	
	public static double endSecs(String token) {
		return endMillis(token) / 1000.0D;
	}
}
