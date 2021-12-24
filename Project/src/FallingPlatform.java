import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class FallingPlatform extends GameObject implements Collidable{
	private float collapse_rate;
	private float[] collapse_motion;
	private Platform[] submodels;
	private static final float[] size = {71,39};
	
	FallingPlatform (float[] pos) {
		submodels = new Platform[6];
		for (int i = 0; i < 6; i++) {
			submodels[i] = new Platform(new float[]{pos[0]+71*i, pos[1]}, "images/platform_falling.png", size);
		}
	}
	
	@Override
	public void collide (Hero other) {
		collapse();
	}
	
	public void collapse () {
	
	}
}