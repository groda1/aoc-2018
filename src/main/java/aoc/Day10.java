package aoc;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import sun.jvm.hotspot.runtime.ppc.PPCCurrentFrameGuess;

public class Day10 {

    final static String regex = "< *(-?\\d+), *(-?\\d+)>";

    final static Pattern pattern = Pattern.compile(regex);

    private static Point parse(String s) {
        Matcher matcher = pattern.matcher(s);

        matcher.find();
        String x = matcher.group(1);
        String y = matcher.group(2);

        matcher.find();
        String velx = matcher.group(1);
        String vely = matcher.group(2);

        return new Point(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(velx),
            Integer.parseInt(vely));
    }

    private static long size(List<Point> points) {
        int max_x = points.stream().mapToInt(p -> p.x).max().getAsInt();
        int max_y = points.stream().mapToInt(p -> p.y).max().getAsInt();
        int min_x = points.stream().mapToInt(p -> p.x).min().getAsInt();
        int min_y = points.stream().mapToInt(p -> p.y).min().getAsInt();

        long width = max_x - min_x + 1;
        long height = max_y - min_y + 1;

        return width * height;
    }

    private static void print(List<Point> points) {
        int max_x = points.stream().mapToInt(p -> p.x).max().getAsInt();
        int max_y = points.stream().mapToInt(p -> p.y).max().getAsInt();
        int min_x = points.stream().mapToInt(p -> p.x).min().getAsInt();
        int min_y = points.stream().mapToInt(p -> p.y).min().getAsInt();

        System.err.println();
        for (int j = min_y; j <= max_y; j++) {
            for (int i = min_x; i <= max_x; i++) {
                int finalI = i;
                int finalJ = j;
                if (points.stream().filter(p -> p.x == finalI && p.y == finalJ).count() > 0) {
                    System.err.print("#");
                } else {
                    System.err.print(" ");
                }
            }
            System.err.println();
        }
    }

    public static void main(String[] args) {
        List<String> input = FileReader.readAsString("aoc/src/main/resources/day10.txt");

        List<Point> points = input.stream().map(Day10::parse).collect(Collectors.toList());

        long min_size = Long.MAX_VALUE;
        int index = 0;

        for (int i = 0; i < 100000; i++) {
            points.forEach(Point::step);

            long size = size(points);

            if (size < min_size) {
                min_size = size;
                index = i;
            }

            //durr
            if (size == 620) {
                print(points);
            }
        }

        System.err.println("size: " + min_size + " " + index);

    }

    private static class Point {
        int x;
        int y;
        int velx;
        int vely;

        Point(int x, int y, int velx, int vely) {
            this.x = x;
            this.y = y;
            this.velx = velx;
            this.vely = vely;
        }

        void step() {
            x += velx;
            y += vely;
        }
    }
}

