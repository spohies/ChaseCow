// NPC object
// teachers!!!!!
// Description: holds the dialogue of NPCs and their location
import java.awt.image.*;
import java.awt.*;

public class NPC {
    private Point gamePos;
    private BufferedImage image;
    private String[][] dialogues; // 2D array for multiple dialogue sets
    private int state = 0; 
    private String name;
    private boolean withinRange = false;
    private int currentDialogueIndex = 0;

    // Constructor
    public NPC(String name, Point location, String[][] dialogues, BufferedImage image) {
        this.gamePos = location;
        this.dialogues = dialogues;
        this.image = image;
        this.name = name;
    }

    // Description: allows the player to interact with the NPC
    // Parameter: none
    // Return: none
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
        // increment dialogue number
        if (currentDialogueIndex < dialogues[state].length) {
            System.out.println(dialogues[state][currentDialogueIndex]);
            currentDialogueIndex++;
        }
        // mark interaction complete
        if (currentDialogueIndex >= dialogues[state].length) {
            currentDialogueIndex = 0; // reset for next interaction, if needed
        }
    }
    

    // Getters
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
    
    public boolean interactable() {
        return withinRange;
    }

    // Setters
    public void setWithinRange(boolean inRange) {
        withinRange = inRange;
    }
    
    public void setState(int i) {
        this.state = i;
    }
    
    public void setCurrentDialogueIndex(int i) {
        this.currentDialogueIndex = i;
    }
}
