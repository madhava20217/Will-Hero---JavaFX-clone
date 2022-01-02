import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class GameManager extends Application{
	public GameManager () {
	}
	
	@Override
	public void start (Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(GameManager.class.getResource("templates/Menu.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 1048, 590);
		
		assert (fxmlLoader.getController() instanceof GameController);
		((GameController)fxmlLoader.getController()).setStageVar(stage);
		
		stage.setTitle("Will Hero");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}
	
	public static void main (String[] args) {
		launch();
	}
}
