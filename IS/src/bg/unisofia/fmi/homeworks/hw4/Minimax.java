package bg.unisofia.fmi.homeworks.hw4;

import java.util.Scanner;

public class Minimax {

    static class Move {
        int row, col;
    }

    static char ai = 'x', player = 'o';

    static void printBoard(char[][] board){
        for (char[] chars : board) {
            for (int j = 0; j < board.length; j++) {
                System.out.printf("%c ", chars[j]);
            }
            System.out.println();
        }
    }

    static boolean isMovesLeft(char[][] board) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == '_')
                    return true;
        return false;
    }

    static boolean isGameOver(char[][]b){
        return !isMovesLeft(b) || evaluate(b) != 0;
    }

    static int evaluate(char[][] b) {
        for (int row = 0; row < 3; row++) {
            if (b[row][0] == b[row][1] &&
                    b[row][1] == b[row][2]) {
                if (b[row][0] == ai)
                    return +10;
                else if (b[row][0] == player)
                    return -10;
            }
        }

        for (int col = 0; col < 3; col++) {
            if (b[0][col] == b[1][col] &&
                    b[1][col] == b[2][col]) {
                if (b[0][col] == ai)
                    return +10;

                else if (b[0][col] == player)
                    return -10;
            }
        }

        // Checking for Diagonals for X or O victory.
        if (b[0][0] == b[1][1] && b[1][1] == b[2][2]) {
            if (b[0][0] == ai)
                return +10;
            else if (b[0][0] == player)
                return -10;
        }

        if (b[0][2] == b[1][1] && b[1][1] == b[2][0]) {
            if (b[0][2] == ai)
                return +10;
            else if (b[0][2] == player)
                return -10;
        }

        return 0;
    }

    static int minimax(char[][] board,
                       int depth, Boolean isMax, int alpha, int beta) {
        int score = evaluate(board);

        if (score == 10)
            return score;

        if (score == -10)
            return score;

        if (!isMovesLeft(board))
            return 0;

        int best;
        if (isMax) {
            best = -1000;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '_') {
                        board[i][j] = ai;
                        best = Math.max(best, minimax(board,
                                depth + 1, false, alpha, beta));
                        board[i][j] = '_';
                        alpha = Math.max(alpha, best);
                        if(beta <= alpha){
                            break;
                        }
                    }
                }
            }
        }
        else {
            best = 1000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '_') {
                        board[i][j] = player;
                        best = Math.min(best, minimax(board,
                                depth + 1, true, alpha, beta));
                        board[i][j] = '_';
                        beta = Math.min(beta, best);
                        if(beta <= alpha){
                            break;
                        }
                    }
                }
            }
        }
        return best;
    }

    static Move findBestMove(char[][] board) {
        int bestVal = -1000;
        Move bestMove = new Move();
        bestMove.row = -1;
        bestMove.col = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '_') {
                    board[i][j] = ai;
                    int moveVal = minimax(board, 0, false, -10, 10);

                    board[i][j] = '_';

                    if (moveVal > bestVal) {
                        bestMove.row = i;
                        bestMove.col = j;
                        bestVal = moveVal;
                    }
                }
            }
        }

        return bestMove;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Do you want to be first?y/n:");
        String ans = in.nextLine();
        boolean isAImove = false;
        if(ans.equals("n")){
            isAImove = true;
        }else{
            player = 'x';
            ai = 'o';
        }
        char[][] board = {{'_', '_', '_'},
                {'_', '_', '_'},
                {'_', '_', '_'}};

        while (!isGameOver(board)) {
            printBoard(board);
            System.out.println();
            if(isAImove){
                Move bestMove = findBestMove(board);
                board[bestMove.row][bestMove.col] = ai;
                isAImove = false;
            }else{
                System.out.println("Enter coordinates of move");
                int row = in.nextInt();
                int col = in.nextInt();
                board[row][col] = player;
                isAImove = true;
            }
        }
        printBoard(board);
    }
}