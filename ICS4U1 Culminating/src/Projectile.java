// projectile object for boss fight
// Description: moves in a straight line

import java.awt.*;
public class Projectile {
    private double x, y, dx, dy;
    static final int SIZE = 10;

    // constructor
    public Projectile(double x, double y, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    // Description: moves the projectile
    // Parameters: none
    // Return: none
    public void move() {
        x += dx * 2; // speed multiplier
        y += dy * 2;
    }

    // Getters
    public Rectangle getRect() {
        return new Rectangle((int) x, (int) y, SIZE, SIZE);
    }

    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }

    public static int getSize() {
        return SIZE;
    }

}
