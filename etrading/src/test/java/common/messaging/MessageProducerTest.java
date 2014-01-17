package common.messaging;

import java.io.File;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import common.Logger;
import common.messaging.MessageProducer;

/*
 * A Test harness for sending messages. 
 * 
 * Internally uses Chronicle library
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class MessageProducerTest {
	private final static Logger log = Logger.getInstance(MessageProducerTest.class);
	
	@Test
	public void send() throws Exception {
		MessageProducer producer = new MessageProducer("t1");
		
		for (int i = 0 ; i < 5; i++) {
			producer.send(new Date());
			Thread.sleep(1*1000);
		}		
	}

	
	@Before
	public void init() throws InterruptedException {
		log.setDebug(true);
		
		File f = new File(System.getProperty("java.io.tmpdir") + File.separator +  "t1.data");
		if (f.exists()) f.delete();
		File f1 = new File(System.getProperty("java.io.tmpdir") + File.separator + "t1.index");
		if (f1.exists()) f1.delete();
		File f2 = new File(System.getProperty("java.io.tmpdir") + File.separator + "t1_idx.index");
		if (f2.exists()) f2.delete();
		Thread.sleep(5 * 1000);
	}
}
