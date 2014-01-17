package common.messaging;

import static org.junit.Assert.assertTrue;

import java.io.File;

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
public class MessageConsumerTest {
	private final static Logger log = Logger.getInstance(MessageConsumerTest.class);
	
	@Before
	public void init() throws InterruptedException {
		log.setDebug(true);
		File f = new File(System.getProperty("java.io.tmpdir") + File.separator +  "t1_counter.data");
		if (f.exists()) f.delete();
		File f1 = new File(System.getProperty("java.io.tmpdir") + File.separator +  "t1_counter.index");
		if (f1.exists()) f1.delete();
		Thread.sleep(5*1000);
	}
	
	@Test
	public void receive() throws Exception {
		Thread.sleep(1 * 1000);		
		MessageListenerImpl impl = new MessageListenerImpl();
		MessageConsumer consumer = new MessageConsumer("t1");
		consumer.addListener(impl);
		
		Thread.sleep(3000);
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
}
