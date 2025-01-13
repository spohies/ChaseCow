import java.awt.image.*;
import java.awt.*;
abstract class Item {
    private String description;
    private BufferedImage image;
    private Point gamePos;
    private String name;

    public Item(String name, String description, BufferedImage image, Point gamePos) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.gamePos = gamePos;
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
}
