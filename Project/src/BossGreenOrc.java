public final class BossGreenOrc extends Orc{
	
	private static final String loc = "images/orc_boss.png";
	private static final int HP = 10;
	private boolean isAlive;
	private Boolean hit;

	//TODO: other inits in orc abstract
	BossGreenOrc (float[] position, GameInstance instance) {
		super(position, 10, loc, new float[]{109, 112}, HP, instance);
		isAlive = true;
		hit = false;
	}

	@Override
	protected boolean canGetHit(){
		if(!getCurrent_game().bossTime()){
			this.hit = true;
			return true;
		}
		return hit;
	}

	public boolean isAlive(){
		return this.isAlive;
	}
}
