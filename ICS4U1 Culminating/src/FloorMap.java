import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
public class FloorMap {
    private Point TLlocation;
    HashSet<Rectangle> walls;
    HashSet<Cow> cows;
    ArrayList<Item> items;
    // NPC[] npcs = new NPC[5];
    BufferedImage bg;

    public FloorMap(Point TLlocation, HashSet<Rectangle> walls, BufferedImage bg) {
        this.TLlocation = TLlocation;
        cows = new HashSet<>();
        items = new ArrayList<>();
        this.bg = bg;
        this.walls = walls;
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

        // for (int i = 0; i < npcs.length; i++) {
        //     double distance = player.getPosition().distance(npcs[i].getLocation().getX(), npcs[i].getLocation().getY());
        //     if (distance < 30) {
        //         NPC.interact();
        //     }
        // }
    }

    public Point getTLLocation() {
        return TLlocation;
    }

    public void setTLlocation(Point TLlocation) {
        this.TLlocation = TLlocation;
    }
}
