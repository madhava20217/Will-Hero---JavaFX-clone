public class Weapon implements Collectable{
	private int level;
	private String sprite;
	private float[] size;
	
	Weapon(int cat){
		switch (cat){
			case 0 -> {sprite = "images/weapon_knife.png"; size = new float[]{86,20};}
			case 1 -> {sprite = "images/weapon_shuriken.png"; size = new float[]{20,20};}
		}
	}
	
	public float[] getSize () {
		return size;
	}
	
	public String getSprite () {
		return sprite;
	}
	
	public void get_collected (Hero hero) {
		//todo
	}
	
	public void use (Hero hero) {
		float[] heropos = hero.getPos();
		float[] pos = {heropos[0]+50, heropos[1]};
		FlyingWeapon p = new FlyingWeapon(pos,new float[]{12,0},0,this);
		// TODO: add item can be polished to process ID internally
		hero.getCurrent_game().add_item(p.getID(), p);
	}
	
	public void upgrade () {
		//todo
	}
	
}

