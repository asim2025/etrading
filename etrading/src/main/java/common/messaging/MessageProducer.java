package common.messaging;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.higherfrequencytrading.chronicle.Excerpt;
import com.higherfrequencytrading.chronicle.impl.IndexedChronicle;
import com.higherfrequencytrading.chronicle.tools.ChronicleTools;

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
	
	private Map<String, IndexedChronicle> chronicles = new HashMap<>();
	private static final String ROOT_DIR = System.getProperty("java.io.tmpdir");
	
	public void send(String destination, Object msg) throws Exception {
		IndexedChronicle chronicle = getChronicle(destination);
		synchronized(chronicle) {
			Excerpt excerpt = chronicle.createExcerpt();
			excerpt.startExcerpt(4096);
			excerpt.writeObject(msg);
			excerpt.finish();
			log.info("sent message to destination:" + destination);
		}
	}
	
	private synchronized IndexedChronicle getChronicle(String q) throws IOException {
		IndexedChronicle c = chronicles.get(q);
		if (c == null) {
			String path = ROOT_DIR + File.separator + q;
			c = new IndexedChronicle(path);
			chronicles.put(q, c);
			ChronicleTools.deleteOnExit(path);
		}
		return c;
	}
}
