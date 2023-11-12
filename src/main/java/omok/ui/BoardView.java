package omok.ui;

import omok.Board;
import omok.Game;
import omok.util.ButtonClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * The `BoardView` class represents the visual representation of the Omok (Gomoku) game board and provides
 * user interaction capabilities.
 */
public class BoardView extends JPanel{

    private final ui ui;

    private final JFrame frame;
    private final JButton[][] cells;

    private final Game game;
    private final Board board;
    private final int size;

    private final int step = 20;
    private final int xmargin = 20;
    private final int ymargin = 20;

    public int getStep(){return this.step;}
    public int getXMargin(){return this.xmargin;}
    public int getYMargin(){return this.ymargin;}
    public int getBoardSize(){return this.size;}

    private final java.util.List<ButtonClickListener> buttonClickListeners = new ArrayList<>();

    /**
     * Constructs a new instance of the `BoardView` class for displaying the Omok game board and handling user interactions.
     *
     * @param ui The user interface object responsible for game control.
     * @param game The game object representing the Omok game.
     */
    public BoardView(ui ui, Game game){
        this.ui = ui;

        this.game = game;
        this.board = this.game.getBoard();
        this.size = this.board.size();

        this.frame = new JFrame("Omok");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setPreferredSize(new Dimension((step*(size-1)) + (2*xmargin), (step*(size-1)) + (4*ymargin) + 2*step));
        this.frame.add(this);

        cells = new JButton[this.size][this.size];

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGameMenuItem = new JMenuItem("New Game");

        newGameMenuItem.setMnemonic(KeyEvent.VK_N);
        newGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        newGameMenuItem.setToolTipText("Start a new game!");
        //ImageIcon icon = new ImageIcon(getClass().getResource("/omok/ui/resources/375.png"));
        //newGameMenuItem.setIcon(icon);

        newGameMenuItem.addActionListener(e -> {
            frame.dispose();
            new ModeSelectionScreen();
        });

        gameMenu.add(newGameMenuItem);
        menuBar.add(gameMenu);
        frame.setJMenuBar(menuBar);

        JToolBar toolbar = new JToolBar();
        JButton newGameButton = new JButton("New Game");
        newGameButton.setToolTipText("Start a new game!");
        newGameButton.addActionListener(e -> {
            frame.dispose();
            new ModeSelectionScreen();
        });

        toolbar.add(newGameButton);

        frame.getContentPane().add(toolbar, BorderLayout.NORTH);

        initializeBoard();

        this.frame.pack();
        this.frame.setVisible(true);
    }

    /**
     * Retrieves the JFrame associated with this board view.
     *
     * @return The JFrame containing the game board.
     */
    public JFrame getFrame(){return this.frame;}

    /**
     * Adds a button click listener to the board for handling user interactions.
     *
     * @param listener The button click listener to add.
     */
    public void addButtonClickListener(ButtonClickListener listener){
        buttonClickListeners.add(listener);
    }

    /**
     * Notifies all registered button click listeners of a button click event.
     *
     * @param i The 0-based column index of the clicked button.
     * @param j The 0-based row index of the clicked button.
     */
    public void notifyButtonClickListeners(int i, int j){
        for (ButtonClickListener listener : buttonClickListeners){
            listener.onButtonClick(i, j);
        }
    }

    /**
     * Initializes the game board by creating and configuring the buttons on the board for user interaction.
     */
    public void initializeBoard(){
        setLayout(null);

        int height = ((step*(size-1)) + (4*ymargin)) - 28;
        int yoffset = height - (step * (size - 1)) - ymargin;

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                cells[i][j] = new JButton();
                int buttonSize = step/2;
                cells[i][j].setPreferredSize(new Dimension(buttonSize, buttonSize));
                cells[i][j].setContentAreaFilled(true);
                cells[i][j].setBorder(BorderFactory.createEmptyBorder());
                cells[i][j].setFocusPainted(false);

                int x = (xmargin + i * step) - step/4;
                int y = (yoffset + j * step) - step/4;
                cells[i][j].setBounds(x, y, buttonSize, buttonSize);

                int finalI = i;
                int finalJ = j;
                cells[i][j].addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseEntered(MouseEvent e){
                        if(board.isEmpty(finalI, finalJ)){
                            buttonHover(cells[finalI][finalJ], true);
                            repaint();}
                    }
                    public void mouseExited(MouseEvent e){
                        buttonHover(cells[finalI][finalJ], false);
                        repaint();}
                });

                int finalJ1 = j;
                int finalI1 = i;
                cells[i][j].addActionListener(e -> {
                    if(board.isEmpty(finalI, finalJ)) {
                        board.placeStone(finalI1, finalJ1, ui.getCurrentTurn());
                        System.out.println(ui.getCurrentTurnStr() + " placed a stone at " + finalI1 + "x" + finalJ1);
                        notifyButtonClickListeners(finalI1, finalJ1);
                    }
                });
                add(cells[i][j]);
            }
        }

        repaint();
    }


    /**
     * Handles button hover effects when the mouse enters or exits a button on the game board.
     *
     * @param button The JButton being hovered over.
     * @param status `true` if the mouse is hovering over the button, `false` if not.
     */
    private void buttonHover(JButton button, boolean status){

        button.putClientProperty("hovered", status);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        int yoffset = getHeight() - (step * (size - 1)) - ymargin;

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, step));
        g.drawString((this.ui.getCurrentTurnStr() + "'s Turn"), xmargin, yoffset - 5);

        paintBoard(g);

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){

                int x = (xmargin + i * step) - step/2;
                int y = (yoffset + j * step) - step/2;
                int radius = step;

                boolean isHovered = Boolean.TRUE.equals(cells[i][j].getClientProperty("hovered"));
                if (isHovered){
                    g.setColor(Color.BLACK);
                    g.drawOval(x, y, radius, radius);
                    g.setColor(Color.RED);
                    g.fillOval(x, y, radius, radius);
                }
            }
        }
    }

    /**
     * Paints the game board, including grid lines and stones, on the board view.
     *
     * @param g The graphics context on which to paint the game board.
     */
    public void paintBoard(Graphics g){
        int yoffset = getHeight() - (step * (size - 1)) - ymargin;

        g.setColor(Color.YELLOW);
        g.fillRect(this.xmargin, yoffset, ((size-1)*step), ((size-1)*step));

        g.setColor(Color.BLACK);
        for(int i = 0; i <= (size-1); i++){
            int x = xmargin + (i * step);
            int y = yoffset + (i * step);
            g.drawLine(xmargin, y, xmargin + ((size-1) * step), y);
            g.drawLine(x, yoffset, x, yoffset + ((size-1) * step));
        }

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){

                int x = (xmargin + i * step) - step/2;
                int y = (yoffset + j * step) - step/2;
                int radius = step;

                if (this.board.isOccupied(i, j)){
                    g.setColor(Color.BLACK);
                    g.drawOval(x, y, radius, radius);
                    if (this.board.isOccupiedBy(i, j, this.game.getWhitePlayer())){
                        g.setColor(Color.WHITE);
                    } else {
                        g.setColor(Color.BLACK);
                    }
                    g.fillOval(x, y, radius, radius);
                }
            }
        }
    }
}
