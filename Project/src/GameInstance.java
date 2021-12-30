import javafx.stage.Stage;

import java.io.Serializable;
import java.util.*;

public class GameInstance implements Serializable{
	public ArrayList<Helmet> helmet_list;
	private Hero hero;
	private LinkedHashMap<UUID, GameObject> gamemap;
	private int coin_count;
	private float panCam;
	
	public void setPanCam (float panCam) {
		this.panCam = panCam;
	}
	
	public float getPanCam(){
		return panCam;
	}
	
	//VARIABLES
	private static boolean resurrection;

	// RATES
	private static final int RESURRECTIONCCOST = 1;			//cost for resurrection
	private static final double GREENORCPROB = 0.6;
	private static final double ORCPLACEPROB = 0.4;
	private static final double CHESTPROB = 0.3;
	private static final double WEAPONPROB = 0.7;
	private static final double COINPROB = 0.2;
	private static final double DOUBLECOINPROB = 0.4;
	
	private static final float[] PLATFORMSIZE = {480, 300};
	
	// OFFSETS
	private static final float[] ORCOFFSET = {160, -50};
	private static final float[] CHESTOFFSET = {250, -55};
	private static final float[] COINOFFSET = {55, -40};
	
	// VARIANCES
	private static final float[] PLATFORMVARIANCE = {60, 100};
	private static final float[] ORCVARIANCE = {50, 100};
	private static final float COINVARIANCE = 50;
	private static final float CHESTVARIANCE = 35;
	
	private static float genrand () {
		return (float)(Math.random() - 0.5);
	}
	
	public void remove_ID(UUID ID){
		gamemap.remove(ID);
	}
	public void add_item(UUID ID, GameObject item){
		gamemap.put(ID, item);
		GameController.add_item(item);
	}
	
	public Hero getHero () {
		return hero;
	}
	
	private void init_gamemap () {
		gamemap = new LinkedHashMap<>(150); // I want hashmap, but also hero should be first object
		hero = new Hero(this);
		gamemap.put(hero.getID(), hero);
		
		// MAKE STARTING PLATFORM
		Platform p0 = new Platform(new float[]{0, 300}, 0);
		gamemap.put(p0.getID(), p0);
		
		for (int i = 1; i < 25; i++) {
			float p_del_x = PLATFORMVARIANCE[0] * genrand();
			float p_del_y = PLATFORMVARIANCE[1] * genrand();
			
			// SET PLATFORM
			if (Math.random() < 0.2){
				FallingPlatform p = new FallingPlatform(new float[]{PLATFORMSIZE[0] * i + p_del_x, PLATFORMSIZE[1] + p_del_y});
				gamemap.put(p.getID(), p);
				for (Platform m : p.getSubmodels()){
					gamemap.put(m.getID(), m);
				}
				continue;
			}
			Platform p = new Platform(new float[]{PLATFORMSIZE[0] * i + p_del_x, PLATFORMSIZE[1] + p_del_y},
				(int)(Math.random() * 3));
			gamemap.put(p.getID(), p);
			
			// ADD ORCS
			if (Math.random() < ORCPLACEPROB) {
				float[] o_del = {genrand() * ORCVARIANCE[0], (float)Math.random() * ORCVARIANCE[1]};
				float[] orcPos = {PLATFORMSIZE[0] * i + ORCOFFSET[0] + p_del_x + o_del[0],
					PLATFORMSIZE[1] + ORCOFFSET[1] + p_del_y - o_del[1]};
				
				//Orc Type;
				Orc orc = (Math.random() < GREENORCPROB) ?
					new GreenOrc(orcPos, this) : new RedOrc(orcPos, this);
				gamemap.put(orc.getID(), orc);
			}
			
			// ADD COINS
			float c_del_x = genrand() * COINVARIANCE;
			if (Math.random() < COINPROB) {
				Coin c1 = new Coin(new float[]{PLATFORMSIZE[0] * i + p_del_x + c_del_x + COINOFFSET[0],
					PLATFORMSIZE[1] + COINOFFSET[1] + p_del_y});
				gamemap.put(c1.getID(), c1);
				if (Math.random() < DOUBLECOINPROB) {
					Coin c2 = new Coin(new float[]{PLATFORMSIZE[0] * i + p_del_x + c_del_x + COINOFFSET[0] + 35,
						PLATFORMSIZE[1] + COINOFFSET[1] + p_del_y});
					gamemap.put(c2.getID(), c2);
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
				gamemap.put(c.getID(), c);
			}
		}
	}
	
	GameInstance () {
		resurrection = false;
		init_gamemap();
	}
	
	public LinkedHashMap<UUID, GameObject> get_gameMap () {
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
	
	public int resurrect () {
		/*
		 * Method for resurrection: -1 for coins less than cost, 0 for success, and 1 for no platform being available
		 * TODO: this is functional, but O(n)
		 */
		if(getCoin_count() < RESURRECTIONCCOST) return -1;
		double XPos = hero.getPos()[0];
		
		//find an appropriate platform
		List<GameObject> map = new ArrayList<>(this.get_gameMap().values());

		//iterating over map
		Iterator<GameObject> iter = map.iterator();

		double viableXPos = 0;

		boolean falling = false;

		// TODO: would be cleaner with a for-each loop, although flexing iterator DP is kinda cool.
		while(iter.hasNext()){
			GameObject iterating = iter.next();

			if((iterating instanceof Platform || (iterating instanceof FallingPlatform
					&&!((FallingPlatform)iterating).getCollapsing()))
					&& iterating.getPos()[0]> XPos && iterating.getVel()[1] < 0.1){
				// TODO: doesnt work because of the way fallingplatform is configured. could be fixed but im not too sure.
				//it is a Platform!
				viableXPos = iterating.getPos()[0];
				break;
			}
			if(iterating instanceof FallingPlatform && ((FallingPlatform)iterating).getCollapsing()){
				falling = true;
			}

		}

		viableXPos = Math.ceil(viableXPos);

		//some basic stuff to ensure it doesn't break the game
		if(viableXPos == 0){
			return 1;
		}

		if(falling) viableXPos+=200;

		hero.clearMoveCount();
		hero.setPos((float)viableXPos, 20);
		hero.set_vel(0,0);
		hero.set_vel(1,0);
		
		// TODO dont need to be a separate method imo
		setResurrection();
		resurrectionDeduction();

		return 0;
	}

	private void resurrectionDeduction(){
		this.add_coins(-RESURRECTIONCCOST);
	}
	public void game_over () {
		//todo method
	}
	
	public void win () {
		//todo method
	}

	private void setResurrection(){
		resurrection = true;
		this.hero.resurrect();
	}

	public boolean hasResurrected() {
		return resurrection;
	}
}
