/**
 * OrderedPlayerList Class for the Project 2 - Battleship game.
 *
 * @author Ruben Nogueira 68536 - 1º LEI
 * @author Alexandre Carvalho 67950 - 1º LEI
 *
 */

public class OrderedPlayerList {
    // variables of OrderedPlayerList Class
    private int nextIndex;
    private final Player[] players;
    private final int sizePlayers;

    /**
     * Constructor for OrderedPlayerList
     * Creates a new OrderedPlayerList with the given array of players.
     * 
     * @param players The array of players
     * @pre players != null
     */
    public OrderedPlayerList(Player[] players) {
        this.players = players;
        sizePlayers = players.length; // sizePlayers is initialized with the length of the array players
        nextIndex = 0; // nextIndex is initialized with 0
    }

    /**
     * Checks if there is a next player
     * 
     * @return boolean Whether there is a next player
     * @pre sizePlayers >= 0 && nextIndex >= 0 && nextIndex != null && players != null
     */
    public boolean hasNextPlayer() {
        return nextIndex < sizePlayers;
    }

    /**
     * This method returns the next player
     * 
     * @return Player The next player
     * @pre nextIndex >= 0 && hasNextPlayer() && players != null
     */
    public Player nextPlayer() {
        return players[nextIndex++];
    }
}
