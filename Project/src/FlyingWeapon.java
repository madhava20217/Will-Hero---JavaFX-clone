public class FlyingWeapon extends GameObject implements Collidable{
	
	private Weapon category;
	
	FlyingWeapon (float[] pos, float[] vel, float angle, Weapon cat) {
		super(pos, vel, new float[]{0,0}, 0.5F, false, false, cat.getSprite(), cat.getSize());
		this.getModel().setRotate(angle);
	}
	
	@Override
	public void move(){
		super.move();
		if (!this.isRendered()){
			this.remove();
		}
	}
	
	@Override
	public void collide (GameObject other) {
		if (other instanceof Hero) return;
		if (other instanceof Orc){
			((Orc)other).get_hit_by_weapon();
			bounce(other, 1);
		}
		this.remove();
	}
}
