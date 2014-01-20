package experimental.esper;

import com.espertech.esper.client.*;

import java.util.Random;
import java.util.Date;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/*
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class StockTick {
	private final static common.Logger log = common.Logger.getInstance(StockTick.class);
	private static Random generator = new Random();	
	private final static String EPL = "select * from Tick(symbol='MSFT').win:length(2) having avg(price) > 6.0";
	
	
	public static void main(String[] args) {
		log.setDebug(true);
		log.showReadableTime(true);
		
		ConsoleAppender appender = new ConsoleAppender(new SimpleLayout());
		Logger.getRootLogger().addAppender(appender);
		Logger.getRootLogger().setLevel((Level) Level.WARN);

		Configuration cfg = new Configuration();
		cfg.addEventType("Tick", Tick.class.getName());
		EPServiceProvider epsp = EPServiceProviderManager.getProvider("CEPEngine", cfg);
		EPRuntime rt = epsp.getEPRuntime();

		EPAdministrator admin = epsp.getEPAdministrator();
		EPStatement stmt = admin.createEPL(EPL);
		stmt.addListener( new UpdateListener() {
			public void update(EventBean[] newData, EventBean[] oldData) {
				log.info("event received: " + newData[0].getUnderlying());
			}
		});

		// generate test data and send to esper
		for (int i = 0; i < 5; i++) {
			generateTestTick(rt);
		}
	}


	public static void generateTestTick(EPRuntime rt) {
		double price = (double) generator.nextInt(10);
		Tick tick = new Tick("MSFT", price, System.currentTimeMillis());
		log.info("tick:" + tick);
		rt.sendEvent(tick);
	}

	public static class Tick {
		String symbol;
		Double price;
		Date timeStamp;

		public Tick(String s, double p, long t) {
			symbol = s;
			price = p;
			timeStamp = new Date(t);
		}

		public double getPrice() { 	return price; 	}
		public String getSymbol() { return symbol; 	}
		public Date getTimeStamp() { return timeStamp; 	}

		@Override
		public String toString() {
			return "price: " + price.toString() + " time: " + timeStamp.toString();
		}
	}
}
