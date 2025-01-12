import java.awt.*;
import java.awt.image.BufferedImage;

class BaseCow extends Cow {
    private FloorMap currentMap;

    public BaseCow(int x, int y, BufferedImage image, FloorMap currentMap) {
        super(10, 1, 2, x, y, image, currentMap, false);
        this.currentMap = currentMap;
    }

    @Override
    public void followPlayer(Player player) {
        Point playerPos = new Point((player.getHitboxC().x + player.getHitboxC().width / 2), (player.getHitboxC().y + player.getHitboxC().height / 2));
        double dx = playerPos.x - this.getMapPos().x;
        double dy = playerPos.y - this.getMapPos().y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) { // so cows don't just walk over suki?
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
                this.inScreenMove(moveX, moveY);
            }
            // System.out.printf("Cow Update: InGame(%d, %d), Screen(%d, %d)\n",
            // this.getGamePos().x, this.getGamePos().y, this.getMapPos().x, this.getMapPos().y);
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