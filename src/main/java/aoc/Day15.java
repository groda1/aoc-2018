package aoc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import aoc.Utils.FileReader;
import aoc.Utils.Point;

public class Day15 {
    private static final char WALL = '#';
    private static final char GROUND = '.';

    private static final int BLOCKED = -2;
    private static final int UNVISITED = -1;

    private static int ELF_DEEPS = 34;

    private static int distance(Point start, Point target) {

        return distanceSearch(start, target).values().stream().mapToInt(v -> v + 1).min()
            .orElse(Integer.MAX_VALUE);
    }

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
        System.err.println("After round " + round);
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
                .sorted(Comparator.comparing(u -> u.p.readingOrder(width))).forEach(u -> {
                System.err.print(String.format(" (%c %d)", u.type.val, u.hp));
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
            units.stream().sorted(Comparator.comparing(u -> u.p.readingOrder(width)))
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

                if (targets.size() > 0) {
                    int min =
                        targets.stream().mapToInt(target -> distance(unit.p, target)).min()
                            .getAsInt();
                    if (min < Integer.MAX_VALUE) {
                        Point chosenTarget =
                            targets.stream().filter(target -> distance(unit.p, target) == min)
                                .sorted(Comparator.comparing(p -> p.readingOrder(width))).findFirst()
                                .get();

                        Map<Point, Integer> distanceMap = distanceSearch(unit.p, chosenTarget);

                        if (distanceMap.size() > 0) {
                            Integer minDist =
                                distanceMap.values().stream().mapToInt(p -> p).min().getAsInt();
                            Point p =
                                distanceMap.entrySet().stream()
                                    .filter(e -> e.getValue().equals(minDist))
                                    .sorted(Comparator.comparing(e -> e.getKey().readingOrder(width)))
                                    .findFirst().get()
                                    .getKey();


                            System.err.println("Moving " + unit.p + " -> " + p);
                            unit.p = p;
                        }
                    }
                }
            }

            // Attack
            OptionalInt minEnemyHp =
                adjacentEnemies(unit).stream().mapToInt(u -> u.hp).min();
            if(minEnemyHp.isPresent()) {
                Unit enemy =
                    adjacentEnemies(unit).stream().filter(u -> u.hp == minEnemyHp.getAsInt())
                        .min(Comparator.comparing(u -> u.p.readingOrder(width))).get();
                enemy.hit();
                if (enemy.hp <= 0) {
                    boolean b = units.remove(enemy);
                }
            }

        }

        return i == (orderedUnits.size());

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

        print(0);
        System.err
            .println("Elves before: " + units.stream().filter(u -> u.type == Type.ELF).count());

        int round = 0;
        while (true) {

            if (tick()) {
                round++;
            }

            print(round);
            if (units.size() == 0 || targetsLeft(units.get(0)) == 0) {
                break;
            }

        }
        int sum = units.stream().mapToInt(u -> u.hp).sum();
        System.err.println(round + " * " + sum + " = " + round * sum);
        System.err
            .println("Elves after: " + units.stream().filter(u -> u.type == Type.ELF).count());
    }

}