public abstract class Chest extends GameObject implements Collidable{
	
	Chest (float[] pos) {
		super(pos, new float[]{0, 0}, new float[]{0, 0}, 0, false, false, "images/chest_closed.png", new float[]{80, 56});
	}
	
	public abstract void collect_chest (Hero hero);
	
	@Override
	public void collide (GameObject other) {
		if(!(other instanceof Hero)) return;
		collect_chest((Hero)other);
	}
}
