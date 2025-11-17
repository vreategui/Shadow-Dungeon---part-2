import bagel.Font;
import bagel.Window;
import bagel.util.Point;

/**
 * Helper methods to display information for the player
 */
public class UserInterface {

    /**
     * Draws the player stats on screen.
     *
     * @param health Player's current health.
     * @param coins Player's current coins.
     * @param weapon Player's weapon level.
     * @param key Player's number of keys.
     */
    public static void drawStats(double health, double coins, int weapon, int key) {
        int fontSize = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("playerStats.fontSize"));
        drawData(String.format("%s %.1f", ShadowDungeon.getMessageProps().getProperty("healthDisplay"), health), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("healthStat")));
        drawData(String.format("%s %.0f", ShadowDungeon.getMessageProps().getProperty("coinDisplay"), coins), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("coinStat")));
        drawData(String.format("%s %d", ShadowDungeon.getMessageProps().getProperty("weaponDisplay"), weapon), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("weaponStat")));
        drawData(String.format("%s %d", ShadowDungeon.getMessageProps().getProperty("keyDisplay"), key), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("keyStat")));
    }

    /**
     * Draws the start screen messages for character selection and movement instructions.
     */
    public static void drawStartMessages() {
        drawTextCentered("title", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("title.fontSize")),
                Double.parseDouble(ShadowDungeon.getGameProps().getProperty("title.y")));
        drawTextCentered("moveMessage", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("prompt.fontSize")),
                Double.parseDouble(ShadowDungeon.getGameProps().getProperty("moveMessage.y")));
        drawTextCentered("selectMessage", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("prompt.fontSize")),
                Double.parseDouble(ShadowDungeon.getGameProps().getProperty("selectMessage.y")));
        Point robotPos = IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("robotMessage"));
        Point marinePos = IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("marineMessage"));

        UserInterface.drawData(ShadowDungeon.getMessageProps().getProperty("robotDescription"),
                Integer.parseInt(ShadowDungeon.getGameProps().getProperty("playerStats.fontSize")),
                robotPos);

        UserInterface.drawData(ShadowDungeon.getMessageProps().getProperty("marineDescription"),
                Integer.parseInt(ShadowDungeon.getGameProps().getProperty("playerStats.fontSize")),
                marinePos);
    }

    /**
     * Draws the end game message based on whether the player won or lost.
     *
     * @param win True if the player won, false if lost.
     */
    public static void drawEndMessage(boolean win) {
        drawTextCentered(win ? "gameEnd.won" : "gameEnd.lost", Integer.parseInt(ShadowDungeon.getGameProps()
                .getProperty("title.fontSize")), Double.parseDouble(ShadowDungeon.getGameProps().getProperty("title.y")));
    }

    /**
     * Draws a text string centered horizontally on the screen.
     *
     * @param textPath Path to the text in the message properties.
     * @param fontSize Size of the font.
     * @param posY Vertical position to draw the text.
     */
    public static void drawTextCentered(String textPath, int fontSize, double posY) {
        Font font = new Font("res/wheaton.otf", fontSize);
        String text = ShadowDungeon.getMessageProps().getProperty(textPath);
        double posX = (Window.getWidth() - font.getWidth(text)) / 2;
        font.drawString(text, posX, posY);
    }

    /**
     * Draws a string at a specific location on the screen.
     *
     * @param data The string to draw.
     * @param fontSize Size of the font.
     * @param location The screen position to draw the string.
     */
    public static void drawData(String data, int fontSize, Point location) {
        Font font = new Font("res/wheaton.otf", fontSize);
        font.drawString(data, location.x, location.y);
    }
}
