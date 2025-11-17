import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * Represents the Default character in the game.
 */
public class Player extends Character {
    private boolean faceLeft = false;
    private boolean choosen = false;
    private boolean dead = false;
    private static final Image RIGHT_IMAGE = new Image("res/player_right.png");
    private static final Image LEFT_IMAGE = new Image("res/player_left.png");

    /**
     * Creates a new Player.
     *
     * @param position The starting position of the player.
     */
    public Player(Point position) {
        super(position, RIGHT_IMAGE, Double.parseDouble(ShadowDungeon.getGameProps().getProperty("initialHealth")), Double.parseDouble(ShadowDungeon.getGameProps().getProperty("movingSpeed")));
        Character.earnCoins(0);
    }

    /**
     * Updates the player's movement based on user input.
     *
     * @param input The current input.
     */
    @Override
    public void update(Input input) {
        double currX = getPosition().x;
        double currY = getPosition().y;

        if (input.isDown(Keys.A)) currX -= getSpeed();
        if (input.isDown(Keys.D)) currX += getSpeed();
        if (input.isDown(Keys.W)) currY -= getSpeed();
        if (input.isDown(Keys.S)) currY += getSpeed();

        faceLeft = input.getMouseX() < currX;

        Rectangle rect = getCurrImage().getBoundingBoxAt(new Point(currX, currY));
        Point topLeft = rect.topLeft();
        Point bottomRight = rect.bottomRight();
        if (topLeft.x >= 0 && bottomRight.x <= Window.getWidth()
                && topLeft.y >= 0 && bottomRight.y <= Window.getHeight()) {
            move(currX, currY);
        }
    }

    /**
     * Moves the player to the specified coordinates.
     *
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    @Override
    public void move(double x, double y) {
        setPrevPosition(getPosition());
        setPosition(new Point(x, y));
    }

    /**
     * Draws the player and its current stats.
     */
    @Override
    public void draw() {
        UserInterface.drawStats(getHealth(), Character.getCoins(), (int)Character.getWeapon(), Character.getKey());
        setCurrImage(faceLeft ? LEFT_IMAGE : RIGHT_IMAGE);
        getCurrImage().draw(getPosition().x, getPosition().y);
    }

    /**
     * Returns the bullets fired by the player (not applicable here).
     *
     * @return {@code null}, as the player does not shoot.
     */
    @Override
    public ArrayList<Bullet> getBullets() {
        return null;
    }

    /**
     * Handles receiving damage (not implemented).
     *
     * @param damage The amount of damage.
     */
    @Override
    public void receiveDamage(double damage) {return;}

    /**
     * Placeholder for applying damage to the player.
     *
     * @param dmg The damage to apply.
     */
    public void takeDamage(double dmg) {return;}

    /**
     * Checks if the player is chosen.
     *
     * @return True if chosen, false otherwise.
     */
    public boolean getChoosen() {return choosen;}

    /**
     * Sets whether the player is chosen.
     *
     * @param choosen True to mark as chosen.
     */
    public void setChoosen(boolean choosen) {this.choosen = choosen;}

    /**
     * Checks if the player is dead.
     *
     * @return True if dead, false otherwise.
     */
    public boolean isDead() {return dead;}

    /**
     * Sets the player's death status.
     *
     * @param dead True to mark as dead.
     */
    public void setDead(boolean dead) {this.dead = dead;}
}
