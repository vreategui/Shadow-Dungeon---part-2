/**
 * Represents coins that can be earned by the player.
 * Classes implementing this interface define how many coins are awarded and what happens
 * when the object is earned.
 */
public interface Earnable {

    /**
     * Returns the number of coins earned from this object.
     *
     * @return the coin value as a double
     */
    public double getCoins();

    /**
     * Performs logic when the object is earned by a character.
     * This may include adding coins, updating stats, or triggering events.
     *
     * @param character the character who earns this object
     */
    public void onEarn(Character character);
}
