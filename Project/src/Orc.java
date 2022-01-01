public abstract class Orc extends GameObject{
	private int hit_endurance;
	private final int coin_drop;
	protected boolean isTaunting;				//variable in order to store information about the status of taunt
	
	private static final double CallOutProbability = 0.002;
	
	Orc (float[] position, float mass, String spritee, float[] size, int HP) {
		super(position, new float[]{0, 3}, new float[]{0, 0}, mass, true, true, spritee, size);
		this.hit_endurance = HP;
		coin_drop = (int)(Math.random()*4)+1;
		isTaunting = false;
	}
	
	abstract void call_out ();
	abstract void call_out_death();
	
	@Override
	public void move () {
		decelerate(0, 1);
		super.move();
		if (super.is_out_of_bounds() && this.getPos()[1] > 500) {
			die();
		}
	}
	
	public void die () {
		float[] pos = this.getPos();
		this.remove();
		Coin c = new Coin(pos, 0);
		
		GameController.getGameInstance().register(c);
		c.collide(GameController.getHero());
		GameController.getHero().add_coins(this.coin_drop);
	}
	
	public void get_hit_by_weapon () {
		hit_endurance--;
		if (hit_endurance == 0) {
			die();
		}
	}
	
	public void smash_hero (Hero hero) {
		// Todo: related to hero death animation, and kill quip
	}
	
	@Override
	public void refresh () {
		super.refresh();
		//call out dialogue, probability 0.1
		if (Math.random() < CallOutProbability) {
			call_out();
		}
	}

	public void setUntaunt(){
		this.isTaunting = false;
	}
}
