import java.io.Serializable;

public abstract class GameObject implements Serializable {
    private float[] pos;
    private float[] vel;
    private float[] acc;

    GameObject(){
        //todo constructor
    }


    public float[] get_pos() {
        //todo method
        return pos;
    }

    public void set_vel() {
        //todo method
    }

    public void set_acc() {
        //todo method
    }

    public void move(){
        //todo method
    }

    public void render(){
        //todo method
    }

    public void derender(){
        //todo method
    }


}
