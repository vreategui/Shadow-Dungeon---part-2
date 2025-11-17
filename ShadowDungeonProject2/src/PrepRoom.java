import bagel.Input;
import bagel.Keys;

import java.util.Map;
import java.util.Properties;
import bagel.Image;

/**
 * Room where the game starts
 */
public class PrepRoom {
    private Player player;
    private Robot robot;
    private Marine marine;
    private Character currCharacter;
    private Door door;
    private RestartArea restartArea;
    private boolean stopCurrentUpdateCall = false;
    private boolean begin = false;


    /**
     * Sets up the room entities from the game properties.
     */
    public void initEntities(Properties gameProperties) {
        // find the configuration of game objects for this room
        for (Map.Entry<Object, Object> entry: gameProperties.entrySet()) {
            String roomSuffix = String.format(".%s", ShadowDungeon.PREP_ROOM_NAME);
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

    /**
     * Updates the room each frame.
     * Handles character switching, movement, and drawing.
     */
    public void update(Input input) {
        UserInterface.drawStartMessages();

        if (currCharacter == null) {
            currCharacter = ShadowDungeon.getCurrCharacter();
            if (currCharacter == null) {
                currCharacter = player; // fallback first time
                ShadowDungeon.setCurrCharacter(player);
            }
        }

        if (currCharacter instanceof Player) {
            ((Player) currCharacter).setChoosen(true);
            if (robot != null) robot.setChoosen(false);
            if (marine != null) marine.setChoosen(false);
        } else if (currCharacter instanceof Robot) {
            ((Robot) currCharacter).setChoosen(true);
            if (marine != null) marine.setChoosen(false);
            if (player != null) player.setChoosen(false);
        } else if (currCharacter instanceof Marine) {
            ((Marine) currCharacter).setChoosen(true);
            if (robot != null) robot.setChoosen(false);
            if (player != null) player.setChoosen(false);
        }


        if (door != null && currCharacter != null) {
            door.update(currCharacter);
            door.draw();
        }
        if (stopUpdatingEarlyIfNeeded()) {
            return;
        }

        if (restartArea != null && currCharacter != null) {
            restartArea.update(input, currCharacter);
            restartArea.draw();
        }
        robot.drawStatue();
        marine.drawStatue();


        if (currCharacter != null) {
            currCharacter.update(input);
            currCharacter.draw();
        }

        if (input.wasPressed(Keys.M) && marine != null) {
            marine.move(currCharacter.getPosition().x, currCharacter.getPosition().y);
            currCharacter = marine;
            ShadowDungeon.setCurrCharacter(marine);
            marine.setChoosen(true);
            if (robot != null) robot.setChoosen(false);
            if (player != null) player.setChoosen(false);
            findDoor().unlock(false);
        }

        if (input.wasPressed(Keys.R) && robot != null) {
            robot.move(currCharacter.getPosition().x, currCharacter.getPosition().y);
            currCharacter = robot;
            ShadowDungeon.setCurrCharacter(robot);
            robot.setChoosen(true);
            if (marine != null) marine.setChoosen(false);
            if (player != null) player.setChoosen(false);
            findDoor().unlock(false);
        }

        if (input.wasPressed(Keys.M) || input.wasPressed(Keys.R) && !findDoor().isUnlocked()){
            findDoor().unlock(false);
        }

        if (input.wasPressed(Keys.R) && !findDoor().isUnlocked()) {
            findDoor().unlock(false);
        }
    }

    private boolean stopUpdatingEarlyIfNeeded() {
        if (stopCurrentUpdateCall) {
            player = null;
            stopCurrentUpdateCall = false;
            return true;
        }
        return false;
    }
    /**
     * Sets the player.
     */
    public void setPlayer(Player player) { this.player = player; }

    /**
     * Sets the robot.
     */
    public void setRobot(Robot robot) { this.robot = robot; }

    /**
     *  Sets the marine.
     */
    public void setMarine(Marine marine) { this.marine = marine; }

    /**
     *  Sets the current character.
     */
    public void setCurrCharacter(Character currCharacter) { this.currCharacter = currCharacter; }

    /**
     *  Flags the update to stop next call.
     */
    public void stopCurrentUpdateCall() { stopCurrentUpdateCall = true; }

    /**
     *  Returns the door.
     */
    public Door findDoor() { return door; }

    /**
     *  Returns the door.
     */
    public Door findDoorByDestination() { return door; }
}
