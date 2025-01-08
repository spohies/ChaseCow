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
    HashMap<Item, String> inventory;
    
    public Player (int hp, int speed, Rectangle hitboxM, Rectangle hitboxC){
        this.hp = hp;
        this.speed = speed;
        this.hitboxM = hitboxM;
        this.hitboxC = hitboxC;
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

    public Point getMapPos() { 
        return new Point(hitboxM.x, hitboxM.y);
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
}