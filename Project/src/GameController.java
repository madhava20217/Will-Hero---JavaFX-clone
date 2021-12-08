import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class GameController{
	public GameController(){
	
	}
	
	private static Stage stage; // if i keep this non-static the game breaks dont ask me why
	private static Scene pausedGame = null;
	
	public void setStageVar(Stage _stage){
		stage = _stage;
	}
	
	@FXML
	private void goToPlay(MouseEvent ignored){
		try{
			FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("templates/PlayScreen.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
		} catch(IOException ignored1) {}
	}
	
	@FXML
	private void goToOverWin(MouseEvent ignored){
		try{
			FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("templates/GameOverWin.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
		} catch(IOException ignored1) {}
	}
	
	@FXML
	private void goToOverLose(MouseEvent ignored){
		try{
			FXMLLoader overLose = new FXMLLoader(GameController.class.getResource("templates/GameOverLose.fxml"));
			stage.setScene(new Scene(overLose.load()));
		} catch(IOException ignored1) {
			System.err.println("IOException when overLose screen.");
		}
	}
	
	@FXML
	private void goToPause(MouseEvent ignored){
		try{
			pausedGame = stage.getScene();
			
			
			FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("templates/PauseScreen.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
		} catch(IOException ignored1) {}
	}
	
	@FXML
	private void goResumeFromPause(MouseEvent ignored){
		try{
			/*FXMLLoader loader = new FXMLLoader(GameManager.class.getResource("templates/Menu.fxml"));
			Scene scene = new Scene(loader.load());*/
			
			stage.setScene(pausedGame);
		}
		catch (NullPointerException ignored1){
			System.err.println("Null Pointer exception when resuming game");
		}
	}
	
	@FXML
	private void goToMainMenu(MouseEvent ignored){
		try{
			FXMLLoader mainLoad = new FXMLLoader(GameController.class.getResource("templates/Menu.fxml"));
			stage.setScene(new Scene(mainLoad.load()));
		}
		catch (IOException ignored1){
			System.err.println("IOException caught when returning to main menu.");
		}
	}
	
	@FXML
	private void goToSaveScreen(MouseEvent ignored){
		try{
			FXMLLoader saveScreen = new FXMLLoader(GameController.class.getResource("templates/SaveScreen.fxml"));
			stage.setScene(new Scene(saveScreen.load()));
		}
		catch (IOException ignored1){
			System.err.println("IOException when going to save screen");
		}
	}
	@FXML
	private void goToLoadScreen(MouseEvent ignored){
		try{
			FXMLLoader saveScreen = new FXMLLoader(GameController.class.getResource("templates/LoadScreen.fxml"));
			stage.setScene(new Scene(saveScreen.load()));
		}
		catch (IOException ignored1){
			System.err.println("IOException when going to save screen");
		}
	}
	
	@FXML
	private void exitGame(MouseEvent ignored){
		System.out.println("Thank you for playing!");
		stage.close();
	}
}
