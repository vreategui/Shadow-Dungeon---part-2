import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;


/**
 * Represents the Marine character in the game.
 */
public class Marine extends Character {
    private static final Image SPRITE = new Image("res/marine_sprite.png");
    private static final Image RIGHT_IMAGE = new Image("res/marine_right.png");
    private static final Image LEFT_IMAGE = new Image("res/marine_left.png");

    private final Point previewPosition;
    private boolean faceLeft = false;
    private boolean choosen = false;
    private boolean dead = false;

    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private int shootCooldown = 0;

    /**
     * Creates a new Marine.
     *
     * @param position The starting position of the Marine.
     */
    public Marine(Point position) {
        super(position,
                RIGHT_IMAGE,
                Double.parseDouble(ShadowDungeon.getGameProps().getProperty("initialHealth")),
                Double.parseDouble(ShadowDungeon.getGameProps().getProperty("movingSpeed")));
        this.previewPosition = position;
    }

    /**
     * Updates the Marine based on user input.
     *
     * @param input The current input.
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
     * Handles shooting logic for the Marine.
     *
     * @param input The current input.
     */
    private void handleShooting(Input input) {
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        if (input.isDown(MouseButtons.LEFT) && shootCooldown <= 0) {
            Point bulletStart = getBoundingBox().centre();
            Point target = new Point(input.getMouseX(), input.getMouseY());
            bullets.add(new Bullet(bulletStart, this.weaponDamage, this.bulletSpeed, target.x, target.y));
            shootCooldown = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("bulletFreq"));
        }
    }


    /**
     * Draws the Marine and its stats.
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
     * Marine does not receive damage in this version.
     *
     * @param damage The amount of damage.
     */
    @Override
    public void receiveDamage(double damage) {
        return;
    }

    /**
     * Returns the list of bullets fired by the Marine.
     *
     * @return The list of bullets.
     */
    @Override
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    /**
     * Draws the Marine statue (preview).
     */
    public void drawStatue() {
        SPRITE.draw(previewPosition.x, previewPosition.y);
    }

    /**
     * Returns the preview position of the Marine.
     *
     * @return The preview position.
     */
    public Point getPreviewPosition() { return previewPosition; }

    /**
     * Checks if the Marine is chosen.
     *
     * @return True if chosen, false otherwise.
     */
    public boolean isChoosen() { return choosen; }

    /**
     * Sets whether the Marine is chosen.
     *
     * @param chosen True to set as chosen.
     */
    public void setChoosen(boolean chosen) { this.choosen = chosen; }

    /**
     * Checks if the Marine is dead.
     *
     * @return True if dead, false otherwise.
     */
    public boolean isDead() { return dead; }
}
