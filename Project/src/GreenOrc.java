public class GreenOrc extends Orc{
	private static final String loc1 = "images/orc_green_scar.png";
	private static final String loc2 = "images/orc_green_ring.png";
	private static final int HP = 4;
	
	GreenOrc (float[] position, GameInstance instance) {
		super(position, (float)(4+3*Math.random()), (Math.random()>0.5)?loc1:loc2, new float[]{60, 60}, HP, instance);
	}
}
