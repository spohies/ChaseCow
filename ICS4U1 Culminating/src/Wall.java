import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.*;


public class Wall implements Comparable<Wall> {

    private Point TLLocation;
    private BufferedImage image;
    private Rectangle rect;
    
    public Wall (Point TLLocation, BufferedImage image, Rectangle rect) {
        this.TLLocation = TLLocation;
        this.image = image;
        this.rect = rect;

        // System.out.println("Wall x: " + rect.x + " y: " + rect.y);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setTLLocation(Point tl) {
        this.TLLocation = tl;
    }

    public Rectangle getRect() {
        return rect;
    }

    public Point getGamePos() {
        return new Point(rect.x, rect.y);
    }

    public void setGamePos(int x, int y) {
        this.rect.setLocation(x, y);
        System.out.println("Wall x: " + x + " y: " + y);
    }

    public int compareTo(Wall w){ 
        int yComparison = Integer.compare(this.rect.y, w.rect.y);
        if (yComparison != 0) {
            return yComparison;
        }
        return Integer.compare(this.rect.x, w.rect.x);
    }
}
