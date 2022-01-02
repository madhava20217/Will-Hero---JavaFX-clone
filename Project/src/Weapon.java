import java.io.Serial;
import java.io.Serializable;

public final class Weapon implements Serializable{
	private int level;
	private String sprite;
	private float[] size;
	private final int cat;
	
	@Serial
	private static final long serialVersionUID = 33;
	
	Weapon (int _cat) {
		level = 1;
		cat = _cat;
		switch (_cat) {
			case 0 -> {
				sprite = "images/weapon_knife.png";
				size = new float[]{69, 16};
			}
			case 1 -> {
				sprite = "images/weapon_shuriken.png";
				size = new float[]{30, 30};
			}
		}
	}
	
	public float[] getSize () {
		return size;
	}
	
	public String getSprite () {
		return sprite;
	}
	
	public int getLevel() {return level;}
	
	public void upgrade () {
		if(level < 3) {
			level++;
		}
	}
	
	@Override
	public boolean equals (Object obj) {
		if (obj.getClass() != Weapon.class) return false;
		return ((Weapon)obj).cat == cat;
	}
	
	public void use (Hero hero) {
		float[] heropos = hero.getPos();
		FlyingWeapon p = new FlyingWeapon(new float[]{heropos[0] + 25, heropos[1]}, new float[]{12, 0}, 0, this);
		hero.getCurrent_game().register(p);
		if (level >= 2) {
			FlyingWeapon q = new FlyingWeapon(new float[]{heropos[0] + 25, heropos[1] - 25}, new float[]{11.5F, -0.5F}, -7,
				this);
			hero.getCurrent_game().register(q);
		}
		if (level >= 3) {
			FlyingWeapon r = new FlyingWeapon(new float[]{heropos[0] + 25, heropos[1] + 25}, new float[]{11.5F, 0.5F}, 7,
				this);
			hero.getCurrent_game().register(r);
		}
	}
}

