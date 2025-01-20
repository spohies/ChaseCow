// Collectible Object
// Extends Item
// Description: collectible object that can be picked up by the player
import java.awt.*;
import java.awt.image.BufferedImage;

public class Collectible extends Item{

    // Constructor
    public Collectible(String name, String description, BufferedImage image, Point gamePos, int reach) {    
        super(name, description, image, gamePos, reach);
    }

}