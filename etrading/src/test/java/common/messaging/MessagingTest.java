package common.messaging;

import java.util.Date;

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
public class MessagingTest {

	@Test
	public void sendAndReceive() throws Exception {
		MessageProducer producer = new MessageProducer();
		producer.send("testDest1", new Date());
		MessageConsumer consumer = new MessageConsumer();
		MessageListenerImpl impl = new MessageListenerImpl();
		consumer.register("testDest1", impl);
		Thread.sleep(10*1000);
	}
	
	private class MessageListenerImpl implements MessageListener {
		@Override
		public void onMessage(Object o) {
			System.out.println(">>>>message received:" + o);				
		}		
		
	}
}
