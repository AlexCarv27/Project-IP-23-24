/**
 * Game class for the Project 2 - Battleship game.
 *
 * @author Ruben Nogueira 68536 - 1º LEI
 * @author Alexandre Carvalho 67950 - 1º LEI
 *
 */

public class Game {
    // constants of Game Class
    private static final int SCORE_MULTIPLIER = 100;
    private static final int SCORE_DIVISOR = 30;
    // variables of Game Class
    private Player currentPlayer;
    private Player[] players;
    private int playerCount;
    private boolean gameOver;

    /**
     * Constructor for Game.
     * Creates a new Game with the given number of players.
     *
     * @param nPlayers The number of players.
     * @pre nPlayers > 1 && nPlayers != null
     */
    public Game(int nPlayers) {
        players = new Player[nPlayers];
        gameOver = false;
        playerCount = 0;
    }

    /**
     * Returns an iterator for the non-eliminated players in the game.
     * 
     * @return PlayerList The iterator for the players.
     * @pre players != null
     */
    public PlayerList PlayerIterator() {
        Player[] aux = new Player[players.length];
        System.arraycopy(players, 0, aux, 0, players.length);
        return new PlayerList(aux);
    }

    /**
     * Returns an iterator for the players ordered by score in descending order.
     * If two players have the same score, they are ordered by name in ascending order.
     * 
     * @return OrderedPlayerList The iterator for the players ordered by
     *         score.
     * @pre players != null
     */
    public OrderedPlayerList OrderedPlayerIterator() {
        Player[] aux = new Player[players.length];
        System.arraycopy(players, 0, aux, 0, players.length);

        sortScores(aux); // sort by score
        sortNameSameScore(aux); // sort by name if same score

        return new OrderedPlayerList(aux);
    }

    /**
     * Sorts an array of Player objects in descending order based on their scores.
     * 
     * @param aux The parameter "aux" is an array of Player objects. It is used as a
     *            temporary array to store the players while sorting.
     * @pre aux != null
     */
    private void sortScores(Player[] aux) {
        for (int i = 0; i < aux.length; i++)
            for (int j = i + 1; j < aux.length; j++)
                if (aux[i].getScore() < aux[j].getScore()) {
                    Player temp = aux[i];
                    aux[i] = aux[j];
                    aux[j] = temp;
                }
    }

    /**
     * Sorts an array of Player objects if two players
     * have the same score, it sorts them based on their names in ascending order.
     *
     * @param aux The parameter "aux" is an array of Player objects. It is used as a
     *            temporary array to store the players while sorting.
     * @pre aux != null
     */
    private void sortNameSameScore(Player[] aux) {
        for (int i = 0; i < aux.length; i++)
            for (int j = i + 1; j < aux.length; j++)
                if (aux[i].getScore() == aux[j].getScore() &&
                        aux[i].getName().compareTo(aux[j].getName()) > 0) {
                    Player temp = aux[i];
                    aux[i] = aux[j];
                    aux[j] = temp;
                }
    }

    /**
     * Creates a new player and adds it to the game.
     * 
     * @param name  The name of the player.
     * @param fleet The fleet of the player.
     * @pre name != null && fleet != null && !hasPlayer(name) && players != null &&
     *      playerCount < players.length.
     */
    public void addPlayer(String name, AvailableFleet fleet) {
        players[playerCount++] = new Player(name, fleet.getBoard());
    }

    /**
     * Sets the current player to the first player in the array of players.
     * Used to start the game.
     * 
     * @pre players != null
     */
    public void setCurrentPlayer() {
        currentPlayer = players[0];
    }

    /**
     * Returns the index of the current player in the array of players.
     * 
     * @return int The index of the current player in the array of players.
     * @pre currentPlayer != null && players != null
     */
    private int getCurrentIndex() {
        int index = 0;
        while (players[index] != currentPlayer)
            index++;
        return index;
    }

    /**
     * Sets the current player to the next non-eliminated player in the players array.
     * Recursive method.
     * 
     * @pre currentPlayer != null && players != null && getCurrentIndex() != null
     */
    public void nextPlayerSwitch() {
        currentPlayer = players[(getCurrentIndex() + 1) % players.length];
        if (currentPlayer.isEliminated())
            nextPlayerSwitch(); // recursive call,
        // if the next player in the array is eliminated, calls the method again
    }

    /**
     * Checks if the player with the given name exists in the game.
     * Returns true if the player exists, false otherwise.
     * 
     * @param name The name to check.
     * @return boolean Whether the player exists.
     * @pre name != null && players != null
     */
    public boolean hasPlayer(String name) {
        int i = 0;
        while (i < players.length && !players[i].hasName(name))
            i++;
        return (i < players.length); // if i < players.length, the player exists
    }

    /**
     * Checks if a player with a given name is eliminated.
     * Returns true if the player is eliminated, false otherwise.
     *
     * @param name The name of the player whose elimination status we want to check.
     * @return boolean Whether the player is eliminated.
     * @pre name != null && hasPlayer(name) && players != null
     */
    public boolean playerEliminated(String name) {
        return getPlayer(name).isEliminated();
    }

