import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Represents a collectible
 */
public class Key {
    private boolean active;
    private Point position;
    private final Image image = new Image("res/key.png");

    /**
     * Creates a new key
     */
    public Key(){
    }


    /**
     * Returns the bounding box of the key for collision detection.
     *
     * @return The bounding box rectangle.
     */
    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(position);
    }

    /**
     * Draws the key on screen if it is active.
     */
    public void draw(){
        if (getActive()){
            image.draw(position.x, position.y);
        }
    }

    /**
     * Sets whether the key is active (visible and interactable).
     *
     * @param active True to activate the key, false to deactivate.
     */
    public void setActive(boolean active) {this.active = active;}

    /**
     * Checks whether the key is currently active.
     *
     * @return True if active, false otherwise.
     */
    public boolean getActive() {return active;}

    /**
     * Sets the position of the key in the game world.
     *
     * @param position The position to place the key.
     */
    public void setPosition(Point position) {this.position = position;}

    /**
     * Returns the current position of the key.
     *
     * @return The key's position as a Point.
     */
    public Point getPosition() {return position;}
}
