import java.awt.image.*;
import java.awt.*;
import java.util.*;
public class Player {
    private int hp;
    private int maxHp;
    private int speed;
    private BufferedImage image;
    private Rectangle hitboxM; // map hitbox
    private Rectangle hitboxC; // combat hitbox
    private int inGameX, inGameY; // in-game coordinates
    HashMap<Item, String> inventory;
    
    public Player (int hp, int speed, Rectangle hitboxM, Rectangle hitboxC, int startPosX, int startPosY, FloorMap currentMap){
        this.hp = hp;
        this.speed = speed;
        this.hitboxM = hitboxM;
        this.hitboxC = hitboxC;
        this.inGameX =  startPosX;
        this.inGameY =  startPosY;
        inventory = new HashMap<Item, String>();
    }

    // getters
    public int getHP(){
        return hp;
    }

    public int getSpeed(){
        return speed;
    }

    public Rectangle getHitboxM(){
        return hitboxM;
    }

    public Rectangle getHitboxC(){
        return hitboxC;
    }

    public Point getGamePos() { 
        return new Point(inGameX, inGameY);
    }

    // setters
    public void setHP(int hp){
        this.hp = hp;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public void takeDamage(int damage){
        hp -= damage;
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
}