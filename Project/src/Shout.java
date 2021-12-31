import javafx.animation.*;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class Shout extends GameObject{
    private final Duration frames;
    private static final float size = 150;
    private Orc caller;

    Shout (float[] pos, String fileno, Orc orc) {
        super(pos, new float[]{0F, 0F}, new float[]{0F, 0F}, 100, false, false, fileno, new float[]{size, size});
        frames = new Duration(2500);
        caller = orc;

        ((ImageView)this.getModel()).setPreserveRatio(true);
        setFade();
    }

    private void setFade () {
        //TODO: modify this to fit your requirements
        //creates new ScaleTransition animation and sets node accordingly
        FadeTransition fade = new FadeTransition();
        fade.setNode(this.getModel());
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setDuration(Duration.seconds(1));

        fade.setOnFinished((e)->{
            this.remove();
            caller.setUntaunt();
        });

        fade.play();
    }

}
