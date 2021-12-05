public abstract class Orc extends GameObject implements Collidable {
    private int hit_endurance;
    private boolean is_alive;
    private int coin_drop;
    private String[] dialogues;
    private float size;
    private String sprite;

    Orc(){
        //todo constructor
    }

    @Override
    public void collide(Collidable other) {
        //todo
    }

    public void die(Hero hero){
        //todo
    }

    public void get_hit_by_weapon(FlyingWeapon weapon){
        //todo
    }

    public void is_alive(){
        //todo
    }

    public void smash_hero(Hero hero){
        //todo
    }
    public void call_out(){
        //todo
    }

}
