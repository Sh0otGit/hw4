package omok.ui;

import omok.Board;
import omok.Game;
import omok.Player;
import omok.PlayerComputer;

/**
 * The ui class represents the user interface for the Omok (Gomoku) game.
 * It provides methods for managing the game's user interface and handling player turns.
 */
public class ui {

    private final Game game;
    private final Board board;

    private Player current;

    /**
     * Creates a new instance of the `ui` class.
     *
     * @param game The game object that represents the Omok game.
     */
    public ui(Game game){
        this.game = game;
        this.board = game.getBoard();
    }

    /**
     * Initializes the game interface and starts the game.
     */
    public void start(){

        // Initialize the game board view
        BoardView boardview = new BoardView(this, this.game);

        // Set the current player to the white player
        this.current = game.getWhitePlayer();

        // Add a button click listener to the game board
        boardview.addButtonClickListener((i, j) -> handleButtonClick(boardview, i, j));
    }

    /**
     * Handles a button click event on the game board.
     *
     * @param boardview The board view where the click event occurred.
     * @param i The column index of the clicked button.
     * @param j The row index of the clicked button.
     */
    private void onButtonClick(BoardView boardview, int i, int j) {
        // Check for game over conditions
        if (board.isWonBy(current)){
            game.setWinner(current);
            boardview.getFrame().dispose();
            new GameEndScreen(game, boardview, current.name());
        }
        if (board.isFull()){
            game.setWinner(new Player(game, "N/A"));
            boardview.getFrame().dispose();
            new GameEndScreen(game, boardview, "draw");
        }

        // Switch to the next player's turn
        if (current == game.getWhitePlayer()){
            current = game.getBlackPlayer();

            // If the current player is the AI (black player), make the AI move
            if(game.getGamemode().equals("AI")){
                PlayerComputer aiplayer = (PlayerComputer) current;
                aiplayer.makeMove();
                handleButtonClick(boardview, i, j);
            }
        } else {
            current = game.getWhitePlayer();
        }
    }

    /**
     * Handles a button click on the game board. This method delegates to the `onButtonClick` method.
     *
     * @param boardView The board view where the click event occurred.
     * @param i The column index of the clicked button.
     * @param j The row index of the clicked button.
     */
    private void handleButtonClick(BoardView boardView, int i, int j){
        onButtonClick(boardView, i, j);
    }

    /**
     * Gets the player whose turn it is currently.
     *
     * @return The current player.
     */
    public Player getCurrentTurn(){
        return current;
    }

    /**
     * Gets a string representation of the current player's color (White or Black).
     *
     * @return A string indicating the current player's color.
     */
    public String getCurrentTurnStr(){
        if(this.current == this.game.getWhitePlayer()){
            return "White";
        } else {
            return "Black";
        }
    }
}
