package bg.unisofia.fmi.homeworks.hw7;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class Kmeans {

    int n = 2;
    int k = 3;
    float[][] clusters;
    List<Node> observations;

    Kmeans(int n, int k, List<Node>observations){
        this.n = n;
        this.k = k;
        clusters = new float[k][n];
        this.observations = observations;
    }

    public static float dist(float[] vec1, float[] vec2) {
        float res = 0;
        for (int i = 0; i < vec1.length; i++) {
            float diff = vec1[i] - vec2[i];
            res += diff * diff;
        }
        return (float) Math.sqrt(res);
    }

    public static void add(float[] vec1, float[] vec2) {
        for (int i = 0; i < vec1.length; i++) {
            vec1[i] += vec2[i];
        }
    }

    public static void divide(float[] vec, float n) {
        for (int i = 0; i < vec.length; i++) {
            vec[i] /= n;
        }
    }

    public void assign() {
        for (Node ob : observations) {
            float minDist = Float.POSITIVE_INFINITY;
            int idx = 0;
            for (int i = 0; i < clusters.length; i++) {
                if (minDist > dist(ob.vec, clusters[i])) {
                    minDist = dist(ob.vec, clusters[i]);
                    idx = i;
                }
            }
            ob.cluster = idx;
        }
    }

    public void update() {
        int[] counts = new int[k];
        for (int i = 0; i < clusters.length; i++) {
            Arrays.fill(clusters[i], 0);
        }
        for (Node ob : observations) {
            add(clusters[ob.cluster], ob.vec);
            counts[ob.cluster]++;
        }
        for (int i = 0; i < clusters.length; i++) {
            if (counts[i] != 0) {
                divide(clusters[i], counts[i]);
            }
        }
    }

    void initializeCenters(){
        Random r = new Random();
        for (int i = 0; i < clusters.length; i++) {
            clusters[i] = observations.get(r.nextInt(observations.size())).vec;
        }
    }

    void train(int iterations){
        initializeCenters();
        for (int i = 0; i < 100; i++) {
            assign();
            update();
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("tiger.jpg"));

        int width = image.getWidth();
        int height = image.getHeight();

        List<Node> observations = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = new Color(image.getRGB(j, i));
                Node node = new Node();
                node.vec = color.getColorComponents(node.vec);
                observations.add(node);
            }
        }

        Kmeans k = new Kmeans(3, 7, observations);


        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int idx = i * width + j;
                Node n = k.observations.get(idx);
                float[] vec = k.clusters[n.cluster];
                Color c = new Color(vec[0], vec[1], vec[2]);
                image.setRGB(j, i, c.getRGB());
            }
        }

        File ouptut = new File("result.jpg");
        ImageIO.write(image, "jpg", ouptut);

    }

}