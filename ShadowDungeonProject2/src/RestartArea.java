import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

/**
 * Area in Prep or End Room where the player can trigger a game reset
 */
public class RestartArea {
    private final Point position;
    private final Image image;

    /**
     * Creates a restart area at the given position.
     * @param position the position where it is to sit
     */
    public RestartArea(Point position) {
        this.position = position;
        this.image = new Image("res/restart_area.png");
    }

    /**
     * Updates the restart area each frame.
     * @param input the input executed by user
     * @param currCharacter the player
     */
    public void update(Input input, Character currCharacter) {
        if (hasCollidedWith(currCharacter) && input.wasPressed(Keys.ENTER)) {
            ShadowDungeon.resetGameState(ShadowDungeon.getGameProps());
        }
    }

    /**
     * Draws the restart area on screen.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }

    /**
     * Checks if the given character is touching the restart area.
     *
     * @param currCharacter The character to check.
     * @return true if the character is colliding with the area, false otherwise.
     */
    public boolean hasCollidedWith(Character currCharacter) {
        return image.getBoundingBoxAt(position).intersects(
                currCharacter.getCurrImage().getBoundingBoxAt(currCharacter.getPosition()));
    }

}
