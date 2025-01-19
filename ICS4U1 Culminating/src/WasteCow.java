import java.awt.*;
import java.awt.image.BufferedImage;
public class WasteCow extends Cow{

    private FloorMap currentMap;

    public WasteCow(int x, int y, BufferedImage image, FloorMap currentMap, Player suki) {
        super(1000, 7, 0, x, y, image, currentMap, false, suki);
        this.currentMap = currentMap;
    }

    @Override
    public void followPlayer(Player player) {}

    @Override
    public void attack(Player player) {
        double dx = player.getHitboxC().x - this.getX();
        double dy = player.getHitboxC().y - this.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < 10) {
            player.takeDamage(this.getDamage());
        }
    }
    
}
