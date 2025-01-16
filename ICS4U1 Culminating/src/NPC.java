import java.awt.image.*;
import java.awt.*;

public class NPC {
    Point gamePos;
    BufferedImage image;
    String[][] dialogues; // A 2D array for multiple dialogue sets
    int state = 0; 
    boolean itemGiven = false;
    String name;
    boolean withinRange = false;
    int currentDialogueIndex = 0;

    public NPC(String name, Point location, String[][] dialogues, BufferedImage image) {
        this.gamePos = location;
        this.dialogues = dialogues;
        this.image = image;
        this.name = name;
    }

    public void interact() {
        if (state >= dialogues.length) {
            System.out.println("We've already spoken.");
            return;
        }
        if (currentDialogueIndex < dialogues[state].length) {
            System.out.println(dialogues[state][currentDialogueIndex]);
            currentDialogueIndex++;
        }
        // Mark interaction complete for this dialogue set
        if (currentDialogueIndex >= dialogues[state].length) {
            currentDialogueIndex = 0; // Reset for next interaction, if needed
            state++; // Move to the next dialogue set
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

    // public boolean hasFinishedDialogue() {
    //     return itemGiven || currentDialogueIndex >= dialogue.length;
    // }
}
