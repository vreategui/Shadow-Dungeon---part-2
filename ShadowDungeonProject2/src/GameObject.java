import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Represents a game object with an image and position.
 */
public abstract class GameObject {
    private final Image image;
    private final Point point;
    private boolean destroyed = false;

    /**
     * Creates a new GameObject.
     *
     * @param point     The position of the object.
     * @param imagePath The file path of the object's image.
     */
    public GameObject(Point point, String imagePath) {
        this.point = point;
        this.image = new Image(imagePath);
    }

    /**
     * Returns the bounding box of this object.
     *
     * @return The bounding box.
     */
    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(point);
    }

    /**
     * Marks this object as destroyed.
     */
    public void destroy() {
        this.destroyed = true;
    }

    /**
     * Checks if the object is destroyed.
     *
     * @return True if destroyed, false otherwise.
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Draws the object if it is not destroyed.
     */
    public void draw() {
        if (!destroyed) {
            image.draw(point.x, point.y);
        }
    }

    /**
     * Updates the object state based on the character.
     *
     * @param currCharacter The current character.
     */
    public void update(Character currCharacter) {
        if (destroyed) return;

        if (hasCollidedWith(currCharacter)) {
            currCharacter.move(currCharacter.getPrevPosition().x, currCharacter.getPrevPosition().y);
        }
    }

    /**
     * Checks for collision with the character.
     *
     * @param currCharacter The character to check collision with.
     * @return True if collided, false otherwise.
     */
    public boolean hasCollidedWith(Character currCharacter) {
        return image.getBoundingBoxAt(point).intersects(currCharacter.getCurrImage().getBoundingBoxAt(currCharacter.getPosition()));
    }
}
