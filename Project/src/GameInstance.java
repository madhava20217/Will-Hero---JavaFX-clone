import java.io.Serializable;
import java.util.ArrayList;

public class GameInstance implements Serializable {
    public ArrayList<Helmet> helmet_list;
    private Hero hero;
    private ArrayList<GameObject> gamemap;
    private int coin_count;
	private static int platformCount = 0;
	private static int OrcCount = 0;
	private static final double GREENORCPROBABILITY = 0.4;			//higher number => less green orcs
	private static final double ORCPLACEPROBABILITY = 0.6;			//higher number => lower probability to place orcs

	private void init_gamemap(){
			gamemap = new ArrayList<>(100);
			Hero hero = new Hero(new float[]{100,300},new float[]{0,-5},new float[]{0,0},2, true, true, this);
			gamemap.add(hero);
			Platform p0 = new Platform(new float[]{100,300},0);
			platformCount++;								//incrementing platform count
			gamemap.add(p0);
			
			for (int i = 1; i < 22; i++) {
				float offset_x = (float)Math.random()*40-20;
				float offset_y = (float)Math.random()*20-10;
				Platform p = new  Platform(new float[]{450*i+offset_x,300+offset_y},(int)(Math.random()*3));
				platformCount++;							//incrementing platform count
				gamemap.add(p);
			}

			//random orc generator	: green orc only for now



			for(int i = 0; i< gamemap.size(); i++){
				GameObject o = gamemap.get(i);
				if(o instanceof Platform && o != p0){		//first platform excluded
					//if it is an instance of platform
					Platform obj = (Platform) o;



					// placing orcs with 70% probability
					if(Math.random() > ORCPLACEPROBABILITY){

						float[] positionInit = obj.getPos();
						float[] size = obj.getSize();
						float[] orcPos = new float[2];

						int side = (Math.random() > 0.5) ? -1: 1;		//determining positive or negative deviation from mean

						orcPos[0] = (float) (positionInit[0] + size[0]/2 + side*(size[0]/2-70));
						orcPos[1] = (float) (positionInit[1] - 100 - 100*Math.random());		//some deviation

						//Orc Type;
						Orc orc = (Math.random() > GREENORCPROBABILITY) ? new GreenOrc(orcPos, this) : new RedOrc(orcPos, this);
						OrcCount++;			//incrementing orc count
						gamemap.add(orc);
					}
				}
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
