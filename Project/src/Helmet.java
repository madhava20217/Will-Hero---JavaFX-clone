import java.io.Serializable;

public class Helmet implements Serializable{
	private final Weapon[] weapon_options;
	
	Helmet () {
		weapon_options = new Weapon[]{new Weapon(0), new Weapon(1)};
	}
	
	public Weapon[] get_weapon_options () {
		return weapon_options;
	}
}
