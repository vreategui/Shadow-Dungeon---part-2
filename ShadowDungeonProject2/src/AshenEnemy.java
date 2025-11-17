import bagel.util.Point;

/**
 * Represents an AshenEnemy in the game, which is a type of Enemy.
 *
 * The AshenEnemy is initialized with specific properties loaded from the game's configuration:
 * health, coin drop value, damage per frame, and shooting frequency.
 */
public class AshenEnemy extends Enemy{
    /**
     * Creates a new AshenEnemy at the given position.
     * The enemy's properties (health, coin drop, damage per frame, and shooting frequency)
     * are read from the ShadowDungeon game properties.
     *
     * @param position the starting position of the AshenEnemy
     */
    public AshenEnemy(Point position) {
        super("res/ashen_bullet_kin.png",
                Double.parseDouble(ShadowDungeon.getGameProps().getProperty("ashenBulletKinHealth")),
                position, Double.parseDouble(ShadowDungeon.getGameProps().getProperty("ashenBulletKinCoin")),
                Double.parseDouble(ShadowDungeon.getGameProps().getProperty("riverDamagePerFrame")),
                Integer.parseInt(ShadowDungeon.getGameProps().getProperty("ashenBulletKinShootFrequency")));
    }

}
