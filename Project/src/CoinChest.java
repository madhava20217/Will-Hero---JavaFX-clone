import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class CoinChest extends Chest{
	
	private final int coins_number;
	private boolean collected;
	private final Duration frames;
	
	CoinChest (float[] pos) {
		super(pos);
		coins_number = (int)(4 * Math.random() + 1);
		collected = false;
		frames = new Duration(2000);
	}
	
	@Override
	public void collect_chest (Hero hero) {
		if (collected) return;
		hero.add_coins(coins_number);
		collected = true;
		//creates new ScaleTransition animation and sets node accordingly
		FadeTransition fade = new FadeTransition(frames);
		fade.setNode(this.getModel());
		fade.setFromValue(1);
		fade.setToValue(0);
		
		fade.setOnFinished((e)->{
			hero.getCurrent_game().get_gameMap().remove(this);
			this.derender();
		});
		
		fade.play();
	}
}
