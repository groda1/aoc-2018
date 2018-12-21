package aoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc.Utils.FileReader;
import aoc.Utils.Point;

public class Day3 {

    final static String regex = "^#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)";
    final static Pattern pattern = Pattern.compile(regex);

    static class Claim {
        int id;

        List<Point> points = new ArrayList<>();

        Claim(int id) {
            this.id = id;
        }

        void addPoint(Point p) {
            points.add(p);
        }

    }

    private static void lol(String s, Map<Point, List<Integer>> map, List<Claim> claimList) {
        Matcher matcher = pattern.matcher(s);

        matcher.find();

        int id = Integer.parseInt(matcher.group(1));
        int offsetX = Integer.parseInt(matcher.group(2));
        int offsetY = Integer.parseInt(matcher.group(3));
        int width = Integer.parseInt(matcher.group(4));
        int height = Integer.parseInt(matcher.group(5));

        Claim claim = new Claim(id);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Point p = new Point(i + offsetX, j + offsetY);
                claim.addPoint(p);

                if (map.containsKey(p)) {
                    map.get(p).add(id);
                } else {
                    ArrayList<Integer> list = new ArrayList<>();
                    list.add(id);
                    map.put(p, list);
                }

            }
        }
        claimList.add(claim);
    }

    public static void main(String[] args) {
        Map<Point, List<Integer>> map = new HashMap<>();
        List<String> lol = FileReader.readAsString("aoc/src/main/resources/day3.txt");

        List<Claim> claimList = new ArrayList<>();
        lol.forEach(s -> lol(s, map, claimList));

        System.err.println(map.size());
        System.err.println(map.values().stream().filter(i -> i.size() > 1).count());

        claimList.forEach(claim -> {
            boolean overlap = false;
            for (Point p : claim.points) {
                if (map.get(p).size() > 1) {
                    overlap = true;
                }
            }
            if (!overlap) {
                System.err.println("not overlapped: " + claim.id);
            }
        });

    }
}
