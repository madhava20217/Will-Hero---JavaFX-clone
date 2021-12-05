import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class GameManager implements Serializable {

    private GameInstance running_save;
    private ArrayList<GameInstance> all_saves;
    private long serialVersionUID;

    GameManager(){
        //todo constructor
    }

    public void save(){
        //todo method
    }

    public void load(){
        //todo method
    }

    public void seralise() throws ClassNotFoundException, IOException{
        //todo method serialisation
    }

    public void deserialise() throws ClassNotFoundException, IOException {
        //todo method deserialisation
    }

}
