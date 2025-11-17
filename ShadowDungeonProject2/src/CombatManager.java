import java.util.ArrayList;
import java.util.List;

/**
 * Manages the combat interactions in the game, including processing projectiles
 * and handling collisions between the player, enemies, and the environment.
 */

public class CombatManager {

    /**
     * Processes combat by updating all active enemy and player projectiles,
     * checking for collisions against the player, enemies, and environmental objects,
     * and removing any inactive projectiles after processing.
     *
     * @param player            the player character object involved in combat
     * @param enemyProjectiles  a list of enemy fireballs
     * @param playerProjectiles a list of projectiles fired by the player
     * @param keyBulletKin      a list of KeyBulletKin enemies
     * @param bulletKins        a list of BulletKin enemies
     * @param ashenEnemies      a list of AshenEnemy enemies
     * @param environment       a list of environmental objects
     */
    public void processCombat(
            Character player,
            List<Fireball> enemyProjectiles,
            List<Bullet> playerProjectiles,
            List<KeyBulletKin> keyBulletKin,
            List<BulletKin> bulletKins,
            List<AshenEnemy> ashenEnemies,
            List<GameObject> environment
    )
    {
        List<Enemy> allEnemies = new ArrayList<>();
        allEnemies.addAll(keyBulletKin);
        allEnemies.addAll(bulletKins);
        allEnemies.addAll(ashenEnemies);

        for (Fireball fireball : enemyProjectiles) {
            fireball.updateAndCheckCollisions(player, allEnemies, environment, ShadowDungeon.screenWidth, ShadowDungeon.screenHeight);
        }
        enemyProjectiles.removeIf(p -> !p.isActive());

        for (Bullet bullet : playerProjectiles) {
            bullet.updateAndCheckCollisions(player, allEnemies, environment, ShadowDungeon.screenWidth, ShadowDungeon.screenHeight);
        }
        playerProjectiles.removeIf(p -> !p.isActive());
    }
}