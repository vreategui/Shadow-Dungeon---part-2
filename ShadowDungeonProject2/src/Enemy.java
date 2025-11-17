import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

/**
 * Abstract base class for all enemies in the game.
 */
public abstract class Enemy {
    private Point position;
    private final Image image;
    private boolean active = false;
    private boolean dead = false;
    private double health;
    private int shootFrequency;
    private double damage;
    private int currentTargetIndex = 0;
    private double coin;
    private double damagePerFrame;
    private int shootCooldown;

    /**
     * Creates a new Enemy.
     *
     * @param imagePath        Path to the image representing the enemy.
     * @param health           The health of the enemy.
     * @param position         The starting position of the enemy.
     * @param coin             The coin value dropped upon death.
     * @param damagePerFrame   Damage dealt per frame when colliding with character.
     * @param shootFrequency   Number of frames between each fireball shot.
     */
    public Enemy(String imagePath, double health, Point position, double coin, double damagePerFrame, int shootFrequency) {
        this.image = new Image(imagePath);
        this.health = health;
        this.shootFrequency = shootFrequency;
        this.position=position;
        this.coin=coin;
        this.damagePerFrame=damagePerFrame;
        this.shootFrequency = shootFrequency;
        this.shootCooldown = shootFrequency;
    }

    /**
     * Updates the enemy's interaction with the character (collision damage only).
     *
     * @param currCharacter The character currently in the game.
     */
    public void update(Character currCharacter) {
        if (hasCollidedWith(currCharacter)) {
            currCharacter.takeDamage(getDamagePerFrame());
        }
    }

    /**
     * Updates the enemy and handles fireball shooting logic.
     *
     * @param currCharacter The character currently in the game.
     * @return A list of Fireballs spawned in this update.
     */
    public ArrayList<Fireball> updateAndShoot(Character currCharacter) {
        ArrayList<Fireball> spawned = new ArrayList<>();
        if (!isActive() || isDead()) {
            return spawned;
        }

        if (hasCollidedWith(currCharacter)) {
            currCharacter.takeDamage(getDamagePerFrame());
        }

        if (shootCooldown <= 0) {
            Point spawnCenter = getBoundingBox().centre();
            Point targetCenter = currCharacter.getCurrImage().getBoundingBoxAt(currCharacter.getPosition()).centre();
            Fireball f = new Fireball(spawnCenter, targetCenter);
            spawned.add(f);
            shootCooldown = shootFrequency; // reset timer
        } else {
            shootCooldown--;
        }
        return spawned;
    }

    /**
     * Draws the enemy on the screen.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }

    /**
     * Gets the bounding box of the enemy.
     *
     * @return The bounding rectangle of the enemy.
     */
    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(position);
    }

    /**
     * Checks whether a bullet has collided with this enemy.
     *
     * @param bullet The bullet to check collision with.
     * @return True if the bullet hits the enemy, false otherwise.
     */
    public boolean checkBulletCollision(Bullet bullet) {
        if (this.isActive() && !this.isDead()) {
            if (bullet.getBoundingBox().intersects(this.getBoundingBox())) {
                this.takeDamage(bullet.getDamage());
                return true;
            }
        }
        return false;
    }

    /**
     * Applies damage to the enemy.
     *
     * @param dmg Amount of damage to apply.
     */
    public void takeDamage(double dmg) {
        if (isDead()) return;
        health -= dmg;
        if (health <= 0) {
            setDead(true);
            setActive(false);
        }
    }

    /**
     * Checks if the enemy has collided with the character.
     *
     * @param currCharacter The character to check collision with.
     * @return True if a collision has occurred, false otherwise.
     */
    public boolean hasCollidedWith(Character currCharacter) {
        return image.getBoundingBoxAt(position)
                .intersects(currCharacter.getCurrImage().getBoundingBoxAt(currCharacter.getPosition()));
    }

    /**
     * Returns whether the enemy is dead.
     *
     * @return True if dead, false otherwise.
     */
    public boolean isDead() { return dead; }

    /**
     * Sets whether the enemy is dead.
     *
     * @param dead True if the enemy is dead.
     */
    public void setDead(boolean dead) { this.dead = dead; }

    /**
     * Returns whether the enemy is active.
     *
     * @return True if active, false otherwise.
     */
    public boolean isActive() { return active; }

    /**
     * Sets whether the enemy is active.
     *
     * @param active True to activate the enemy.
     */
    public void setActive(boolean active) { this.active = active; }

    /**
     * Gets the current health of the enemy.
     *
     * @return The current health value.
     */
    public double getHealth() { return health; }

    /**
     * Gets the amount of coins dropped when the enemy is defeated.
     *
     * @return Coin value.
     */
    public double getCoin() {return coin;}

    /**
     * Sets the amount of coins this enemy will drop.
     *
     * @param coin Coin value to set.
     */
    public void setCoin(double coin) {this.coin = coin;}

    /**
     * Sets the damage dealt per frame to the character.
     *
     * @param damage Damage per frame.
     */
    public void setDamagePerFrame(double damage){ this.damagePerFrame = damage;}

    /**
     * Gets the damage dealt per frame to the character.
     *
     * @return Damage per frame.
     */
    public double getDamagePerFrame(){return damagePerFrame;}

    /**
     * Gets the position of the enemy.
     *
     * @return Current position.
     */
    public Point getPosition() { return position; }

    /**
     * Sets the position of the enemy.
     *
     * @param position New position.
     */
    public void setPosition(Point position) { this.position = position; }
}
