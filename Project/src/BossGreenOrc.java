import javafx.animation.PauseTransition;
import javafx.util.Duration;

public final class BossGreenOrc extends Orc{
	
	private static final String loc = "images/orc_boss.png";
	private static final int HP = 25;
	
	BossGreenOrc (float[] position) {
		super(position, 8F, loc, new float[]{109, 112}, HP);
	}
	
	@Override
	public void call_out () {
		//boss orc doesn't call out!
	}
	
	@Override
	public void call_out_death () {
		// boss orc doesn't call out!
	}
	
	@Override
	public void die () {
		GameController.pause();
		super.die();
		PauseTransition delay = new PauseTransition(Duration.seconds(1));
		delay.setOnFinished(e->{
			GameController.resume();
			GameController.getGameInstance().win();
		});
		delay.play();
	}
}
