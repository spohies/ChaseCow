// Door object
// Description: teleports player to a different map
import java.awt.*;

public class Door {
    private Rectangle doorRect; // hitbox of the door
    private Integer mapDest; // map that you are teleporting to
    private Point exitPos; // spawn location in the map that you are teleporting to 
    private boolean interactable = false; // if the player is within range of the door
    
    // Constructor
    public Door (Rectangle doorRect, Integer mapDest, Point exitPos) {
        this.doorRect = doorRect;
        this.mapDest = mapDest;
        this.exitPos = exitPos;
    }

    // Getters and Setters
    public Rectangle getDoorRect() {
        return doorRect;
    }

    public int getMapDest() {
        return mapDest;
    }

    public Point getExitPos() {
        return exitPos; 
    }

    public void setInteractable(boolean withinRange) {
        interactable = withinRange;
    }

    // Description: checks if the player is within range of the door
    // Parameters: n/a
    // Returns: true if the player is within range of the door
    public boolean interactable() {
        return interactable;
    }

}
