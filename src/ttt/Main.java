package ttt;
	

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("TicTacToeView.fxml"));
			Parent root = loader.load();
			TicTacToeController controller = loader.getController();
			
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setOnCloseRequest(e -> {
				controller.endListening();
			});
			
			/*
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("TicTacToeView.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setOnCloseRequest(e -> {
				root.getController().endListening();
			});
			*/
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
