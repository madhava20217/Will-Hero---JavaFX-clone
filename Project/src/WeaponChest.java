public class WeaponChest extends Chest{
	private Weapon weapon;
	
	WeaponChest (float[] pos) {
		super(pos, "images/chest_weapon_closed.png");
	}
	
	@Override
	public void collect_chest (Hero hero) {
		if(isCollected()) return;
		weapon = new Weapon((int)(GameController.getGameInstance().getHero().get_helmet().get_weapon_options().length*Math.random()));
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
