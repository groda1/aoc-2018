package aoc;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import aoc.Utils.FileReader;

public class Day12 {

    private static String state;

    private static HashMap<String, Boolean> rules = new HashMap<>();
    private static int index;

    private static String update() {
        StringBuilder sb = new StringBuilder();

        sb.append(state, 0, 2);

        for (int i = 2; i < state.length() - 2; i++) {
            String substring = state.substring(i - 2, i + 3);

            Optional<Boolean> foo =
                rules.entrySet().stream().filter(r -> r.getKey().equals(substring))
                    .map(Entry::getValue).findFirst();

            if (foo.isPresent()) {
                sb.append(foo.get() ? "#" : ".");
            } else {
                sb.append(".");
            }

        }
        sb.append(state.substring(state.length() - 2));

        String newState = sb.toString();

        if (newState.endsWith("#..")) {
            newState = newState + ".";
        } else if (newState.endsWith("....")) {
            newState = newState.substring(0, newState.length() - 1);
        }

        if (newState.startsWith("..#")) {
            newState = "." + newState;
            index--;
        } else if (newState.startsWith("....")) {
            newState = newState.substring(1);
            index++;
        }

        return newState;
    }

    private static int value() {
        int sum = 0;
        char[] s = state.toCharArray();
        for (int i = 0; i < s.length; i++) {
            if (s[i] == '#') {
                sum += i + index;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        List<String> input = FileReader.readAsString("aoc/src/main/resources/day12.txt");

        state = "..." + input.get(0).substring(15) + "...";
        index -= 3;

        for (int i = 2; i < input.size(); i++) {
            String rule = input.get(i).substring(0, 5);
            String to = input.get(i).substring(9, 10);

            rules.put(rule, to.equals("#"));

        }

        int i = 0;
        for (; i < 200; i++) {

            String newState = update();
            if (newState.equals(state)) {
                break;
            }
            state = newState;
            System.err.println(i + 1 + " " + state + " " + value() + " " + index);
        }
        System.err.println(i + 1 + " " + state + " " + value() + " " + index);

        long herp = (50000000000L - (i + 1)) * 5;
        System.err.println(value() + herp);

    }

}
