import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

/**
 * Store where the player can buy upgrades.
 * Allows purchasing weapon and health upgrades.
 */
public class Store {
    private final Image image = new Image("res/store.png");
    private final Point position;
    private boolean active = false;
    Character character;
    Bullet bullet;

    /**
     * Creates a store at the position specified in the game properties.
     */
    public Store(){
        position= IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("store"));
    }

    /**
     * Updates the store logic each frame.
     * Handles purchasing weapons, health, and restarting the game.
     *
     * @param input The player input.
     */
    public void update(Input input) {
        if (!active || character == null){
            return;
        }
        if (bullet == null) {
            if (!character.getBullets().isEmpty()) {
                bullet= character.getBullets().get(0); // first bullet is the current weapon
            } else {
                return;
            }
        }
        if (input.wasPressed(Keys.L)) {
            double weaponCost = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("weaponPurchase"));
            double weaponStandardDamage = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("weaponStandardDamage"));
            double weaponAdvancedDamage = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("weaponAdvanceDamage"));
            double weaponEliteDamage = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("weaponEliteDamage"));

            if (Character.getCoins() >= weaponCost) {
                double newDamage;
                int currentLevel = (int) Character.getWeapon();

                if (currentLevel == 0) {
                    newDamage = weaponAdvancedDamage;
                    Character.setWeapon(1);
                } else if (currentLevel == 1) {
                    newDamage = weaponEliteDamage;
                    Character.setWeapon(2);
                } else {
                    return;
                }
                Character.earnCoins(-weaponCost);
                character.setWeaponDamage(newDamage);
                for (Bullet bullet : character.getBullets()) {
                    bullet.setDamage(newDamage);
                }
            }
        }
        if (input.wasPressed(Keys.E)) {
            double healthCost = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("healthPurchase"));
            double healthBonus = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("healthBonus"));

            if (Character.getCoins() >= healthCost) {
                Character.earnCoins(-healthCost);
                Character.setHealth(Character.getHealth() + healthBonus);
            }
        }
        if (input.wasPressed(Keys.P)) {
            ShadowDungeon.resetGameState(ShadowDungeon.getGameProps());
            character = ShadowDungeon.getCurrCharacter();
            bullet = null;
        }
    }
    /**
     * Draws the store if it is active.
     */
    public void draw(){
        if (active) {
            image.draw(position.x, position.y);
        }
    }

    /**
     * Sets the bullet reference for weapon upgrades.
     * @param bullet the bullets occuring
     */
    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    /**
     * Sets store active.
     * @param active the status
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Checks whether store is active
     * @return whether the store is active.
     */
    public boolean getActive(){
        return active;
    }
}
