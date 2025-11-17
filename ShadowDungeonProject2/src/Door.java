import bagel.Image;
import bagel.util.Point;

/**
 * Door which can be locked or unlocked, allows the player to move to the room it's connected to
 */
public class Door extends GameObject {
    private final Point position;
    public final String toRoomName;
    public BattleRoom battleRoom; // only set if this door is inside a BattleRoom
    private boolean unlocked = false;
    private boolean justEntered = false; // when the player only just entered this door's room
    private boolean shouldLockAgain = false;
    private static final Image LOCKED_DOOR_IMAGE = new Image("res/locked_door.png");
    private static final Image UNLOCKED_DOOR_IMAGE = new Image("res/unlocked_door.png");

    /**
     * Creates a door leading to another room.
     *
     * @param position    The position of the door.
     * @param toRoomName  The name of the room this door connects to.
     */
    public Door(Point position, String toRoomName) {
        super(position, "res/locked_door.png"); // GameObject constructor
        this.toRoomName = toRoomName;
        this.position = position;
    }

    /**
     * Creates a door inside a BattleRoom.
     *
     * @param position    The position of the door.
     * @param toRoomName  The name of the connected room.
     * @param battleRoom  The BattleRoom this door belongs to.
     */
    public Door(Point position, String toRoomName, BattleRoom battleRoom) {
        super(position, "res/locked_door.png"); // GameObject constructor
        this.toRoomName = toRoomName;
        this.position = position;
        this.battleRoom = battleRoom;
    }

    /**
     * Updates the door's state based on the player's position.
     *
     * @param currCharacter The current character interacting with the door.
     */
    @Override
    public void update(Character currCharacter) {
        if (hasCollidedWith(currCharacter)) {
            onCollide(currCharacter);
        } else {
            onNoLongerCollide();
        }
    }

    private void onCollide(Character currCharacter) {
        if (unlocked && !justEntered) {
            ShadowDungeon.changeRoom(toRoomName);
        } else if (!unlocked) {
            currCharacter.move(currCharacter.getPrevPosition().x, currCharacter.getPrevPosition().y);
        }
    }

    private void onNoLongerCollide() {
        if (unlocked && justEntered) {
            justEntered = false;
            if (shouldLockAgain && battleRoom != null && !battleRoom.isComplete()) {
                lock();
                battleRoom.activateEnemies();
            }
        }
    }

    /**
     * Unlocks the door.
     *
     * @param justEntered Whether the player just entered through this door.
     */
    public void unlock(boolean justEntered) {
        unlocked = true;
        this.justEntered = justEntered;
    }

    /** Locks the door. */
    public void lock() {
        unlocked = false;
    }

    /**
     * Checks if the door is unlocked.
     *
     * @return true if unlocked, false otherwise.
     */
    public boolean isUnlocked() {
        return unlocked;
    }

    /** Marks the door to lock again after use. */
    public void setShouldLockAgain() {
        this.shouldLockAgain = true;
    }

    /** Draws the door on the screen. */
    @Override
    public void draw() {
        Image img = unlocked ? UNLOCKED_DOOR_IMAGE : LOCKED_DOOR_IMAGE;
        img.draw(position.x, position.y); // Bagel draws centered
    }

    /**
     * Returns the position of the door.
     *
     * @return The door's position.
     */
    public Point getPosition() {
        return position;
    }
}
