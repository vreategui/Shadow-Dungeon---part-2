import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a fireball projectile shot by enemies.
 */
public class Fireball extends Projectile {

    /**
     * Creates a Fireball with damage and speed loaded from game properties.
     *
     * @param startPos  Starting position of the fireball.
     * @param targetPos Target position the fireball is aimed at.
     */
    public Fireball(Point startPos, Point targetPos) {
        super(
                startPos,
                Double.parseDouble(ShadowDungeon.getGameProps().getProperty("fireballDamage")),
                Double.parseDouble(ShadowDungeon.getGameProps().getProperty("fireballSpeed")),
                targetPos.x,
                targetPos.y,
                "res/fireball.png"
        );
    }

    /**
     * Updates fireball movement, handles collisions with player and environment (except rivers),
     * and deactivates it if it hits the player.
     *
     * @param player        The player character.
     * @param enemies       List of all enemies (unused here, handled in super).
     * @param environment   List of environment objects to check collisions against.
     * @param screenWidth   Width of the game screen.
     * @param screenHeight  Height of the game screen.
     */
    @Override
    public void updateAndCheckCollisions(Character player, List<Enemy> enemies, List<GameObject> environment, double screenWidth, double screenHeight) {
        // Filter out rivers so fireballs can cross them
        List<GameObject> filteredEnvironment = new ArrayList<>();
        for (GameObject obj : environment) {
            if (!(obj instanceof River)) filteredEnvironment.add(obj);
        }

        super.updateAndCheckCollisions(player, enemies, filteredEnvironment, screenWidth, screenHeight);

        // Damage the player if intersecting
        if (getBoundingBox().intersects(player.getBoundingBox())) {
            player.takeDamage(damage);
            deactivate();
        }
    }

}
