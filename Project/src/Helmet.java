import java.io.Serial;
import java.io.Serializable;

public final class Helmet implements Serializable{
	@Serial
	private static final long serialVersionUID = 33;
	
	private final Weapon[] weapon_options;
	
	Helmet () {
		weapon_options = new Weapon[]{new Weapon(0), new Weapon(1)};
	}
	
	public Weapon[] get_weapon_options () {
		return weapon_options;
	}
}
