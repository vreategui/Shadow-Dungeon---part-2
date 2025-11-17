import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Bullet projectile fired by a character.
 * Handles movement, collisions, and interaction with enemies and environment objects.
 */
public class Bullet extends Projectile {

    /**
     * Constructs Bullet.
     *
     * @param startPos The starting position of the bullet.
     * @param damage   The damage this bullet deals on impact.
     * @param speed    The speed at which the bullet travels.
     * @param targetX  The x-coordinate of the bullet's target.
     * @param targetY  The y-coordinate of the bullet's target.
     */
    public Bullet(Point startPos, double damage, double speed, double targetX, double targetY){
        super(startPos, damage, speed, targetX, targetY, "res/bullet.png");
    }

    /**
     * Updates the bullet's position and checks for collisions with enemies and the environment.
     * Deals damage to enemies and collects coins if applicable.
     *
     * @param player       The character who fired the bullet.
     * @param enemies      The list of enemies in the room.
     * @param environment  The list of collidable environment objects.
     * @param screenWidth  The width of the game screen.
     * @param screenHeight The height of the game screen.
     */
    @Override
    public void updateAndCheckCollisions(
            Character player,
            List<Enemy> enemies,
            List<GameObject> environment,
            double screenWidth,
            double screenHeight
    ) {
        List<GameObject> filteredEnvironment = new ArrayList<>();
        for (GameObject obj : environment){
            if (!(obj instanceof River)) filteredEnvironment.add(obj);
        }
        super.updateAndCheckCollisions(player, enemies, filteredEnvironment, screenWidth, screenHeight);
        checkCollisions(new ArrayList<>(environment), player);

        for (Enemy enemy : enemies){
            if (!isActive()) break;
            if (enemy.isDead() || !enemy.isActive()) continue;
//            if (enemy instanceof KeyBulletKin && enemy.isDead()) continue;
            if (getBoundingBox().intersects(enemy.getBoundingBox())){
                enemy.takeDamage(damage);
                deactivate();
                if (enemy.isDead() && !(enemy instanceof KeyBulletKin)){
                    Character.earnCoins(enemy.getCoin() + player.getBonusCoins());
                }
                break;
            }
        }
    }

    /**
     * Checks collisions between bullet and environment objects.
     * Deactivates the bullet and interacts with objects when a collision occurs.
     *
     * @param collidableObjects The list of objects to check collisions against.
     * @param player            The character who fired the bullet.
     */
    public void checkCollisions(ArrayList<Object> collidableObjects, Character player){
        for (Object object : collidableObjects){
            if (object instanceof Table table){
                if (!table.isDestroyed() && this.getBoundingBox().intersects(table.getBoundingBox())){
                    table.destroy();
                    this.deactivate();
                    return;
                }
            } else if (object instanceof Basket basket){
                if (!basket.isDestroyed() && this.getBoundingBox().intersects(basket.getBoundingBox())){
                    basket.destroy();
                    Character.earnCoins(basket.getCoins());
                    this.deactivate();
                    return;
                }
            } else if (object instanceof Door door){
                if (this.getBoundingBox().intersects(door.getBoundingBox())){
                    this.deactivate();
                    return;
                }
            }
        }
    }
}
