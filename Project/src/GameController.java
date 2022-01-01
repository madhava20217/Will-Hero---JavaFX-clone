import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/*
TODO: 1) Add delete save feature 2) Add tooltip indicating save/load file status
TODO: 2) Create a blank label and have it display save game information: have them deserialised and then display it
 */
public class GameController{
	// JAVAFX OBJECTS
	private static Stage stage;
	private static Scene curr_scene;
	private static AnchorPane frame;
	private static AnimationTimer clock;
	private static ImageView weapon;
	
	// GAME OBJECTS
	private static GameInstance gameInstance;
	private static LinkedHashMap<UUID, GameObject> map;
	private static Hero hero;
	private static float panCam;
	
	private static boolean gameOver = false; // TODO: try and get rid of this
	
	public static GameInstance getGameInstance () {
		return gameInstance;
	}
	
	public static void setWeapon(Weapon w){
		weapon.setImage(new Image(w.getSprite()));
		weapon.setFitWidth(w.getSize()[0]);
		weapon.setFitHeight(w.getSize()[1]);
	}
	
	public static Hero getHero () {
		return hero;
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
	
	private GameInstance getCurrGameInstance () {
		// singleton method
		if (gameInstance == null) {
			return new GameInstance();
		} else {
			panCam = gameInstance.getPanCam();
			return gameInstance;
		}
	}
	
	public static void add_object (GameObject o) {
		if (frame != null) {
			frame.getChildren().add(o.getModel());
		}
	}
	
	private void reset_params () {
		// reset params = end game
		if (clock != null) clock.stop();
		panCam = 0;
		gameInstance = null;
		curr_scene = null;
		weapon = null;
		map = new LinkedHashMap<>();
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
			curr_scene = new Scene(fxmlLoader.load());
			stage.setScene(curr_scene);
			frame = (AnchorPane)curr_scene.lookup("#frame");
			weapon = (ImageView)curr_scene.lookup("#weapon");
			
			gameInstance = getCurrGameInstance();
			
			map = gameInstance.get_gameMap();
			hero = gameInstance.getHero();
			
			for (GameObject o : map.values()) {
				if (o.getModel() == null) { // happens when you deserialise
					o.setModel();
					frame.getChildren().add(o.getModel());
				}
			}
			
			d = (Label)curr_scene.lookup("#distance");
			c = (Label)curr_scene.lookup("#coin_count");
		} catch (IOException ignored1) {
			d = null;
			c = null;
		}
		distance = d; // distance is a function-level variable
		count = c;
		clock = new AnimationTimer(){
			@Override
			public void handle (long l) {
				List<GameObject> objects = map.values().stream().toList();
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
					//returns in case it gameOver in order to prevent nullpointerexceptions, otherwise continues
					//(in case of resurrection)
					clock.stop();
					GameOver();
					if (gameOver) return;
				}
				
				if (gameInstance.isWon()) {
					//for determining whether the game is over (boss orc killed) or not
					//necessarily returns from the game in this case
					goToOverWin();
					return;
				}
				count.setText("x " + gameInstance.getCoin_count());
			}
		};
		frame.setDisable(false);
		// prevents race conditions where double clicking start can crash game because hero
		// hasn't loaded yet
		clock.start();
	}
	
	@FXML
	public void GameOver () {
		if (!gameInstance.canResurrect()) {
			goToOverLose(null);
			gameOver = true;
		} else {
			//fetch VBox, set button functions, then set opacity.
			VBox resurrectionMenu = (VBox) stage.getScene().lookup("#vbox");

			Button resurrect = (Button) stage.getScene().lookup("#resurrect");
			Button endGame = (Button) stage.getScene().lookup("#endgame");

			resurrectionMenu.setVisible(true);
			endGame.setDisable(false);
			resurrect.setDisable(false);
		}
	}

	@FXML
	private void resurrectButtonHandler(MouseEvent ignored){
		VBox resurrectionMenu = (VBox) stage.getScene().lookup("#vbox");
		Button resurrect = (Button) stage.getScene().lookup("#resurrect");
		Button endGame = (Button) stage.getScene().lookup("#endgame");
		javafx.scene.image.ImageView message = (ImageView) stage.getScene().lookup("#message");

		int retValue = gameInstance.resurrect();
		if(retValue != 0){
			//not enough coins or other reason
			FadeTransition fade = new FadeTransition();
			fade.setNode(message);
			fade.setFromValue(0); fade.setToValue(1);
			fade.setDuration(Duration.millis(250));
			fade.play();
			return;
		}
		resurrect.setDisable(true);
		endGame.setDisable(true);
		resurrectionMenu.setVisible(false);
		clock.start();
	}

	@FXML
	private void endGameButtonHandler(MouseEvent click){
		Button resurrect = (Button) stage.getScene().lookup("#resurrect");
		Button endGame = (Button) stage.getScene().lookup("#endgame");
		resurrect.setOnMouseClicked(null);
		endGame.setOnMouseClicked(null);
		goToOverLose(click);
	}
	
	@FXML
	private void handle_click (MouseEvent click) {
		if (click.getButton() == MouseButton.PRIMARY) {
			hero.launch();
		} else {
			hero.cycleWeapon();
		}
	}
	
	private void goToOverWin () {
		try {
			clock.stop();
			FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("templates/GameOverWin.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			stage.setScene(scene);
			
			Label coins = (Label)stage.getScene().lookup("#coins");
			coins.setText(String.valueOf(gameInstance.getCoin_count()));
		} catch (IOException ignored1) {
		}
	}
	
	@FXML
	public void goToOverLose (MouseEvent ignored) {
		try {
			FXMLLoader overLose = new FXMLLoader(GameController.class.getResource("templates/GameOverLose.fxml"));
			stage.setScene(new Scene(overLose.load()));
			
			Label coins = (Label)stage.getScene().lookup("#coins");
			coins.setText(String.valueOf(gameInstance.getCoin_count()));
		} catch (IOException ignored1) {
		}
	}
	
	@FXML
	private void goToPause (MouseEvent ignored) {
		try {
			clock.stop(); // game pauses running when we go to pause screen
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
			stage.setScene(curr_scene);
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
			Button save1 = (Button)stage.getScene().lookup("#save_1");
			Button save2 = (Button)stage.getScene().lookup("#save_2");
			Button save3 = (Button)stage.getScene().lookup("#save_3");
			Button save4 = (Button)stage.getScene().lookup("#save_4");
			Button save5 = (Button)stage.getScene().lookup("#save_5");
			
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
			
			PauseTransition delay = new PauseTransition(Duration.seconds(3));
			delay.setOnFinished(ignored1->stage.close());
			delay.play();
		} catch (IOException ignored1) {
			System.err.println("IOException caught when exiting");
		}
	}
	
	@FXML
	private void saveGame (MouseEvent X) {
		String ID = ((Button)X.getSource()).getId();
		int slot = Integer.parseInt(ID.split("_")[1]);
		try {
			gameInstance.setPanCam(panCam);
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Project/res/saves/save" + slot + ".txt"));
			out.writeObject(gameInstance);
		} catch (IOException ignored) {
		}
	}
	
	@FXML
	private void loadGame (MouseEvent X) {
		String ID = ((Button)X.getSource()).getId();
		int slot = Integer.parseInt(ID.split("_")[1]);
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("Project/res/saves/save" + slot + ".txt"));
			gameInstance = (GameInstance)(in.readObject());
		} catch (IOException | ClassNotFoundException ignored) {
		}
		goToPlay(null);
	}
	
	private void setHoverActionLoad (Button b) {
		// TODO: do something better with the handler
		b.hoverProperty().addListener(e->System.out.println(b.getId() + " save file located!!!!!"));
	}
}
