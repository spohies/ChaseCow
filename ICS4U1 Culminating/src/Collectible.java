// import java.nio.Buffer;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Collectible extends Item{
    private boolean isComsumable;
    private int bonusHP;
    private BufferedImage image;
    private Point gamePos; 


    public Collectible(String name, String description, boolean isComsumable, int bonusHP, BufferedImage image, Point gamePos) {    
        super(name, description, image, gamePos);
        this.isComsumable = isComsumable;
        this.bonusHP = bonusHP;
    }

    public boolean getComsumability() {
        return this.isComsumable;
    }

    public int getBonusHP() {
        return this.bonusHP;
    }
}
