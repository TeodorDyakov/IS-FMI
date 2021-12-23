package bg.unisofia.fmi.homeworks.hw5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class HouseData {
    public static void main(String[] args) throws FileNotFoundException {
        List<CategoricalVector> houseData = new ArrayList<>();
        Scanner scanner = new Scanner(new File("house-votes-84.data"));
        while(scanner.hasNext()){
            String line = scanner.nextLine();
            if(line.contains("?")){
                continue;
            }
            String[] tokens = line.split(",");
            String[] featureVector = new String[tokens.length - 1];

            for(int i = 1; i < tokens.length; i++){
                featureVector[i - 1] = tokens[i];
            }
            CategoricalVector categoricalVector = new CategoricalVector(featureVector, tokens[0]);
            houseData.add(categoricalVector);
        }

        Collections.shuffle(houseData);
        List<List<CategoricalVector>> sets = new ArrayList<>();
        int setSz = houseData.size()/10;
        for(int i = 0; i < 10; i++){
            sets.add(houseData.subList(i*setSz, (i+1)*setSz));
        }
        int totalAccurate = 0;
        int total = 0;
        for(int i = 0; i < 10; i++){
            List<CategoricalVector>trainData = new ArrayList<>();
            List<CategoricalVector>testData = sets.get(i);
            for(int j = 0; j < 10; j++){
                if(i != j){
                    trainData.addAll(sets.get(i));
                }
            }
            CategoricalNaiveBayes nb = new CategoricalNaiveBayes(trainData);
            nb.train();
            int accurate = 0;
            for(CategoricalVector v : testData){
                String predicted = nb.classify(v);
                if(predicted.equals(v.className)){
                    accurate++;
                }
            }
            totalAccurate += accurate;
            total += testData.size();
            System.out.printf("Testing on set number %d, accuracy: %f\n", i, (float)accurate/testData.size());

        }
        System.out.printf("Average accuracy: %f\n", (float)totalAccurate/total);
    }
}
