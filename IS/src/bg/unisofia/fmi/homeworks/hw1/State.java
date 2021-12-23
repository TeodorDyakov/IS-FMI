package bg.unisofia.fmi.homeworks.hw1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class State {

    public char[][] board;
    public String move;

    public State(char[][] board, String move) {
        this.board = board;
        this.move = move;
    }

    public List<State> getNeighbours() {
        List<State> neighbours = new ArrayList<>();

        int[] dy = {1, 0, -1, 0};
        int[] dx = {0, 1, 0, -1};
        String[] moves = {"left", "up", "right", "down"};

        int freeX = 0, freeY = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == '0') {
                    freeX = i;
                    freeY = j;
                }
            }
        }

        for (int k = 0; k < dx.length; k++) {
            char[][] b = new char[board.length][board[0].length];
            for (int i = 0; i < b.length; i++) {
                b[i] = board[i].clone();
            }

            int moveX = freeX + dx[k], moveY = freeY + dy[k];
            if (moveX >= 0 && moveX < board.length && moveY >= 0 && moveY < board.length) {
                b[freeX][freeY] = b[moveX][moveY];
                b[moveX][moveY] = '0';
            }
            State nextState = new State(b, moves[k]);

            neighbours.add(nextState);
        }
        return neighbours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Arrays.deepEquals(board, state.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (char[] chars : board) {
            for (int j = 0; j < board[0].length; j++) {
                res.append(chars[j]).append(" ");
            }
            res.append(System.lineSeparator());
        }
        return res.toString();
    }
}
