package exchange.gateway;

import common.Logger;
import quickfix.Application;
import quickfix.DefaultMessageFactory;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.UnsupportedMessageType;
import quickfix.fix42.MessageCracker;
import quickfix.fix42.NewOrderSingle;

/* 
 * Exchange Gateway Server is a FIX engine supporting FIX 4.2 Protocol
 * 
 * Process client OMS connectivity and FIX instructions (orders etc).
 * Send Execution reports back to client OMS.
 *  
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */

public class FIXGatewayServer extends MessageCracker implements Application {
	private final static Logger log = Logger.getInstance(FIXGatewayServer.class);

	// TODO: make path relative
	private static final String CONFIG = "C:/Users/asim/git/etrading/etrading/src/main/resources/gatewayserver.properties";
	
	public static void main(String[] args) throws Exception {
		SessionSettings settings = new SessionSettings(CONFIG);
		Application application = new FIXGatewayServer();
		FileStoreFactory fileStoreFactory = new FileStoreFactory(settings);
		MessageFactory messageFactory = new DefaultMessageFactory();
		FileLogFactory fileLogFactory = new FileLogFactory(settings);
		SocketAcceptor socketAcceptor = new SocketAcceptor(application, fileStoreFactory,
				settings, fileLogFactory, messageFactory);
		socketAcceptor.start();
	}
	
	@Override
	public void onMessage(NewOrderSingle message, SessionID sessionID) {
		log.info("NewOrderSingle - message:" + message + ", sessionID:" + sessionID);
		
	}
	
	@Override
	public void fromAdmin(Message arg0, SessionID arg1) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		log.info("fromAdmin - message:" + arg0 + ", sessionID:" + arg1);
	}

	@Override
	public void fromApp(Message message, SessionID sessionId) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		log.info("fromApp - message:" + message + ", sessionID:" + sessionId);
		crack(message, sessionId);
	}

	@Override
	public void onCreate(SessionID arg0) {
		log.info("onCreate - sessionID:" + arg0);
	}

	@Override
	public void onLogon(SessionID arg0) {
		log.info("onLogon - sessionID:" + arg0);
	}

	@Override
	public void onLogout(SessionID arg0) {
		log.info("onLogout - sessionID:" + arg0);
	}

	@Override
	public void toAdmin(Message arg0, SessionID arg1) {
		log.info("toAdmin - message:" + arg0 + ", sessionID:" + arg1);
	}

	@Override
	public void toApp(Message arg0, SessionID arg1) throws DoNotSend {
		log.info("toApp - message:" + arg0 + ", sessionID:" + arg1);
	}

}
