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
    private boolean colliding;

    // Constructor
    public Cow(int hp, int damage, int speed, int x, int y, BufferedImage image, FloorMap currentMap, boolean colliding) {
        this.hp = hp;
        this.damage = damage;
        this.speed = speed;
        this.inGameX = x;
        this.inGameY = y;
        this.x = x+(currentMap.getTLLocation().x + currentMap.getBG().getWidth()/2);
        this.y = y+(currentMap.getTLLocation().y + currentMap.getBG().getHeight()/2);
        // System.out.println("Cow x: " + this.x + " y: " + this.y);
        this.image = image;
        this.colliding = colliding;
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

    public void render(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, null);
        } else {
            g.setColor(Color.RED); // if sprite breaks
            g.fillRect(x, y, 50, 50); // default size for placeholder
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
    public void setMapPos(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void inGameMove(int dx, int dy) {
        this.inGameX += dx;
        this.inGameY += dy;
    }
    public void inScreenMove(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public Rectangle getHitbox() {
        return new Rectangle(this.x, this.y, this.image.getWidth(), this.image.getHeight());
    }

    public void setCollision(boolean colliding) {
        this.colliding = colliding;
    }

    public boolean isColliding() {
        return colliding;
    }
}
