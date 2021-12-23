public abstract class Chest extends GameObject{

    Chest(float[] pos){
			super(pos, new float[]{0,0},new float[]{0,0},0,false,false,"images/chest_closed.png",new float[]{80,56});
    }

    public abstract void collect_chest(Hero hero);
}
