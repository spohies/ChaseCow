import java.awt.image.*;
import java.awt.*;

public class NPC {
    Point location;
    BufferedImage image;
    static String[] dialogue;
    static boolean itemGiven = false;
    
        public NPC (Point location, String[] dialogue, BufferedImage image) {
            this.location = location;
            this.dialogue = dialogue;
            this.image = image;
        }
    
        public static void interact() {
            if (!itemGiven) {
                for (int i = 0; i < dialogue.length; i++) {
                // display dialogue, press to see next line
            }
        } else {
            // display already interacted
        }
    }
    
    public Point getLocation() {
        return location;
    }
    
}