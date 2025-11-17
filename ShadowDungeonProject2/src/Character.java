import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * The Character class represents a general character in the game.
 * It stores position, health, speed, weapon stats, and shared attributes like coins and keys.
 *
 * This class is abstract and should be extended by specific character types (e.g., Player, Enemy).
 */
public abstract class Character {
    private Point position;
    private Point prevPosition;
    private Point previewPosition;
    private Image currImage;
    private static double coins = 0;
    private static double health;
    private static double speed;
    private static double weapon = 0;
    private static int key = 0;
    public double bulletSpeed = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("bulletSpeed"));
    public double weaponDamage = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("weaponStandardDamage"));
    private Set<String> collectedKeys = new HashSet<>();

    /**
     * Creates a Character with the given position, image, health, and speed.
     *
     * @param position the starting position of the character
     * @param image the image representing the character
     * @param health the initial health of the character
     * @param speed the movement speed of the character
     */
    public Character(Point position, Image image, double health, double speed) {
        this.position = position;
        this.prevPosition = position;
        this.currImage = image;
        Character.health = health;
        Character.speed = speed;
    }

    /**
     * Resets all shared character stats such as coins, keys, weapon, and health.
     *
     * @param startingHealth the health value to reset to
     */
    public static void resetSharedStats(double startingHealth) {
        coins = 0;
        key = 0;
        weapon = 0;
        health = startingHealth;
    }

    /**
     * Allows the character to pick up a key and increase the key count.
     *
     * @param key the key being picked up
     */
    public void pickUpKey(Key key) {
        if (key.getActive()) {
            key.setActive(false);
            setKey(getKey() + 1);
        }
    }

    /**
     * Moves the character to a new position.
     *
     * @param x the x-coordinate of the new position
     * @param y the y-coordinate of the new position
     */
    public void move(double x, double y) {
        this.prevPosition = this.position;
        this.position = new Point(x, y);
    }

    /**
     * Reduces the character's health by the given damage amount.
     * If health reaches zero, triggers game over.
     *
     * @param dmg the amount of damage taken
     */
    public void takeDamage(double dmg) {
        setHealth(health - dmg);
        if (health <= 0) {
            setHealth(0);
            System.out.println("Character died!");
            ShadowDungeon.changeToGameOverRoom();
        }
    }

    /**
     * Creates a bullet based on the character's current position and weapon stats.
     *
     * @return a Bullet object representing the shot fired by the character
     */
    public Bullet createBulletTemplate() {
        return new Bullet(this.position, weaponDamage, bulletSpeed, this.position.x, this.position.y);
    }

    /**
     * Returns the number of bonus coins earned by the character.
     *
     * @return bonus coins amount (default is 0)
     */
    public double getBonusCoins() {
        return 0;
    }

    /**
     * Returns the bounding box of the character for collision detection.
     *
     * @return the Rectangle bounding box around the character
     */
    public Rectangle getBoundingBox() {
        return currImage.getBoundingBoxAt(position);
    }

    /**
     * Records that the character has collected a key in a specific room.
     *
     * @param roomName the name of the room where the key was collected
     */
    public void collectKey(String roomName) {
        collectedKeys.add(roomName);
    }

    /**
     * Checks if the character has already collected a key from the given room.
     *
     * @param roomName the name of the room to check
     * @return true if the key was collected, false otherwise
     */
    public boolean hasCollectedKey(String roomName) {
        return collectedKeys.contains(roomName);
    }

    /**
     * Returns current position
     * @return the current position of the character
     */
    public Point getPosition() { return position; }

    /**
     * Sets position
     * @param p the new position of the character
     */
    public void setPosition(Point p) { this.position = p; }

    /**
     * Returns previous position
     * @return the previous position of the character
     */
    public Point getPrevPosition() { return prevPosition; }

    /**
     * Sets previous position
     * @param p the previous position of the character
     */
    public void setPrevPosition(Point p) { this.prevPosition = p; }

    /**
     * Gets the current image
     * @return the current image representing the character
     */
    public Image getCurrImage() { return currImage; }

    /**
     * Sets current image
     * @param img the new image to represent the character
     */
    public void setCurrImage(Image img) { this.currImage = img; }

    /**
     * Gets the health of the character
     * @return the current health of the character
     */
    public static double getHealth() { return health;}

    /**
     * Sets health
     * @param h the new health value
     */
    public static void setHealth(double h) { health = h; }

    /**
     * Gets the speed
     * @return the character's movement speed
     */
    public static double getSpeed() { return speed; }

    /**
     * Sets speed
     * @param s the new movement speed
     */
    public static void setSpeed(double s) { speed = s; }

    /**
     * Gets coins
     * @return the total coins collected by the character
     */
    public static double getCoins() { return coins; }

    /**
     * Sets coins
     * @param amount the amount of coins to add
     */
    public static void earnCoins(double amount) { coins += amount; }

    /**
     * Gets weapon
     * @return the weapon level or damage multiplier
     */
    public static double getWeapon() { return weapon; }

    /**
     * Sets weapon
     * @param weapon the new weapon value
     */
    public static void setWeapon(int weapon) { Character.weapon = weapon; }

    /**
     * Gets Key
     * @return the total number of keys collected
     */
    public static int getKey() { return key; }

    /** @param k the new number of keys */
    public static void setKey(int k) { key = k; }

    /** Sets Weapon Damage
     * @param weapon the new weapon damage
     */
    public void setWeaponDamage(double weapon) { this.weaponDamage = weapon; }

    /**
     * Called when the character receives damage (e.g., from the river).
     * @param damage the amount of damage taken
     */
    public abstract void receiveDamage(double damage);

    /**
     * Updates the character's state based on input.
     * @param input the player input or game state
     */
    public abstract void update(bagel.Input input);

    /** Draws the character to the screen. */
    public abstract void draw();

    /**
     * Returns a list of bullets fired by the character.
     * @return an ArrayList of Bullet objects
     */
    public abstract ArrayList<Bullet> getBullets();
}
