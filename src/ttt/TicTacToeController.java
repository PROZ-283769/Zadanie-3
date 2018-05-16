package ttt;

import java.util.Random;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ttt.GameState.Player;

public class TicTacToeController {
	
	JMSConsumer jmsConsumer;
	JMSContext jmsContext;
	GameState gameState;
	Producer producer;
	Consumer consumer;
	String selector;

	@FXML
	public Label label;

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
		selector = String.valueOf(new Random().nextInt(654321));
		gameState = new GameState(selector);
		producer = new Producer(selector);
		consumer = new Consumer(this, gameState, selector);
		producer.initializeConnection();
		receiveQueueMessagesAsynch();
		initializeButtonArray();
		
	}

	private void handleButton(int a, int b) {
		//System.out.println("IN->MOVE");
		//System.out.println(gameState.getTurn());
		//System.out.println(gameState.getMovesCounter());
		if (!gameState.getTurn() || buttons[a][b].getText()!="")
			return;
		Platform.runLater( () -> {label.setText("Opponent's turn!");});
		gameState.toggleTurn();
		buttons[a][b].setText(gameState.getPlayerString());
		gameState.advanceMovesCounter();
		
		if (gameState.checkMove(a, b)) {
			producer.sendWinMove(a, b);
			label.setText("You won!");
			Platform.runLater( () -> {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("You won!");
				alert.setHeaderText(null);
				alert.setContentText("Press OK to restart game!");
				alert.showAndWait();

			gameState.resetGame();
			for(int i=0; i<3; ++i)
				for(int j=0; j<3; ++j)
					buttons[i][j].setText("");
			});
			return;
		}
		else {
			System.out.println("handleButton sendMove!");
			producer.sendMove(a, b);
		}
		
		if(gameState.getMovesCounter()==9) {
			Platform.runLater( () -> {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Tie!");
				alert.setHeaderText(null);
				alert.setContentText("Press OK to restart game!");
				alert.showAndWait();

			gameState.resetGame();
			for(int i=0; i<3; ++i)
				for(int j=0; j<3; ++j)
					buttons[i][j].setText("");
			});
			return;
		}
		//System.out.println("OUT->MOVE");
		//System.out.println(gameState.getTurn());
		//System.out.println(gameState.getMovesCounter());
	}

	public void opponentMoveAt(int a, int b) {

		if(gameState.getPlayer() == Player.O)
			Platform.runLater( () -> {buttons[a][b].setText("X");});
		else
			Platform.runLater( () -> {buttons[a][b].setText("O");});
		
		if(gameState.getMovesCounter()==9) {
			Platform.runLater( () -> {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Tie!");
				alert.setHeaderText(null);
				alert.setContentText("Press OK to restart game!");
				alert.showAndWait();

			gameState.resetGame();
			for(int i=0; i<3; ++i)
				for(int j=0; j<3; ++j)
					buttons[i][j].setText("");
			});
		}
	}
	
	public void opponentWins() {
		Platform.runLater( () -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("You lost!");
			alert.setHeaderText(null);
			alert.setContentText("Press OK to restart game!");
			alert.showAndWait();

			gameState.resetGame();
			for(int i=0; i<3; ++i)
				for(int j=0; j<3; ++j)
					buttons[i][j].setText("");
			});

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
		
			Queue queue = new com.sun.messaging.Queue("ATJQueue");
			jmsConsumer = jmsContext.createConsumer(queue, "SELECTOR<> '" + selector + "'");
			
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
