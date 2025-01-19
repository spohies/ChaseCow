// wall object
// Description: for collision detection!
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.*;

public class Wall implements Comparable<Wall> {

    private Point TLLocation;
    private BufferedImage image;
    private Rectangle rect;
    
    // constructor
    public Wall(Point TLLocation, BufferedImage image, Rectangle rect) {
        this.TLLocation = TLLocation;
        this.image = image;
        this.rect = rect;
    }

    // Getters
    public BufferedImage getImage() {
        return image;
    }

    public Point getTLLocation() {
        return TLLocation;
    }

    public Rectangle getRect() {
        return rect;
    }

    public Point getGamePos() {
        return new Point(rect.x, rect.y);
    }

    // Setters
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setTLLocation(Point tl) {
        this.TLLocation = tl;
    }

    public void setGamePos(int x, int y) {
        this.rect.setLocation(x, y);
        System.out.println("Wall x: " + x + " y: " + y);
    }

    // Description: compares the y values of the walls
    // Parameter: wall object
    // Return: the integer value of the comparison
    public int compareTo(Wall w) { 
        int yComparison = Integer.compare(this.rect.y, w.rect.y);
        if (yComparison != 0) {
            return yComparison;
        }
        return Integer.compare(this.rect.x, w.rect.x);
    }
}
