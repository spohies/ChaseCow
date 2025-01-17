import java.awt.image.*;
import java.awt.*;
abstract class Item implements Comparable{
    private String description;
    private BufferedImage image;
    private Point gamePos;
    private String name;
    private int reach;

    public Item(String name, String description, BufferedImage image, Point gamePos, int reach) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.gamePos = gamePos;
        this.reach = reach;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public Point getGamePos() {
        return this.gamePos;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public String getName() {
        return this.name;
    }

    public int getReach() {
        return reach;
    }

    public Rectangle getRect() {
        return new Rectangle (gamePos.x, gamePos.y, image.getWidth(), image.getHeight());
    }

    public void setName(String name) {
        this.name = name;
    }

    public int compareTo(Item i) {
        if (i == null) {
            throw new NullPointerException("Compared item is null");
        }
        return this.name.compareTo(i.name);
    }
}
