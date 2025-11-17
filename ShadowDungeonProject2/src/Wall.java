import bagel.Image;
import bagel.util.Point;

/**
 * A wall object placed in the game environment.
 * Acts as an obstacle that the player cannot pass through.
 */
public class Wall extends GameObject {

    /**
     * Creates a wall at the given position.
     * @param point The position of the wall.
     */
    public Wall(Point point) {
        super(point, "res/wall.png");
    }
}