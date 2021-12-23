import java.io.Serializable;
import java.util.ArrayList;

public class GameInstance implements Serializable{
	public ArrayList<Helmet> helmet_list;
	private Hero hero;
	private ArrayList<GameObject> gamemap;
	private int coin_count;
	
	// RATES
	private static final double GREENORCPROB = 0.6;
	private static final double ORCPLACEPROB = 0.4;
	private static final double CHESTPROB = 0.3;
	private static final double WEAPONPROB = 0.5;
	private static final double COINPROB = 0.2;
	private static final double DOUBLECOINPROB = 0.4;
	
	private static final float[] PLATFORMSIZE = {480, 300};
	
	// OFFSETS
	private static final float[] ORCOFFSET = {300, -50};
	private static final float[] CHESTOFFSET = {225, -54};
	private static final float[] COINOFFSET = {100, -40};
	
	// VARIANCES
	private static final float[] PLATFORMVARIANCE = {60, 100};
	private static final float[] ORCVARIANCE = {50, 100};
	private static final float COINVARIANCE = 50;
	private static final float CHESTVARIANCE = 50;
	
	private static float genrand () {
		return (float)(Math.random() - 0.5);
	}
	
	private void init_gamemap () {
		gamemap = new ArrayList<>(100);
		hero = new Hero(new float[]{100, 250}, new float[]{0, -5}, new float[]{0, 0}, 2, true, true, this);
		gamemap.add(hero);
		
		// MAKE STARTING PLATFORM
		Platform p0 = new Platform(new float[]{0, 300}, 0);
		gamemap.add(p0);
		
		for (int i = 1; i < 25; i++) {
			float p_del_x = PLATFORMVARIANCE[0] * genrand();
			float p_del_y = PLATFORMVARIANCE[1] * genrand();
			
			// SET PLATFORM
			Platform p = new Platform(new float[]{PLATFORMSIZE[0] * i + p_del_x, PLATFORMSIZE[1] + p_del_y},
				(int)(Math.random() * 3));
			gamemap.add(p);
			
			// ADD ORCS
			if (Math.random() < ORCPLACEPROB) {
				float[] o_del = {genrand() * ORCVARIANCE[0], (float)Math.random() * ORCVARIANCE[1]};
				float[] orcPos = {PLATFORMSIZE[0] * i + ORCOFFSET[0] + p_del_x + o_del[0],
					PLATFORMSIZE[1] + ORCOFFSET[1] + p_del_y - o_del[1]};
				
				//Orc Type;
				Orc orc = (Math.random() < GREENORCPROB) ?
					new GreenOrc(orcPos, this) : new RedOrc(orcPos, this);
				gamemap.add(orc);
			}
			
			// ADD COINS
			float c_del_x = genrand() * COINVARIANCE;
			if (Math.random() < COINPROB) {
				Coin c1 = new Coin(new float[]{PLATFORMSIZE[0] * i + p_del_x + c_del_x + COINOFFSET[0],
					PLATFORMSIZE[1] + COINOFFSET[1] + p_del_y});
				gamemap.add(c1);
				if (Math.random() < DOUBLECOINPROB) {
					Coin c2 = new Coin(new float[]{PLATFORMSIZE[0] * i + p_del_x + c_del_x + COINOFFSET[0] + 35,
						PLATFORMSIZE[1] + COINOFFSET[1] + p_del_y});
					gamemap.add(c2);
				}
			}
			
			// ADD CHESTS
			float ch_del_x = genrand() * CHESTVARIANCE;
			Chest c;
			if (Math.random() < CHESTPROB) {
				if (Math.random() < WEAPONPROB) {
					c = new WeaponChest(new float[]{PLATFORMSIZE[0] * i + p_del_x + ch_del_x + CHESTOFFSET[0],
						PLATFORMSIZE[1] + CHESTOFFSET[1] + p_del_y});
				} else {
					c = new CoinChest(new float[]{PLATFORMSIZE[0] * i + p_del_x + ch_del_x + CHESTOFFSET[0],
						PLATFORMSIZE[1] + CHESTOFFSET[1] + p_del_y});
				}
				gamemap.add(c);
			}
		}
	}
	
	GameInstance () {
		init_gamemap();
	}
	
	public ArrayList<GameObject> get_gameMap () {
		return gamemap;
	}
	
	public void add_coins (int _coin_count) {
		coin_count += _coin_count;
	}
	
	public int getCoin_count () {
		return coin_count;
	}
	
	public void start () {
		//todo method
	}
	
	public void stop () {
		//todo method
	}
	
	public void pause () {
		//todo method
	}
	
	public void resume () {
		//todo method
	}
	
	public void exit () {
		//todo method
	}
	
	public void resurrect () {
		//todo method
	}
	
	public void game_over () {
		//todo method
	}
	
	public void win () {
		//todo method
	}
}
