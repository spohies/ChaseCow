import java.awt.*;
import java.awt.image.BufferedImage;

class SpaceCow extends Cow {
    private FloorMap currentMap;

    public SpaceCow(int x, int y, BufferedImage image, FloorMap currentMap, Player suki) {
        super(150, 8, 3, x, y, image, currentMap, false, suki);
        this.currentMap = currentMap;
    }

    @Override
    public void followPlayer(Player player) {
        Point playerPos = new Point(
                (player.getGamePos().x + player.getHitboxM().width / 2),
                (player.getGamePos().y + player.getHitboxM().height / 2));
        Point cowPos = this.getGamePos();
        double dx = playerPos.x - cowPos.x;
        double dy = playerPos.y - cowPos.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Damage player if within 20 distance
        if (distance <= 20) {
            tryAttack(player);
        }

        if (distance > 0) {
            // Normalize the direction vector (dx, dy)
            double directionX = dx / distance;
            double directionY = dy / distance;

            // Calculate the movement for this step
            int moveX = (int) Math.round(directionX * this.getSpeed());
            int moveY = (int) Math.round(directionY * this.getSpeed());

            // Check for collisions with other cows
            Rectangle futureHitbox = new Rectangle(this.getHitbox());
            futureHitbox.translate(moveX, moveY);
            boolean collision = false;
            for (Cow otherCow : currentMap.getCows()) {
                if (otherCow != this && futureHitbox.intersects(otherCow.getHitbox())) {
                    collision = true;
                    break;
                }
            }

            if (!collision) {
                this.inGameMove(moveX, moveY);
            }
        }
    }

    @Override
    public void attack(Player player) {
        double dx = player.getHitboxC().x - this.getX();
        double dy = player.getHitboxC().y - this.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < 20) {
            player.takeDamage(this.getDamage());
        }
    }
}