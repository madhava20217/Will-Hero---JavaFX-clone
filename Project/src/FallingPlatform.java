import java.util.Timer;
import java.util.TimerTask;

public class FallingPlatform extends GameObject implements Collidable{
	private final static int collapse_rate = 350;
	private final Platform[] submodels;
	private boolean collapsing;
	private static final float[] size = {71,39};
	
	FallingPlatform (float[] pos) {
		super(pos,new float[]{0,0}, new float[]{0,0}, 1000, false, false, null, new float[]{size[0]*6,size[1]});
		submodels = new Platform[6];
		collapsing = false;
		for (int i = 0; i < 6; i++) {
			submodels[i] = new Platform(new float[]{pos[0]+71*i, pos[1]}, "images/platform_falling.png", size);
		}
	}
	
	public Platform[] getSubmodels () {
		return submodels;
	}
	
	@Override
	public void collide (GameObject other) {
		if(other instanceof Hero)
			collapse();
	}
	
	public void collapse () {
		if (collapsing)
			return;
		collapsing = true;
		Timer t = new Timer();
		GameController.getGameInstance().remove_ID(this.getID());
		t.schedule(
			new TimerTask() {
				private int count = 0;
				@Override
				public void run() {
					submodels[count++].setG(true);
					if (count == 6)
						t.cancel();
				}
			}
			, 0, collapse_rate);
	}

	public boolean getCollapsing(){
		return this.collapsing;
	}
}