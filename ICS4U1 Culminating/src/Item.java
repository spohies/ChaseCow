import java.awt.image.*;
abstract class Item {
    private String description;
    private BufferedImage image;

    public Item(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription (String description) {
        this.description = description;
    }
}
