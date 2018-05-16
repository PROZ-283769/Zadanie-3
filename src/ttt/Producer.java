package ttt;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Topic;

public class Producer {

	private Queue queue;
	private String selector;
	private JMSProducer jmsProducer;
	JMSContext jmsContext;

	Producer(String selector_) {
		selector = selector_;
	}

	public void initializeConnection() {
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		try { // [hostName][:portNumber][/serviceName]
				// 7676 numer portu, na którym JMS Service nasłuchuje połączeń
			((com.sun.messaging.ConnectionFactory) connectionFactory)
					.setProperty(com.sun.messaging.ConnectionConfiguration.imqAddressList, "localhost:7676/jms");
			jmsContext = connectionFactory.createContext();
			jmsProducer = jmsContext.createProducer();
			queue = new com.sun.messaging.Queue("ATJQueue");

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void sendMove(int a, int b) {

		try {
			Message msg = jmsContext.createMessage();
			msg.setStringProperty("SELECTOR", selector);
			msg.setStringProperty("MESSAGE", "m" + a + " " + b);
			jmsProducer.send(queue, msg);
			System.out.printf("Wiadomość '%s' została wysłana.\n", msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void sendWinMove(int a, int b) {
		try {
			Message msg = jmsContext.createMessage();
			msg.setStringProperty("SELECTOR", selector);
			msg.setStringProperty("MESSAGE", "w" + a + " " + b);
			jmsProducer.send(queue, msg);
			System.out.printf("Wiadomość '%s' została wysłana.\n", msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	
	public void sendWait() {
		try {
			Message msg = jmsContext.createMessage();
			msg.setStringProperty("SELECTOR", selector);
			msg.setStringProperty("MESSAGE", "A"+selector);
			jmsProducer.send(queue, msg);
			System.out.printf("Wiadomość '%s' została wysłana.\n", msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	public void endConnection() {
		jmsContext.close();
	}

}
