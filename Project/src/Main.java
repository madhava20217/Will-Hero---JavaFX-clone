import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{
	@Override
	public void start (Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("templates/PlayScreen.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 1048, 590);

		stage.setTitle("Will Hero");
		stage.setScene(scene);
		stage.show();
	}
	public static void main (String[] args) {
		launch();
	}
	
	@FXML
	protected void handle_mouse(ActionEvent event) {
		event.consume();
		System.out.println("Hello, World!");
	}
}
