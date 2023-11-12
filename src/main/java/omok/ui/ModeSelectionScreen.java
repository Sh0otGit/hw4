package omok.ui;

import omok.Game;

import javax.swing.*;
import java.awt.*;

/**
 * The `ModeSelectionScreen` class represents the main menu of the Omok (Gomoku) game,
 * where players can select the game mode (Player vs. Player or Player vs. AI).
 */
public class ModeSelectionScreen extends JFrame {

    /**
     * Constructs a new instance of the `ModeSelectionScreen` class and initializes the user interface.
     * Players can choose between Player Mode or AI Mode to start the game.
     */
    public ModeSelectionScreen(){

        setTitle("Welcome to Omok! (5-In-A-Row!)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);

        JLabel instructionsLabel = new JLabel("<html><center>Select a game mode:<br><br>"
                + "Player Mode: Play against another player locally.<br>"
                + "AI Mode: Play against an AI opponent.</center></html>");
        instructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton playerButton = new JButton("Player");
        JButton aiButton = new JButton("AI");

        playerButton.addActionListener(e -> startGame("Player"));
        aiButton.addActionListener(e -> startGame("AI"));
        JPanel panel = new JPanel();
        panel.add(playerButton);
        panel.add(aiButton);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(instructionsLabel, BorderLayout.NORTH);
        contentPane.add(panel, BorderLayout.CENTER);
        add(panel);

        setVisible(true);
    }

    /**
     * Starts the Omok game with the specified game mode and disposes of the mode selection screen.
     *
     * @param gamemode The selected game mode ("Player" or "AI").
     */
    private void startGame(String gamemode){
        this.dispose();
        Game game = new Game(20, gamemode);
        new ui(game).start();
    }
}
