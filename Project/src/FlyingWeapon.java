public class FlyingWeapon extends GameObject implements Collidable{
	private final Weapon category; // currently unused, usable as a Strategy DP technically.
	
	FlyingWeapon (float[] pos, float[] vel, float angle, Weapon cat) {
		super(pos, vel, new float[]{0, 0}, 0.5F, false, false, cat.getSprite(), cat.getSize());
		category = cat;
		this.getModel().setRotate(angle);
	}
	
	@Override
	public void move () {
		super.move();
		if (!this.isRendered()) {
			// deleted when it goes off-screen
			this.remove();
		}
	}
	
	@Override
	public void collide (GameObject other) {
		if (other instanceof Hero) return;  // since it spawns in hero, we don't want it to despawn immediately.
		if (other instanceof Orc) {
			((Orc)other).get_hit_by_weapon();
			bounce(other, 1);
		}
		this.remove();
	}
}
