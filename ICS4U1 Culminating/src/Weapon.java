import java.awt.Point;
import java.awt.image.BufferedImage;

public class Weapon extends Item{
    private int reach;
    private int damage;

    public Weapon(String name, String description, int reach, int damage, BufferedImage image, Point gamePos) {
        super(name, description, image, gamePos);
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
