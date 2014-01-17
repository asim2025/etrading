package common.messaging;

import java.io.File;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

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
public class MessagingSenderTest {

	
	@Test
	public void send() throws Exception {
		MessageProducer producer = new MessageProducer("t1");
		producer.send(new Date());	
		
	}

	
	//@Before
	public void init() throws InterruptedException {
		File f = new File(System.getProperty("java.io.tmpdir") + File.separator +  "t1.data");
		if (f.exists()) f.delete();
		File f1 = new File(System.getProperty("java.io.tmpdir") + File.separator + "t1.index");
		if (f1.exists()) f1.delete();
		Thread.sleep(5 * 1000);
	}
}
