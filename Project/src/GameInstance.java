import java.io.Serializable;
import java.util.ArrayList;

public class GameInstance implements Serializable {
    public ArrayList<Helmet> helmet_list;
    private Hero hero;
    private ArrayList<GameObject> gamemap;
    private int coin_count;
		private static int platformCount = 0;
		private static final double GREENORCPROBABILITY = 0.4;			//higher number => less green orcs
		private static final double ORCPLACEPROBABILITY = 0.6;			//higher number => lower probability to place orcs
	
		private static float genrand(){
			return (float)(Math.random()-0.5);
		}

	private void init_gamemap(){
			gamemap = new ArrayList<>(100);
			Hero hero = new Hero(new float[]{100,250},new float[]{0,-5},new float[]{0,0},2, true, true, this);
			gamemap.add(hero);
			
			// MAKE STARTING PLATFORM
			Platform p0 = new Platform(new float[]{0,300},0);
			gamemap.add(p0);

			for (int i = 1; i < 25; i++) {
				float offset_x = 60*genrand();
				float offset_y = 100*genrand();
				
				// SET PLATFORM
				Platform p = new Platform(new float[]{450*i+offset_x,300+offset_y},(int)(Math.random()*3));
				gamemap.add(p);
				
				// ADD ORCS
				// placing orcs with 70% probability
				if(Math.random() > ORCPLACEPROBABILITY){
					float[] orc_offset = {genrand()*50, (float)Math.random()*100};
					float[] orcPos = {450*i+300+offset_x+orc_offset[0],250+offset_y-orc_offset[1]};
					
					//Orc Type;
					Orc orc = (Math.random() > GREENORCPROBABILITY) ?
						new GreenOrc(orcPos, this) : new RedOrc(orcPos, this);
					gamemap.add(orc);
				}
				
				// ADD COINS
				float coincheck = genrand();
				float coin_offset = genrand()*50;
				if (coincheck > 0.2){
					Coin c1 = new Coin(new float[]{450*i+offset_x+coin_offset+100,260+offset_y});
					gamemap.add(c1);
					if (coincheck > 0.4){
						Coin c2 = new Coin(new float[]{450*i+offset_x+coin_offset+135,260+offset_y});
						gamemap.add(c2);
					}
				}
				
				// ADD CHESTS
				float chestcheck = genrand();
				float chest_offset = genrand()*50;
				Chest c;
				if (chestcheck > 0.3){
					if (chestcheck > 0.4){
						c = new WeaponChest(new float[]{450*i+offset_x+chest_offset+225,246+offset_y});
					}
					else{
						c = new CoinChest(new float[]{450*i+offset_x+chest_offset+225,246+offset_y});
					}
					gamemap.add(c);
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
