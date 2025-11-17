import bagel.Input;
import bagel.Keys;

import java.util.Map;
import java.util.Properties;

/**
 * Room where the game ends when the player either completes all rooms or dies
 */
public class EndRoom {
    private Character currCharacter;
    private Door door;
    private RestartArea restartArea;
    private boolean isGameOver = false;
    private boolean stopCurrentUpdateCall = false;
    private Store store;
    private boolean paused = false;

    /**
     * Constructs the EndRoom and initializes the in-room store.
     */
    public EndRoom() {
        this.store = new Store();
    }

    /**
     * Initializes entities (e.g., door, restart area) within the end room
     * using the provided game configuration properties.
     *
     * @param gameProperties The configuration containing room and entity data.
     */
    public void initEntities(Properties gameProperties) {
        for (Map.Entry<Object, Object> entry: gameProperties.entrySet()) {
            String roomSuffix = String.format(".%s", ShadowDungeon.END_ROOM_NAME);
            if (entry.getKey().toString().contains(roomSuffix)) {
                String objectType = entry.getKey().toString().substring(0, entry.getKey().toString().length() - roomSuffix.length());
                String propertyValue = entry.getValue().toString();

                switch (objectType) {
                    case "door":
                        String[] coordinates = propertyValue.split(",");
                        door = new Door(IOUtils.parseCoords(propertyValue), coordinates[2]);
                        break;
                    case "restartarea":
                        restartArea = new RestartArea(IOUtils.parseCoords(propertyValue));
                        break;
                    default:
                }
            }
        }
    }
    private void drawWorld() {
        if (door != null) {
            door.draw();
        }
        if (restartArea != null) {
            restartArea.draw();
        }
        if (currCharacter != null) {
            currCharacter.draw();
            for (Bullet b : currCharacter.getBullets()) {
                b.draw();
            }
        }
        UserInterface.drawEndMessage(!isGameOver);
    }

    private void updateWorld(Input input) {
        if (isGameOver) {
            findDoor().lock();
        }
        if (door != null) {
            door.update(currCharacter);
        }
        if (restartArea != null) {
            restartArea.update(input, currCharacter);
        }
        if (stopUpdatingEarlyIfNeeded()) {
            return;
        }
        if (currCharacter != null) {
            currCharacter.update(input);
            for (Bullet b : currCharacter.getBullets()) {
                b.update();
            }
        }
    }

    /**
     * Updates the state of the EndRoom based on user input.
     * This includes updating the store, player, door, and restart area.
     *
     * @param input The current input from the player.
     */
    public void update(Input input) {
        if (input.wasPressed(Keys.SPACE)) {
            store.setActive(!store.getActive());
        }

        if (store.getActive()) {
            drawWorld();
            store.update(input);
            store.draw();
            return;
        }
        updateWorld(input);
        drawWorld();
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
     * Sets the current player character for this room and initializes the store with it.
     *
     * @param currCharacter The player's character.
     */
    public void setCurrCharacter(Character currCharacter) {
        this.currCharacter = currCharacter;
        store.character = currCharacter;
        store.setBullet(currCharacter.createBulletTemplate());
    }

    /**
     * Sets the paused state of the room.
     *
     * @param paused true if the room should be paused, false otherwise.
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Flags the current update cycle to stop early.
     */
    public void stopCurrentUpdateCall() {
        stopCurrentUpdateCall = true;
    }

    /**
     * Marks the game as over. Typically called when the player dies or wins.
     */
    public void isGameOver() {
        isGameOver = true;
    }

    /**
     * Returns the door object in this room.
     *
     * @return the door object.
     */
    public Door findDoor() {
        return door;
    }

    /**
     * Returns the door object based on destination.
     * In this implementation, always returns the same door.
     *
     * @return the door object.
     */
    public Door findDoorByDestination() {
        return door;
    }
}
