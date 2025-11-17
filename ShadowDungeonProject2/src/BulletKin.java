import bagel.util.Point;
import java.util.ArrayList;

/**
 * Represents an BulletKin in the game, which is a type of Enemy.
 *
 * The AshenEnemy is initialized with specific properties loaded from the game's configuration:
 * health, coin drop value, damage per frame, and shooting frequency.
 */

public class BulletKin extends Enemy {

    /**
     * Creates a new BulletEnemy at the given position.
     * The enemy's properties (health, coin drop, damage per frame, and shooting frequency)
     * are read from the ShadowDungeon game properties.
     *
     * @param position the starting position of the AshenEnemy
     */
    public BulletKin(Point position) {
        super("res/bullet_kin.png",
                Double.parseDouble(ShadowDungeon.getGameProps().getProperty("bulletKinHealth")),
                position,Double.parseDouble(ShadowDungeon.getGameProps().getProperty("bulletKinCoin")),
                Double.parseDouble(ShadowDungeon.getGameProps().getProperty("riverDamagePerFrame")),
                Integer.parseInt(ShadowDungeon.getGameProps().getProperty("bulletKinShootFrequency")));
    }


}
