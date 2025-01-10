import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
public class FloorMap {
    private Point TLLocation;
    HashSet<Rectangle> rectWalls;
    HashSet<Triangle> triWalls;
    HashSet<Cow> cows;
    ArrayList<Item> items;
    // NPC[] npcs = new NPC[5];
    BufferedImage bg;
    private Rectangle[] doors;

    public FloorMap(Point TLLocation, HashSet<Rectangle> rectWalls, HashSet<Triangle> triWalls, BufferedImage bg, Rectangle[] doors, HashSet<Cow> cows) {
        this.TLLocation = TLLocation;
        this.cows = cows;
        items = new ArrayList<>();
        this.bg = bg;
        this.rectWalls = rectWalls;
        this.triWalls = triWalls;
        this.doors = doors;
    }
    
    public void updateCows(Player player) {
        Iterator<Cow> iterator = this.cows.iterator();

        while (iterator.hasNext()) {
            Cow cow = iterator.next();

            // ADJUST THESE FOR MIDDLE OF COW ??
            double distance = player.getGamePos().distance(cow.getGamePos().x, cow.getGamePos().y);
            
            if (distance < 300) {
                // System.out.println("following");
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
        return TLLocation;
    }

    public void setTLLocation(Point TLLocation) {
        this.TLLocation = TLLocation;
    }

    public HashSet<Triangle> getTriWalls() {
        return triWalls;
    }

    public void setTriWalls(HashSet<Triangle> walls) {
        this.triWalls = walls;
    }
    public HashSet<Rectangle> getRectWalls() {
        return rectWalls;
    }

    public void setRectWalls(HashSet<Rectangle> walls) {
        this.rectWalls = walls;
    }

    public HashSet<Cow> getCows() {
        return cows;
    }

    public void setCows(HashSet<Cow> cows) {
        this.cows = cows;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public BufferedImage getBG() {
        return bg;
    }
}
