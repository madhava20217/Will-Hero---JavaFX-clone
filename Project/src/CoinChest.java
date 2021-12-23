public class CoinChest extends Chest{

    private int coins_number;

    CoinChest(float[] pos){
			super(pos);
			coins_number = (int)(4*Math.random()+1);
    }

    @Override
    public void collect_chest(Hero hero) {
        //todo
    }
}
