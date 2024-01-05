/**
 * Main class for the Project 2 - Battleship game.
 *
 * @author Ruben Nogueira 68536 - 1º LEI
 * @author Alexandre Carvalho 67950 - 1º LEI
 *
 */

// imports
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    // constants of Main Class
    private static final String NOTEXISTENT_MESSAGE = "Nonexistent player";
    private static final String WINNER_MESSAGE = "%s won the game!\n";
    private static final String INVALID_COMMAND_MESSAGE = "Invalid command";
    private static final String NOT_OVER_MESSAGE = "The game was not over yet...";
    private static final String GAMEOVER_MESSAGE = "The game is over";
    private static final String INVALIDSHOT_MESSAGE = "Invalid shot";
    private static final String SELF_SHOT_MESSAGE = "Self-inflicted shot";
    private static final String ELIMINATED_SHOT_MESSAGE = "Eliminated player";
    private static final String NEXT_MESSAGE = "Next player: %s\n";
    private static final String SCORE_MESSAGE = "%s has %d points\n";
    private static final String PLAYER_COMMAND = "player";
    private static final String PLAYERS_COMMAND = "players";
    private static final String SHOOT_COMMAND = "shoot";
    private static final String FLEET_COMMAND = "fleet";
    private static final String SCORE_COMMAND = "score";
    private static final String SCORES_COMMAND = "scores";
    private static final String QUIT_COMMAND = "quit";
    private static final String FILENAME_FLEETS = "fleets.txt";
    private static final int MAX_FLEETS = 10;
    // variables of Main Class
    private static Game game;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner inFleets = new Scanner(new FileReader(FILENAME_FLEETS));
        AvailableFleet[] fleets = getAvailableFleets(inFleets);
        inFleets.close();

        Scanner inUser = new Scanner(System.in);
        game = createGame(inUser, fleets);

        String command;
        do {
            command = inUser.next();
            runCommand(command, inUser);
        } while (!command.equals(QUIT_COMMAND));
        inUser.close();
    }

    /**
     * Creates the game, reading the number of players, their names and the fleet they choose.
     *
     * @param inUser the scanner from which the user input is read
     * @param fleets the array of available fleets
     * @return Game the game created
     * @pre inUser != null && fleets != null
     */
    private static Game createGame(Scanner inUser, AvailableFleet[] fleets) {
        int nPlayers = inUser.nextInt();
        inUser.nextLine();
        game = new Game(nPlayers); // creates the game with the number of players
        for (int i = 0; i < nPlayers; i++) {
            String name = inUser.nextLine().trim();
            int index = inUser.nextInt() - 1; // -1 because the index of the fleets array starts at 0
            inUser.nextLine();
            game.addPlayer(name, fleets[index]); // adds the player to the game
        }
        game.setCurrentPlayer(); // sets the current player to the first player
        return game;
    }

    /**
     * Reads the available fleets from the file "fleets.txt".
     *
     * @param in the scanner from which the fleets are read
     * @return AvailableFleet[] the array of available fleets
     * @pre in != null
     */
    private static AvailableFleet[] getAvailableFleets(Scanner in) {
        // in order to optimize the memory usage, the array of fleets is initialized with a size of 10
        // instead of creating a new array every time a new fleet is read
        AvailableFleet[] fleets = new AvailableFleet[MAX_FLEETS];
        int fleetCount = 0;
        while (in.hasNextLine() && in.hasNextInt()) {
            char[][] board = new char[in.nextInt()][in.nextInt()]; // reads the dimensions of the board
            for (int i = 0; i < board.length; i++)
                System.arraycopy(in.next().toCharArray(), 0, board[i], 0, board[i].length);
            AvailableFleet fleet = new AvailableFleet(board);

            // adds the fleet to the array of fleets
            if (fleetCount < fleets.length)
                fleets[fleetCount++] = fleet;
            else { // if the array is full, creates a new array with double the size
                AvailableFleet[] aux = new AvailableFleet[fleets.length * 2];
                System.arraycopy(fleets, 0, aux, 0, fleets.length);
                aux[fleetCount++] = fleet;
                fleets = aux;
            }
        }
        return fleets;
    }

    /**
     * Runs the given command.
     * If the command is invalid, prints "Invalid command".
     *
     * @param command String with the command to run
     * @param in      the scanner from which the user input is read
     * @pre command != null && in != null
     */
    private static void runCommand(String command, Scanner in) {
        switch (command) {
            case PLAYER_COMMAND -> playerCommand();
            case PLAYERS_COMMAND -> playersCommand();
            case SHOOT_COMMAND -> shootCommand(in);
            case FLEET_COMMAND -> fleetCommand(in);
            case SCORE_COMMAND -> scoreCommand(in);
            case SCORES_COMMAND -> scoresCommand();
            case QUIT_COMMAND -> quitCommand();
            default -> {
                System.out.println(INVALID_COMMAND_MESSAGE);
                in.nextLine();
            }
        }
    }

    /**
     * Runs the player command.
     * If the game is over, prints "The game is over".
     * Otherwise, prints "Next player: " followed by the name of the current player.
     *
     * @pre game != null
     */
    private static void playerCommand() {
        if (game.isOver())
            System.out.println(GAMEOVER_MESSAGE);
        else
            System.out.printf(NEXT_MESSAGE, game.getNameOfCurrent());
    }

    /**
     * Runs the players command.
     * Prints the name of each player in the game.
     * Does not print eliminated players.
     *
     * @pre game != null
     */
    private static void playersCommand() {
        PlayerList players = game.PlayerIterator();
        while (players.hasNextPlayer())
            System.out.println(players.nextPlayer().getName());
    }

    /**
     * Runs the fleet command.
     * Prints the fleet of the player with the given name.
     * If the player does not exist, prints "Nonexistent player".
     *
     * @param in the scanner from which the player name is read
     * @pre game != null && in != null
     */
    private static void fleetCommand(Scanner in) {
        String name = in.nextLine().trim(); // reads the name of the player
        if (game.hasPlayer(name)) {
            char[][] board = game.getBoardOf(name);
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++)
                    System.out.print(board[i][j]);
                System.out.println();
            }
        } else // if the player does not exist, prints "Nonexistent player"
            System.out.println(NOTEXISTENT_MESSAGE);
    }

    /**
     * Runs the score command.
     * Prints the score of the player with the given name.
     * If the player does not exist, prints "Nonexistent player".
     *
     * @param in the scanner from which the player name is read
     * @pre game != null && in != null
     */
    private static void scoreCommand(Scanner in) {
        String name = in.nextLine().trim();
        if (game.hasPlayer(name))
            System.out.printf(SCORE_MESSAGE, name, game.getScoreOf(name));
        else
            System.out.println(NOTEXISTENT_MESSAGE);
    }

    /**
     * Runs the scores command.
     * Prints the name and score of each player in the game.
     * By default, the players are ordered by score, from highest to lowest.
     *
     * @pre game != null
     */
    private static void scoresCommand() {
        OrderedPlayerList players = game.OrderedPlayerIterator();
        while (players.hasNextPlayer()) {
            Player player = players.nextPlayer();
            System.out.printf(SCORE_MESSAGE, player.getName(), player.getScore());
        }
    }

    /**
     * Runs the shoot command.
     * If the game is over, prints "The game is over".
     * If the player does not exist, prints "Nonexistent player".
     * If the player has been eliminated, prints "Eliminated player".
     * If the player shoots at himself, prints "Self-inflicted shot".
     * If the position is invalid, prints "Invalid shot".
     * Otherwise, shoots at the given position.
     *
     * @param in the scanner from which the position and player name are read
     * @pre game != null && in != null
     */
    private static void shootCommand(Scanner in) {
        int row = in.nextInt(); // reads the row of the position
        int col = in.nextInt(); // reads the column of the position
        String name = in.nextLine().trim(); // reads the name of the player

        if (!game.isOver())
            if (game.hasPlayer(name))
                if (game.playerEliminated(name))
                    System.out.println(ELIMINATED_SHOT_MESSAGE);
                else if (game.getNameOfCurrent().equals(name))
                    System.out.println(SELF_SHOT_MESSAGE);
                else if (game.isInvalidPosition(row, col, name))
                    System.out.println(INVALIDSHOT_MESSAGE);
                else
                    game.shoot(row, col, name);
            else
                System.out.println(NOTEXISTENT_MESSAGE);
        else
            System.out.println(GAMEOVER_MESSAGE);
    }

    /**
     * Runs the quit command.
     * If the game is over, prints "The game is over".
     * Otherwise, prints the name of the winner.
     *
     * @pre game != null
     */
    private static void quitCommand() {
        if (game.isOver())
            System.out.printf(WINNER_MESSAGE, game.getWinnerName());
        else
            System.out.println(NOT_OVER_MESSAGE);
    }
}