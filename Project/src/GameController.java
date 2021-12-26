import com.sun.javafx.scene.ImageViewHelper;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GameController{
	public GameController () {
	
	}
	
	private static LinkedHashMap<UUID, GameObject> map;
	private static List<GameObject> objects;
	private static GameInstance gameInstance;
	private static Stage stage; // if i keep this non-static the game breaks dont ask me why
	private static Scene pausedGame = null;
	private static AnimationTimer clock;
	private static float panCam;
	private static AnchorPane frame;
	
	public static GameInstance getGameInstance () {
		return gameInstance;
	}
	
	public static float getPanCam () {
		return panCam;
	}
	
	public static void panCamera (float x) {
		panCam += x;
	}
	
	public void setStageVar (Stage _stage) {
		stage = _stage;
	}
	
	public static void add_item(GameObject o){
		if(frame != null) {
			frame.getChildren().add(o.getModel());
		}
	}
	
	@FXML
	private void goToPlay (MouseEvent ignored) {
		// TODO: audio stuff?
		Toolkit.getDefaultToolkit().beep();
		
		// label of the distance counter in game header
		final Label distance;
		final Label count;
		Label d; // temp variable
		Label c; // temp variable
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("templates/PlayScreen.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
			
			gameInstance = new GameInstance();
			gameInstance.setStage(stage);
			frame = (AnchorPane)scene.lookup("#frame");
			map = gameInstance.get_gameMap();
			for (GameObject o : map.values()) {
				frame.getChildren().add(o.getModel());
			}
			
			d = (Label)scene.lookup("#distance");
			c = (Label)scene.lookup("#coin_count");
			assert (objects.get(0).getClass() == Hero.class); // hero needs to be first for collision stuff
		} catch (IOException ignored1) {
			d = null;
			c = null;
		}
		distance = d; // distance is a function-level variable
		count = c;
		clock = new AnimationTimer(){
			@Override
			public void handle (long l) {
				objects = map.values().stream().toList(); // TODO: this is O(n), and i really dont know if it can be done
				// faster.
				for (GameObject obj : objects) {
					obj.move();
					if (obj.isRendered()) {
						obj.refresh();
					}
				}
				
				for (int i = 0; i < objects.size() - 1; i++) {
					for (int j = i + 1; j < objects.size(); j++) {
						float[] overlaps = objects.get(i).getOverlaps(objects.get(j));
						if (overlaps[0] > 0 && overlaps[1] > 0) {
							// if there is no overlap, ther will be no interaction
							if (objects.get(i).isTangible() && objects.get(j).isTangible()) {
								// if even one object is intangible, there will be no physical collision
								objects.get(i).bounce(objects.get(j), 0.9865F);
							}
							
							if (objects.get(j) instanceof Collidable && objects.get(i).isTangible()) {
								((Collidable)objects.get(j)).collide(objects.get(i));
							}
						}
					}
				}
				
				assert distance != null;
				distance.setText(String.valueOf(gameInstance.getHero().getDistance()));
				
				if (!gameInstance.getHero().is_alive()) {
					//TODO here
					clock.stop();
					GameOver();
				}
				count.setText("x " + gameInstance.getCoin_count());
			}
		};
		clock.start();
	}

	@FXML
	public void GameOver () {

		if(gameInstance.hasResurrected()){
			goToOverLose(null);
		}
		else{
			//fetch VBox, set button functions, then set opacity.
			VBox resurrectionMenu = (VBox) stage.getScene().lookup("#vbox");

			Button resurrect = (Button) stage.getScene().lookup("#resurrect");
			Button endGame = (Button) stage.getScene().lookup("#endgame");
			javafx.scene.image.ImageView message = (ImageView) stage.getScene().lookup("#message");

			resurrect.setOnMouseClicked(e->{
				int retValue = gameInstance.resurrect();
				if(retValue != 0){
					//not enough coins or other reason
					FadeTransition fade = new FadeTransition();
					fade.setNode(message);
					fade.setFromValue(0);
					fade.setToValue(1);
					fade.setDuration(Duration.millis(250));
					fade.play();

					return;
				}
				resurrect.setOnMouseClicked(null);
				endGame.setOnMouseClicked(null);
				resurrectionMenu.setVisible(false);
				clock.start();
			});

			endGame.setOnMouseClicked(e->{
				resurrect.setOnMouseClicked(null);
				endGame.setOnMouseClicked(null);
				goToOverLose(e);
			});

		resurrectionMenu.setVisible(true);


		}
	}
	
	@FXML
	private void move_hero (MouseEvent ignored) {
		gameInstance.getHero().launch();
	}
	
	private void reset_params () {
		// reset params = end game
		if (clock != null) clock.stop();
		panCam = 0;
		map = new LinkedHashMap<>();
		objects = new LinkedList<>();
	}
	
	@FXML
	private void reset (MouseEvent ignored) {
		reset_params();
		Toolkit.getDefaultToolkit().beep();
		goToPlay(null);
	}
	
	@FXML
	private void goToOverWin (MouseEvent ignored) {
		try {
			int coinCount = ((Hero)objects.get(0)).getCurrent_game().getCoin_count();
			reset_params();
			FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("templates/GameOverWin.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
			Label coins = (Label) stage.getScene().lookup("#coins");
			coins.setText(String.valueOf(coinCount));
		} catch (IOException ignored1) {
		}
	}
	
	@FXML
	public void goToOverLose (MouseEvent ignored) {
		try {
			int coinCount = ((Hero)objects.get(0)).getCurrent_game().getCoin_count();
			reset_params();
			FXMLLoader overLose = new FXMLLoader(GameController.class.getResource("templates/GameOverLose.fxml"));
			stage.setScene(new Scene(overLose.load()));
			Label coins = (Label) stage.getScene().lookup("#coins");
			coins.setText(String.valueOf(coinCount));
		} catch (IOException ignored1) {
			System.err.println("IOException when overLose screen.");
		}
	}
	
	@FXML
	private void goToPause (MouseEvent ignored) {
		try {
			clock.stop(); // game pauses running when we go to pause screen
			pausedGame = stage.getScene();
			FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("templates/PauseScreen.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
		} catch (IOException ignored1) {
		}
	}
	
	@FXML
	private void returnToPause (MouseEvent ignored) {
		// when we return from save menu to pause screen
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("templates/PauseScreen.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
		} catch (IOException ignored1) {
		}
	}
	
	@FXML
	private void goResumeFromPause (MouseEvent ignored) {
		try {
			stage.setScene(pausedGame);
			clock.start();
		} catch (NullPointerException ignored1) {
			System.err.println("Null Pointer exception when resuming game");
		}
	}
	
	@FXML
	private void goToMainMenu (MouseEvent ignored) {
		try {
			reset_params();
			FXMLLoader mainLoad = new FXMLLoader(GameController.class.getResource("templates/Menu.fxml"));
			stage.setScene(new Scene(mainLoad.load()));
		} catch (IOException ignored1) {
			System.err.println("IOException caught when returning to main menu.");
		}
	}
	
	@FXML
	private void goToSaveScreen (MouseEvent ignored) {
		try {
			FXMLLoader saveScreen = new FXMLLoader(GameController.class.getResource("templates/SaveScreen.fxml"));
			stage.setScene(new Scene(saveScreen.load()));
		} catch (IOException ignored1) {
			System.err.println("IOException when going to save screen");
		}
	}
	
	@FXML
	private void goToLoadScreen (MouseEvent ignored) {
		try {
			FXMLLoader saveScreen = new FXMLLoader(GameController.class.getResource("templates/LoadScreen.fxml"));
			stage.setScene(new Scene(saveScreen.load()));
			Button save1 = (Button)stage.getScene().lookup("#save1");
			Button save2 = (Button)stage.getScene().lookup("#save2");
			Button save3 = (Button)stage.getScene().lookup("#save3");
			Button save4 = (Button)stage.getScene().lookup("#save4");
			Button save5 = (Button)stage.getScene().lookup("#save5");

			setHoverActionLoad(save1);
			setHoverActionLoad(save2);
			setHoverActionLoad(save3);
			setHoverActionLoad(save4);
			setHoverActionLoad(save5);
		} catch (IOException ignored1) {
			System.err.println("IOException when going to save screen");
		}
	}
	
	@FXML
	private void exitGame (MouseEvent ignored) {
		try {
			FXMLLoader exitScreen = new FXMLLoader(GameController.class.getResource("templates/Exit.fxml"));
			stage.setScene(new Scene(exitScreen.load()));
			System.out.println("Thank you for playing!");
			PauseTransition delay = new PauseTransition(Duration.seconds(3));
			delay.setOnFinished(ignored1->stage.close());
			delay.play();
		} catch (IOException ignored1) {
			System.err.println("IOException caught when exiting");
		}
	}
	
	@FXML
	private void saveGame (MouseEvent ignored) {
		//TODO: implement it for the final game
		System.out.println("Saving game");
	}
	
	@FXML
	private void loadGame (MouseEvent ignored) {
		//TODO: implement it for the final game
		System.out.println("Loading game");
	}

	private void setHoverActionLoad(Button b){
		// TODO: do something better with the handler
		b.hoverProperty().addListener(e->{
			System.out.println(b.getId() + " save file located!!!!!");
		});
	}
}
