import java.awt.image.*;
import java.awt.*;

public class NPC {
    Point gamePos;
    BufferedImage image;
    static String[] dialogue;
    static boolean itemGiven = false;

    public NPC(Point location, String[] dialogue, BufferedImage image) {
        this.gamePos = location;
        this.dialogue = dialogue;
        this.image = image;
    }

    public void interact() {
        if (!itemGiven) {
            for (int i = 0; i < dialogue.length; i++) {
                System.out.println(dialogue[i]);
            }
            itemGiven = true; // mark as interacted to prevent repeating dialogue
        } else {
            System.out.println("get out of here already");
        }
    }

    public Point getGamePos() {
        return this.gamePos;
    }

}