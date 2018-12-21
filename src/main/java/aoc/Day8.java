package aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import aoc.Utils.FileReader;

public class Day8 {

    private static class Node {
        List<Node> children = new ArrayList<>();
        List<Integer> meta = new ArrayList<>();
    }

    private static LinkedList<Integer> foo = new LinkedList<>();

    private static int sum(Node n) {
        int tot = n.children.stream().mapToInt(Day8::sum).sum();
        tot += n.meta.stream().mapToInt(m -> m).sum();
        return tot;
    }

    private static int sum_part2(Node n) {
        if (n.children.isEmpty()) {
            return n.meta.stream().mapToInt(m -> m).sum();
        } else {
            int sum = 0;
            for (Integer m : n.meta) {
                if ((m - 1) < n.children.size()) {
                    sum += sum_part2(n.children.get(m - 1));
                }

            }
            return sum;
        }
    }

    private static Node parse() {
        Node n = new Node();
        int nChildren = foo.pop();
        int nMeta = foo.pop();

        for (int i = 0; i < nChildren; i++) {
            n.children.add(parse());
        }
        for (int i = 0; i < nMeta; i++) {
            n.meta.add(foo.pop());
        }

        return n;
    }

    public static void main(String[] args) {
        List<String> input = FileReader.readAsString("aoc/src/main/resources/day8.txt");
        Arrays.stream(input.get(0).split(" ")).map(Integer::parseInt).forEach(foo::add);

        Node n = parse();

        System.err.println(sum(n));
        System.err.println(sum_part2(n));
    }
}

