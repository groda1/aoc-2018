package aoc;

import java.util.List;

import aoc.Utils.FileReader;

public class Day5 {

    public static String collapse(String s) {
        StringBuilder sb = new StringBuilder();
        char[] chars = s.toCharArray();

        int i = 0;
        for (i = 0; i < chars.length; i++) {

            if (i == chars.length - 1) {
                sb.append(chars[i]);
                break;
            }

            Character c = chars[i];
            Character next = chars[i + 1];

            if (Character.isUpperCase(c) && Character.isLowerCase(next)) {

                if (c.toString().equals(next.toString().toUpperCase())) {
                    // Skip next
                    sb.append(s.substring(i + 2));
                    break;
                } else {
                    sb.append(c);
                }

            } else if (Character.isLowerCase(c) && Character.isUpperCase(next)) {
                if (c.toString().equals(next.toString().toLowerCase())) {
                    sb.append(s.substring(i + 2));
                    break;
                } else {
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }

        return sb.toString();

    }

    private static String collapsing(String line) {
        int len = line.length();

        while (true) {

            line = collapse(line);

            if (len == line.length()) {
                break;
            } else {
                len = line.length();
            }

        }
        return line;

    }

    public static void main(String[] args) {
        List<String> input = FileReader.readAsString("aoc/src/main/resources/day5.txt");

        String line = input.get(0);
        System.out.println(line.length());

        int min = line.length();

        for (char i = 'a'; i <= 'z'; i++) {

            String chara = String.valueOf(i);
            String newLine = line.replaceAll(String.valueOf(i), "");

            newLine =
                newLine.replaceAll(String.valueOf(i).toUpperCase(), "");

            String foo = collapsing(newLine);
            System.out.println(i + " : " + foo.length());

            min = Math.min(foo.length(), min);
        }

        System.err.println(min);

    }
}
