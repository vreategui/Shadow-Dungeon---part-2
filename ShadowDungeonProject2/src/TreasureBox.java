import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

/**
 * A treasure box that gives coins to the player when collected.
 */
public class TreasureBox extends GameObject implements Earnable {
    private final double coinValue;

    /**
     * Creates a treasure box at the given position.
     *
     * @param point The position of the treasure box.
     * @param coinValue The number of coins the box gives when opened.
     */
    public TreasureBox(Point point, int coinValue) {
        super(point, "res/treasure_box.png");
        this.coinValue = coinValue;
    }

    /**
     * Gets coins
     * @return coin value of treasure
     */
    public double getCoins() {
        return coinValue;
    }

    /**
     * Rewards the character with coins and uses one key.
     * @param character The character opening the box.
     */
    @Override
    public void onEarn(Character character) {
        Character.earnCoins(coinValue);
        Character.setKey(Character.getKey() - 1);
        this.destroy();
    }

    /**
     * Updates the treasure box and checks if it should be opened.
     *
     * @param input The game input.
     * @param currCharacter The current character.
     */
    public void update(Input input, Character currCharacter) {
        if (isDestroyed()) return;
        if (hasCollidedWith(currCharacter) && input.wasPressed(Keys.K) && Character.getKey() >= 1) {
            onEarn(currCharacter);
        }
    }

}
