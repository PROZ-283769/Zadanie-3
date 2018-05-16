package ttt;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.Topic;

public class Producer {
	
	//private Queue queue;
	private Topic topic;
	private JMSProducer jmsProducer;
	JMSContext jmsContext;

	public void initializeConnection() {
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		try { // [hostName][:portNumber][/serviceName]
				// 7676 numer portu, na którym JMS Service nasłuchuje połączeń
			((com.sun.messaging.ConnectionFactory) connectionFactory)
					.setProperty(com.sun.messaging.ConnectionConfiguration.imqAddressList, "localhost:7676/jms");
			jmsContext = connectionFactory.createContext();
			jmsProducer = jmsContext.createProducer();
			topic = new com.sun.messaging.Topic("ATJTopic");
			//queue = new com.sun.messaging.Queue("ATJQueue");

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void sendMove(int a, int b) {

		String msg = "m" + a + " " + b;
		//jmsProducer.send(queue, msg);
		jmsProducer.send(topic, msg);
		System.out.printf("Wiadomość '%s' została wysłana.\n", msg);
	}

	public void sendWinMove(int a, int b) {
		String msg = "w"+ a + " " + b;
		//jmsProducer.send(queue, msg);
		jmsProducer.send(topic, msg);
		System.out.printf("Wiadomość '%s' została wysłana.\n", msg);
	}

	public void endConnection() {
		jmsContext.close();
	}

}
