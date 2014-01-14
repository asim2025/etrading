package common.messaging;

/*
 * The implementor of this interface will be called 
 * when a message arrives. 
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public interface MessageListener {
	public void onMessage(Object o);
}
