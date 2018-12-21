package aoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aoc.Utils.FileReader;
import aoc.Utils.Point;

public class Day17 {

    public enum State {
        CLAY,
        FLOWING,
        STAGNANT
    }

    private static void print() {
        int startX = map.keySet().stream().mapToInt(p -> p.x).min().getAsInt();
        int endX = map.keySet().stream().mapToInt(p -> p.x).max().getAsInt();

        for (int i = startHeight; i <= endHeight; i++) {
            for (int j = startX; j <= endX; j++) {
                State state = map.get(new Point(j, i));

                if (state != null) {
                    switch (state) {
                        case CLAY:
                            System.err.print("#");
                            break;
                        case FLOWING:
                            System.err.print("|");
                            break;
                        case STAGNANT:
                            System.err.print("~");
                            break;
                    }
                } else {
                    System.err.print(".");
                }
            }
            System.err.println();
        }
        System.err.println();
    }

    private static int startHeight;
    private static int endHeight;

    private static boolean isEmpty(Point p) {
        return !map.containsKey(p);
    }

    private static boolean water(Point point) {
        System.err.println("water: " + point);

        if (point.y > endHeight) {
            return true;
        }

        if (point.y >= startHeight) {
            map.put(point, State.FLOWING);
        }

        boolean flowing = false;

        if (isEmpty(point.down())) {
            flowing = water(point.down());
        } else if (map.get(point.down()) == State.FLOWING) {
            flowing = true;
        }

        if (!flowing) {

            boolean flowingRight = false;
            boolean flowingLeft = false;

            List<Point> traversedPoints = new ArrayList<>();
            traversedPoints.add(point);

            // Fill right
            Point p = point.right();
            while (map.get(p) != State.CLAY) {
                map.put(p, State.FLOWING);
                traversedPoints.add(p);
                if (isEmpty(p.down())) {
                    if (water(p.down())) {
                        flowingRight = true;
                        break;
                    }
                } else if (map.get(p.down()) == State.FLOWING) {
                    flowingRight = true;
                    break;
                }
                p = p.right();
            }

            // Fill left
            p = point.left();
            while (map.get(p) != State.CLAY) {
                map.put(p, State.FLOWING);
                traversedPoints.add(p);
                if (isEmpty(p.down())) {
                    if (water(p.down())) {
                        flowingLeft = true;
                        break;
                    }
                } else if (map.get(p.down()) == State.FLOWING) {
                    flowingLeft = true;
                    break;
                }
                p = p.left();
            }

            flowing = flowingRight | flowingLeft;

            if (!flowing) {
                traversedPoints.forEach(t -> map.put(t, State.STAGNANT));
            }
        }
        return flowing;

    }

    static Map<Point, State> map = new HashMap<>();

    public static void main(String[] args) {

        List<String> input = FileReader.readAsString("aoc/src/main/resources/day17.txt");

        for (String line : input) {
            String[] lines = line.split(",");

            boolean vertical = lines[0].startsWith("x");

            int firstVal = Integer.parseInt(lines[0].split("=")[1]);
            String[] secondVal = lines[1].split("=")[1].split("\\.\\.");

            int secondValStart = Integer.parseInt(secondVal[0]);
            int secondValEnd = Integer.parseInt(secondVal[1]);

            for (int i = secondValStart; i <= secondValEnd; i++) {

                if (vertical) {
                    map.put(new Point(firstVal, i), State.CLAY);
                } else {
                    map.put(new Point(i, firstVal), State.CLAY);
                }
            }
        }

        startHeight = map.keySet().stream().mapToInt(p -> p.y).min().getAsInt();
        endHeight = map.keySet().stream().mapToInt(p -> p.y).max().getAsInt();

        print();
        water(new Point(500, 0));

        print();
        System.err.println(map.values().stream().filter(val -> val != State.CLAY).count());
        System.err.println(map.values().stream().filter(val -> val == State.STAGNANT).count());

    }

}