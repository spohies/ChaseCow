// abstract Item class
// superclass of weapons and collectibles
// Description: uhhh its just an item... name description position... thats it
import java.awt.image.*;
import java.awt.*;
abstract class Item implements Comparable<Item> {
    
    private String description;
    private BufferedImage image;
    private Point gamePos;
    private String name;
    private int reach;

    // Constructor
    public Item(String name, String description, BufferedImage image, Point gamePos, int reach) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.gamePos = gamePos;
        this.reach = reach;
    }

    // Getters
    public String getDescription() {
        return this.description;
    }

    public Point getGamePos() {
        return this.gamePos;
    }
    
    public Rectangle getRect() {
        return new Rectangle(gamePos.x, gamePos.y, image.getWidth(), image.getHeight());
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

    // Setters
    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Description: compares the names of two items
    // Parameters: item to compare to
    // Returns: int value of comparison
    public int compareTo(Item i) {
        if (i == null) {
            throw new NullPointerException("Compared item is null");
        }
        return this.name.compareTo(i.name);
    }
}
