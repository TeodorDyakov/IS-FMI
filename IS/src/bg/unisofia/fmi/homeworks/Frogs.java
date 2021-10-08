package bg.unisofia.fmi.homeworks;

import java.util.*;

public class Frogs {

    static Map<String, String> prev = new HashMap<>();

    static List<String> neighbours(String state) {
        List<String> neighbours = new ArrayList<>();
        char[] frogs = state.toCharArray();
        int freeIdx = state.indexOf(" ");

        if (freeIdx >= 1 && frogs[freeIdx - 1] == '<') {
            swap(freeIdx - 1, freeIdx, frogs);
            neighbours.add(new String(frogs));
            swap(freeIdx - 1, freeIdx, frogs);
        }

        if (freeIdx >= 2 && frogs[freeIdx - 2] == '<') {
            swap(freeIdx - 2, freeIdx, frogs);
            neighbours.add(new String(frogs));
            swap(freeIdx - 2, freeIdx, frogs);
        }
        if (freeIdx < frogs.length - 1 && frogs[freeIdx + 1] == '>') {
            swap(freeIdx + 1, freeIdx, frogs);
            neighbours.add(new String(frogs));
            swap(freeIdx + 1, freeIdx, frogs);
        }

        if (freeIdx < frogs.length - 2 && frogs[freeIdx + 2] == '>') {
            swap(freeIdx + 2, freeIdx, frogs);
            neighbours.add(new String(frogs));
            swap(freeIdx + 2, freeIdx, frogs);
        }
        return neighbours;
    }

    static boolean isEndState(String state) {
        int n = state.length() / 2;
        return state.matches(String.format(">{%d} <{%d}", n, n));
    }

    static void swap(int left, int right, char[] arr) {
        char tmp = arr[left];
        arr[left] = arr[right];
        arr[right] = tmp;
    }

    static Set<String> visited = new HashSet<>();
    static Queue<String> states = new ArrayDeque<>();

    static void solve(String frogs) {
        states.add(frogs);
        visited.add(frogs);
        while (!states.isEmpty()) {

            String currState = states.poll();

            if (isEndState(currState)) {
                List<String> path = new ArrayList<>();

                while (currState != null) {
                    path.add(currState);
                    currState = prev.get(currState);
                }
                Collections.reverse(path);
                path.forEach(System.out::println);
                return;
            }

            for (String neighbour : neighbours(currState)) {
                if (!visited.contains(neighbour)) {
                    states.add(neighbour);
                    prev.put(neighbour, currState);
                    visited.add(neighbour);
                }
            }
        }
    }

    public static void main(String[] args) {
        String init = "<<< >>>";
        solve(init);
    }
}
