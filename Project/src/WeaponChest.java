public class WeaponChest extends Chest{
	private final Weapon weapon;
	
	WeaponChest (float[] pos) {
		super(pos, "images/chest_weapon_closed.png");
		weapon = new Weapon((int)(2*Math.random()));
	}
	
	@Override
	public void collect_chest (Hero hero) {
		if(isCollected()) return;
		super.collect_chest(hero);
		for (Weapon w: hero.getAvailable_weapons()){
			if (w.equals(weapon)){
				w.upgrade();
				return;
			}
		}
		hero.getAvailable_weapons().add(weapon);
		hero.cycleWeapon();
	}
}
