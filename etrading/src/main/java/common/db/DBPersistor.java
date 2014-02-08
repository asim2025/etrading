package common.db;

import java.util.concurrent.ConcurrentLinkedQueue;

import common.Logger;
import common.messaging.MessageConsumer;
import common.messaging.MessageListener;

/*
 * Asynchronously store data in the database.
 *  
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class DBPersistor {
	private final static Logger log = Logger.getInstance(DBPersistor.class);
	private final static String DB_QUEUE = "db_queue";
	
	private ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<>();
	private MessageConsumer dbConsumer;
	private Thread dbPersistorThread;
	
	public static void main(String[] args) throws Exception {
		DBPersistor dbPersistor = new DBPersistor();
		dbPersistor.start();
		log.info("started...");
		//Thread.currentThread().join();
	}
	
	
	public void start() throws Exception {
		dbPersistorThread = new Thread(new DBPersistorRunner(), "DBPersistorThread");
		dbPersistorThread.start();
		
		dbConsumer = new MessageConsumer(DB_QUEUE);
		dbConsumer.addListener(new MessageListener() {

			@Override
			public void onMessage(Object o) {
				queue.offer(o);
			}
		});
	}


	private void persist(Object o) {
		//TODO: write record to database using JDBC
		// for now just log to stdout
		log.info("stored entry in db:" + o);
	}
	
	private class DBPersistorRunner implements Runnable {
		@Override
		public void run() {
			while (true) {
				Object o = queue.poll();
				if (o!=null) persist(o);
				//else spin lock
			}

		}
	}
	
}
