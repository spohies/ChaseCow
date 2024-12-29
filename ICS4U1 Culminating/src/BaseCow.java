import java.awt.*;
class BaseCow extends Cow {
    public BaseCow(int x, int y) {
        super(10, 1, 1, x, y);
    }

    @Override
    public void followPlayer(Rectangle player) {
        double dx = player.x - this.getX();
        double dy = player.y - this.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) { // Prevent division by zero
            x += (dx / distance) * this.getSpeed();
            y += (dy / distance) * this.getSpeed();
        }
        
        // TODO Auto-generated method stub
    }

    @Override
    public void attack(Rectangle player) {
        double dx = player.x - this.getX();
        double dy = player.y - this.getY();
        double distance = Math.sqrt(dx * dx + dy * dy)
         
        if (distance < 5) {
            // .takeDamage(this.getDamage());
        }
        
        // TODO Auto-generated method stub
    }

}