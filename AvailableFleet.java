/**
 * AvailableFleet class for the Project 2 - Battleship game.
 *
 * @author Ruben Nogueira 68536 - 1º LEI
 * @author Alexandre Carvalho 67950 - 1º LEI
 *
 */

public class AvailableFleet {
    // variables of AvailableFleet Class
    private final char[][] board;

    /**
     * Constructor for AvailableFleets
     * Creates a new AvailableFleet with the given board.
     *
     * @param board The board of the fleet
     * @pre board != null
     */
    public AvailableFleet(char[][] board) {
        this.board = board;
    }

    /**
     * This method returns the board of the fleet
     *
     * @return char[][] The board of the fleet
     * @pre board != null
     */
    public char[][] getBoard() {
        return board;
    }
}
