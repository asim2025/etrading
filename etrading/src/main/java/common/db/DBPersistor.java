package common.db;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
	
	private BlockingQueue<Object> queue = new ArrayBlockingQueue<>(10000);
	private MessageConsumer dbConsumer;
	private Thread dbPersistorThread;
	
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("unused")
		DBPersistor dbPersistor = new DBPersistor();
		log.info("started...");
		Thread.currentThread().join();
	}
	
	public DBPersistor() throws Exception {
		dbPersistorThread = new Thread(new DBPersistorRunner(), "DBPersistorThread");
		dbPersistorThread.start();
		
		dbConsumer = new MessageConsumer(DB_QUEUE);
		dbConsumer.addListener(new MessageListener() {

			@Override
			public void onMessage(Object o) {
				try {
					queue.put(o);
				} catch (InterruptedException ioe) {
					ioe.printStackTrace();
				}
			}
		});
	}


	private void persist(Object o) throws Exception {
		//TODO: write record to database using JDBC
		// for now just log to stdout
		log.info("stored entry in db:" + o);
	}
	
	private class DBPersistorRunner implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Object o = queue.take();
					persist(o);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}
	
}
