import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * The Robot character that the player can control.
 * Can move, shoot bullets, and take damage.
 */
public class Robot extends Character {
    private static final Image SPRITE = new Image("res/robot_sprite.png");
    private static final Image RIGHT_IMAGE = new Image("res/robot_right.png");
    private static final Image LEFT_IMAGE = new Image("res/robot_left.png");
    private final double EXTRACOINS =  Double.parseDouble(ShadowDungeon.getGameProps().getProperty("robotExtraCoin"));
    private final Point previewPosition;
    private boolean faceLeft = false;
    private boolean choosen = false;
    private boolean dead = false;
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private int shootCooldown = 0;

    /**
     * Creates a Robot at the given position.
     * @param position The starting position of the robot.
     */
    public Robot(Point position) {
        super(position, RIGHT_IMAGE, Double.parseDouble(ShadowDungeon.getGameProps().getProperty("initialHealth")), Double.parseDouble(ShadowDungeon.getGameProps().getProperty("movingSpeed")));
        this.previewPosition = position;
    }


    /**
     * Updates the robot each frame.
     * Handles movement, direction, and shooting.
     *
     * @param input The current player input.
     */
    @Override
    public void update(Input input) {
        double currX = getPosition().x;
        double currY = getPosition().y;
        double speed = getSpeed();

        if (input.isDown(Keys.A)) currX -= speed;
        if (input.isDown(Keys.D)) currX += speed;
        if (input.isDown(Keys.W)) currY -= speed;
        if (input.isDown(Keys.S)) currY += speed;

        faceLeft = input.getMouseX() < currX;
        setCurrImage(faceLeft ? LEFT_IMAGE : RIGHT_IMAGE);

        Rectangle rect = getCurrImage().getBoundingBoxAt(new Point(currX, currY));
        Point topLeft = rect.topLeft();
        Point bottomRight = rect.bottomRight();
        if (topLeft.x >= 0 && bottomRight.x <= Window.getWidth()
                && topLeft.y >= 0 && bottomRight.y <= Window.getHeight()) {
            move(currX, currY);
        }

        handleShooting(input);

        for (Bullet b : bullets) {
            b.update();
            b.draw();
        }
    }

    /**
     * Gets the extra coins
     * @return extra coins gained by the robot.
     */
    @Override
    public double getBonusCoins() {
        return this.EXTRACOINS;
    }

    /**
     * Handles shooting bullets when the mouse is pressed.
     * @param input the input executed by user
     */
    private void handleShooting(Input input) {
        // Decrease cooldown each frame
        if (shootCooldown > 0) {
            shootCooldown--;
        }

        // Only shoot if left mouse button is pressed and cooldown is ready
        if (input.isDown(MouseButtons.LEFT) && shootCooldown <= 0) {

            // Spawn bullet from the center of the character
            Point bulletStart = getBoundingBox().centre();

            // Target is the mouse position
            Point target = new Point(input.getMouseX(), input.getMouseY());

            // Create new bullet and add to player's bullet list
            bullets.add(new Bullet(bulletStart, this.weaponDamage, this.bulletSpeed, target.x, target.y));

            // Reset cooldown
            shootCooldown = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("bulletFreq"));
        }
    }


    /**
     * Draws the robot and its stats if it is chosen.
     */
    @Override
    public void draw() {
        UserInterface.drawStats(Character.getHealth(), Character.getCoins(),
                (int)Character.getWeapon(), Character.getKey());
        if (choosen) {
            getCurrImage().draw(getPosition().x, getPosition().y);
        }
    }

    /**
     * Reduces health when taking damage.
     * @param damage the amount of damage taken
     */
    @Override
    public void receiveDamage(double damage) {
        setHealth(getHealth() - damage);
        if (getHealth() <= 0) {
            dead = true;
            ShadowDungeon.changeToGameOverRoom();
        }
    }

    /**
     * Get Bullets
     * @return the list of bullets fired by the robot
     */
    @Override
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    /**
     * Draws the robot statue in the prep room.
     */
    public void drawStatue() {SPRITE.draw(previewPosition.x, previewPosition.y);}

    /**
     * Sets whether the robot is chosen.
     */
    public void setChoosen(boolean chosen) { this.choosen = chosen; }

}
