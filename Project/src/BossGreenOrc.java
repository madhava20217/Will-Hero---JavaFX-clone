public class BossGreenOrc extends Orc{

    private static final String loc = "images/orc_boss.png";

    //TODO: other inits in orc abstract
    BossGreenOrc(float[] position, GameInstance instance){
        super(position, 10, loc, new float[]{109,112}, instance);
    }


}
