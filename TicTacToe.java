import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe extends JFrame implements ActionListener {
    // GUI elements
    private JLabel userInfoLabel;
    private JPanel topPanel, centerPanel, bottomPanel, rightPanel;
    private JTextArea usernameTextArea;
    private JButton[][] buttonGrid;
    private JButton startButton, replayButton, resetButton, quitButton;
    // game variables
    private String username = "";
    private boolean gameStarted = false;
    private char[][] gameBoard = new char[3][3];
    private char playerSymbol, aiSymbol;
    private boolean playerTurn;
    private int emptyCells = 9;

    public TicTacToe() {
        // set up GUI
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        this.setPreferredSize(new Dimension(500, 500));
        setLayout(new BorderLayout());

        // top panel
        userInfoLabel = new JLabel("Welcome to Tic Tac Toe!");
        topPanel = new JPanel();
        topPanel.add(userInfoLabel);
        add(topPanel, BorderLayout.NORTH);

        // center panel
        centerPanel = new JPanel(new GridLayout(3, 3));
        buttonGrid = new JButton[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttonGrid[i][j] = new JButton("");
                buttonGrid[i][j].setFont(new Font("Arial", Font.PLAIN, 50));
                buttonGrid[i][j].addActionListener(this);
                centerPanel.add(buttonGrid[i][j]);
            }
        }
        add(centerPanel, BorderLayout.CENTER);

        // bottom panel
        bottomPanel = new JPanel();
        usernameTextArea = new JTextArea(1, 10);
        usernameTextArea.setLineWrap(false);
        usernameTextArea.setText("Enter username");
        usernameTextArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                usernameTextArea.setText("");
            }
        });
        bottomPanel.add(usernameTextArea);
        add(bottomPanel, BorderLayout.SOUTH);

        // right panel
        rightPanel = new JPanel(new GridLayout(2, 1));
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        replayButton = new JButton("Re-play");
        replayButton.setEnabled(false);
        replayButton.addActionListener(this);
        rightPanel.add(startButton);
        rightPanel.add(replayButton);
        add(rightPanel, BorderLayout.EAST);

        // menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        menu.add(resetButton);
        quitButton = new JButton("Quit");
        quitButton.addActionListener(this);
        menu.add(quitButton);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // display GUI
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // handle button clicks
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == startButton) {
            // start new game
            username = usernameTextArea.getText().trim();
            if (username.equals("") || username.equals("Enter username")) {
                userInfoLabel.setText("Please enter a valid username");
            } else {
                userInfoLabel.setText("Game started: " + username + " vs AI");
                resetBoard();
                gameStarted = true;
                startButton.setEnabled(false);
                replayButton.setEnabled(true);
                playerTurn = true;
                if (Math.random() > 0.5) {
                    // AI goes first
                    aiMove();
                }
            }
        } else if (source == resetButton) {
            // reset game
            resetBoard();
            gameStarted = false;
            startButton.setEnabled(true);
            replayButton.setEnabled(false);
            usernameTextArea.setText("Enter username");
            userInfoLabel.setText("Welcome to Tic Tac Toe!");
        } else if (source == quitButton) {
            // quit game
            System.exit(0);
        } else if (gameStarted && source == replayButton) {
            // replay current game
            resetBoard();
            playerTurn = true;
            if (Math.random() > 0.5) {
                // AI goes first
                aiMove();
            }
        } else if (gameStarted && source instanceof JButton) {
            // handle button clicks in game board
            JButton clickedButton = (JButton) source;
            int row = -1, col = -1;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (clickedButton == buttonGrid[i][j]) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }
            if (row != -1 && col != -1 && gameBoard[row][col] == 0 && playerTurn) {
                // player move
                gameBoard[row][col] = playerSymbol;
                clickedButton.setText(Character.toString(playerSymbol));
                emptyCells--;
                playerTurn = false;
                if (checkWin(playerSymbol)) {
                    // player wins
                    userInfoLabel.setText(username + " wins!");
                    gameStarted = false;
                    startButton.setEnabled(true);
                    replayButton.setEnabled(false);
                } else if (emptyCells == 0) {
                    // tie game
                    userInfoLabel.setText("Tie game!");
                    gameStarted = false;
                    startButton.setEnabled(true);
                    replayButton.setEnabled(false);
                } else {
                    // AI move
                    aiMove();
                }
            }
        }
    }
    
    
    // reset game board
    private void resetBoard() {
        emptyCells = 9;
        playerSymbol = 'O';
        aiSymbol = 'X';
        playerTurn = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameBoard[i][j] = 0;
                buttonGrid[i][j].setText("");
                buttonGrid[i][j].setEnabled(true);
            }
        }
    }

    // handle AI move
    private void aiMove() {
        int row = -1, col = -1;
        while (row == -1 || col == -1) {
            int r = (int) (Math.random() * 3);
            int c = (int) (Math.random() * 3);
            if (gameBoard[r][c] == 0) {
                row = r;
                col = c;
            }
        }
        gameBoard[row][col] = aiSymbol;
        buttonGrid[row][col].setText(Character.toString(aiSymbol));
        buttonGrid[row][col].setEnabled(false);
        emptyCells--;
        playerTurn = true;
        if (checkWin(aiSymbol)) {
            // AI wins
            userInfoLabel.setText("AI wins!");
            gameStarted = false;
            startButton.setEnabled(true);
            replayButton.setEnabled(false);
        } else if (emptyCells == 0) {
            // tie game
            userInfoLabel.setText("Tie game!");
            gameStarted = false;
            startButton.setEnabled(true);
            replayButton.setEnabled(false);
        }
    }
    
    // check if symbol wins
    private boolean checkWin(char symbol) {
        // check rows
        for (int i = 0; i < 3; i++) {
            if (gameBoard[i][0] == symbol && gameBoard[i][1] == symbol && gameBoard[i][2] == symbol) {
                return true;
            }
        }
        // check columns
        for (int i = 0; i < 3; i++) {
            if (gameBoard[0][i] == symbol && gameBoard[1][i] == symbol && gameBoard[2][i] == symbol) {
                return true;
            }
        }
        // check diagonals
        if (gameBoard[0][0] == symbol && gameBoard[1][1] == symbol && gameBoard[2][2] == symbol) {
            return true;
        }
        if (gameBoard[0][2] == symbol && gameBoard[1][1] == symbol && gameBoard[2][0] == symbol) {
            return true;
        }
        return false;
    }
    
    // main method
    public static void main(String[] args) {
        new TicTacToe();
    }
}