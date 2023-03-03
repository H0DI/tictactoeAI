import java.util.Scanner;

class TicTacToe {

    // The board and its size
    private char[][] board;
    private int boardSize = 3;

    // Symbols for human and computer players
    private char humanSymbol = 'X';
    private char computerSymbol = 'O';

    // Scanner for user input
    private Scanner scanner;

    public TicTacToe() {
        // Initialize the board as empty
        board = new char[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = ' ';
            }
        }

        // Initialize the scanner
        scanner = new Scanner(System.in);
    }

    // Print the board to the console
    public void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < boardSize; i++) {
            System.out.print("| ");
            for (int j = 0; j < boardSize; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println();
            System.out.println("-------------");
        }
    }

    // Check if the board is full
    public boolean isBoardFull() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    // Check if there is a winner
    public boolean hasWinner() {
        return checkRows() || checkColumns() || checkDiagonals();
    }

    // Check if any row has a winner
    public boolean checkRows() {
        for (int i = 0; i < boardSize; i++) {
            if (board[i][0] != ' ' &&
                    board[i][0] == board[i][1] &&
                    board[i][1] == board[i][2]) {
                return true;
            }
        }
        return false;
    }

    // Check if any column has a winner
    public boolean checkColumns() {
        for (int j = 0; j < boardSize; j++) {
            if (board[0][j] != ' ' &&
                    board[0][j] == board[1][j] &&
                    board[1][j] == board[2][j]) {
                return true;
            }
        }
        return false;
    }

    // Check if any diagonal has a winner
    public boolean checkDiagonals() {
        return (board[0][0] != ' ' &&
                board[0][0] == board[1][1] &&
                board[1][1] == board[2][2]) ||
                (board[0][2] != ' ' &&
                        board[0][2] == board[1][1] &&
                        board[1][1] == board[2][0]);
    }

    // Get the optimal computer move using the minimax algorithm
    public void getOptimalComputerMove() {
        int bestScore = Integer.MIN_VALUE;
        int row = -1, col = -1;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = computerSymbol;
                    int score = minimax(0, false);
                    board[i][j] = ' ';

                    if (score > bestScore) {
                        bestScore = score;
                        row = i;
                        col = j;
                    }
                }
            }
        }

        // Make the move
        board[row][col] = computerSymbol;
    }

    // The minimax algorithm
    private int minimax(int depth, boolean isMaximizingPlayer) {
        int score = evaluate();

        // Base cases
        if (hasWinner() && isMaximizingPlayer) {
            return score - depth;
        } else if (hasWinner() && !isMaximizingPlayer) {
            return score + depth;
        } else if (isBoardFull()) {
            return 0;
        }

        // Recursive cases
        if (isMaximizingPlayer) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = computerSymbol;
                        int currentScore = minimax(depth + 1, false);
                        board[i][j] = ' ';
                        bestScore = Math.max(currentScore, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = humanSymbol;
                        int currentScore = minimax(depth + 1, true);
                        board[i][j] = ' ';
                        bestScore = Math.min(currentScore, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    // Evaluation function
    private int evaluate() {
        if (checkRows() && computerSymbol == 'O') {
            return 1;
        } else if (checkColumns() && computerSymbol == 'O') {
            return 1;
        } else if (checkDiagonals() && computerSymbol == 'O') {
            return 1;
        } else if (checkRows() && humanSymbol == 'X') {
            return -1;
        } else if (checkColumns() && humanSymbol == 'X') {
            return -1;
        } else if (checkDiagonals() && humanSymbol == 'X') {
            return -1;
        } else {
            return 0;
        }
    }


    public static void main(String[] args) {

        TicTacToe game = new TicTacToe();

        System.out.println("Welcome to Tic Tac Toe!");
        System.out.println("You are playing as X and the computer is playing as O.");

        // Loop until the game is over
        while (true) {
            // Human player's turn
            game.printBoard();
            System.out.println("Enter row and column with a space between them (1-3):");
            int row = game.scanner.nextInt() - 1;
            int col = game.scanner.nextInt() - 1;

            // Check if the move is valid
            if (row < 0 || row >= game.boardSize ||
                    col < 0 || col >= game.boardSize ||
                    game.board[row][col] != ' ') {
                System.out.println("Invalid move. Try again.");
                continue;
            }

            game.board[row][col] = game.humanSymbol;

            // Check if the game is over
            if (game.hasWinner()) {
                game.printBoard();
                System.out.println("You win!");
                break;
            } else if (game.isBoardFull()) {
                game.printBoard();
                System.out.println("It's a tie!");
                break;
            }

            // Computer player's turn
            game.getOptimalComputerMove();

            // Check if the game is over
            if (game.hasWinner()) {
                game.printBoard();
                System.out.println("You lose!");
                break;
            } else if (game.isBoardFull()) {
                game.printBoard();
                System.out.println("It's a tie!");
                break;
            }
        }
    }
}