import bagel.Input;
import bagel.Keys;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Represents a battle room in the game where combat and interactions occur.
 */
public class BattleRoom {
    private Character currCharacter;
    private final Store store;
    private final EntityManager entityManager;
    private final CombatManager combatManager;
    private boolean stopCurrentUpdateCall = false;
    private boolean isComplete = false;
    private final String roomName;

    /**
     *Constructs a BattleRoom
     *Initializes the store, entity manager, and combat manager for the room.
     * @param roomName
     */
    public BattleRoom(String roomName) {
        store = new Store();
        this.entityManager = new EntityManager();
        this.combatManager = new CombatManager();
        this.roomName = roomName;
    }

    /**
     *Initializes all entities in the room using the provided game properties.
     *
     * @param gameProperties
     */
    public void initEntities(Properties gameProperties) {
        entityManager.initEntities(gameProperties, roomName, this);
    }

    /**
     * Updates the state of the room based on player input.
     * Handles toggling the store, pausing world updates, and updating the environment.
     *
     * @param input The current input state from the player.
     */

    public void update(Input input) {
        if (input.wasPressed(Keys.SPACE)) {store.setActive(!store.getActive());}
        if (store.getActive()) {
            drawWorld();//pause everything
            store.update(input);
            store.draw();
            return;
        }
        updateWorld(input);
    }

    private void drawWorld() {
        entityManager.drawDoors();
        entityManager.drawAll(currCharacter);
        if (currCharacter != null) currCharacter.draw();
    }

    private void updateWorld(Input input) {
        entityManager.updatePrimaryDoor(currCharacter);
        if (stopUpdatingEarlyIfNeeded()) return;
        entityManager.updateSecondaryDoor(currCharacter);
        if (stopUpdatingEarlyIfNeeded()) return;
        entityManager.updateAndDrawEnvironment(input, currCharacter,roomName);
        ArrayList<Fireball> newFireballs = entityManager.updateAndShootEnemies(currCharacter);
        entityManager.getFireballs().addAll(newFireballs);
        combatManager.processCombat(
                currCharacter,
                entityManager.getFireballs(),
                currCharacter.getBullets(),
                entityManager.getKeyBulletKin(),
                entityManager.getBulletKins(),
                entityManager.getAshenEnemies(),
                (List<GameObject>) (List<?>) entityManager.getCollidableEnvironment()
        );
        if (currCharacter != null) { currCharacter.update(input); currCharacter.draw(); }
        if (entityManager.noMoreEnemies() && !isComplete()) {
            setComplete(true);
            unlockAllDoors();
        }
    }

    /**
     * Stops the current update call and resets the room state.
     */
    public void stopCurrentUpdateCall() {
        stopCurrentUpdateCall = true;
        entityManager.resetRoomState();
    }

    /**
     * Sets the current character in the room and updates the store to reference this character.
     *
     * @param currCharacter The character to set as active in this room.
     */
    public void setCurrCharacter(Character currCharacter) {
        this.currCharacter = currCharacter;
        store.character = currCharacter;
        store.setBullet(currCharacter.createBulletTemplate());

    }

    /**
     * Finds a door in the room based on its destination name.
     *
     * @param roomName The name of the room the door leads to.
     * @return The Door object that leads to the specified room.
     */
    public Door findDoorByDestination(String roomName) {
        return entityManager.findDoorByDestination(roomName);
    }

    private void unlockAllDoors() {
        entityManager.unlockAllDoors();
    }

    private boolean stopUpdatingEarlyIfNeeded() {
        if (stopCurrentUpdateCall) {
            currCharacter = null;
            stopCurrentUpdateCall = false;
            return true;
        }
        return false;
    }

    /**
     * Checks whether the room has been completed (all enemies defeated).
     *
     * @return True if the room is complete, false otherwise.
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Sets the completion status of the room.
     *
     * @param complete True to mark the room as complete, false otherwise.
     */
    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    /**
     * Activates all enemies in the room.
     */
    public void activateEnemies() {
        entityManager.activateEnemies();
    }


    /**
     * Sets the player for the room.
     * Currently unused but kept for compatibility.
     *
     * @param player The Player object to set.
     */
    public void setPlayer(Player player) {
    }
}