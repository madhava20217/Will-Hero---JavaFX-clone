import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Coin extends GameObject implements Collectable, Collidable{

    private boolean collected;
    private int frames;
    private int value;

    Coin(Node model){
        //todo constructor
        super(new float[]{0,0} , new float[]{0F, 0F}, new float[]{0F, 0F}, 0, false, false, "coin.png", new float[]{0,0});
        frames = 50;
        value = 1;
        collected = false;
    }

    public void get_collected(Hero hero){
        //todo: implement a scaling or a "collected" state change animation


        //creates new ScaleTransition animation and sets node accordingly

        ((Hero) hero).add_coins(this.value);
        collected = true;
        TranslateTransition trans = new TranslateTransition();
        trans.setNode((ImageView) this.getModel());
        trans.setByY(-200);

        FadeTransition fade = new FadeTransition();
        fade.setNode((ImageView)this.getModel());
        fade.setFromValue(1);
        fade.setToValue(0);

        ScaleTransition animation = new ScaleTransition();
        animation.setNode((ImageView)this.getModel());

        animation.setDuration(Duration.millis(250));
        animation.setByX(2);
        animation.setByY(2);

        trans.play();
        animation.play();
        fade.play();

    }

    public void derender(){
        this.getModel().setVisible(false);
        super.derender();
    }


    @Override
    public void collide(Hero other) {
        //TODO constructor
        ((Hero) other).add_coins(this.value);
        get_collected(other);

    }

}
