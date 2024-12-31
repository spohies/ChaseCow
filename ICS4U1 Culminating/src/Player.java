import java.awt.image.*;
import java.awt.*;
import java.util.*;
public class Player {
    private int hp;
    private int speed;
    private BufferedImage image;
    private Rectangle hitbox;
    HashSet<Item> inventory;
    
    public Player (int hp, int speed, Rectangle hitbox){
        this.hp = hp;
        this.speed = speed;
        this.hitbox = hitbox;
        inventory = new HashSet<Item>();
    }

    // getters
    public int getHP(){
        return hp;
    }

    public int getSpeed(){
        return speed;
    }

    public Rectangle getHitbox(){
        return hitbox;
    }

    public Point getPosition() {
        return new Point(hitbox.x, hitbox.y);
    }

    // setters
    public void setHP(int hp){
        this.hp = hp;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }
    
    public void setHitbox(Rectangle hitbox){
        this.hitbox = hitbox;
    }

    public void takeDamage(int damage){
        hp -= damage;
    }
}