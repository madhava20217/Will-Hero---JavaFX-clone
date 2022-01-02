public final class WeaponChest extends Chest{
	WeaponChest (float[] pos) {
		super(pos, "images/chest_weapon_closed.png");
	}
	
	@Override
	public void get_collected (Hero hero) {
		super.get_collected(hero);
		
		Weapon weapon = new Weapon((int)(GameController.getGameInstance().getHero().get_helmet().get_weapon_options()
			.length * Math.random()));
		for (Weapon w : hero.getAvailable_weapons()) {
			if (w.equals(weapon)) {
				w.upgrade();
				if(hero.getCurrent_weapon().equals(w)) GameController.setWeapon(w);
				return;
			}
		}
		hero.getAvailable_weapons().add(weapon);
		hero.cycleWeapon();
	}
}
