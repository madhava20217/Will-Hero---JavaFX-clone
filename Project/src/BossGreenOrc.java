public class BossGreenOrc extends Orc{

    private static final String loc = "images/orc_boss.png";
    private static final int HP = 8;
    //TODO: other inits in orc abstract
    BossGreenOrc(float[] position, GameInstance instance){
        super(position, 10, loc, new float[]{109,112}, HP,instance);
    }


}
