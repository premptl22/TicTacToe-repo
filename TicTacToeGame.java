import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TicTacToeGame extends Frame {
    private Button[][] buttons;
    private char currentPlayer;
    private char[][] board;
    private Font buttonFont;
    private TextArea xStepsArea, oStepsArea, resultArea;
    private ArrayList<String> xSteps, oSteps;
    private int xWins, oWins, totalGames;

    public TicTacToeGame() {
        buttons = new Button[3][3];
        board = new char[3][3];
        xSteps = new ArrayList<>();
        oSteps = new ArrayList<>();
        currentPlayer = 'X'; // Player X starts

        // Initialize win counters
        xWins = 0;
        oWins = 0;
        totalGames = 0;

        // Set font for buttons (smaller font for smaller squares)
        buttonFont = new Font("Arial", Font.BOLD, 30); // Reduced font size for smaller squares

        // Frame settings
        setTitle("Tic-Tac-Toe Game");
        setSize(600, 600);
        setLayout(new BorderLayout());

        // Title panel at the top
        Panel titlePanel = new Panel();
        titlePanel.setLayout(new FlowLayout());
        Label titleLabel = new Label("Tic-Tac-Toe Game", Label.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Central panel for the game grid (between upper and lower parts)
        Panel gridPanel = new Panel();
        gridPanel.setLayout(new GridLayout(3, 3, 5, 5)); // 5px gap between buttons

        // Initialize buttons and board with smaller buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new Button("");
                buttons[i][j].setFont(buttonFont);
                buttons[i][j].setBackground(new Color(255, 255, 255));
                buttons[i][j].setForeground(new Color(0, 0, 0));
                buttons[i][j].addActionListener(new ButtonListener(i, j));
                buttons[i][j].setPreferredSize(new Dimension(70, 70)); // Even smaller square size
                gridPanel.add(buttons[i][j]);
                board[i][j] = ' '; // Empty cell
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        // Left panel for displaying X's steps
        Panel leftPanel = new Panel();
        leftPanel.setLayout(new BorderLayout());
        Label xLabel = new Label("X's Moves", Label.CENTER);
        xLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(xLabel, BorderLayout.NORTH);

        xStepsArea = new TextArea();
        xStepsArea.setEditable(false);
        leftPanel.add(xStepsArea, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        // Right panel for displaying O's steps
        Panel rightPanel = new Panel();
        rightPanel.setLayout(new BorderLayout());
        Label oLabel = new Label("O's Moves", Label.CENTER);
        oLabel.setFont(new Font("Arial", Font.BOLD, 16));
        rightPanel.add(oLabel, BorderLayout.NORTH);

        oStepsArea = new TextArea();
        oStepsArea.setEditable(false);
        rightPanel.add(oStepsArea, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Bottom panel for displaying the result
        Panel bottomPanel = new Panel();
        resultArea = new TextArea();
        resultArea.setEditable(false);
        bottomPanel.add(resultArea);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private class ButtonListener implements ActionListener {
        private int row, col;

        public ButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (board[row][col] == ' ') {
                board[row][col] = currentPlayer;
                buttons[row][col].setLabel(String.valueOf(currentPlayer));
                buttons[row][col]
                        .setBackground(currentPlayer == 'X' ? new Color(255, 200, 200) : new Color(200, 255, 200));

                // Update the steps areas
                String move = "Row: " + (row + 1) + ", Col: " + (col + 1);
                if (currentPlayer == 'X') {
                    xSteps.add(move);
                    xStepsArea.setText(String.join("\n", xSteps));
                } else {
                    oSteps.add(move);
                    oStepsArea.setText(String.join("\n", oSteps));
                }

                if (checkWin()) {
                    resultArea.setText("Player " + currentPlayer + " wins!");
                    updateStats(currentPlayer);
                    showEndGameDialog();
                } else if (checkDraw()) {
                    resultArea.setText("It's a draw!");
                    showEndGameDialog();
                }

                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X'; // Switch player
            }
        }
    }

    private boolean checkWin() {
        // Check rows, columns, and diagonals for a win
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer)
                return true;
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer)
                return true;
        }
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer)
            return true;
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer)
            return true;
        return false;
    }

    private boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateStats(char winner) {
        totalGames++;
        if (winner == 'X') {
            xWins++;
        } else if (winner == 'O') {
            oWins++;
        }
    }

    private void resetBoard() {
        // Reset the board after each round
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
                buttons[i][j].setLabel("");
                buttons[i][j].setBackground(new Color(255, 255, 255));
            }
        }
        xSteps.clear();
        oSteps.clear();
        xStepsArea.setText("");
        oStepsArea.setText("");
        resultArea.setText("");
        currentPlayer = 'X'; // Reset to Player X
    }

    private void showEndGameDialog() {
        Dialog dialog = new Dialog(this, "Game Over", true);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(resultArea.getText()));

        Button nextGameButton = new Button("Next Game");
        nextGameButton.setBackground(new Color(100, 255, 100));
        nextGameButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextGameButton.addActionListener(e -> {
            dialog.setVisible(false);
            resetBoard();
        });

        Button exitButton = new Button("Exit");
        exitButton.setBackground(new Color(255, 100, 100));
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.addActionListener(e -> {
            dialog.setVisible(false);
            showWinningRatioDialog();
        });

        dialog.add(nextGameButton);
        dialog.add(exitButton);

        dialog.setSize(250, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showWinningRatioDialog() {
        Dialog dialog = new Dialog(this, "Winning Ratio", true);
        dialog.setLayout(new GridLayout(4, 1)); // Grid layout with 4 rows for the stats

        // Calculate the win ratio
        double xWinRatio = (totalGames == 0) ? 0 : (double) xWins / totalGames * 100;
        double oWinRatio = (totalGames == 0) ? 0 : (double) oWins / totalGames * 100;

        // Display the stats in separate lines
        dialog.add(new Label("Total Matches Played: " + totalGames));
        dialog.add(new Label("Player X Wins: " + xWins));
        dialog.add(new Label("Player O Wins: " + oWins));

        Button okButton = new Button("OK");
        okButton.addActionListener(e -> System.exit(0));
        dialog.add(okButton);

        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new TicTacToeGame();
    }
}
