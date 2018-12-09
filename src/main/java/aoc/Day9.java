package aoc;

import java.util.Arrays;

public class Day9 {

    private static class Marble {
        int num;
        Marble prev = this;
        Marble next = this;

        Marble(int num) {
            this.num = num;
        }
    }

    private static int place(int m) {
        if(m % 23 > 0) {
            Marble marble = new Marble(m);
            marble.next = current.next.next;
            marble.prev = current.next;
            current.next.next.prev = marble;
            current.next.next = marble;
            current = marble;

            return 0;
        } else {
            Marble remove = current.prev.prev.prev.prev.prev.prev.prev;
            remove.prev.next = remove.next;
            remove.next.prev = remove.prev;
            current = remove.next;

            return m + remove.num;
        }
    }

    private static Marble current = new Marble(0);

    public static void main(String[] args) {
        int players = 455;
        int last = 71223 * 100;
        int player = 0;

        long score[] = new long[players];
        for (int i = 1; i <= last; i++) {

            score[player] += place(i);

            player++;
            if(player >= players) {
                player = 0;
            }
        }

        System.err.println(Arrays.stream(score).max().getAsLong());
    }
}

