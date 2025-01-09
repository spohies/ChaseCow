import java.awt.*;
import java.awt.image.BufferedImage;

class BaseCow extends Cow {
    public BaseCow(int x, int y, BufferedImage image, FloorMap currentMap) {
        super(10, 1, 1, x, y, image, currentMap);
    }

    @Override
    public void followPlayer(Player player) {
        Point playerPos = player.getGamePos();
        double dx = playerPos.x - this.getGamePos().x;
        double dy = playerPos.y - this.getGamePos().y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 0) { // Prevent division by zero
            int moveX = (int)((dx / distance) * this.getSpeed());
            int moveY = (int)((dy / distance) * this.getSpeed());
            this.move(moveX, moveY);
        }
    }

    @Override
    public void attack(Player player) {
        double dx = player.getHitboxC().x - this.getX();
        double dy = player.getHitboxC().y - this.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
         
        if (distance < 5) {
            player.takeDamage(this.getDamage());
        }
        
        // TODO Auto-generated method stub
    }
}