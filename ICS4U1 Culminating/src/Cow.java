import java.awt.image.*;
import java.awt.*;
abstract class Cow {
    // instance variables
    private int hp; 
    private int damage; // damage dealt to player
    private int speed;
    protected BufferedImage image;

    protected int x;
    protected int y;
    private int inGameX, inGameY; // in-game coordinates
    private boolean colliding;

    private long lastAttackTime = 0; // Timestamp for last attack
    private static final int COOLDOWN = 500; // Cooldown time in milliseconds

    // Constructor
    public Cow(int hp, int damage, int speed, int x, int y, BufferedImage image, FloorMap currentMap, boolean colliding, Player player) {
        this.hp = hp;
        this.damage = damage;
        this.speed = speed;
        this.inGameX = x;
        this.inGameY = y;
        this.x = inGameX - currentMap.getTLLocation().x;
        this.y = inGameY - currentMap.getTLLocation().y;

        System.out.println("SCREEN Cow x: " + this.x + " y: " + this.y);
        System.out.println("GAME Cow x: " + this.inGameX + " y: " + this.inGameY);
        
        this.image = image;
        this.colliding = colliding;

        updateScreenCoords(player);
    }

      // Update screen coordinates based on player's position
      public void updateScreenCoords(Player player) {
        this.x = this.inGameX - player.getGamePos().x + (player.getHitboxC().width / 2);
        this.y = this.inGameY - player.getGamePos().y + (player.getHitboxC().height / 2);
    }

    public void move(int speedX, int speedY, Player player) {
        this.inGameX += speedX;
        this.inGameY += speedY;
        updateScreenCoords(player);
    }


    public void tryAttack(Player player) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime >= COOLDOWN) {
            player.takeDamage(this.damage);
            lastAttackTime = currentTime;
        }
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

    public boolean isAlive() {
        return hp > 0;
    }

    public Point getGamePos() {
        return new Point(this.inGameX, this.inGameY);
    }
    public void setGamePos(int x, int y) {
        this.inGameX = x;
        this.inGameY = y;
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
        return new Rectangle(this.inGameX, this.inGameY, this.image.getWidth(), this.image.getHeight());
    }

    public void setCollision(boolean colliding) {
        this.colliding = colliding;
    }

    public boolean isColliding() {
        return colliding;
    }
}
