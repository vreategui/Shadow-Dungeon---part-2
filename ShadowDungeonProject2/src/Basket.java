import bagel.util.Point;

/**
 * Represents a Basket in the game that can be collected to earn coins.
 */
public class Basket extends GameObject implements Earnable{
    private final double coins;

    /**
     * Creates a Basket at the specified position.
     * @param point the position of the Basket in the game world
     */

    public Basket(Point point) {
        super(point, "res/basket.png");
        this.coins = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("basketCoin"));
    }

    /**
     * Returns the number of coins this Basket provides.
     * @return the coin value of the Basket
     */
    public double getCoins() {
        return coins;
    }

    /**
     * Called when a Character collects the Basket.
     * Adds coins to the character and destroys the Basket.
     *
     * @param character the Character collecting the Basket
     */
    @Override
    public void onEarn(Character character) {
        Character.earnCoins(coins);
        this.destroy();
    }
}
