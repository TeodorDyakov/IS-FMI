package bg.unisofia.fmi.homeworks.hw7;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static bg.unisofia.fmi.homeworks.hw7.Unbalance.colors;
import static bg.unisofia.fmi.homeworks.hw7.Unbalance.drawCenteredCircle;

public class Normal {

    public static void experiment(String dataRelativePath, String resultFileName, int numberOfClusters, double scalingFactor) throws IOException {
        System.out.println("\nExperiment with " + dataRelativePath + " and " + numberOfClusters + " clusters");
        File file = new File(dataRelativePath);

        Scanner s = new Scanner(file);
        List<Node> points = new ArrayList<>();

        while (s.hasNext()) {
            String[] tokens = s.nextLine().split("\\s+");
            Node node = new Node();
            node.vec = new float[2];
            node.vec[0] = Float.parseFloat(tokens[0]);
            node.vec[1] = Float.parseFloat(tokens[1]);
            points.add(node);
        }

        int k = numberOfClusters;
        for (int restart = 1; restart <= 10; restart++) {
            double totalAverageDistance = 0;
            Kmeans kmeans = new Kmeans(2, k, points);
            kmeans.train(100);

            BufferedImage img = new BufferedImage(1000, 1000,
                    BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < img.getHeight(); i++) {
                for (int j = 0; j < img.getWidth(); j++) {
                    img.setRGB(j, i, Color.white.getRGB());
                }
            }

            Graphics2D g = (Graphics2D) img.getGraphics();

            for (Node n : points) {
                int x = (int) (n.vec[0] * scalingFactor);
                int y = (int) (n.vec[1] * scalingFactor);
                g.setColor(colors[n.cluster]);
                drawCenteredCircle(g, x, y, 5);
            }

            double[] distancesSum = new double[k];
            double[] distancesCount = new double[k];
            for (Node n1 : points) {
                for (Node n2 : points) {
                    if (n1 != n2 && n1.cluster == n2.cluster) {
                        distancesSum[n1.cluster] += Kmeans.dist(n1.vec, n2.vec);
                        distancesCount[n1.cluster]++;
                    }
                }
            }

            for (int i = 0; i < k; i++) {
                if(distancesCount[i] != 0){
                    totalAverageDistance += distancesSum[i] / distancesCount[i];
                }
            }
            System.out.println("Restart number: " + restart);
            System.out.println("Intracluster distance (Sum of average diameter distance): " + totalAverageDistance);
            ImageIO.write(img, "jpg", new File(resultFileName + restart + ".jpg"));
            System.out.println("Result written to " + resultFileName + (restart) +".jpg" + "\n");
        }
    }
    public static void main(String[] args) throws IOException {
        experiment("normal/normal.txt", "normalClusters", 2, 100);
        experiment("unbalance/unbalance.txt", "unbalanceClusters", 8, 0.001);
    }
}
