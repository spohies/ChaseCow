import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class FloorMap {
    int mapID;
    private Point TLLocation;
    HashSet<Wall> rectWalls;
    TreeSet<Wall> innerWalls;
    HashSet<Triangle> triWalls;
    HashSet<Cow> cows;
    ArrayList<Weapon> weapons;
    ArrayList<Collectible> collectibles;
    ArrayList<NPC> npcs;
    BufferedImage bg;
    ArrayList<Door> doors;
    private Point[] cowLocations;

    // public FloorMap(int mapID, Point TLLocation, BufferedImage bg, HashSet<Wall> rectWalls, HashSet<Triangle> triWalls) {
    //     this.mapID = mapID;
    //     this.TLLocation = TLLocation;
    //     this.bg = bg;
    //     this.npcs = npcs;
    //     this.cows = new HashSet<>();
    //     this.doors = new ArrayList<>();
    //     this.rectWalls = rectWalls;
    //     this.triWalls = triWalls;
    //     this.innerWalls = new TreeSet<>();
    //     this.weapons = new ArrayList<>();
    //     this.collectibles = new ArrayList<>();
    //     this.weapons = new ArrayList<>();
    //     this.collectibles = new ArrayList<>();
    // }

    public FloorMap(Point TLLocation, HashSet<Wall> rectWalls, HashSet<Triangle> triWalls, TreeSet<Wall> innerWalls, BufferedImage bg,
            ArrayList<Door> doors, HashSet<Cow> cows, ArrayList<NPC> npcs, ArrayList<Weapon> weapons, ArrayList<Collectible> collectibles) {
        this.TLLocation = TLLocation;
        this.rectWalls = rectWalls;
        this.triWalls = triWalls;
        this.innerWalls = innerWalls;
        this.bg = bg;
        this.doors = doors;
        this.cows = cows;
        if (npcs != null) {
            this.npcs = npcs;
        }
        this.weapons = weapons;
        this.collectibles = collectibles;
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
                this.removeCow(cow);
            }
        }
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

    public Point getTLLocation() {
        return TLLocation;
    }

    public void setTLLocation(Point TLLocation) {
        this.TLLocation = TLLocation;
    }

    public ArrayList<NPC> getNPCs() {
        return npcs;
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }
    
    public TreeSet<Wall> getInnerWalls() {
        return innerWalls;
    }

    public void addCow(Cow cow) {
        this.cows.add(cow);
    }
    public void removeCow(Cow cow) {
        this.cows.remove(cow);
    }
}
