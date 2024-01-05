/**
 * Game Class for the Project 2 - Battleship game.
 *
 * @author Ruben Nogueira 68536 - 1º LEI
 * @author Alexandre Carvalho 67950 - 1º LEI
 *
 */

public class Player {
    // constants of Player Class
    private static final char HIT = '*';
    private static final char EMPTY = '.';
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String[] directions = { LEFT, RIGHT, UP, DOWN };
    // variables of Player Class
    private final String name;
    private int score;
    private boolean hasBeenEliminated;
    private final char[][] originalBoard;
    private char[][] board;

    /**
     * Constructor for Player
     * Creates a new Player with the given name and fleet.
     *
     * @param name  Name of the player
     * @param fleet Array of arrays of chars that represents the fleet of the player
     * @pre name != null && fleet != null && fleet.length > 0
     */
    public Player(String name, char[][] fleet) {
        this.name = name; // name of the player
        score = 0; // initial score of the player with value 0
        hasBeenEliminated = false; // initial value of the variable hasBeenEliminated with value false

        board = new char[fleet.length][fleet[0].length]; // board of the player, copy the fleet
        for (int i = 0; i < maxRow(); i++)
            System.arraycopy(fleet[i], 0, board[i], 0, maxCol());

        originalBoard = new char[fleet.length][fleet[0].length]; // originalBoard of the player, copy the fleet
        for (int i = 0; i < maxRow(); i++)
            System.arraycopy(fleet[i], 0, originalBoard[i], 0, maxCol());
    }

    /**
     * This method receives an int called score and adds or subtracts the value to
     * the player's score
     * 
     * @param score int that represents the score to add or subtract
     * @pre score != null
     */
    public void updateScore(int score) {
        this.score += score;
    }

    /**
     * This method returns the score of the player
     * 
     * @return int The player's score
     */
    public int getScore() {
        return score;
    }

    /**
     * This method returns the name of the player
     * 
     * @return String The player's name
     * @pre name != null
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns the board of the player
     *
     * @return char[][] The player's board
     * @pre board != null
     */
    public char[][] getFleet() {
        return board;
    }

    /**
     * Checks if the player has the given name
     *
     * @param name String that represents the name to check
     * @return boolean Whether the player has the given name
     * @pre name != null && this.name != null
     */
    public boolean hasName(String name) {
        return this.name.equals(name);
    }

    /**
     * Eliminates the player
     *
     * @pre hasBeenEliminated != null
     */
    public void eliminatePlayer() {
        hasBeenEliminated = true;
    }

    /**
     * Checks if the player has been eliminated
     * 
     * @return boolean Whether the player has been eliminated
     * @pre hasBeenEliminated != null
     */
    public boolean isEliminated() {
        return hasBeenEliminated;
    }

    /**
     * Updates the given position in the board with the value HIT
     * 
     * @param row int that represents the number of rows
     * @param col int that represents the number of columns
     * @pre row >= 0 && col >= 0 && row < maxRow() && col < maxCol() && board !=
     *      null
     */
    public void updateBoard(int row, int col) {
        board[row][col] = HIT;
    }

    /**
     * This method receives an array called position and returns the length of the
     * ship that was hit,
     * or the negative length of the ship that was already hit,
     * or 0 if the position is empty
     * 
     * @param row int that represents the row of the position to take the hit
     * @param col int that represents the column of the position to take the hit
     * @return int Length of the ship that was hit, or the negative length of the
     *              ship that was already hit, or 0 if the position is empty
     * @pre board != null && originalBoard != null && row != null && col != null
     */
    public int takeHit(int row, int col) {
        int length = 0;
        if (board[--row][--col] != EMPTY) { // --row and --col because the board index's starts at 0
            int[] position = new int[] { row, col };
            length += 1;
            for (int i = 0; i < directions.length; i++)
                length += chooseDir(directions[i], position, originalBoard[row][col]);
            if (originalBoard[row][col] != board[row][col])
                length = -length; // ship was already hit
            else
                updateBoard(row, col);
        }
        return length;
    }

    /**
     * Checks the length of the ship that was hit in the given direction
     * 
     * @param direction String that represents the direction to check
     * @param position  array that represents the position to take the hit
     * @param target    char that represents the target to check for
     * @return int Length of the ship that was hit in the given direction
     * @pre direction != null && position != null && target != null && board != null
     *      && originalBoard != null
     */
    private int chooseDir(String direction, int[] position, char target) {
        int rowDelta = 0;
        int colDelta = 0;

        switch (direction) {
            case LEFT -> colDelta = -1;
            case RIGHT -> colDelta = 1;
            case UP -> rowDelta = -1;
            case DOWN -> rowDelta = 1;
        }

        return checkDirection(position, target, rowDelta, colDelta);
    }

    /**
     * Checks the length of the ship that was hit in the given direction
     * It's an auxiliary method for the method ChooseDir (which is an
     * auxiliary method for the method
     * takeHit).
     *
     * @param position array that represents the position to take the hit
     * @param target   char that represents the target to check for
     * @param rowDelta int that represents the row delta
     * @param colDelta int that represents the column delta
     * @return int
     * @pre position != null && target != null && rowDelta != null
     *      && colDelta != null && board != null && originalBoard != null
     */
    private int checkDirection(int[] position, char target, int rowDelta, int colDelta) {
        int length = 0;
        int row = position[0] + rowDelta;
        int col = position[1] + colDelta;

        while (row >= 0 && row < maxRow() && col >= 0 && col < maxCol() &&
                originalBoard[row][col] == target) {
            length++;
            updateBoard(row, col);
            row += rowDelta;
            col += colDelta;
        }

        return length;
    }

    /**
     * Checks if all the ships of the player have been sunk
     *
     * @return boolean Whether all the ships of the player have been sunk
     * @pre board != null
     */
    public boolean shipsSunk() {
        boolean shipsSunk = true;
        for (int i = 0; shipsSunk && i < maxRow(); i++)
            for (int j = 0; shipsSunk && j < maxCol(); j++)
                if (board[i][j] != HIT && board[i][j] != EMPTY)
                    shipsSunk = false;
        return shipsSunk;
    }

    /**
     * This method returns the number of rows in board
     * 
     * @return int Number of rows in board
     * @pre board != null
     */
    public int maxRow() {
        return board.length;
    }

    /**
     * This method returns the number of columns in board
     * 
     * @return int Number of columns in board
     * @pre board != null
     */
    public int maxCol() {
        return board[0].length;
    }
}
