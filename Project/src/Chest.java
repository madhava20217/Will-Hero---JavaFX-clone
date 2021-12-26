import javafx.animation.FadeTransition;
import javafx.util.Duration;

public abstract class Chest extends GameObject implements Collidable{
	private boolean collected;
	private final Duration frames;
	Chest (float[] pos, String sprite) {
		super(pos, new float[]{0, 0}, new float[]{0, 0}, 0, false, false, sprite, new float[]{80, 56});
		collected = false;
		frames = new Duration(2000);
	}
	
	boolean isCollected(){
		return collected;
	}
	
	public void collect_chest (Hero hero){
		collected = true;
		//creates new ScaleTransition animation and sets node accordingly
		FadeTransition fade = new FadeTransition(frames);
		fade.setNode(this.getModel());
		fade.setFromValue(1);
		fade.setToValue(0);
		
		fade.setOnFinished((e)->{
			hero.getCurrent_game().remove_ID(this.getID());
			this.derender();
		});
		
		fade.play();
	}
	
	@Override
	public void collide (GameObject other) {
		if(!(other instanceof Hero)) return;
		collect_chest((Hero)other);
	}
}