    /**
     * Checks if the game is over.
     * Returns true if the game is over, false otherwise.
     * 
     * @return boolean Whether the game is over.
     * @pre gameOver != null
     */
    public boolean isOver() {
        return gameOver;
    }

    /**
     * Returns the name of the current player.
     * 
     * @return String The name of the current player.
     * @pre currentPlayer != null
     */
    public String getNameOfCurrent() {
        return currentPlayer.getName();
    }

    /**
     * Returns the score of the player with the given name.
     * 
     * @param name The name of the player.
     * @return int The score of the player.
     * @pre name != null && hasPlayer(name)
     */
    public int getScoreOf(String name) {
        return getPlayer(name).getScore();
    }

    /**
     * Returns the player with the given name.
     * 
     * @param name The name of the player to return.
     * @return Player The player with the given name.
     * @pre name != null && hasPlayer(name) && players != null
     */
    public Player getPlayer(String name) {
        int i = 0;
        while (i < players.length && !players[i].hasName(name))
            i++;
        return players[i];
        // players[i] is never out of bounds because of hasPlayer(name) in the pre-condition
    }

    /**
     * Returns the board of the player with the given name.
     * 
     * @param name The name of the player.
     * @return char[][] The board of the player.
     * @pre name != null && hasPlayer(name)
     */
    public char[][] getBoardOf(String name) {
        return getPlayer(name).getFleet();
    }

    /**
     * Shoots at the given position of the given player.
     * Updates the score of the current player.
     * 
     * @param row  int that represents the row of the position to shoot at.
     * @param col  int that represents the col of the position to shoot at.
     * @param name String that represents the name of the player to shoot at.
     * @pre position != null && name != null && hasPlayer(name) && players != null
     */
    public void shoot(int row, int col, String name) {
        Player otherPlayer = getPlayer(name);
        int length = otherPlayer.takeHit(row, col);
        if (length > 0) // if hit, add score
            currentPlayer.updateScore(length * SCORE_MULTIPLIER);
        else // already hit, subtract score
            currentPlayer.updateScore(length * SCORE_DIVISOR);

        if (otherPlayer.shipsSunk())
            otherPlayer.eliminatePlayer(); // eliminate player if all ships sunk

        int survivingPlayers = 0; // check number of surviving players
        for (int i = 0; i < players.length; i++)
            if (!players[i].isEliminated())
                survivingPlayers++;

        if (survivingPlayers == 1) { // if only one player is left, the game is over
            currentPlayer.updateScore(currentPlayer.getScore()); // duplicate score of last survivor (current player)
            gameOver = true;
        }

        nextPlayerSwitch(); // switch player
    }

    /**
     * Returns the last survivor of the game.
     * 
     * @return Player The last survivor of the game.
     * @pre isOver() && players != null
     */
    private Player lastSurvivor() {
        Player lastSurvivor = null;
        for (int i = 0; i < players.length; i++)
            if (!players[i].isEliminated())
                lastSurvivor = players[i];
        // lastSurvivor is never null because of isOver() in the pre-condition
        // there's always just one player left when the game is over
        return lastSurvivor;
    }

    /**
     * Returns the name of the winner of the game, (player's name with
     * the highest score)
     * If there is more than one player with the highest score, returns the name of
     * the last survivor.
     *
     * @return String The name of the winner of the game.
     * @pre isOver() && players != null && maxScorePlayer() != null &&
     *      lastSurvivor() != null
     */
    public String getWinnerName() {
        String winnerName = null;
        if (playersWithMaxScore() == 1)
            winnerName = maxScorePlayer().getName();
        else if (playersWithMaxScore() > 1)
            winnerName = lastSurvivor().getName();
        return winnerName;
    }

    /**
     * Returns the player with the highest score from the array of players.
     * 
     * @return int Highest score from the array of players.
     * @pre players != null
     */
    private Player maxScorePlayer() {
        Player maxScorePlayer = null;
        int maxScore = 0;
        for (int i = 0; i < players.length; i++)
            if (players[i].getScore() > maxScore) {
                maxScore = players[i].getScore();
                maxScorePlayer = players[i];
            }
        return maxScorePlayer;
    }

    /**
     * Returns the number of players who have the maximum score.
     * 
     * @return int count of players who have the maximum score.
     * @pre players != null && maxScorePlayer() != null
     */
    private int playersWithMaxScore() {
        int count = 0;
        for (int i = 0; i < players.length; i++)
            if (players[i].getScore() == maxScorePlayer().getScore())
                count++;
        return count;
    }

    /**
     * Checks if the given position is invalid, returns boolean.
     * Returns true if the position is invalid, false otherwise.
     * 
     * @param row  int that represents the row of the position
     * @param col  int that represents the col of the position
     * @param name of Player to shoot at
     * @return boolean whether the position is invalid
     * @pre row != null && col != null && name != null && hasPlayer(name) && players != null
     */
    public boolean isInvalidPosition(int row, int col, String name) {
        Player otherPlayer = getPlayer(name);
        return (row < 1 || col < 1 ||
                row > otherPlayer.maxRow() || col > otherPlayer.maxCol());
    }
}
