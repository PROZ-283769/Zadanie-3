package ttt;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Topic;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ttt.GameState.Player;

public class TicTacToeController {
	
	JMSConsumer jmsConsumer;
	JMSContext jmsContext;
	GameState gameState;
	Producer producer;
	Consumer consumer;

	@FXML
	private Label label;

	@FXML
	private Button button00;
	@FXML
	private Button button01;
	@FXML
	private Button button02;
	@FXML
	private Button button11;
	@FXML
	private Button button10;
	@FXML
	private Button button12;
	@FXML
	private Button button20;
	@FXML
	private Button button21;
	@FXML
	private Button button22;

	private Button[][] buttons;

	@FXML
	public void initialize() {
		gameState = new GameState();
		
		producer = new Producer();
		consumer = new Consumer(this, gameState);
		producer.initializeConnection();
		receiveQueueMessagesAsynch();
		initializeButtonArray();

	}

	private void handleButton(int a, int b) {
		System.out.println("IN->MOVE");
		System.out.println(gameState.getTurn());
		System.out.println(gameState.getMovesCounter());
		if (!gameState.getTurn())
			return;
		if (!gameState.isItMyMove(a, b)) {
			gameState.toggleTurn();
			buttons[a][b].setText(gameState.getPlayerString());
			gameState.advanceMovesCounter();
			if (gameState.checkMove(a, b)) {
				label.setText("You won!");
				producer.sendWinMove(a, b);
			}
			else {
				System.out.println("handleButton sendMove!");
				producer.sendMove(a, b);
			}
		}
		System.out.println("OUT->MOVE");
		System.out.println(gameState.getTurn());
		System.out.println(gameState.getMovesCounter());
	}

	public void opponentMoveAt(int a, int b) {
		if(gameState.isItMyMove(a, b))
			return;
		if(gameState.getPlayer() == Player.O)
			Platform.runLater( () -> {buttons[a][b].setText("X");});
		else
			Platform.runLater( () -> {buttons[a][b].setText("O");});
	}
	
	public void opponentWins() {
		Platform.runLater( () -> {label.setText("Opponent won!");});
	}
	// ugly, but FXML forces this
	@FXML
	private void handleButton00() {
		handleButton(0, 0);
	}

	@FXML
	private void handleButton01() {
		handleButton(0, 1);
	}

	@FXML
	private void handleButton02() {
		handleButton(0, 2);
	}

	@FXML
	private void handleButton10() {
		handleButton(1, 0);
	}

	@FXML
	private void handleButton11() {
		handleButton(1, 1);
	}

	@FXML
	private void handleButton12() {
		handleButton(1, 2);
	}

	@FXML
	private void handleButton20() {
		handleButton(2, 0);
	}

	@FXML
	private void handleButton21() {
		handleButton(2, 1);
	}

	@FXML
	private void handleButton22() {
		handleButton(2, 2);
	}

	public void initializeButtonArray() {

		buttons = new Button[3][3];
		buttons[0][0] = button00;
		buttons[0][1] = button01;
		buttons[0][2] = button02;
		buttons[1][0] = button10;
		buttons[1][1] = button11;
		buttons[1][2] = button12;
		buttons[2][0] = button20;
		buttons[2][1] = button21;
		buttons[2][2] = button22;

	}
	
	public void receiveQueueMessagesAsynch() {
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
	    jmsContext = connectionFactory.createContext();
		try {
			((com.sun.messaging.ConnectionFactory) connectionFactory)
			.setProperty(
					com.sun.messaging.ConnectionConfiguration.imqAddressList,
					"localhost:7676/jms");
			Topic topic = new com.sun.messaging.Topic("ATJTopic");
			jmsConsumer = jmsContext.createConsumer(topic);
			//Queue queue = new com.sun.messaging.Queue("ATJQueue");
			//jmsConsumer = jmsContext.createConsumer(queue);
			
			//jmsConsumer.setMessageListener(new Consumer());
			jmsConsumer.setMessageListener(consumer);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		//jmsContext.close();
	}

	public void endListening() {
		jmsConsumer.close();
		jmsContext.close();
	}

}
