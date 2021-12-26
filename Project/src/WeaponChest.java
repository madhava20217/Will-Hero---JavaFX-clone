public class WeaponChest extends Chest{
	private final Weapon weapon;
	
	WeaponChest (float[] pos) {
		super(pos);
		weapon = new Weapon((int)(2*Math.random()));
	}
	
	@Override
	public void collect_chest (Hero hero) {
		for (Weapon w: hero.getAvailable_weapons()){
			if (w.equals(weapon)){
				w.upgrade();
				return;
			}
		}
		hero.getAvailable_weapons().add(weapon);
	}
}
