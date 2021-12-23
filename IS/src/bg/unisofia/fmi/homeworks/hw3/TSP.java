package bg.unisofia.fmi.homeworks.hw3;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TSP {

    List<City> graph;
    int populationSize = 100;
    int numberOfGenerations = 1000;
    List<Integer> checkPoints = List.of(1, 100, 200, 500, 1000);
    static double mutationProb = 0.2;
    static double elitismCutoff = 0.3;

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

    public float calculatePathLength(int[] data){
        float len = 0;
        for (int i = 1; i < data.length; i++) {
            City c1 = graph.get(data[i]);
            City c2 = graph.get(data[i - 1]);
            len += c1.dist(c2);
        }
        return len;
    }

    public List<Integer> randomPerm(int n){
        List<Integer> perm = IntStream.range(0, n).boxed().collect(Collectors.toList());
        Collections.shuffle(perm);
        return perm;
    }

    public void mutate(List<List<Integer>>population){
        Random rng = new Random();
        for(var individual : population){
            if(rng.nextFloat() < mutationProb){
                int p1 = rng.nextInt(individual.size());
                int p2 = rng.nextInt(individual.size());
                int x1 = individual.get(p1);
                int x2 = individual.get(p2);
                individual.set(p1, x2);
                individual.set(p2, x1);
            }
        }
    }

    public List<Integer> optimize() {
        List<List<Integer>> population = new ArrayList<>();
        for(int i = 0; i < populationSize; i++){
            population.add(randomPerm(graph.size()));
        }
        for(int i = 1; i <= numberOfGenerations; i++){
            sortByFitness(population);
            double bestDistance = calculatePathLength(population.get(population.size()-1));
            if(checkPoints.contains(i)){
                System.out.printf("Shortest path length in population number %d: %f\n" , i,  bestDistance);
            }
            List<List<Integer>>nextGeneration = new ArrayList<>();
            int elitismCount = (int) (elitismCutoff * population.size());
            nextGeneration.addAll(population.subList(population.size() - elitismCount, population.size()));
            List<List<Integer>>selected = rouletteSelection(population, populationSize);
            for(int j = 0; j < selected.size(); j++){
                var p1 = selected.get(j);
                var p2 = selected.get(selected.size()-1-j);
                nextGeneration.add(crossover(p1, p2));
            }
            mutate(population);
            population = nextGeneration;
        }
        return population.get(populationSize-1);
    }

    public void sortByFitness(List<List<Integer>>list){
        list.sort(Comparator.comparingDouble(this::fitness));
    }

    public static void testOnCities(){
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
        int data[] = new int[12];
        for(int i = 0; i < data.length; i++){
            data[i] = i;
        }
        TSP tsp = new TSP(cities);
        int minIdx = 0;
        float min = Float.POSITIVE_INFINITY;
        long cnt = 0;
        while(Permutation.findNextPermutation(data)){
            cnt++;
            float len = tsp.calculatePathLength(data);
            if(min > len){
                min = len;
            }
            if(cnt % 1_000_000 == 0){
                System.out.println(cnt);
            }
        }
//        var res = tsp.optimize();
        System.out.println(min);
    }


    public static void testOnRandomInstance(){
        Random rng = new Random();
        List<City>cities = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            cities.add(new City(rng.nextFloat()*100, rng.nextFloat()*100, String.valueOf(i)));
        }
        TSP tsp = new TSP(cities);
        tsp.optimize();
    }

    public static void main(String[] args) {
//        TSP.testOnRandomInstance();
        TSP.testOnCities();
    }
}