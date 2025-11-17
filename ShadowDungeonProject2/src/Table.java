import bagel.util.Point;

/**
 * A table object placed in the game environment.
 */
public class Table extends GameObject {

    /**
     * Creates a table at the given position.
     * @param point The position of the table.
     */
    public Table(Point point) {
        super(point, "res/table.png");
    }
}
