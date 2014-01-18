package exchange.orderbook;

import java.io.IOException;

import common.Logger;
import common.messaging.MessageConsumer;
import common.messaging.MessageListener;
import common.messaging.MessageProducer;

/*
 * Process filled orders.  
 *  
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class ExecutionService {
	private final static Logger log = Logger.getInstance(ExecutionService.class);
	private final static String EXEC_QUEUE = "exec_queue";
	private final static String DB_QUEUE = "db_queue";		
	private final static String FIX_QUEUE = "fix_exec_queue";
	
	private MessageConsumer consumer;			// receive executed orders
	private MessageProducer fixProducer;		// send notifications to client via fix gatway
	private MessageProducer dbProducer;			// send db persist request
	
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("unused")
		ExecutionService service = new ExecutionService();
		log.info("started...");
		Thread.currentThread().join();
	}
	
	
	public ExecutionService() throws IOException {
		fixProducer = new MessageProducer(FIX_QUEUE);
		dbProducer = new MessageProducer(DB_QUEUE);
		
		consumer = new MessageConsumer(EXEC_QUEUE);
		consumer.addListener(new MessageListener() {

			@Override
			public void onMessage(Object o) {
				Order order = (Order) o;
				log.info("message recevied:" + order);
				// TODO: order enrichments e.g. price, side etc 
				dbProducer.send(order);
				fixProducer.send(order);
			}
		});
	}
}
