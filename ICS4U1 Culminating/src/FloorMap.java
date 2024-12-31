import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
public class FloorMap {
    private Point location;
    HashSet<Rectangle> walls;
    HashSet<Cow> cows;
    ArrayList<Item> items;
    BufferedImage bg;

    public FloorMap() {
        cows = new HashSet<>();
        walls = new HashSet<>();
        items = new ArrayList<>();
    }
    
    public void updateCows(Player player) {
        Iterator<Cow> iterator = cows.iterator();

        while (iterator.hasNext()) {
            Cow cow = iterator.next();
            double distance = player.getPosition().distance(cow.getX(), cow.getY());

            if (distance < 100) {
                cow.followPlayer(player);
            }

            if (!cow.isAlive()) {
                iterator.remove();
            }
        }
    }
}
