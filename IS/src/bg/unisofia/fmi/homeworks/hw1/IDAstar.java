package bg.unisofia.fmi.homeworks.hw1;

import java.util.*;

public class IDAstar {

    public static int pos = -1;
    public static int FOUND = -1;

    public static boolean isEndState(State state) {
        return manhattanDist(state) == 0;
    }

    static int manhattanDist(State state) {
        int dist = 0;
        char[][] board = state.board;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int goal = 0;

                if(board[i][j] == '0'){
                    goal = pos - 1;
                    if(pos == -1){
                        goal = 8;
                    }
                }else{
                    int num = board[i][j] - '0';
                    if(num < pos || pos == -1){
                        goal = num - 1;
                    }else{
                        goal = num;
                    }
                }

                int goalY = goal / board.length;
                int goalX = goal % board[0].length;
                dist += Math.abs(goalX - j) + Math.abs(goalY - i);
            }
        }
        return dist;
    }

    public static Stack<State> idaStar(State root){
        int bound = manhattanDist(root);
        Stack<State>path = new Stack<>();
        path.push(root);

        while(true){
            int t = search(path, 0, bound);
            if(t == FOUND){
                return path;
            }
            if(t == Integer.MAX_VALUE){
                return null;
            }
            bound = t;
        }
    }

    public static int search(Stack<State>path, int g, int bound){
        State node = path.peek();
        int f = g + manhattanDist(node);
        if(f > bound){
            return f;
        }
        if(isEndState(node)){
            return FOUND;
        }
        int min = Integer.MAX_VALUE;
        for(State succ : node.getNeighbours()) {
            if (!path.contains(succ)) {
                path.push(succ);
                int t = search(path, g + 1, bound);
                if(t == FOUND){
                    return FOUND;
                }
                if(t < min){
                    min = t;
                }
                path.pop();
            }
        }
        return min;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int side = (int)Math.sqrt(n+1);
        pos = scanner.nextInt();

        char[][] board = new char[side][side];
        for(int i = 0; i < side; i++){
            for(int j = 0; j < side; j++){
                board[i][j] = scanner.next().charAt(0);
            }
        }
        State state = new State(board, null);
        long tic = System.currentTimeMillis();
        Stack<State> solution = idaStar(state);
        System.out.printf("Time to find a solution : %d ms\n", System.currentTimeMillis() - tic);
        System.out.println(solution.size() - 1);

        for(State s : solution){
            if(s.move == null){
                continue;
            }
            System.out.println(s.move);
        }

    }
}
