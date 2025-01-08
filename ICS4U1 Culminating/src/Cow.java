import java.awt.image.*;
import java.awt.*;
abstract class Cow {
    // instance variables
    private int hp; 
    private int damage; // damage dealt to player
    private int speed;
    private BufferedImage image;
    protected int x;
    protected int y;
    private int inGameX, inGameY; // in-game coordinates

    // Constructor
    public Cow(int hp, int damage, int speed, int x, int y, BufferedImage image) {
        this.hp = hp;
        this.damage = damage;
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.inGameX = x;
        this.inGameY = y;
        this.image = image;
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
        return this.inGameX;    
    }
    public int getY() {
        return this.inGameY;    
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

    public void setGameX(int x) {
        this.inGameX = x;
    }

    public void setGameY(int y) {
        this.inGameY = y;
    }

    // get hurt by player
    public void hurt(int playerDamage) {
        this.setHP(this.hp - playerDamage);
    }

    abstract public void followPlayer(Player player);

    // deal damage based on cow type
    abstract public void attack(Player player);

    public void render(Graphics g, int offsetX, int offsetY) {
        if (image != null) {
            g.drawImage(image, inGameX + offsetX, inGameY + offsetY, null);
        } else {
            g.setColor(Color.RED); // if sprite breaks
            g.fillRect(inGameX + offsetX, inGameY + offsetY, 50, 50); // default size for placeholder
        }
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public Point getGamePos() {
        return new Point(this.inGameX, this.inGameY);
    }
    public Point getMapPos() {
        return new Point(this.x, this.y);
    }

    public void move(int dx, int dy) {
        this.inGameX += dx;
        this.inGameY += dy;
    }
}
