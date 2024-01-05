/**
 * PlayerList Class for the Project 2 - Battleship game.
 *
 * @author Ruben Nogueira 68536 - 1º LEI
 * @author Alexandre Carvalho 67950 - 1º LEI
 *
 */
public class PlayerList {
    // variables of PlayerList Class
    private final Player[] players;
    private int nextIndex;
    private final int sizePlayers;

    /**
     * Constructor for PlayerList
     * Creates a new PlayerList with the given array of players.
     * 
     * @param players The array of players
     */
    public PlayerList(Player[] players) {
        this.players = players;
        sizePlayers = players.length; // sizePlayers is initialized with the length of the array players
        nextIndex = 0; // nextIndex is initialized with 0
        advance(); // advance to the first non-eliminated player
    }

    /**
     * Advances the index of the array players to the next non-eliminated player
     *
     * @pre nextIndex >= 0, sizePlayers >= 0 && players != null
     */
    private void advance() {
        while (nextIndex < sizePlayers && !criterion(players[nextIndex]))
            nextIndex++;
    }

    /**
     * This method checks if there is a next player
     *
     * @return boolean Whether there is a next player
     * @pre nextIndex >= 0, sizePlayers >= 0
     */
    public boolean hasNextPlayer() {
        return (nextIndex < sizePlayers);
    }

    /**
     * This method returns the next player
     * 
     * @return Player The next player
     * @pre nextIndex >= 0 && hasNextPlayer() && players != null
     */
    public Player nextPlayer() {
        Player player = players[nextIndex++];
        advance();
        return player;
    }

    /**
     * This method checks if the player isn't eliminated
     * 
     * @param player The player to check
     * @return boolean Whether the player is not eliminated
     * @pre player != null
     */
    public boolean criterion(Player player) {
        return !player.isEliminated();
    }
}
