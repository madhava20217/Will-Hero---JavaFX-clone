import javafx.scene.Node;

public class Coin extends GameObject implements Collectable, Collidable{

    private boolean collected;
    private int frames;
    private int value;

    Coin(Node model){
        //todo constructor
        super(model , new float[]{0F, 0F}, new float[]{0F, 0F}, 0, false, false);
        frames = 200;
        value = 1;
        collected = false;
    }

    public void get_collected(Hero hero){
        //todo: implement a scaling or a "collected" state change animation
        this.getModel().setScaleX(0.5);
        this.getModel().setScaleY(0.5);
        this.getModel().scaleXProperty();
        this.getModel().scaleYProperty();
        collected = true;
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

    @Override
    public void refresh() {
        this.set_vel(1, 0.25F);
        if(!collected)frames--;
        if(frames == 0) derender();
    }

}
