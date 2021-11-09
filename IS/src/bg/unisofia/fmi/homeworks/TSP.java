package bg.unisofia.fmi.homeworks;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TSP {

    List<City> graph;

    public TSP(List<City> graph) {
        this.graph = graph;
    }

    public List<Integer> crossover(List<Integer> p1, List<Integer> p2) {
        Random rng = new Random();
        int start = rng.nextInt(p1.size());
        int end = rng.nextInt(p1.size() - start) + start;
        List<Integer> p1segment = p1.subList(start, end);

        List<Integer> child = new ArrayList<>();
        for (int i = 0; i < p1.size(); i++) {
            child.add(0);
        }
        int idx = 0;
        for (int i = start; i < end; i++) {
            child.set(i, p1.get(i));
        }

        List<Integer> p2Part = new ArrayList<>(p2);
        p2Part.removeAll(p1segment);

        for (int i = 0; i < p1.size(); i++) {
            if (i == start) {
                i = end;
            }
            child.set(i, p2Part.get(idx++));
        }
        return child;
    }

    public List<List<Integer>> rouletteSelection(List<List<Integer>> pool, int newSelectionSize) {
        float[] arr = new float[pool.size()];
        float sum = pool.stream().map(this::fitness).reduce(0f, Float::sum);
        arr[0] = fitness(pool.get(0)) / sum;

        for (int i = 1; i < arr.length; i++) {
            arr[i] = arr[i - 1] + fitness(pool.get(i)) / sum;
        }
        Random rng = new Random();
        List<List<Integer>> newSelection = new ArrayList<>();

        for (int i = 0; i < newSelectionSize; i++) {
            float rn = rng.nextFloat();
            for (int j = 0; j < arr.length; j++) {
                if (arr[j] > rn) {
                    newSelection.add(pool.get(j));
                    break;
                }
            }
        }
        return newSelection;
    }

    float fitness(List<Integer> path) {
        return 1f / calculatePathLength(path);
    }

    public float calculatePathLength(List<Integer> path) {
        float len = 0;
        for (int i = 1; i < path.size(); i++) {
            City c1 = graph.get(path.get(i));
            City c2 = graph.get(path.get(i - 1));
            len += c1.dist(c2);
        }
        return len;
    }

    public List<Integer> randomPerm(int n){
        List<Integer> perm = IntStream.range(0, n).boxed().collect(Collectors.toList());
        Collections.shuffle(perm);
        return perm;
    }

    static int populationSize = 100;
    static int numberOfGenerations = 1000;

    public List<Integer> optimize() {
        List<List<Integer>> population = new ArrayList<>();
        for(int i = 0; i < populationSize; i++){
            population.add(randomPerm(graph.size()));
        }
        for(int i = 0; i < numberOfGenerations; i++){
            List<List<Integer>>selected = rouletteSelection(population, populationSize);
            List<List<Integer>>nextGeneration = new ArrayList<>();

            for(List<Integer> s1 : selected){
                for(List<Integer> s2 : selected){
                    List<Integer> child = crossover(s1, s2);
                    nextGeneration.add(child);
                }
            }
            sortByFitness(nextGeneration);
            population = nextGeneration.subList(nextGeneration.size() - populationSize, nextGeneration.size());
        }
        return population.get(populationSize-1);
    }

    public void sortByFitness(List<List<Integer>>list){
        list.sort(Comparator.comparingDouble(this::fitness));
    }

    public static void main(String[] args) {
        List<City> cities = new ArrayList<>();
        cities.add(new City(0.190032E-03, -0.285946E-03, "Aberystwyth"));
        cities.add(new City(383.458, -0.608756E-03, "Brighton"));
        cities.add(new City(-27.0206, -282.758, "Edinburgh"));
        cities.add(new City(335.751, -269.577, "Exeter"));
        cities.add(new City(69.4331, -246.780, "Glasgow"));
        cities.add(new City(168.521, 31.4012, "Inverness"));
        cities.add(new City(320.350, -160.900, "Liverpool"));
        cities.add(new City(179.933, -318.031, "London"));
        cities.add(new City(492.671, -131.563, "Newcastle"));
        cities.add(new City(112.198, -110.561, "Nottingham"));
        cities.add(new City(306.320, -108.090, "Oxford"));
        cities.add(new City(217.343, -447.089, "Stratford"));

        TSP tsp = new TSP(cities);
        var res = tsp.optimize();
        System.out.println(tsp.calculatePathLength(res));
    }
}