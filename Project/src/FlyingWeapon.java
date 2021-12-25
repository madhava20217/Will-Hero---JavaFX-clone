public class FlyingWeapon extends GameObject implements Collidable{
	
	private Weapon category;
	
	FlyingWeapon (float[] pos, float[] vel, float angle, Weapon cat) {
		super(pos, vel, new float[]{0,0}, 1, false, false, cat.getSprite(), cat.getSize());
		this.getModel().setRotate(angle);
	}
	
	@Override
	public void derender(){
		// remove weapon when it goes off screen
		super.derender();
		GameController.getGameInstance().remove_ID(this.getID());
	}
	
	@Override
	public void collide (GameObject other) {
		if (other instanceof Orc){
			((Orc)other).get_hit_by_weapon(this);
		}
		this.derender();
	}
}
