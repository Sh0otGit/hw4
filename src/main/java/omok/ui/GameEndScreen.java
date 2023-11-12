package omok.ui;

import omok.Game;

import javax.swing.*;
import java.awt.*;

/**
 * The `GameEndScreen` class represents the end screen of the Omok (Gomoku) game, displayed when the game is over.
 * It displays the winner's name or a draw message and provides an option to start a new game.
 */
public class GameEndScreen extends JPanel{

    private final JFrame frame;
    private final BoardView boardView;

    /**
     * Constructs a new instance of the `GameEndScreen` class, which displays the end screen of the game.
     *
     * @param game The game object representing the Omok game.
     * @param boardView The board view to display the game board.
     * @param winner The name of the winner or "draw" if the game ended in a draw.
     */
    public GameEndScreen(Game game, BoardView boardView, String winner){
        this.boardView = boardView;
        this.frame = new JFrame("Game Over");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setPreferredSize(new Dimension((boardView.getStep()*(boardView.getBoardSize()-1)) + (2*boardView.getXMargin()), (boardView.getStep()*(boardView.getBoardSize()-1)) + (4*boardView.getYMargin()) + 2*boardView.getStep()));
        this.frame.add(this);

        JPanel bottomPanel = new JPanel();
        JLabel winnerLabel = new JLabel(winner + " won!");
        if (game.getWinner().name().equals("draw")){
            winnerLabel = new JLabel("The game ended in a draw!");
        }
        JButton newGameButton = new JButton("New Game");

        newGameButton.addActionListener(e -> {
            frame.dispose();
            new ModeSelectionScreen();
        });
        bottomPanel.add(winnerLabel);
        bottomPanel.add(newGameButton);
        this.setLayout(new BorderLayout());
        this.add(bottomPanel, BorderLayout.SOUTH);

        this.frame.pack();
        this.frame.setVisible(true);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        boardView.paintBoard(g);
    }
}