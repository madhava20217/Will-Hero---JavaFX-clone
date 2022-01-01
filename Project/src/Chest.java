import javafx.animation.FadeTransition;
import javafx.util.Duration;

public abstract class Chest extends GameObject implements Collidable, Collectable{
	private boolean collected;
	private final Duration frames;
	
	Chest (float[] pos, String sprite) {
		super(pos, new float[]{0, 0}, new float[]{0, 0}, 0, false, false, sprite, new float[]{80, 56});
		collected = false;
		frames = new Duration(2000);
	}
	
	boolean isCollected () {
		return collected;
	}
	
	@Override
	public void get_collected (Hero hero) {
		if (collected) return;
		collected = true;
		// creates new ScaleTransition animation and sets node accordingly
		FadeTransition fade = new FadeTransition(frames);
		fade.setNode(this.getModel());
		fade.setFromValue(1);
		fade.setToValue(0);
		
		fade.setOnFinished(e->this.remove());
		fade.play();
	}
	
	@Override
	public void collide (GameObject obj) {
		if (!(obj instanceof Hero) || collected)
			return;
		get_collected((Hero)obj);
	}
}
