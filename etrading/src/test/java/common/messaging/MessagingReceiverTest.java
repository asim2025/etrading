package common.messaging;

import org.junit.Test;

/*
 * A Test harness to receive messages. 
 * 
 * Internally uses Chronicle library
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class MessagingReceiverTest {

	@Test
	public void receive() throws Exception {
		Thread.sleep(1 * 1000);
		MessageConsumer consumer = new MessageConsumer("t1");
		consumer.addListener(new MessageListener() {
			@Override
			public void onMessage(Object o) {
				System.out.println(">>>>message received:" + o);				
			}		
		});
	}

}
