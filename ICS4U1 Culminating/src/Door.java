import java.awt.*;

public class Door {
    private Rectangle doorRect;
    private Integer mapDest;
    private Point exitPos; // spawn location in the map that you are teleporting to 
    private boolean interactable = false;
    
    public Door (Rectangle doorRect, Integer mapDest, Point exitPos) {
        this.doorRect = doorRect;
        this.mapDest = mapDest;
        this.exitPos = exitPos;
    }

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

    public boolean interactable() {
        return interactable;
    }

}
