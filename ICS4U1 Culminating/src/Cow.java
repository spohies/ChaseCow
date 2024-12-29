import java.awt.image.*;
import java.awt.*;
abstract class Cow {
    // instance variables
    private int hp; 
    private int damage; // damage dealt to player
    private int speed;
    private BufferedImage image;
    private int x, y;

    // Constructor
    public Cow(int hp, int damage, int speed, int x, int y) {
        this.hp = hp;
        this.damage = damage;
        this.speed = speed;
        this.x = x;
        this.y = y;
    }

    // Getters
    public int getHP() {
        return this.hp;    
    }
    public int getDamage() {
        return this.damage;    
    }
    public int getSpeed() {
        return this.speed;    
    }
    public BufferedImage getImage() {
        return this.image;    
    }
    public int getX() {
        return this.x;    
    }
    public int getY() {
        return this.y;    
    }

    // Setters
    public void setHP(int hp) {
        this.hp = hp;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // get hurt by player
    public void hurt(int playerDamage) {
        this.setHP(this.hp - playerDamage);
    }

    abstract public void followPlayer(Rectangle player);

    // deal damage based on cow type
    abstract public void attack(Rectangle player);

    public void render(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, null);
        } else {
            g.setColor(Color.RED); // if sprite breaks
            g.fillRect(x, y, image.getWidth(), image.getHeight());
        }
    }

    public boolean isAlive() {
        return hp > 0;
    }
}
