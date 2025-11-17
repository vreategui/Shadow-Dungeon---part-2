import bagel.Image;
import bagel.util.Point;

/**
 * A river object
 */
public class River extends GameObject {
    private final double damagePerFrame;

    /**
     * Creates a river at the given position.
     * @param point The position of the river.
     */
    public River(Point point) {
        super(point, "res/river.png");
        this.damagePerFrame = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("riverDamagePerFrame"));
    }

    /**
     * Damages the character if they collide with the river.
     * @param currCharacter The character to check collision with.
     */
    @Override
    public void update(Character currCharacter) {
        if (isDestroyed()) return;

        if (hasCollidedWith(currCharacter)) {
            currCharacter.receiveDamage(damagePerFrame);
        }
    }
}