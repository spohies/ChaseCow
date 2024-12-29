public class Weapon extends Item{
    private int reach;
    private int damage;

    public Weapon(String description, int reach, int damage) {
        super(description);
        this.reach = reach;
        this.damage = damage;
    }

    public int getReach() {
        return this.reach;
    }

    public int getDamage() {
        return this.damage;
    }
}
