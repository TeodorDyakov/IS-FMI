package bg.unisofia.fmi.homeworks.hw3;

public class City {
    public double x, y;
    public String name;

    public City(double x, double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public double dist(City c) {
        return Math.sqrt(Math.pow(c.x - x, 2) + Math.pow(c.y - y, 2));
    }
}
