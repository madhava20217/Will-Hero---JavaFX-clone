public final class BossGreenOrc extends Orc{
	
	private static final String loc = "images/orc_boss.png";
	private static final int HP = 25;

	//TODO: other inits in orc abstract
	BossGreenOrc (float[] position, GameInstance instance) {
		super(position, 8F, loc, new float[]{109, 112}, HP, instance);
	}

	@Override
	public void call_out(){
		//boss orc doesn't call out!
		return;
	}

	@Override
	public void call_out_death(){
		// boss orc doesn't call out!
		return;
	}

	@Override
	public void die(){
		super.die();
		GameController.getGameInstance().win();
	}
}
