package aoc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import aoc.Utils.FileReader;

public class Day18 {

    public enum State {
        OPEN('.'),
        TREE('|'),
        LUMBERYARD('#');

        private char val;

        State(char val) {
            this.val = val;
        }

        static State get(char c) {
            return Arrays.stream(State.values())
                .filter(s -> s.val == c)
                .findFirst()
                .orElse(null);
        }
    }

    private static void print(State[][] map) {

        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; j < map.length; j++) {
                System.err.print(map[j][i].val);
            }
            System.err.println();
        }
        System.err.println();
    }

    private static int adjacent(State[][] map, int x, int y, State type) {
        int num = 0;

        if (x - 1 >= 0) {

            if (y - 1 >= 0 && map[x - 1][y - 1] == type) {
                num++;
            }

            if (y + 1 < map[0].length && map[x - 1][y + 1] == type) {
                num++;
            }

            if (map[x - 1][y] == type) {
                num++;
            }
        }

        if (x + 1 < map.length) {
            if (y - 1 >= 0 && map[x + 1][y - 1] == type) {
                num++;
            }

            if (y + 1 < map[0].length && map[x + 1][y + 1] == type) {
                num++;
            }

            if (map[x + 1][y] == type) {
                num++;
            }
        }

        if (y - 1 >= 0 && map[x][y - 1] == type) {
            num++;
        }

        if (y + 1 < map[0].length && map[x][y + 1] == type) {
            num++;
        }

        return num;
    }

    private static void tickCol(State[][] map, State[][] newMap, int x) {

        int height = map[0].length;

        for (int y = 0; y < height; y++) {
            if (map[x][y] == State.OPEN && adjacent(map, x, y, State.TREE) >= 3) {
                newMap[x][y] = State.TREE;
            } else if (map[x][y] == State.TREE && adjacent(map, x, y, State.LUMBERYARD) >= 3) {
                newMap[x][y] = State.LUMBERYARD;
            } else if (map[x][y] == State.LUMBERYARD) {

                if (adjacent(map, x, y, State.LUMBERYARD) >= 1
                    && adjacent(map, x, y, State.TREE) >= 1) {
                    newMap[x][y] = State.LUMBERYARD;
                } else {
                    newMap[x][y] = State.OPEN;
                }
            } else {
                newMap[x][y] = map[x][y];
            }
        }
    }

    private static State[][] tick(State[][] map) {

        int width = map.length;
        int height = map[0].length;

        State[][] newMap = new State[width][height];

        for (int x = 0; x < width; x++) {
            tickCol(map, newMap, x);
        }

        return newMap;
    }

    public static void main(String[] args) {

        List<String> input = FileReader.readAsString("aoc/src/main/resources/day18.txt");

        int height = input.size();
        int width = input.stream().mapToInt(String::length).max().getAsInt();

        State[][] map = new State[width][height];

        int y = 0;
        for (String line : input) {
            int x = 0;
            for (char c : line.toCharArray()) {
                map[x][y] = State.get(c);
                x++;
            }
            y++;
        }

        HashMap<Long, Long> durr = new HashMap<>();

        long minutes = 1000000000L - 500;

        for(int i = 0; i < 500; i++) {
            map = tick(map);
        }

        long i=0;
        long offset= 0;

        for (i = 0; i < minutes; i++) {
            map = tick(map);

            long lumberyards =
                Arrays.stream(map).flatMap(Arrays::stream).filter(s -> s == State.LUMBERYARD)
                    .count();

            long trees =
                Arrays.stream(map).flatMap(Arrays::stream).filter(s -> s == State.TREE).count();

            long val = lumberyards * trees;

            if (offset == 0 && durr.containsKey(val)) {

                offset = i - durr.get(val);
                System.err.println("repeat " + i + ", off=" + (i - durr.get(val)));

                long skip = ((minutes - i) / offset) * offset;
                i+= skip;


                System.err.println("Skipped to:" + i);

            }
            durr.put(val, i);

        }

        print(map);
        long lumberyards =
            Arrays.stream(map).flatMap(Arrays::stream).filter(s -> s == State.LUMBERYARD)
                .count();
        long trees =
            Arrays.stream(map).flatMap(Arrays::stream).filter(s -> s == State.TREE).count();

        System.err.println(lumberyards + " * " + trees + " = " + lumberyards * trees);

    }

}

