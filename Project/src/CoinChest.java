public class CoinChest extends Chest{
	private final int coins_number;
	
	CoinChest (float[] pos) {
		super(pos, "images/chest_coin_closed.png");
		coins_number = (int)(4 * Math.random() + 1);
	}
	
	@Override
	public void collect_chest (Hero hero) {
		if (isCollected()) return;
		hero.add_coins(coins_number);
		super.collect_chest(hero);
	}
}
