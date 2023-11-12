package omok;

/**
 * The `Game` class represents an Omok (Gomoku) game with specified settings, including game mode and board size.
 */
public class Game {

    public int stonesToWin;
    private final Player whitePlayer;
    private Player blackPlayer;
    private Player winner;
    private final Board board;
    private final String gamemode;

    /**
     * Creates a new instance of the `Game` class with the specified board size and game mode.
     *
     * @param size     The size of the game board.
     * @param gamemode The game mode, which can be "Player" or "AI."
     */
    public Game(int size, String gamemode) {
        this.stonesToWin = 5;
        this.whitePlayer = new Player(this, "White");
        this.blackPlayer = new Player(this, "Black");

        // If the game mode is "AI," set the black player to be an AI player.
        if ("AI".equals(gamemode)) {
            this.blackPlayer = new PlayerComputer(this, "Black");
        }

        this.board = new Board(this, size);
        this.gamemode = gamemode;
    }

    /**
     * Retrieves the game board associated with this game.
     *
     * @return The game board object.
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Retrieves the white player in the game.
     *
     * @return The white player object.
     */
    public Player getWhitePlayer() {
        return this.whitePlayer;
    }

    /**
     * Retrieves the black player in the game.
     *
     * @return The black player object, which can be a human or AI player.
     */
    public Player getBlackPlayer() {
        return this.blackPlayer;
    }

    /**
     * Retrieves the game mode for this game, which can be "Player" or "AI."
     *
     * @return The game mode of this game.
     */
    public String getGamemode() {
        return this.gamemode;
    }

    /**
     * Sets the winner of the game.
     *
     * @param winner The player who won the game or "N/A" for a draw.
     */
    public void setWinner(Player winner) {
        this.winner = winner;
    }

    /**
     * Retrieves the player who won the game or "N/A" for a draw.
     *
     * @return The winning player or "N/A" for a draw.
     */
    public Player getWinner() {
        return this.winner;
    }
}
