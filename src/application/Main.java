package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Load the FXML file
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/ufrn/imd/view/TelaPrincipal.fxml"));
		Parent root = loader.load();

		primaryStage.setTitle("Batalha Naval");

		Scene scene = new Scene(root, 1000, 600);

		// Set the scene on the primary stage
		primaryStage.setScene(scene);

		// Show the primary stage
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
