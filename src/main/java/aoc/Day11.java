package aoc;

public class Day11 {

    private static int powerLevel(int x, int y, int serial) {
        return (((x + 10) * y + serial) * (x + 10) / 100) % 10 - 5;
    }

    private static int SIZE = 300;
    private static int[][] cache = new int[SIZE][SIZE];

    public static void main(String[] args) {
        int serial = 1788;

        int max = -1;
        int x = 0;
        int y = 0;
        int size = 0;

        for (int i = 1; i <= SIZE; i++) {
            for (int j = 1; j <= SIZE; j++) {
                cache[i - 1][j - 1] = powerLevel(i, j, serial);
            }
        }

        for (int k = 2; k <= 300; k++) {
            for (int i = 1; i <= SIZE - k + 1; i++) {
                for (int j = 1; j <= SIZE - k + 1; j++) {

                    int power = cache[i - 1][j - 1];

                    for (int l = 0; l < k - 1; l++) {
                        power += powerLevel(i + k - 1, j + l, serial);
                        power += powerLevel(i + l, j + k - 1, serial);
                    }
                    power += powerLevel(i + k - 1, j + k - 1, serial);

                    cache[i - 1][j - 1] = power;

                    if (power > max) {
                        max = power;
                        x = i;
                        y = j;
                        size = k;
                    }
                }
            }
        }

        System.err
            .println("x: " + x + " y: " + y + " size: " + size + "*" + size + " power: " + max);

    }
}

