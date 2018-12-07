package aoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Day7 {
    static HashMap<String, List<String>> steps = new HashMap<>();
    static Set<String> taken = new HashSet<>();

    private static int time(String s) {
        return 51 + Character.getNumericValue(s.charAt(0));
    }

    public static void main(String[] args) {
        List<String> input = FileReader.readAsString("aoc/src/main/resources/day7.txt");

        for (String line : input) {
            String before = line.substring(5, 6);
            String after = line.substring(36, 37);

            if (!steps.containsKey(before)) {
                steps.put(before, new ArrayList<>());
            }
            if (!steps.containsKey(after)) {
                steps.put(after, new ArrayList<>());
            }
            steps.get(after).add(before);
        }

        // Part 1
//        StringBuilder sb = new StringBuilder();
//        while (!steps.isEmpty()) {
//            String step = steps.entrySet().stream().filter(e ->
//                taken.containsAll(e.getValue())).map(Entry::getKey).sorted().findFirst().get();
//
//            sb.append(step);
//            steps.remove(step);
//            taken.add(step);
//        }
//        System.err.println(sb.toString());

        //Part 2
        int workers = 5;
        int time = 0;

        Map<String, Integer> ongoing = new HashMap<>();

        while (!steps.isEmpty()) {
            Optional<String> stepOpt = steps.entrySet().stream().filter(e ->
                taken.containsAll(e.getValue())).map(Entry::getKey).sorted().findFirst();

            while (stepOpt.isPresent() && workers > 0) {
                String step = stepOpt.get();

                ongoing.put(step, time(step) + time);
                steps.remove(step);
                workers--;

                stepOpt = steps.entrySet().stream().filter(e ->
                    taken.containsAll(e.getValue())).map(Entry::getKey).sorted().findFirst();
            }

            int lowest = ongoing.values().stream().mapToInt(e -> e).min().getAsInt();
            List<String> finished =
                ongoing.entrySet().stream().filter(e -> e.getValue() == lowest)
                    .map(Entry::getKey)
                    .collect(Collectors.toList());

            finished.forEach(ongoing::remove);
            taken.addAll(finished);
            time = lowest;
            workers += finished.size();
        }
        if(!ongoing.isEmpty()) {
            time = ongoing.values().stream().mapToInt(v -> v).max().getAsInt();
        }

        System.err.println("time: " + time);
    }
}

