
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

/**
 *  NOTHING FOR YOU TO DO HERE.
 */

/**
 * An instance is a GUI that can display a Connect Four game.
 */
public class GUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private GamePanel gamePanel;
    private JLabel msgLabel;
    private JButton[] columnButtons;
    private Game game;

    /**
     * Constructor: a GUI attached to Game game.
     */
    public GUI(Game game, int NUM_COLS, int NUN_ROWS) {
        super("Connect Four AI");

        this.game = game;

        setLayout(new BorderLayout());

        gamePanel = new GamePanel(this, new Board());
        columnButtons = new JButton[NUM_COLS];

        //Message Panel
        JPanel msgPanel = new JPanel();
        msgPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        msgPanel.setPreferredSize(new Dimension(getWidth(), 18));
        msgPanel.setLayout(new GridLayout(1, 4));
        msgLabel = new JLabel("");
        msgPanel.add(msgLabel);

        //Toolbar of Column Buttons
        JToolBar toolbar = setUpToolBar();

        add(toolbar, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
        add(msgPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Set up the action buttons at the top of the board for human interaction.
     */
    private JToolBar setUpToolBar() {
        for (int i = 0; i < columnButtons.length; i++) {
            columnButtons[i] = new JButton("Column " + i);
            columnButtons[i].addActionListener(evt -> {
                JButton s = (JButton) evt.getSource();
                int j = new Integer(s.getText().replaceAll("Column ", ""));
                game.columnClicked(j);
            });
        }

        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        for (JButton j : columnButtons)
            toolBar.add(j);

        return toolBar;
    }

    /**
     * Tell the GUI that the game is over and that winner has won
     * (it's a tie if winner = null).
     */

    public void notifyGameOver(Player winner) {
        setMsg(winner == null ? "Tie game!" : winner + " won the game!!!");
    }

    /**
     * Display message s at the bottom of the GUI.
     */
    public void setMsg(String s) {
        msgLabel.setText(s);
    }

    /**
     * Update this GUI to make Move m and display Board b.
     */
    public void updateGUI(Board b, Move m) {
        gamePanel.playColumn(m.player(), m.column()); // responsible for animation
        gamePanel.updateBoard(b); // must be called after (not before) playColumn
        repaint();
    }
}