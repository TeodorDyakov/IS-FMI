package bg.unisofia.fmi.homeworks.hw7;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Unbalance {
    public static void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
        x = x - (r / 2);
        y = y - (r / 2);
        g.fillOval(x, y, r, r);
    }

    static Color[] colors = new Color[]{Color.BLACK, Color.BLUE, Color.RED, Color.GRAY, Color.GREEN, Color.pink,
            Color.CYAN, Color.ORANGE};

    public static void main(String[] args) throws IOException {
        File file = new File("unbalance/unbalance.txt");

        Scanner s = new Scanner(file);
        List<Node> points = new ArrayList<>();

        while(s.hasNext()){
            String[] tokens = s.nextLine().split("\\s+");
            Node node = new Node();
            node.vec = new float[2];
            node.vec[0] = Float.parseFloat(tokens[0]);
            node.vec[1] = Float.parseFloat(tokens[1]);
            points.add(node);
        }

        Kmeans kmeans = new Kmeans(2, 8, points);
        kmeans.train(100);

        BufferedImage img = new BufferedImage(1000, 1000,
                BufferedImage.TYPE_INT_RGB);
        for(int i =0; i < img.getHeight(); i++){
            for(int j =0; j < img.getWidth(); j++){
                img.setRGB(j, i, Color.white.getRGB());
            }
        }
        Graphics2D g = (Graphics2D) img.getGraphics();

        for(Node n : points){
            int x = (int)(n.vec[0]/600);
            int y = (int)(n.vec[1]/600);
            g.setColor(colors[n.cluster]);
            drawCenteredCircle(g, x, y, 5);
        }

        ImageIO.write(img, "jpg", new File("result.jpg"));
    }
}
