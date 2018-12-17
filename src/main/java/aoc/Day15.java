package aoc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Day15 {
    private static final char WALL = '#';
    private static final char GROUND = '.';

    private static final int BLOCKED = -2;
    private static final int UNVISITED = -1;

    private static int ELF_DEEPS = 3;

    private static Map<Point, Integer> distanceSearch(Point start, Point target) {
        LinkedList<Point> visitQueue = new LinkedList<>();

        int[][] distGrid = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                if (map[i][j] == WALL || unitAtPoint(i, j) != null) {
                    distGrid[i][j] = BLOCKED;
                } else {
                    distGrid[i][j] = UNVISITED;
                }

            }
        }
        distGrid[target.x][target.y] = 0;

        visitQueue.add(target);

        while (!visitQueue.isEmpty()) {
            Point p = visitQueue.pop();

            // Search left
            if (p.x - 1 >= 0 && distGrid[p.x - 1][p.y] == UNVISITED) {
                distGrid[p.x - 1][p.y] = distGrid[p.x][p.y] + 1;
                visitQueue.add(p.left());
            }
            // Search right
            if (p.x + 1 < width && distGrid[p.x + 1][p.y] == UNVISITED) {
                distGrid[p.x + 1][p.y] = distGrid[p.x][p.y] + 1;
                visitQueue.add(p.right());
            }
            // Search up
            if (p.y - 1 >= 0 && distGrid[p.x][p.y - 1] == UNVISITED) {
                distGrid[p.x][p.y - 1] = distGrid[p.x][p.y] + 1;
                visitQueue.add(p.up());
            }
            //Search down
            if (p.y + 1 < height && distGrid[p.x][p.y + 1] == UNVISITED) {
                distGrid[p.x][p.y + 1] = distGrid[p.x][p.y] + 1;
                visitQueue.add(p.down());
            }
        }
        //
        //        for (int j = 0; j < height; j++) {
        //            for (int i = 0; i < width; i++) {
        //                System.err.print(distGrid[i][j] + " ");
        //            }
        //            System.err.println();
        //        }

        Map<Point, Integer> distanceMap = new HashMap<>();

        if (start.x - 1 >= 0 && distGrid[start.x - 1][start.y] >= 0) {
            distanceMap.put(start.left(), distGrid[start.x - 1][start.y]);
        }
        if (start.x + 1 < width && distGrid[start.x + 1][start.y] >= 0) {
            distanceMap.put(start.right(), distGrid[start.x + 1][start.y]);
        }
        if (start.y - 1 >= 0 && distGrid[start.x][start.y - 1] >= 0) {
            distanceMap.put(start.up(), distGrid[start.x][start.y - 1]);
        }
        if (start.y + 1 < height && distGrid[start.x][start.y + 1] >= 0) {
            distanceMap.put(start.down(), distGrid[start.x][start.y + 1]);
        }
        return distanceMap;

    }

    enum Type {
        ELF('E'),
        GOBLIN('G');

        final char val;

        Type(char val) {
            this.val = val;
        }
    }

    private static class Unit {
        Type type;
        int hp = 200;
        Point p;

        Unit(Type type, Point p) {
            this.type = type;
            this.p = p;
        }

        void hit() {
            if (type == Type.GOBLIN) {
                hp -= ELF_DEEPS;
            } else {
                hp -= 3;
            }
        }
    }

    private static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int readingOrder() {
            return y * map.length + x;
        }

        Point left() {
            return new Point(x - 1, y);
        }

        Point right() {
            return new Point(x + 1, y);
        }

        Point up() {
            return new Point(x, y - 1);
        }

        Point down() {
            return new Point(x, y + 1);
        }

        List<Point> adjacent() {
            List<Point> list = new ArrayList<>(4);
            Point w = new Point(x - 1, y);
            Point e = new Point(x + 1, y);
            Point n = new Point(x, y - 1);
            Point s = new Point(x, y + 1);

            list.add(w);
            list.add(e);
            list.add(n);
            list.add(s);

            return list;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Point point = (Point)o;

            if (x != point.x)
                return false;
            return y == point.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y;
        }
    }

    private static Unit unitAtPoint(int x, int y) {
        Optional<Unit> unit =
            units.stream().filter(u -> u.p.x == x && u.p.y == y).findFirst();

        return unit.orElse(null);
    }

    private static boolean validPoint(Point p) {
        return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height && map[p.x][p.y] == GROUND
            && unitAtPoint(p.x, p.y) == null;

    }

    private static void print(int round) {
        System.err.println("Round " + round);
        for (int y = 0; y < height; y++) {
            int finalY = y;
            for (int x = 0; x < width; x++) {

                Unit unit = unitAtPoint(x, y);
                if (unit != null) {
                    System.err.print(unit.type.val);
                } else {
                    System.err.print(map[x][y]);
                }
            }

            units.stream().filter(u -> u.p.y == finalY)
                .sorted(Comparator.comparing(u -> u.p.readingOrder())).forEach(u -> {
                System.err.print(String.format(" (%c %d)", u.type.val, u.hp, u.p.x, u.p.y));
            });

            System.err.println();
        }
    }

    private static int targetsLeft(Unit unit) {

        return (int)units.stream().filter(u -> u.type != unit.type).count();

    }

    private static List<Unit> adjacentEnemies(Unit unit) {
        List<Point> adj = unit.p.adjacent();

        return units.stream().filter(u -> u.type != unit.type && adj.contains(u.p))
            .collect(Collectors.toList());

    }

    private static boolean tick() {
        List<Unit> orderedUnits =
            units.stream().sorted(Comparator.comparing(u -> u.p.readingOrder()))
                .collect(Collectors.toList());

        int i = 0;
        for (Unit unit : orderedUnits) {

            if (targetsLeft(unit) == 0) {
                break;
            }

            i++;

            if (unit.hp <= 0) {
                continue;
            }

            // Move
            if (adjacentEnemies(unit).size() == 0) {
                Set<Point> targets =
                    units.stream().filter(u -> u.type != unit.type).map(u -> u.p.adjacent())
                        .flatMap(Collection::stream).filter(Day15::validPoint)
                        .collect(Collectors.toSet());

                Map<Point, Integer> distanceMap =
                    targets.stream().map(t -> distanceSearch(unit.p, t).entrySet())
                        .flatMap(Collection::stream).collect(
                        Collectors.toMap(Entry::getKey, Entry::getValue, Integer::min));

                if (distanceMap.size() > 0) {
                    Integer minDist =
                        distanceMap.values().stream().mapToInt(p -> p).min().getAsInt();
                    Point p =
                        distanceMap.entrySet().stream().filter(e -> e.getValue().equals(minDist))
                            .sorted(Comparator.comparing(e -> e.getKey().readingOrder()))
                            .findFirst().get()
                            .getKey();

                    System.err.println("Moving " + unit.p + " -> " + p);

                    unit.p = p;

                    //                    distanceMap.entrySet().forEach(
                    //                        p -> System.err.println(p.getKey().x + ", " + p.getKey().y + " = " + p.getValue()));

                }
            }

            // Attack
            Optional<Unit> enemy =
                adjacentEnemies(unit).stream().sorted(Comparator.comparing(u -> u.hp))
                    .findFirst();
            if (enemy.isPresent()) {
                enemy.get().hit();
                if (enemy.get().hp <= 0) {
                    units.remove(enemy.get());
                }
            }
        }

        if (i == (orderedUnits.size())) {
            return true;
        } else {
            return false;
        }

    }

    private static int height;
    private static int width;
    static char[][] map;
    private static List<Unit> units;

    public static void main(String[] args) {
        List<String> input = FileReader.readAsString("aoc/src/main/resources/day15.txt");

        height = input.size();
        width = input.stream().mapToInt(String::length).max().getAsInt();

        map = new char[width][height];
        units = new ArrayList<>();

        int y = 0;
        for (String line : input) {
            int x = 0;
            for (char c : line.toCharArray()) {

                if (c == Type.GOBLIN.val) {
                    units.add(new Unit(Type.GOBLIN, new Point(x, y)));
                    map[x][y] = GROUND;
                } else if (c == Type.ELF.val) {
                    units.add(new Unit(Type.ELF, new Point(x, y)));
                    map[x][y] = GROUND;
                } else {
                    map[x][y] = c;
                }
                x++;
            }
            y++;
        }

        System.err
            .println("Elves before: " + units.stream().filter(u -> u.type == Type.ELF).count());

        int round = 0;
        while (true) {
            print(round);
            if (tick()) {
                round++;
            } else {
                break;
            }
            //            print(round);
        }
        //        print();

        int sum = units.stream().mapToInt(u -> u.hp).sum();
        System.err.println(round + " * " + sum + " = " + round * sum);
        System.err
            .println("Elves after: " + units.stream().filter(u -> u.type == Type.ELF).count());
    }

}