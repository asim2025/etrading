package common.messaging;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
	private static final String ROOT_DIR = System.getProperty("java.io.tmpdir") + File.separator;
	
	private List<MessageListener> listeners = new LinkedList<>();
	private IndexedChronicle chr;
	private IndexedChronicle idxChr;
	
	private Thread runner;
	
	public MessageConsumer(String dest) throws IOException {
		chr = new IndexedChronicle(ROOT_DIR + dest);
		idxChr = new IndexedChronicle(ROOT_DIR + dest + "_idx");
		
		runner = new Thread(new Runner());
		runner.setName("MessageConsumer");
		runner.start();
	}
	
	public void addListener(MessageListener listener) {
		log.info("register listener:" + listener);
		listeners.add(listener);
	}
	
	public void deleteListener(MessageListener listener) {
		log.info("remove listener:" + listener);
		listeners.remove(listener);
	}
	
	
	private class Runner implements Runnable {
		
		@Override
		public void run() {
			while (true) {
				Excerpt excerpt = chr.createExcerpt();
				long size = excerpt.size();
				long index = getLastIndex();
				
				while (index < size && listeners.size() > 0) {
					log.debug("index:" + index + ",size:" + size);
					excerpt.index(index);
					Object o = excerpt.readObject();
					
					for (MessageListener l : listeners) {
						log.info("notifying listener:" + l);
						l.onMessage(o);	
					}
					
					index++;
					size = excerpt.size();
					saveIndex(index);					
				}		
				excerpt.close();
				
				try {
					Thread.sleep(1);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
		
		private long getLastIndex() {
			Excerpt ex = idxChr.createExcerpt();
			long size = ex.size();
			long index = 1;
			
			if (size > 0) {
				ex.index(size-1);
				index = ex.readLong();
				ex.close();
			}
			return index;
		}
				
		private void saveIndex(long idx) {
			Excerpt ex = idxChr.createExcerpt();
			ex.startExcerpt(64);
			ex.writeLong(idx);
			ex.finish();
		}
	}
	
}
