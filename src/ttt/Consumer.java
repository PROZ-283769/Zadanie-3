package ttt;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ttt.GameState.Player;

public class Consumer implements MessageListener {

	TicTacToeController controller;
	GameState gameState;
	String selector;
	
	Consumer(TicTacToeController controller_, GameState gameState_, String selector_){
		controller = controller_;
		gameState = gameState_;
		selector = selector_;
	}
	
	
	@Override
	public void onMessage(Message message) {
		if (message == null)
			return;
		//System.out.println("Recived message!");
		try {
		String textMessage = message.getStringProperty("MESSAGE");
		if(textMessage == null)
			return;
		//System.out.println("MESSAGE: "+textMessage);
		if(textMessage.charAt(0)=='A') {
			gameState.setOpponentSelector(textMessage.substring(1, textMessage.length()));
			return;
		}
		//System.out.println("INCOMING->BEFORE");
		//System.out.println(gameState.getTurn());
		//System.out.println(gameState.getMovesCounter());
		

			int a = (int)textMessage.charAt(1)-48;
			int b = (int)textMessage.charAt(3)-48;
			if(gameState.isItMyMove(a, b))
				return;
			if(textMessage.charAt(0)=='m') {
				if(gameState.getMovesCounter() == 0) {
					gameState.setPlayer(Player.O);
					gameState.toggleTurn();
				}
				gameState.advanceMovesCounter();
				controller.opponentMoveAt(a, b);
				gameState.toggleTurn();
			}
			else {
				controller.opponentMoveAt(a, b);
				controller.opponentWins();
				
			}
			Platform.runLater( () -> {controller.label.setText("Your turn!");});
			//System.out.printf("Odebrano wiadomość:'%s'\n", textMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		//System.out.println("INCOMING->AFTER");
		//System.out.println(gameState.getTurn());
		//System.out.println(gameState.getMovesCounter());
	}
	
	
}
