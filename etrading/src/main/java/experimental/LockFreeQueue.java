package experimental;

/*
 * Lock Free Queue
 * 
 * Fast data structure to allow concurrent read/writes by a single producer and consumer.
 * The priority is to avoid synchronization/locking and second priority is to avoid GC pauses.
 * The memory is not released until necessary.
 * 
 * The data structure grows up to the capacity and then re-uses previously allocated un-used 
 * entries.
 * 
 * The add method won't overwrite un-consumed entries. If max number of entries have been allocated
 * then the add method will return false. The caller can continue or try again later.
 * 
 * The get method returns un-consumed entries. If data structure is emtpy then caller will 
 * receive null. The caller can continue or try again later.
 * 
 * The default capacity is 10,000.  The caller can override default capacity by passing a different
 * value to the constructor.
 * 
 * ** NOTE : 
 * 
 *  Current implementation has some issues. For example, nodectr is defined volatile and ++ (increment) on 
 *  volatile is not atomic. An easy fix is to define nodectr as AtomicInteger.  However, I will be 
 *  completely rewriting the implementation using CAS or AQS. 
 * 
 * ***
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class LockFreeQueue<E> {
	private final static int DEFAULT_CAPACITY = 10000;
	
	private Node<E> root;
	private Node<E> writeptr;
	private Node<E> readptr;
	
	private int capacity;
	private volatile int nodectr;
	
	public LockFreeQueue() {
		this(DEFAULT_CAPACITY);
	}
	
	public LockFreeQueue(int capacity) {
		if (capacity<=1) throw new RuntimeException("capacity must be > 1");
		this.capacity = capacity;
	}
	
	public boolean add(E data) {
		if (data==null) throw new RuntimeException("null objects are not allowed.");
		
		if (nodectr++ < capacity) {
			Node<E> n = new Node<E>();
			n.data = data;
			
			if (root == null) {
				root = n;
			} else {
				writeptr.next = n;
			}
			writeptr = n;
			return true;
		} else {
			if (readptr == null) return false; // list is full
			
			if (writeptr.next == null) { // make list circular
				writeptr.next = root;
			}
			
			if (writeptr.next == readptr.next) return false; // list is full
			writeptr = writeptr.next;
			writeptr.data = data;
			return true;
		}
	}
		
			
	public E get() {
		if (root == null) return null; // empty
		if (readptr == null) { // first read
			readptr = root;
		} else {
			Node<E> next = readptr.next;
			if (next == writeptr.next) return null; // don't go past writeptr
			readptr = next;
		}
		E data = readptr.data;
		return data;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void print() {
		Node<E> node = root;
		for (int i = 0; i < capacity; i++) {
			System.out.println("i:" + i + ",data:" + node.data + ",node:" + node + ",next:" + node.next);
			node = node.next;
		}
	}
	
	public static class Node<E> {
		private E data;
		private Node<E> next;
	}
	
}
