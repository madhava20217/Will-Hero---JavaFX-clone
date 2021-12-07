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

	private static Scene pausedGame = null;

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
	private void go_to_over_lose(MouseEvent click){
		try{
			FXMLLoader overLose = new FXMLLoader(GameManager.class.getResource("templates/GameOverLose.fxml"));
			stage.setScene(new Scene(overLose.load()));
		} catch(IOException ignored1) {
			System.err.println("IOException when overLose screen.");
		}
	}
	
	@FXML
	private void go_to_pause(MouseEvent ignored){
		try{
			pausedGame = stage.getScene();


			FXMLLoader fxmlLoader = new FXMLLoader(GameManager.class.getResource("templates/PauseScreen.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
		} catch(IOException ignored1) {}
	}

	@FXML
	private void goResumeFromPause(MouseEvent e){
		try{
			/*FXMLLoader loader = new FXMLLoader(GameManager.class.getResource("templates/Menu.fxml"));
			Scene scene = new Scene(loader.load());*/

			stage.setScene(pausedGame);
		}
		catch (NullPointerException nptr){
			System.err.println("Null Pointer exception when resuming game");
		}
	}

	@FXML
	private void goToMainMenu(MouseEvent click){
		try{
			FXMLLoader mainLoad = new FXMLLoader(GameManager.class.getResource("templates/Menu.fxml"));
			stage.setScene(new Scene(mainLoad.load()));
		}
		catch (IOException ioex){
			System.err.println("IOException caught when returning to main menu.");
		}
	}

	@FXML
	private void goToSaveScreen(MouseEvent click){
		try{
			FXMLLoader saveScreen = new FXMLLoader(GameManager.class.getResource("templates/SaveScreen.fxml"));
			stage.setScene(new Scene(saveScreen.load()));
		}
		catch (IOException ioex){
			System.err.println("IOException when going to save screen");
		}
	}
	@FXML
	private void goToLoadScreen(MouseEvent click){
		try{
			FXMLLoader saveScreen = new FXMLLoader(GameManager.class.getResource("templates/LoadScreen.fxml"));
			stage.setScene(new Scene(saveScreen.load()));
		}
		catch (IOException ioex){
			System.err.println("IOException when going to save screen");
		}
	}

	@FXML
	private void exitGame(MouseEvent click){
		System.out.println("Thank you for playing!");
		stage.close();
	}


	public static void main (String[] args) {
		launch();
	}
}
