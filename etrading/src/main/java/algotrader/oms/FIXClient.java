package algotrader.oms;

import quickfix.Application;
import quickfix.DefaultMessageFactory;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.UnsupportedMessageType;
import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;
import quickfix.fix42.MessageCracker;
import quickfix.fix42.NewOrderSingle;
import common.Logger;

/* 
 * Manage connection to Exchange Gateway Server via FIX 4.2 Protocol
 * 
 * Send orders and receive execution reports.
 *   
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class FIXClient extends MessageCracker implements Application {
	private final static Logger log = Logger.getInstance(FIXClient.class);
	
	// TODO: remove absolute paths
	private final static String CONFIG = "C:/Users/asim/git/etrading/etrading/src/main/resources/client.properties";
	
	
	public static void main(String[] args) throws Exception {
		SessionSettings settings = new SessionSettings(CONFIG);
		FIXClient application = new FIXClient();
		FileStoreFactory fileStoreFactory = new FileStoreFactory(settings);
		MessageFactory messageFactory = new DefaultMessageFactory();
		SocketInitiator socketInitiator = new SocketInitiator(application,
				fileStoreFactory, settings, messageFactory);
		socketInitiator.start();
		SessionID sessionId = socketInitiator.getSessions().get(0);
		Session.lookupSession(sessionId).logon();
		Thread.sleep(5 * 1000);
		Message m1 = application.createNewSingleOrder();
		Session.sendToTarget(m1, sessionId);
		Thread.sleep(5 * 1000);
		
		log.info("done.");
	}
	
	
	public Message createNewSingleOrder() {
		NewOrderSingle orderMessage = new NewOrderSingle(
				new ClOrdID("1"),
				new HandlInst('1'),
				new Symbol("MSFT"),
				new Side(Side.BUY),
				new TransactTime(),
				new OrdType(OrdType.LIMIT)
		);
		orderMessage.set(new OrderQty(100));
		orderMessage.set(new Price(100.20));
		return orderMessage;
	}
	
	@Override
	public void fromAdmin(Message arg0, SessionID arg1) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		log.info("fromAdmin: message=" + arg0 + ", sessionID=" + arg1);
	}

	@Override
	public void fromApp(Message arg0, SessionID arg1) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		log.info("fromApp: message=" + arg0 + ", sessionID=" + arg1);
	}

	@Override
	public void onCreate(SessionID arg0) {
		log.info("onCreate: sessionID=" + arg0);
	}

	@Override
	public void onLogon(SessionID arg0) {
		log.info("onLogon: sessionID=" + arg0);
	}

	@Override
	public void onLogout(SessionID arg0) {
		log.info("onLogout: sessionID=" + arg0);
	}

	@Override
	public void toAdmin(Message arg0, SessionID arg1) {
		log.info("toAdmin: message=" + arg0 + ", sessionID=" + arg1);
	}

	@Override
	public void toApp(Message arg0, SessionID arg1) throws DoNotSend {
		log.info("toApp: message=" + arg0 + ", sessionID=" + arg1);
	}

}
