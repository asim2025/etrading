package common.messaging;

import java.io.File;
import java.io.IOException;

import com.higherfrequencytrading.chronicle.Excerpt;
import com.higherfrequencytrading.chronicle.impl.IndexedChronicle;
import common.Logger;

/*
 * A helper class to send messages. 
 * 
 * Internally uses Chronicle library
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class MessageProducer {
	private static final Logger log = Logger.getInstance(MessageProducer.class);
	private static final String ROOT_DIR = System.getProperty("java.io.tmpdir") + File.separator;
	
	private IndexedChronicle chr;
	private String dest;
	
	public MessageProducer(String dest) throws IOException {
		this.dest = dest;
		chr = new IndexedChronicle(ROOT_DIR + dest);
	}
	
	
	public synchronized void send(Object msg) {
		log.info("sending message:" + msg);
		Excerpt excerpt = chr.createExcerpt();
		excerpt.startExcerpt(4096); // 4k bytes
		excerpt.writeObject(msg);
		excerpt.finish();
		log.info("message sent:" + dest);
	}	
}
