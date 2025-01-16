import java.awt.image.*;
import java.io.IOException;
import java.awt.*;
import java.util.*;

import javax.imageio.ImageIO;

public class Player {
    private int hp;
    private int maxHp;
    private int speed;
    private BufferedImage image;
    private String currentDirection; // e.g., "up", "down", "left", "right"
    private Rectangle hitboxM; // map hitbox
    private Rectangle hitboxC; // combat hitbox
    private int inGameX, inGameY; // in-game coordinates
    ArrayList<Item> inventory;
    BufferedImage playerImageDown;
    BufferedImage playerImageUp;
    BufferedImage playerImageRight;
    BufferedImage playerImageLeft;
    int equippedItem= 0;

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
        this.image = spriteDown;
    }

    // getters
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

    // setters
    public void setHP(int hp) {
        this.hp = hp;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        System.out.println("hp: " + this.hp);
        if (this.hp <= 0) {
            System.out.println("suki died.");
        }
    }

    public void move(int dx, int dy) {
        inGameX += dx;
        inGameY += dy;
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
            case "up":
                this.image = playerImageUp;
                break;
            case "down":
                this.image = playerImageDown;
                break;
            case "left":
                this.image = playerImageLeft;
                break;
            case "right":
                this.image = playerImageRight;
                break;
        }
    }

    // Getters for direction and sprite
    public String getCurrentDirection() {
        return currentDirection;
    }

    public BufferedImage getCurrentSprite() {
        return image;
    }

    // Example methods to get sprites for directions
    private BufferedImage getUpSprite() {
        // Load or return the appropriate sprite
        return playerImageUp;
    }

    private BufferedImage getDownSprite() {
        return playerImageDown;
    }

    private BufferedImage getLeftSprite() {
        return playerImageLeft;
    }

    private BufferedImage getRightSprite() {
        return playerImageRight;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void addToInventory(Item item) {
        inventory.add(item);
    }

    public int getEquippedItem() {
        return equippedItem;
    }
    public void setEquippedItem(int i) {
        equippedItem = i;
    }
    
}