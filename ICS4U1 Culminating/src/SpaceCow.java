// space cow object
// extends cow class
// Description: stornger than a basecow

import java.awt.*;
import java.awt.image.BufferedImage;

class SpaceCow extends Cow {
    private FloorMap currentMap;

    // constructor
    public SpaceCow(int x, int y, BufferedImage image, FloorMap currentMap, Player suki) {
        super(150, 4, 3, x, y, image, currentMap, false, suki);
        this.currentMap = currentMap;
    }


    // Description: follow player and try attack
    // Parameter: player to follow
    // Return: none
    @Override
    public void followPlayer(Player player) {
        Point playerPos = new Point(
                (player.getGamePos().x + player.getHitboxM().width / 2),
                (player.getGamePos().y + player.getHitboxM().height / 2));
        Point cowPos = this.getGamePos();
        double dx = playerPos.x - cowPos.x;
        double dy = playerPos.y - cowPos.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // damage player if close enough
        if (distance <= 20) {
            tryAttack(player);
        }

        // move towards player
        if (distance > 0) {
            // direction vector (dx, dy)
            double directionX = dx / distance;
            double directionY = dy / distance;

            // calculate the movement 
            int moveX = (int) Math.round(directionX * this.getSpeed());
            int moveY = (int) Math.round(directionY * this.getSpeed());

            // check for collisions with other cows
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

    // Description: attack player if close enough
    // Parameter: player to attack
    // Return: none
    @Override
    public void attack(Player player) {
        double dx = player.getHitboxC().x - this.getX();
        double dy = player.getHitboxC().y - this.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        // player takes damage if close enough
        if (distance < 20) {
            player.takeDamage(this.getDamage());
        }
    }
}