import java.io.Serializable;
import java.util.ArrayList;

public class GameInstance implements Serializable {
    public ArrayList<Helmet> helmet_list;
    private Hero hero;
    private ArrayList<GameObject> gamemap;
    private int coin_count;
		
		private void init_gamemap(){
			gamemap = new ArrayList<>(100);
			Hero hero = new Hero(new float[]{100,300},new float[]{0,-5},new float[]{0,0},2, true, true, this);
			gamemap.add(hero);
			Platform p0 = new Platform(new float[]{100,300},0);
			gamemap.add(p0);
			
			for (int i = 1; i < 22; i++) {
				float offset_x = (float)Math.random()*40-20;
				float offset_y = (float)Math.random()*20-10;
				Platform p = new  Platform(new float[]{450*i+offset_x,300+offset_y},(int)(Math.random()*3));
				gamemap.add(p);
			}
		}

    GameInstance(){
			init_gamemap();
    }
		
		public ArrayList<GameObject> get_gameMap(){
			return gamemap;
		}

		public void add_coins(int _coin_count){
			coin_count += _coin_count;
		}
		
		public int getCoin_count(){
			return coin_count;
		}
		
    public void start(){
        //todo method
    }

    public void stop(){
        //todo method
    }

    public void pause(){
        //todo method
    }

    public void resume(){
        //todo method
    }

    public void exit(){
        //todo method
    }

    public void resurrect(){
        //todo method
    }

    public void game_over(){
        //todo method
    }

    public void win(){
        //todo method
    }
}
