public class GreenOrc extends Orc{
    private static final String loc1 = "images/orc_green_scar.png";
    private static final String loc2 = "images/orc_green_ring.png";

    //TODO: other inits in orc abstract
    GreenOrc(float[] position, GameInstance instance){
        super(position, 5, loc1, new float[]{60,60}, instance);
    }
}
