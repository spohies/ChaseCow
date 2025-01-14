import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class FloorMap {
    private Point TLLocation;
    HashSet<Wall> rectWalls;
    HashSet<Triangle> triWalls;
    HashSet<Cow> cows;
    ArrayList<Weapon> weapons;
    ArrayList<Collectible> collectibles;
    NPC npc;
    BufferedImage bg;
    private Rectangle[] doors;
    private Point[] cowLocations;

    public FloorMap(Point TLLocation, HashSet<Wall> rectWalls, HashSet<Triangle> triWalls, BufferedImage bg,
            Rectangle[] doors, HashSet<Cow> cows, NPC npc, ArrayList<Weapon> weapons, ArrayList<Collectible> collectibles ) {
        this.TLLocation = TLLocation;
        this.cows = cows;
        this.bg = bg;
        this.rectWalls = rectWalls;
        this.triWalls = triWalls;
        this.doors = doors;
        this.weapons = weapons;
        this.collectibles = collectibles;
        if (npc != null) {
            this.npc = npc;
        }
    }

    public void updateCows(Player player) {
        Iterator<Cow> iterator = this.cows.iterator();

        while (iterator.hasNext()) {
            Cow cow = iterator.next();

            // Calculate distance using in-game positions
            double distance = player.getGamePos().distance(cow.getGamePos());

            // System.out.println(cow.getGamePos());
            // System.out.println(distance);
            if (distance < 300) {
                cow.followPlayer(player);
                // System.out.println(distance);
            }

            if (!cow.isAlive()) {
                iterator.remove();
            }
        }
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

    public HashSet<Wall> getRectWalls() {
        return rectWalls;
    }

    public void setRectWalls(HashSet<Wall> walls) {
        this.rectWalls = walls;
    }

    public HashSet<Cow> getCows() {
        return cows;
    }

    public void setCows(HashSet<Cow> cows) {
        this.cows = cows;
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public ArrayList<Collectible> getItems() {
        return collectibles;
    }

    public void removeItem(Weapon weapon) {
        weapons.remove(weapon);
    }

    // overloaded method
    public void removeItem(Collectible i) {
        collectibles.remove(i);
    }


    public BufferedImage getBG() {
        return bg;
    }

    
}
