package aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day14 {

    private static int elf1 = 0;
    private static int elf2 = 1;
    private static List<Integer> recipes = new ArrayList<>();

    private static void combine() {
        int combined = recipes.get(elf1) + recipes.get(elf2);

        if (combined < 10) {
            recipes.add(combined);
        } else {
            recipes.add(combined / 10);
            recipes.add(combined % 10);
        }

        elf1 = (elf1 + recipes.get(elf1) + 1) % recipes.size();
        elf2 = (elf2 + recipes.get(elf2) + 1) % recipes.size();
    }

    public static void main(String[] args) {

        recipes.add(3);
        recipes.add(7);

        int input = 320851;
        String input2 = String.valueOf(input);

        // Part 1
        while (recipes.size() < input + 10) {
            combine();
        }

        long sum = recipes.stream().skip(recipes.size() - 10).map(String::valueOf)
            .collect(Collectors.collectingAndThen(Collectors.joining(),
                Long::new));
        System.err.println(sum);

        // Part 2
        for (long i = 0;; i++) {
            if (i % 10000000 == 0) {
                String rec = recipes.stream().map(String::valueOf).collect(Collectors.joining());
                if (rec.contains(input2)) {
                    System.err.println(rec.indexOf(input2));
                    break;
                }

            }

            combine();

        }
    }

}