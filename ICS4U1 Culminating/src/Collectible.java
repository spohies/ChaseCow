// import java.nio.Buffer;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Collectible extends Item{
    private BufferedImage image;
    private Point gamePos; 


    public Collectible(String name, String description, BufferedImage image, Point gamePos, int reach) {    
        super(name, description, image, gamePos, reach);
    }


    @Override
    public int compareTo(Object arg0) {
        return 0;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
