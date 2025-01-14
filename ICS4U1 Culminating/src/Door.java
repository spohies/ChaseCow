import java.awt.*;

public class Door {
    private Rectangle doorRect;
    private int mapDest;
    private Point exitPos; // spawn location in the map that you are teleporting to 

    public Door (Rectangle doorRect, int mapDest, Point exitPos) {
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
}
