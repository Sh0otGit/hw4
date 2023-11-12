package omok;

/**
 * The `PlayerComputer` class represents an AI player in the Omok (Gomoku) game.
 * This AI player can make smart moves based on calculated move scores.
 */
public class PlayerComputer extends Player {

    /**
     * Creates a new AI player for the Omok game.
     *
     * @param game The game in which the AI player participates.
     * @param name The name of the AI player.
     */
    public PlayerComputer(Game game, String name) {
        super(game, name);
    }

    /**
     * Makes a move for the AI player, selecting the best move based on calculated move scores.
     */
    public void makeMove() {
        int bestMoveX = -1;
        int bestMoveY = -1;
        int maxScore = 0;

        for (int x = 0; x < this.game.getBoard().size(); x++) {
            for (int y = 0; y < this.game.getBoard().size(); y++) {
                if (this.game.getBoard().isEmpty(x, y)) {
                    int score = calculateMoveScore(x, y, this);
                    if (score > maxScore) {
                        maxScore = score;
                        bestMoveX = x;
                        bestMoveY = y;
                    }
                }
            }
        }
        if (bestMoveX != -1) {
            this.game.getBoard().placeStone(bestMoveX, bestMoveY, this);
        } else {
            this.game.getBoard().placeRandomStone(this);
        }
    }

    /**
     * Calculates a score for a potential move based on the current board state.
     *
     * @param x      The column index of the potential move.
     * @param y      The row index of the potential move.
     * @param player The player for whom the score is calculated.
     * @return The calculated score for the potential move.
     */
    private int calculateMoveScore(int x, int y, Player player) {
        int score = 0;
        if (this.game.getBoard().checkForWinningRow(5, player, x, y)) {
            score += 1000;
        }
        if (this.game.getBoard().checkForWinningRow(5, this.game.getWhitePlayer(), x, y)) {
            score += 800;
        }
        if (this.game.getBoard().checkForWinningRow(4, this.game.getWhitePlayer(), x, y)) {
            score += 500;
        }
        if (this.game.getBoard().checkForWinningRow(4, player, x, y)) {
            score += 500;
        } else if (this.game.getBoard().checkForWinningRow(3, player, x, y)) {
            score += 250;
        } else if (this.game.getBoard().checkForWinningRow(2, player, x, y)) {
            score += 100;
        }
        return score;
    }
}
