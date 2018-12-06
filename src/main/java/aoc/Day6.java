package aoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day6 {
    static Point[][] grid;
    static List<Point> pointList = new ArrayList<>();

    private static int distance(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    private static List<Point> findClosest(int x, int y) {
        if (grid[x][y] != null) {
            return Collections.singletonList(grid[x][y]);
        }
        int min = pointList.stream().mapToInt(c -> distance(new Point(x, y), c)).min().getAsInt();
        return pointList.stream().filter(c -> distance(new Point(x, y), c) == min)
            .collect(Collectors.toList());
    }

    private static void calcArea(int x, int y) {
        List<Point> closest = findClosest(x, y);
        if (borderPoint(x, y)) {
            closest.forEach(c -> c.inf = true);
        }
        if (closest != null && closest.size() == 1) {
            closest.get(0).area++;
        }
    }

    private static boolean borderPoint(int x, int y) {
        return (x == 0 || y == 0 || x == (grid.length - 1) || y == (grid[0].length - 1));
    }

    private static int totDist(int x, int y) {
        return pointList.stream().mapToInt(p -> distance(new Point(x, y), p)).sum();
    }

    public static void main(String[] args) {
        List<String> input = FileReader.readAsString("aoc/src/main/resources/day6.txt");

        for (String line : input) {
            String[] foo = line.split(", ");

            int x = Integer.parseInt(foo[0]);
            int y = Integer.parseInt(foo[1]);

            pointList.add(new Point(x, y));
        }

        int maxX = pointList.stream().mapToInt(p -> p.x).max().getAsInt();
        int maxY = pointList.stream().mapToInt(p -> p.y).max().getAsInt();

        grid = new Point[maxX + 1][maxY + 1];
        pointList.forEach(p -> grid[p.x][p.y] = p);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                calcArea(i, j);
            }
        }

        System.err
            .println(pointList.stream().filter(p -> !p.inf).mapToInt(p -> p.area).max().getAsInt());

        // part 2
        int regionSize = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (totDist(i, j) < 10000) {
                    regionSize++;
                }
            }
        }
        System.err.println("region size: " + regionSize);
    }

    private static class Point {
        int x;
        int y;

        boolean inf;
        int area = 0;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
