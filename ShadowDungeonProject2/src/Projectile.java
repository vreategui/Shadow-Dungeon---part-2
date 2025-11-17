import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.List;


/**
 * Base class for all projectiles in the game.
 * Handles movement, collision, and drawing.
 */
public abstract class Projectile {
    protected Image image;
    protected Point position;
    protected double dx, dy;
    protected final double speed;
    protected double damage;
    protected boolean active = true;

    /**
     * Creates a projectile that moves toward a target position.
     *
     * @param startPos  Starting position of the projectile.
     * @param damage    Damage the projectile deals.
     * @param speed     Speed of the projectile.
     * @param targetX   X coordinate of the target.
     * @param targetY   Y coordinate of the target.
     * @param imagePath Path to the projectile image.
     */
    public Projectile(Point startPos, double damage, double speed, double targetX, double targetY, String imagePath) {
        this.position = new Point(startPos.x, startPos.y);
        this.damage = damage;
        this.speed = speed;
        this.image = new Image(imagePath);
        double vx = targetX - startPos.x;
        double vy = targetY - startPos.y;
        double length = Math.sqrt(vx * vx + vy * vy);

        if (length != 0) {
            dx = (vx / length) * speed;
            dy = (vy / length) * speed;
        } else {
            dx = 0;
            dy = 0;
        }
    }

    /**
     *  Moves the projectile each frame.
     */
    public void update() {
        if (!active) return;
        position = new Point(position.x + dx, position.y + dy);
    }

    /**
     * Draws the projectile if it’s active.
     */
    public void draw() {
        if (active) image.draw(position.x, position.y);
    }

    /**
     * Returns the bounding box of the projectile based on its current position.
     *
     * @return a Rectangle representing the projectile's current bounding box
     */
    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(position);
    }

    /**
     * Checks if the projectile has moved off-screen.
     * @param screenWidth the width of the game screen in pixels
     * @param screenHeight the height of the game screen in pixels
     * @return true if the projectile is out of bounds
     *         false otherwise
     */
    public boolean isOutOfBounds(int screenWidth, int screenHeight) {
        return position.x < 0 || position.x > screenWidth || position.y < 0 || position.y > screenHeight;
    }

    /**
     * Updates the projectile and checks for collisions with enemies or environment
     * @param player the player character associated with the projectile
     * @param enemies the list of enemies in the game world
     * @param environment the list of environmental objects that can block or be hit by the projectile
     * @param screenWidth the width of the game screen in pixels
     * @param screenHeight the height of the game screen in pixels
     */
    public void updateAndCheckCollisions(
            Character player,
            List<Enemy> enemies,
            List<GameObject> environment,
            double screenWidth,
            double screenHeight
    )
    {
        if (!active) return;
        update();
        draw();
        for (GameObject obj : environment) {
            if (!obj.isDestroyed() && getBoundingBox().intersects(obj.getBoundingBox())) {
                handleEnvironmentCollision(obj);
                return;
            }
        }

        // If out of bounds
        if (isOutOfBounds((int)screenWidth, (int)screenHeight)) {
            deactivate();
            return;
        }

    }

    private void handleEnvironmentCollision(GameObject obj) {
        deactivate();
    }

    /**
     * Checks if the projectile is still active.
     * @return true if the projectile is active, false if it has been deactivated.
     */
    public boolean isActive() { return active; }

    /**
     * Deactivates the projectile.
     */
    public void deactivate() { active = false; }

    /**
     * Gets the damage
     * @return the projectile’s damage value.
     */
    public double getDamage() { return damage; }

    /**
     * Gets position
     * @return the projectile’s position.
     */
    public Point getPosition() { return position; }

    /**
     * Sets the projectile’s damage value.
     * @param damage the damage caused by projectile
     */
    public void setDamage(double damage) { this.damage = damage; }
}
