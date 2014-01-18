package exchange.gateway;

import java.io.IOException;

import common.Logger;
import common.messaging.MessageConsumer;
import common.messaging.MessageListener;
import common.messaging.MessageProducer;
import exchange.orderbook.Order;
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

	// TODO: use config file
	private static final String CONFIG = "C:/Users/asim/git/etrading/etrading/src/main/resources/gatewayserver.properties";
	private static final String ORDER_QUEUE = "order_queue";
	private final static String FIX_QUEUE = "fix_exec_queue"; 
	private MessageProducer orderProducer;		// send orders to Order Matching Service
	private MessageConsumer execConsumer;		// receive orders from Execution Service
	
	
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
	
	public FIXGatewayServer() throws IOException {
		orderProducer = new MessageProducer(ORDER_QUEUE);
		execConsumer = new MessageConsumer(FIX_QUEUE);
		execConsumer.addListener(new MessageListener() {

			@Override
			public void onMessage(Object o) {
				Order order = (Order) o;
				log.info("filled order recevied:" + order);
				//TODO: send to client
			} 
		});
	}
	
	@Override
	public void onMessage(NewOrderSingle message, SessionID sessionID) throws FieldNotFound {
		log.info("NewOrderSingle - message:" + message + ", sessionID:" + sessionID);
		String ticker = message.getSymbol().getValue();
		int ordType = message.getOrdType().getValue();
		char side = message.getSide().getValue();
		int shares = (int) message.getOrderQty().getValue();
		double price = message.getPrice().getValue();
		int nprice = normalizePrice(price);
		long entryTime = message.getTransactTime().getValue().getTime();
		
		Order o = new Order(ticker, ordType, side, shares, nprice, entryTime);
		log.info("sending order:" + o);
		orderProducer.send(o);
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

	private int normalizePrice(double prc) {
		return (int) prc;	// TODO: convert 123.13 to 12313
	}
	
	/*
	private double denormalizePrice(int prc) {
		return 0;  // TODO: convert 12313 to 123.13
	}
	*/
}
