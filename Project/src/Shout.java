import javafx.animation.*;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class Shout extends GameObject{
    private final Duration frames;
    private static final float size = 150;
    private final Orc bound_to;
		
    Shout (String fileno, Orc orc) {
        super(new float[]{orc.getPos()[0], orc.getPos()[1]-80},
	        new float[]{0F, 0F}, new float[]{0F, 0F}, 100, true, false, fileno, new float[]{size, size});
        frames = new Duration(2000);
        bound_to = orc;

        ((ImageView)this.getModel()).setPreserveRatio(true);
        setFade();
    }
	
	@Override
	public void move(){
			setPos(bound_to.getPos()[0], bound_to.getPos()[1]-80);
	}
	
	private void setFade () {
        FadeTransition fade = new FadeTransition();
        fade.setNode(this.getModel());
        fade.setFromValue(1); fade.setToValue(0);
        fade.setDuration(frames);

        fade.setOnFinished(e->{
            this.remove();
            bound_to.setUntaunt();
        });

        fade.play();
    }
}
