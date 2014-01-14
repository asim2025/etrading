package common.messaging;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.higherfrequencytrading.chronicle.Excerpt;
import com.higherfrequencytrading.chronicle.impl.IndexedChronicle;

import common.Logger;

/*
 * A helper class to receive messages. 
 * 
 * Internally uses Java Chronicle library.
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
*/
public class MessageConsumer {
	private static final Logger log = Logger.getInstance(MessageConsumer.class);
	private static final String ROOT_DIR = System.getProperty("java.io.tmpdir");
	
	private Map<String, MessageListener> listeners = new HashMap<>();
	private Map<String, IndexedChronicle> chronicles = new HashMap<>();
	private Map<String, Integer> indexes = new HashMap<>();
	
	private Thread runner;
	
	public MessageConsumer() {
		runner = new Thread(new Runner());
		runner.start();
	}
	
	public void register(String destination, MessageListener listener) throws IOException {
		log.info("register destination:" + destination + ", listener:" + listener);
		listeners.put(destination, listener);
		indexes.put(destination, 0);
		chronicles.put(destination, new IndexedChronicle(ROOT_DIR + File.separator + destination));
	}
	
	
	private class Runner implements Runnable {
		public void run() {
			while (true) {
				for (Map.Entry<String, IndexedChronicle> entry : chronicles.entrySet()) {
					String dest = entry.getKey();
					IndexedChronicle chronicle = entry.getValue();
					MessageListener listener = listeners.get(dest);
					int index = indexes.get(dest);

					Excerpt excerpt = chronicle.createExcerpt();
					if (index <= excerpt.size()) {
						excerpt.index(index);
						Object o = excerpt.readObject();
						listener.onMessage(o);
						log.info("notifying listener:" + listener);
						indexes.put(dest, index++);
					}
						
					/*
					if (!excerpt.nextIndex())
						continue;
					
					Object o = excerpt.readObject();
					
					listener.onMessage(o);
					log.info("notifying listener:" + listener);
					excerpt.finish();
					*/
/*					
					while (excerpt.nextIndex()) {
						Object o = excerpt.readObject();
						listener.onMessage(o);
						log.info("notifying listener:" + listener);
						excerpt.finish();
					}
*/
					excerpt.finish();
				}
			}
		}
	}
	
}
