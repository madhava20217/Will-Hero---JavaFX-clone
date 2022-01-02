import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public final class Hero extends GameObject{
	private static final int MAXLAUNCHES = 4;
	private static final int LAUNCHVEL = 10;
	private final GameInstance current_game;
	
	private final Helmet current_helmet;
	private final List<Weapon> available_weapons;
	private Weapon current_weapon;
	private int weapon_index;
	
	private boolean is_alive;
	
	private boolean CROSSING;
	private int moveCnt;
	private int distance;
	
	Hero (GameInstance gi) {
		super(new float[]{100, 250}, new float[]{0, -5}, new float[]{0, 0}, 2, true, true, "images/knight.png",
			new float[]{33, 44});
		
		current_game = gi;
		current_helmet = new Helmet();
		available_weapons = new ArrayList<>();
		current_weapon = null;
		weapon_index = 0;
		
		is_alive = true;
		moveCnt = 0;
		CROSSING = false;
	}
	
	// GETTERS / MUTATORS
	public Helmet get_helmet () {
		return current_helmet;
	}
	
	public Weapon getCurrent_weapon() {return current_weapon;}
	
	public List<Weapon> getAvailable_weapons () {
		return available_weapons;
	}
	
	public GameInstance getCurrent_game () {
		return current_game;
	}
	
	public boolean is_alive () {
		return is_alive;
	}
	
	public int getDistance () {
		return distance;
	}
	
	public void add_coins (int coin_count) {
		current_game.add_coins(coin_count);
	}
	
	public void clearMoveCount () {
		this.moveCnt = 0;
	}
	
	public void die () {
		is_alive = false;
	}
	
	public void resurrect () {
		is_alive = true;
	}
	
	public void cycleWeapon () {
		if (available_weapons.size() == 0) return;
		weapon_index = (weapon_index + 1) % available_weapons.size();
		current_weapon = available_weapons.get(weapon_index);
		GameController.setWeapon(current_weapon);
	}
	
	public void launch () {
		if (moveCnt >= MAXLAUNCHES || (Math.abs(get_vel(0)) > 1)) return;
		moveCnt++;
		set_vel(0, LAUNCHVEL);
		if (current_weapon != null) current_weapon.use(this); // Strategy Design Pattern
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
			if (vel[0] < LAUNCHVEL - 0.3) { // with less than launch velocity
				CROSSING = true;
				distance++;
				set_vel(0, 100 - pos[0] % 100); // clip to co-ordinate
			}
		} else if (((vel[0] + pos[0]) % 100 > 50 && pos[0] % 100 < 50) && vel[0] < 0) { // crossing RTL
			CROSSING = true;
			set_vel(0, -pos[0] % 100); // clip to co-ordinate
		}
		// after this is called, if we have clipped velocity, then hero's position will be exactly n.100
		super.move();
		Node model = getModel();
		if (model.getTranslateX() > 0) {
			GameController.panCamera(((pos[0] - getP0()[0] - GameController.getPanCam()) / 8));
		}
		if (is_out_of_bounds() && pos[1] > 50) { // went out of bounds from below
			die();
		}
	}
	
	@Override
	public void bounce (GameObject other, float e) {
		float[] overlaps = this.getOverlaps(other);
		if (overlaps[0] > overlaps[1]) { // if collision is vertical
			if (this.getPos()[1] > other.getPos()[1]) {
				// if hero is below orc, he will die
				if (other instanceof Orc) {
					((Orc)other).call_out_death();
					die();
					return;
				}
			}
			clearMoveCount();
			if (other instanceof Platform) { // if bouncing on platform
				set_vel(1, -5);
				return;
			}
		}
		super.bounce(other, e);
	}
}
