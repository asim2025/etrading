package common.messaging;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import common.Logger;

/*
 * A Test harness to receive messages. 
 * 
 * Internally uses Chronicle library
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class MessagingTest {
	private final static Logger log = Logger.getInstance(MessagingTest.class);
	
	@Before
	public void init() throws InterruptedException {
		log.setDebug(true);
		deleteFile("t1_counter.data");
		deleteFile("t1_counter.index");
		deleteFile("t1.data");
		deleteFile("t1.index");
		deleteFile("t1_idx.index");
		Thread.sleep(1 * 1000); // add delay
	}
	

	@Test
	public void sendAndReceive() throws Exception {
		send("t1");
		Thread.sleep(1 * 1000);	// add delay	
		receive("t1");
	}
	
	private void receive(String dest) throws Exception {
		MessageListenerImpl impl = new MessageListenerImpl();
		MessageConsumer consumer = new MessageConsumer(dest);
		consumer.addListener(impl);
		Thread.sleep(1*1000); // add delay
		int ncount = impl.getCount();
		System.out.println("ncount:" + ncount);
		assertTrue( ncount == 5 );
	}

	
	private class MessageListenerImpl implements MessageListener {
		private int ncount;
		
		@Override
		public void onMessage(Object o) {
			System.out.println(">>>>message received:" + o);
			ncount++;
		}		
		
		public int getCount() { return ncount; }
	}
	
	
	private void send(String dest) throws Exception {
		MessageProducer producer = new MessageProducer(dest);		
		for (int i = 0 ; i < 5; i++) {
			producer.send(new Date());
		}		
	}

	private void deleteFile(String name) {
		File f = new File(System.getProperty("java.io.tmpdir") + File.separator +  name);
		if (f.exists()) f.delete();
	}
}

