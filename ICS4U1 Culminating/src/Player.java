// player object
// Description: the object the user controls. has an inventory and can move around the map

import java.awt.image.*;
import java.io.IOException;
import java.awt.*;
import java.util.*;

public class Player {
    private int hp;
    private int maxHp = 100;
    private int speed;
    private BufferedImage image;
    private String currentDirection; // e.g. "up", "down", "left", "right"
    private Rectangle hitboxM; // map hitbox
    private Rectangle hitboxC; // combat hitbox
    private int inGameX, inGameY; // in-game coordinates
    ArrayList<Item> inventory;
    BufferedImage playerImageDown;
    BufferedImage playerImageUp;
    BufferedImage playerImageRight;
    BufferedImage playerImageLeft;
    int equippedItem = 0;
    String equippedItemName = "";

    // Constructor
    public Player(int hp, int speed, Rectangle hitboxM, Rectangle hitboxC, int startPosX, int startPosY, BufferedImage spriteDown, BufferedImage spriteUp, BufferedImage spriteRight, BufferedImage spriteLeft) throws IOException {
        this.hp = hp;
        this.speed = speed;
        this.hitboxM = hitboxM;
        this.hitboxC = hitboxC;
        this.inGameX = startPosX;
        this.inGameY = startPosY;
        inventory = new ArrayList<Item>();
        this.playerImageDown = spriteDown;
        this.playerImageUp = spriteUp;
        this.playerImageRight = spriteRight;
        this.playerImageLeft = spriteLeft;
        this.currentDirection = "down";
        this.image = spriteDown; // start facing down
    }

    // Getters
    public int getHP() {
        return hp;
    }

    public int getSpeed() {
        return speed;
    }

    public Rectangle getHitboxM() {
        return hitboxM;
    }

    public Rectangle getHitboxC() {
        return hitboxC;
    }

    public Point getGamePos() {
        return new Point(inGameX, inGameY);
    }

    public String getCurrentDirection() {
        return currentDirection;
    }

    public BufferedImage getCurrentSprite() {
        return image;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public int getEquippedItem() {
        return equippedItem;
    }

    public int getMaxHP() {
        return maxHp;
    }

    public String getEquippedItemName() {
        return equippedItemName;
    }

    // Setters
    public void setHP(int hp) {
        this.hp = hp;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setGameX(int i) {
        inGameX = i;
    }

    public void setGameY(int i) {
        inGameY = i;
    }

    public void setDirection(String direction) {
        this.currentDirection = direction;

        // Update sprite image based on direction
        switch (direction) {
            case "left":
                this.image = playerImageLeft;
                break;
            case "right":
                this.image = playerImageRight;
                break;
            case "up":
                this.image = playerImageUp;
                break;
            case "down":
                this.image = playerImageDown;
                break;
        }
    }

    public void setEquippedItem(int i) {
        equippedItem = i;
    }

    public void setEquippedItemName(String name) {
        equippedItemName = name;
    }

    //OTHER METHODS:

    // Description: player takes damage
    // parameters: amount of damage
    // return: void
    public void takeDamage(int damage) {
        this.hp -= damage;
        System.out.println("hp: " + this.hp);
        if (this.hp <= 0) {
            this.hp = 0;
            System.out.println("suki died.");
        }
    }

    // Description: player moves
    // parameters: amount to move
    // return: void
    public void move(int dx, int dy) {
        inGameX += dx;
        inGameY += dy;
    }

    // Description: add item to inventory (MAKES USE OF SORT)
    // Parameters: item to add
    // Return: void
    public void addToInventory(Item item) {
        inventory.add(item);
        inventory.sort(new SortByName());
    }

    // Description: remove item from inventory
    // Parameters: index of item to remove
    // Return: void
    public void removeFromInventory(int index) {
        inventory.remove(index);
    }

    // Description: search inventory for item (MAKES USE OF BINARY SEARCH)
    // Parameters: name of item
    // Return: index of item in inventory
    public int searchInventory(String itemName) {
        return Collections.binarySearch(inventory, new Collectible(itemName, null, null, null, 0), new SortByName());
    }
}