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

import ttt.GameState.Player;

public class Consumer implements MessageListener {

	TicTacToeController controller;
	GameState gameState;
	
	Consumer(TicTacToeController controller_, GameState gameState_){
		controller = controller_;
		gameState = gameState_;
	}
	
	
	@Override
	public void onMessage(Message message) {
		System.out.println("Recived message!");
		TextMessage textMessage = (TextMessage) message;
		
		System.out.println("INCOMING->BEFORE");
		System.out.println(gameState.getTurn());
		System.out.println(gameState.getMovesCounter());
		
		try {
			int a = (int)textMessage.getText().charAt(1)-48;
			int b = (int)textMessage.getText().charAt(3)-48;
			if(gameState.isItMyMove(a, b))
				return;
			if(textMessage.getText().charAt(0)=='m') {
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
			System.out.printf("Odebrano wiadomość:'%s'\n", textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		System.out.println("INCOMING->AFTER");
		System.out.println(gameState.getTurn());
		System.out.println(gameState.getMovesCounter());
	}
	
	
}
