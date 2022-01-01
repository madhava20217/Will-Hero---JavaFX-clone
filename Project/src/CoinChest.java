public class CoinChest extends Chest{
	private final int coins_number;
	
	CoinChest (float[] pos) {
		super(pos, "images/chest_coin_closed.png");
		coins_number = (int)(4 * Math.random() + 1);
	}
	
	@Override
	public void get_collected (Hero hero) {
		if (isCollected()) return;
		super.get_collected(hero);
		hero.add_coins(coins_number);
	}
}
