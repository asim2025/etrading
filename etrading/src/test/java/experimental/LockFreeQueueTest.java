package experimental;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LockFreeQueueTest {
	private LockFreeQueue<Integer> list;
	private final int CAPACITY = 3;
	
	@Before
	public void init() {
		list = new LockFreeQueue<Integer>(CAPACITY);
	}
	
	@After
	public void destroy() {
		list = null;
	}
	
	@Test
	public void addSingleItem() {
		boolean status = list.add(1);
		assertTrue(status);
	}
	
	@Test
	public void addFullCapacity() {
		for (int i = 0; i < list.getCapacity(); i++) {
			boolean status = list.add(i);
			assertTrue(status);
		}
	}
	
	@Test
	public void addOverCapacityFail() {
		addFullCapacity();
		boolean status = list.add(4);
		assertFalse(status);
	}
	
	@Test
	public void readEmpty() {
		Integer i = list.get();
		assertNull(i);
	}
	
	@Test
	public void readSingleItem() {
		list.add(1);
		Integer i = list.get();
		assertEquals(i, new Integer(1));
	}
	
	@Test
	public void readFull() {
		addFullCapacity();
		for (int i = 0; i < list.getCapacity(); i++) {
			Integer obj = list.get();
			assertEquals(obj, new Integer(i));
		}
	}
	
	@Test
	public void addGetMultiThreaded() throws InterruptedException {
		Thread producer = new Thread() {
			public void run() {
				for (int i = 0; i < list.getCapacity()+2; i++) {
					boolean status = list.add(i);
					System.out.println("add(): data:" + i + ",status:" + status);
				}
			}
		};
		
		Thread consumer = new Thread() {
			public void run() {
				for (int i=0; i < list.getCapacity(); i++) {
					Integer data = list.get();
					System.out.println("get(): data:" + data);
					try {
						Thread.sleep(100);						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		producer.start();
		consumer.start();
		producer.join();
		consumer.join();
		list.print();
	}
}
