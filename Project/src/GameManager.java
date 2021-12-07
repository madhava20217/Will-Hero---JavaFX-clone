import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class GameManager extends Application implements Serializable {
	private static Stage stage; // if i keep this non-static the game breaks dont ask me why
	public GameManager(){
	
	}
	
	@Override
	public void start (Stage _stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(GameManager.class.getResource("templates/Menu.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 1048, 590);
		
		stage = _stage;
		stage.setTitle("Will Hero");
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
	private void handle_mouse(MouseEvent ignored){
		try{
			FXMLLoader fxmlLoader = new FXMLLoader(GameManager.class.getResource("templates/PlayScreen.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
		} catch(IOException ignored1) {}
	}
	
	@FXML
	private void go_to_over_win(MouseEvent ignored){
		try{
			FXMLLoader fxmlLoader = new FXMLLoader(GameManager.class.getResource("templates/GameOverWin.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
		} catch(IOException ignored1) {}
	}
	
	@FXML
	private void go_to_over_lose(MouseEvent ignored){
		try{
			FXMLLoader fxmlLoader = new FXMLLoader(GameManager.class.getResource("templates/GameOverLose.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
		} catch(IOException ignored1) {}
	}
	
	@FXML
	private void go_to_pause(MouseEvent ignored){
		try{
			FXMLLoader fxmlLoader = new FXMLLoader(GameManager.class.getResource("templates/PauseScreen.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
		} catch(IOException ignored1) {}
	}
	
	public static void main (String[] args) {
		launch();
	}
}
