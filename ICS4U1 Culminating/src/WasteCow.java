// boss cow waste cow
// extends cow class
// description: 
import java.awt.image.BufferedImage;
public class WasteCow extends Cow{

    private FloorMap currentMap;

    // constructor
    public WasteCow(int x, int y, BufferedImage image, FloorMap currentMap, Player suki) {
        super(1000, 2, 0, x, y, image, currentMap, false, suki);
        this.currentMap = currentMap;
    }

    // doesn't follow player because he ate too many caf cookies
    @Override
    public void followPlayer(Player player) {}

    // description: attacks if player is too too close
    // parameters: player object
    // return: void
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
