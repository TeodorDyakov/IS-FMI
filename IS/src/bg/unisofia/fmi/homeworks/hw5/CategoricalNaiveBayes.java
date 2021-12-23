package bg.unisofia.fmi.homeworks.hw5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoricalNaiveBayes {

    List<CategoricalVector> observations;
    CategoricalNaiveBayes(List<CategoricalVector> observations){
        this.observations = observations;
    }

    Map<String, List<Map<String,Integer>>> classNameToFeatureCounts = new HashMap<>();
    public Map<String,Integer> classCounts = new HashMap<>();

    public void train(){
        for(var ob : observations){
            List<Map<String,Integer>> listOfCounts = new ArrayList<>();
            for(int i = 0; i < observations.get(0).vector.length; i++){
                listOfCounts.add(new HashMap<>());
            }
            classNameToFeatureCounts.putIfAbsent(ob.className, listOfCounts);
            classCounts.merge(ob.className, 1, Integer::sum);
        }

        for(var ob : observations){
            var listOfCounts = classNameToFeatureCounts.get(ob.className);
            int idx = 0;
            for(String feature : ob.vector){
                var map = listOfCounts.get(idx);
                map.merge(feature,1, Integer::sum);
                idx++;
            }
        }
    }

    public String classify(CategoricalVector observation){
        String MAPClassName = "";
        double maxLogProb = Double.NEGATIVE_INFINITY;
        for(String className : classCounts.keySet()){
            var featureCounts = classNameToFeatureCounts.get(className);
            int totalCount = classCounts.get(className);
            double prob = Math.log((double)totalCount / observations.size());
            String[] vector = observation.vector;
            for(int i = 0; i < vector.length; i++){
                String featureValue = vector[i];

                int numberOfObservationsFromClassWithGivenFeatureValue = 0;
                if(featureCounts.get(i).get(featureValue) == null){
                    numberOfObservationsFromClassWithGivenFeatureValue = 0;
                }else{
                    numberOfObservationsFromClassWithGivenFeatureValue = featureCounts.get(i).get(featureValue);
                }
                double p = (double)(numberOfObservationsFromClassWithGivenFeatureValue + 1) /(totalCount + featureCounts.get(i).keySet().size());
                prob += Math.log(p);
//                System.out.println(p);
//                System.out.println(className);
            }
            if(maxLogProb <= prob){
                maxLogProb = prob;
                MAPClassName = className;
            }
        }
//        System.out.println(maxLogProb);
//        System.out.println(MAPClassName);
        return MAPClassName;
    }
    public static void test(){
        var ob1 = new CategoricalVector(new String[]{"y", "n"}, "class1");
        var ob2 = new CategoricalVector(new String[]{"y", "n"}, "class1");
        var ob3 = new CategoricalVector(new String[]{"y", "y"}, "class1");
        var ob4 = new CategoricalVector(new String[]{"y", "n"}, "class2");
        var ob5 = new CategoricalVector(new String[]{"n", "y"}, "class2");
        var v = List.of(ob1, ob2, ob3, ob4, ob5);
        var naiveBayes = new CategoricalNaiveBayes(v);
        naiveBayes.train();
        System.out.println(naiveBayes.classNameToFeatureCounts);
        //P(ob1 is in class1) = 4/5 * 3/5 * 3/5 = 36/125 Math.log(36/125) == -1.2448
        naiveBayes.classify(ob1);
    }

    public static void main(String[] args) {
        test();
    }
}
