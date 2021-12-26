public class RedOrc extends Orc{
	private static final String loc1 = "images/orc_red_angry.png";
	private static final String loc2 = "images/orc_red_ring.png";
	private static final int HP = 10;
	
	//TODO: other inits in orc abstract
	RedOrc (float[] position, GameInstance instance) {
		super(position, (float)(5+4*Math.random()), (Math.random()>0.5)?loc1:loc2, new float[]{60, 60}, HP, instance);
	}
}
