public class Hero implements Collidable{

    private Weapon[] available_weapons;
    private Weapon current_weapon;
    private Helmet current_helmet;
    private boolean is_alive;
    private boolean resurrected;
    private GameInstance current_game;

    Hero(){
        //todo constructor
    }




    @Override
    public void collide(Collidable other) {
        //todo method
    }

    public void add_coins(int coin_count){
        //todo method
    }
    public void die(){
        //todo method
    }
    public Helmet get_helmet(){
        //todo method
        return null;
    }
    public boolean is_alive(){
        return is_alive;
    }
    public void resurrect(){
        //todo method
    }
    public void equip_helmet(Helmet helm){
        //todo method
    }
    public void equip_weapon(Weapon weapon){
        //todo method
    }
}
