package ttt;

public class GameState {

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getPlayerString() {
		if (this.player == Player.O)
			return "O";
		else
			return "X";
	}

	public int getMovesCounter() {
		return movesCounter;
	}

	public void advanceMovesCounter() {
		this.movesCounter = movesCounter + 1;
	}

	public void toggleTurn() {
		turn = !turn;
	}

	public boolean getTurn() {
		return turn;
	}

	public boolean isItMyMove(int a, int b) {
		return moves[a][b];
	}
	public boolean checkMove(int a, int b) {
		moves[a][b] = true;
		if (moves[0][0] && moves[1][0] && moves[2][0] || moves[0][1] && moves[1][1] && moves[2][1]
				|| moves[0][2] && moves[1][2] && moves[2][2] || moves[0][0] && moves[0][1] && moves[0][2]
				|| moves[1][0] && moves[1][1] && moves[1][2] || moves[2][0] && moves[2][1] && moves[2][2]
				|| moves[0][0] && moves[1][1] && moves[2][2] || moves[2][0] && moves[1][1] && moves[0][2])
			return true;
		return false;

	}

	public enum Player {
		X, O
	};

	private Player player;
	private int movesCounter;
	private boolean turn;
	boolean[][] moves;

	public GameState() {
		moves = new boolean[3][3];
		player = Player.X;
		movesCounter = 0;
		turn = true;
	}

}
