// BaseCow Object
// Extends Cow
// Description: follows the player and attacks them if they are within a certain distance
import java.awt.*;
import java.awt.image.BufferedImage;

class BaseCow extends Cow {
    private FloorMap currentMap;

    // constructor
    public BaseCow(int x, int y, BufferedImage image, FloorMap currentMap, Player suki) {
        super(100, 3, 2, x, y, image, currentMap, false, suki);
        this.currentMap = currentMap;
    }

    // Description: follows the player and attacks them if they are within a certain distance
    // Parameter: player to follow
    // Return: void
    @Override
    public void followPlayer(Player player) {
        Point playerPos = new Point(
                (player.getGamePos().x + player.getHitboxM().width / 2),
                (player.getGamePos().y + player.getHitboxM().height / 2));
        Point cowPos = this.getGamePos();
        double dx = playerPos.x - cowPos.x;
        double dy = playerPos.y - cowPos.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // damage player if within 20 distance
        if (distance <= 20) {
            tryAttack(player);
        }

        if (distance > 0) {
            // direction vector (dx, dy)
            double directionX = dx / distance;
            double directionY = dy / distance;

            // calculate the movement
            int moveX = (int) Math.round(directionX * this.getSpeed());
            int moveY = (int) Math.round(directionY * this.getSpeed());

            // check for potential collisions with other cows
            Rectangle futureHitbox = new Rectangle(this.getHitbox());
            futureHitbox.translate(moveX, moveY);
            boolean collision = false;
            for (Cow otherCow : currentMap.getCows()) {
                if (otherCow != this && futureHitbox.intersects(otherCow.getHitbox())) {
                    collision = true;
                    break;
                }
            }

            // move if no collision
            if (!collision) {
                this.inGameMove(moveX, moveY);
            }
        }
    }

    // Description: attacks the player
    // Parameter: player to attack
    // Return: void
    @Override
    public void attack(Player player) {

        // calculate distance between player and cow
        double dx = player.getHitboxC().x - this.getX();
        double dy = player.getHitboxC().y - this.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        // damage player if within 10 distance
        if (distance < 10) {
            player.takeDamage(this.getDamage());
        }
    }
}