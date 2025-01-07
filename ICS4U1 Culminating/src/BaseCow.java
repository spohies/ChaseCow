import java.awt.*;
import java.awt.image.BufferedImage;

class BaseCow extends Cow {
    public BaseCow(int x, int y, BufferedImage image) {
        super(10, 1, 1, x, y, image);
    }

    @Override
    public void followPlayer(Player player) {
        Point playerPos = player.getPosition();
        double dx = playerPos.x - this.getX();
        double dy = playerPos.y - this.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 0) { // Prevent division by zero
            System.out.println(distance);
            this.x += (dx / distance) * this.getSpeed();
            this.y += (dy / distance) * this.getSpeed();
        }
        
        // TODO Auto-generated method stub
    }

    @Override
    public void attack(Player player) {
        double dx = player.getHitbox().x - this.getX();
        double dy = player.getHitbox().y - this.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
         
        if (distance < 5) {
            player.takeDamage(this.getDamage());
        }
        
        // TODO Auto-generated method stub
    }
}