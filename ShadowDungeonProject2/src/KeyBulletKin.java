import bagel.util.Point;

import java.util.ArrayList;

/**
 * A special type of enemy that follows a path and drops a key upon death.
 */
public class KeyBulletKin extends Enemy {
    private final double SPEED;
    private final ArrayList<Point> path;
    private int currentTargetIndex = 0;
    private final Key key;
    private static final double INITIAL_HEALTH = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("keyBulletKinHealth"));
//  private static double health = INITIAL_HEALTH;
    private static final double KIN_DAMAGE_PER_FRAME = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("riverDamagePerFrame"));
    private static final double KIN_SPEED = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("keyBulletKinSpeed"));
    private static final double KIN_COIN = 0;
    private static final int KIN_SHOOT_FREQ = 0;
    private boolean keyDropped = false;

    private double health;

    /**
     * Creates a KeyBulletKin that follows a given path.
     *
     * @param path The list of points representing the patrol path.
     */
    public KeyBulletKin(ArrayList<Point> path) {
        super(
                "res/key_bullet_kin.png",
                INITIAL_HEALTH,
                path.get(0),
                KIN_COIN,
                KIN_DAMAGE_PER_FRAME,
                KIN_SHOOT_FREQ
        );

        this.SPEED = KIN_SPEED;
        this.path = path;
        this.key = new Key();
        this.key.setActive(false);

        double initialHealth = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("keyBulletKinHealth"));
        this.health = initialHealth;
    }

    /**
     * Updates the enemy's movement and handles collisions with the character.
     *
     * @param currCharacter The character in the game.
     */
    @Override
    public void update(Character currCharacter) {
        if (!isActive()) return;

        if (health > 0) {
            followPath();
        } else {
            setActive(false);
            setDead(true);
            return;
        }
        if (hasCollidedWith(currCharacter)) {
            currCharacter.takeDamage(KIN_DAMAGE_PER_FRAME);
        }
    }

    /**
     * Inflicts damage to the KeyBulletKin and drops the key if it dies.
     *
     * @param dmg Amount of damage to apply.
     */
    @Override
    public void takeDamage(double dmg) {
        if (isDead()) return;
        health -= dmg;
        if (health <= 0) {
            setActive(false);
            setDead(true);
            deactivateAndDropKey();
        }
    }
    /**
     * Returns the current health of the KeyBulletKin.
     *
     * @return Current health value.
     */
    @Override
    public double getHealth() {
        return this.health;
    }

    private void followPath() {
        if (path == null || path.isEmpty()) return;
        Point target = path.get(currentTargetIndex);
        double dx = target.x - getPosition().x;
        double dy = target.y - getPosition().y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < SPEED) {
            currentTargetIndex = (currentTargetIndex + 1) % path.size();
            return;
        }
        dx /= distance;
        dy /= distance;
        setPosition(new Point(getPosition().x + dx * SPEED, getPosition().y + dy * SPEED));
    }

    private void deactivateAndDropKey() {
        setActive(false);
        if (!keyDropped) {
            key.setPosition(getPosition());
            key.setActive(true);
            keyDropped = true;
        }
    }

    /**
     * Gets the current position of the enemy.
     *
     * @return The position as a Point.
     */
    @Override
    public Point getPosition() {
        return super.getPosition();
    }

    /**
     * Sets the current position of the enemy.
     *
     * @param position New position as a Point.
     */
    @Override
    public void setPosition(Point position) {
        super.setPosition(position);
    }

    /**
     * Returns the key dropped by this enemy.
     *
     * @return The Key object.
     */
    public Key getKey() { return key; }
}