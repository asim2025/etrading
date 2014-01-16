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
		MessageProducer producer = new MessageProducer("testDest1");
		producer.send(new Date());
		
		MessageConsumer consumer = new MessageConsumer("testDest1");
		MessageListenerImpl impl = new MessageListenerImpl();
		consumer.addListener(impl);
		Thread.sleep(10*1000);
	}
	
	private class MessageListenerImpl implements MessageListener {
		@Override
		public void onMessage(Object o) {
			System.out.println(">>>>message received:" + o);				
		}		
		
	}
}
