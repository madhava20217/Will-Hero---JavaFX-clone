import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class Orc extends GameObject{
	private int hit_endurance;
	private final int coin_drop;
	private String[] dialogues;
	private float size;
	private String sprite;
	private final GameInstance current_game;
	
	private static final double CallOutProbability = 0.0005;
	
	//TODO: other inits
	Orc (float[] position, float mass, String spritee, float[] size, int HP, GameInstance instance) {
		super(position, new float[]{0, 3}, new float[]{0, 0}, mass, true, true, spritee, size);
		// TODO: im pretty sure we have no reason to store this in orc
		this.current_game = instance;
		this.hit_endurance = HP;
		coin_drop = (int)(Math.random()*4)+1;
	}

	@Override
	public void move () {
		decelerate(0, 1);
		super.move();
		if (super.is_out_of_bounds() && this.getPos()[1] > 500){
			die();
		}
	}
	
	public void die () {
		float[] pos = this.getPos();
		this.remove();
		Coin c = new Coin(pos, 0);

		current_game.add_item(c);
		c.collide(current_game.getHero());
		current_game.add_coins(this.coin_drop);
	}
	
	public void get_hit_by_weapon () {
		hit_endurance--;
		if(hit_endurance == 0){
			die();
		}
	}
	
	public void smash_hero (Hero hero) {
		// Todo: related to hero death animation, and kill quip
	}
	
	public void call_out () {
		//todo
		System.out.println("CALLED OUT!");
	}
	@Override
	public void refresh () {
		super.refresh();
		//call out dialogue, probability 0.05
		if (Math.random() < CallOutProbability) {
			call_out();
		}
	}
}
