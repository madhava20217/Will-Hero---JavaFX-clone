import javafx.scene.Node;

public class Hero extends GameObject{
	
	private Weapon[] available_weapons;
	private Weapon current_weapon;
	private Helmet current_helmet;
	private boolean is_alive;
	private boolean resurrected;
	private final GameInstance current_game;
	
	private boolean CROSSING;
	private int moveCnt;
	private int distance;
	
	Hero (GameInstance gi) {
		super(new float[]{100, 250}, new float[]{0, -5}, new float[]{0, 0}, 2, true, true, "images/knight.png",
			new float[]{33, 44});
		moveCnt = 0;
		CROSSING = false;
		is_alive = true;
		current_game = gi;
	}
	
	public int getDistance () {
		return distance;
	}
	
	public GameInstance getCurrent_game () {
		return current_game;
	}
	
	public void launch () {
		if (moveCnt >= 5 || (Math.abs(get_vel(0)) > 1)) return;
		moveCnt++;
		set_vel(0, 10);
	}
	
	@Override
	public void move () {
		float[] pos = getPos();
		float[] vel = getVel();
		decelerate(0, 0.4F); // air resistance
		
		if (CROSSING) { // after crossing over a co-ordinate
			// applies inertia to snapping after it is done
			CROSSING = false;
			decelerate(0, 20F); // apply very very very strong air resistance
		} else if (((vel[0] + pos[0]) % 100 < 50 && pos[0] % 100 > 50) && vel[0] > 0) { // crossing LTR
			// 9.5 instead of 10 to avoid floating point problems, 9.9 will work too
			if (vel[0] < 9.5) { // with less than launch velocity
				CROSSING = true;
				distance++;
				set_vel(0, 100 - pos[0] % 100); // clip to co-ordinate
			}
		} else if (((vel[0] + pos[0]) % 100 > 50 && pos[0] % 100 < 50) && vel[0] < 0) { // crossing RTL
			CROSSING = true;
			// distance--;  //TODO: check this more properly
			set_vel(0, -pos[0] % 100); // clip to co-ordinate
		}
		// after this is called, if we have clipped velocity, then hero's position will be exactly n.100
		super.move();
		Node model = getModel();
		if (model.getTranslateX() > 0) {
			GameController.panCamera(((pos[0] - getP0()[0] - GameController.getPanCam())/ 8));
		}
		if (is_out_of_bounds() && pos[1] > 50) { // went out of bounds from below
			die();
		}
	}
	
	@Override
	public void bounce (GameObject other, float e) {
		float[] overlaps = this.getOverlaps(other);
		if (overlaps[0] > overlaps[1]) { // if collision is vertical
			if(this.getPos()[1] > other.getPos()[1]){
				// if hero is below whatever he is colliding with, he will die
				die();
				return;
			}
			moveCnt = 0;
			if (other instanceof Platform) { // if bouncing on platform
				set_vel(1, -5);
				return;
			}
		}
		super.bounce(other, e);
	}
	
	public void add_coins (int coin_count) {
		current_game.add_coins(coin_count);
	}
	
	public void die () {
		is_alive = false;
	}
	
	public Helmet get_helmet () {
		return current_helmet;
	}
	
	public boolean is_alive () {
		return is_alive;
	}
	
	public void resurrect () {
		is_alive = true;
	}
	
	public void equip_helmet (Helmet helm) {
		current_helmet = helm;
	}
	
	public void equip_weapon (Weapon weapon) {
		current_weapon = weapon;
	}
}
