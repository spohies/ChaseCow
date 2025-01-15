import java.awt.image.*;
import java.awt.*;

public class NPC {
    Point gamePos;
    BufferedImage image;
    String[] dialogue;
    boolean itemGiven = false;
    String name;
    boolean withinRange = false;
    int currentDialogueIndex = 0;

    public NPC(String name, Point location, String[] dialogue, BufferedImage image) {
        this.gamePos = location;
        this.dialogue = dialogue;
        this.image = image;
        this.name = name;
    }

    public void interact() {
        if (!itemGiven) {
            if (currentDialogueIndex < dialogue.length) {
                System.out.println(dialogue[currentDialogueIndex]);
                currentDialogueIndex++;
            }
            if (currentDialogueIndex == dialogue.length) {
                itemGiven = true; // Mark as interacted after the last line
            }
        } else {
            System.out.println("We've already spoken.");
        }
    }
    

    public Point getGamePos() {
        return this.gamePos;
    }

    public String getName() {
        return name;
    }

    public void setWithinRange(boolean inRange) {
        withinRange = inRange;
    }

    public boolean interactable() {
        return withinRange;
    }

    public boolean hasFinishedDialogue() {
        return itemGiven || currentDialogueIndex >= dialogue.length;
    }
}
