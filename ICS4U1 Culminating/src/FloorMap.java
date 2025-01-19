// MAP OBJECT
// where all the fun stuff happens (miserbale)
// LOTS OF SETS AND STUFF
// Description: map class that contains all the walls, cows, npcs, weapons, and collectibles in the map

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

    // Constructor
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

    // Getters
    public HashSet<Triangle> getTriWalls() {
        return triWalls;
    }

    public HashSet<Wall> getRectWalls() {
        return rectWalls;
    }

    public HashSet<Cow> getCows() {
        return cows;
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public ArrayList<Collectible> getItems() {
        return collectibles;
    }

    public BufferedImage getBG() {
        return bg;
    }

    public Point getTLLocation() {
        return TLLocation;
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

    public int getMapID() {
        return mapID;
    }

    // Setters
    public void setTriWalls(HashSet<Triangle> walls) {
        this.triWalls = walls;
    }

    public void setRectWalls(HashSet<Wall> walls) {
        this.rectWalls = walls;
    }

    public void setCows(HashSet<Cow> cows) {
        this.cows = cows;
    }

    public void setTLLocation(Point TLLocation) {
        this.TLLocation = TLLocation;
    }

    // OTHER METHODS:

    // Description: updates the cows in the map
    // Parameters: player 
    // Returns: void
    public void updateCows(Player player) {

        // iterate through each cow
        Iterator<Cow> iterator = this.cows.iterator();

        while (iterator.hasNext()) {
            Cow cow = iterator.next();

            // calculate distance between player and cow using in-game positions
            double distance = player.getGamePos().distance(cow.getGamePos());

            // if the distance is less than 300, the cow will follow the player
            if (distance < 300) {
                cow.followPlayer(player);
            }

            // if the cow is dead, remove it from the map
            if (!cow.isAlive()) {
                iterator.remove();
            }
        }
    }

    // Description: removes the weapon from the map
    // Parameters: weapon
    // Returns: void
    public void removeItem(Weapon weapon) {
        weapons.remove(weapon);
    }

    // Description: removes the collectible from the map
    // Parameters: collectible
    // Returns: void
    public void removeItem(Collectible i) {
        collectibles.remove(i);
    }

    // Description: add cow to map
    // Parameters: cow
    // Returns: void
    public void addCow(Cow cow) {
        this.cows.add(cow);
    }

    // Description: remove cow from map
    // Parameters: cow
    // Returns: void
    public void removeCow(Cow cow) {
        this.cows.remove(cow);
    }
}
