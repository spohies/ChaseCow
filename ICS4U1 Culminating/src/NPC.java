import java.awt.image.*;
import java.awt.*;

public class NPC {
    private Point gamePos;
    private BufferedImage image;
    private String[][] dialogues; // A 2D array for multiple dialogue sets
    private int state = 0; 
    private boolean itemGiven = false;
    private String name;
    private boolean withinRange = false;
    private int currentDialogueIndex = 0;

    public NPC(String name, Point location, String[][] dialogues, BufferedImage image) {
        this.gamePos = location;
        this.dialogues = dialogues;
        this.image = image;
        this.name = name;
    }

    public void interact() {
        if (state >= dialogues.length || currentDialogueIndex >= dialogues[state].length) {
            if (state >= dialogues.length) {
                System.out.println("We've already spoken.");
            }
            else if (currentDialogueIndex >= dialogues[state].length) {
                System.out.println("Come back later.");
            }
            return;
        }
        if (currentDialogueIndex < dialogues[state].length) {
            System.out.println(dialogues[state][currentDialogueIndex]);
            currentDialogueIndex++;
        }
        // Mark interaction complete for this dialogue set
        if (currentDialogueIndex >= dialogues[state].length) {
            currentDialogueIndex = 0; // Reset for next interaction, if needed
        }
    }
    

    public Point getGamePos() {
        return this.gamePos;
    }

    public String getName() {
        return name;
    }

    public int getState() {
        return state;
    }

    public String[][] getDialogues() {
        return dialogues;
    }

    public int getCurrentDialogueIndex() {
        return currentDialogueIndex;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setWithinRange(boolean inRange) {
        withinRange = inRange;
    }

    public boolean interactable() {
        return withinRange;
    }

    public void setState(int i) {
        this.state = i;
    }


    // public boolean hasFinishedDialogue() {
    //     return itemGiven || currentDialogueIndex >= dialogue.length;
    // }
}
