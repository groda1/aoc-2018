package aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {

    enum Direction {
        WEST, NORTH, EAST, SOUTH
    }

    private static class Cart {
        int x;
        int y;
        Direction dir;

        int turn = 0;
        boolean collided = false;

        public Cart(int x, int y, Direction dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        void collide() {
            collided = true;
        }
    }

    private static List<Cart> collision(List<Cart> carts, Cart cart) {

        return carts.stream().filter(c -> c.x == cart.x && c.y == cart.y && !c.collided)
            .collect(Collectors.toList());
    }

    private static boolean tick(List<Cart> carts, char[][] map) {

        for (Cart c : carts) {
            switch (c.dir) {
                case WEST:
                    c.x--;
                    break;
                case NORTH:
                    c.y--;
                    break;
                case EAST:
                    c.x++;
                    break;
                case SOUTH:
                    c.y++;
                    break;
            }

            List<Cart> colliding = collision(carts, c);

            if (colliding.size() > 1) {
                System.err.println("COLLISION " + c.x + ", " + c.y);
                colliding.forEach(Cart::collide);
            }

            switch (map[c.x][c.y]) {
                case '/':
                    switch (c.dir) {
                        case WEST:
                            c.dir = Direction.SOUTH;
                            break;
                        case NORTH:
                            c.dir = Direction.EAST;
                            break;
                        case EAST:
                            c.dir = Direction.NORTH;
                            break;
                        case SOUTH:
                            c.dir = Direction.WEST;
                            break;
                    }
                    break;
                case '\\':
                    switch (c.dir) {
                        case WEST:
                            c.dir = Direction.NORTH;
                            break;
                        case NORTH:
                            c.dir = Direction.WEST;
                            break;
                        case EAST:
                            c.dir = Direction.SOUTH;
                            break;
                        case SOUTH:
                            c.dir = Direction.EAST;
                            break;
                    }
                    break;
                case '+':
                    switch (c.dir) {
                        case WEST:
                            if (c.turn == 0)
                                c.dir = Direction.SOUTH;
                            else if (c.turn == 2)
                                c.dir = Direction.NORTH;
                            break;
                        case NORTH:
                            if (c.turn == 0)
                                c.dir = Direction.WEST;
                            else if (c.turn == 2)
                                c.dir = Direction.EAST;
                            break;
                        case EAST:
                            if (c.turn == 0)
                                c.dir = Direction.NORTH;
                            else if (c.turn == 2)
                                c.dir = Direction.SOUTH;
                            break;
                        case SOUTH:
                            if (c.turn == 0)
                                c.dir = Direction.EAST;
                            else if (c.turn == 2)
                                c.dir = Direction.WEST;
                            break;
                    }
                    c.turn++;
                    if (c.turn == 3) {
                        c.turn = 0;
                    }
                    break;

            }
        }

        carts.removeAll(carts.stream().filter(c -> c.collided).collect(Collectors.toList()));

        return true;
    }

    public static void main(String[] args) {
        List<String> input = FileReader.readAsString("aoc/src/main/resources/day13.txt");

        int height = input.size();
        int width = input.stream().mapToInt(String::length).max().getAsInt();

        char[][] map = new char[width][height];
        List<Cart> carts = new ArrayList<>();

        int y = 0;
        for (String line : input) {
            int x = 0;

            for (char c : line.toCharArray()) {
                map[x][y] = c;

                Cart cart = null;
                if (map[x][y] == 'v') {
                    cart = new Cart(x, y, Direction.SOUTH);
                    map[x][y] = '|';
                } else if (map[x][y] == '^') {
                    cart = new Cart(x, y, Direction.NORTH);
                    map[x][y] = '|';
                } else if (map[x][y] == '>') {
                    cart = new Cart(x, y, Direction.EAST);
                    map[x][y] = '-';
                } else if (map[x][y] == '<') {
                    cart = new Cart(x, y, Direction.WEST);
                    map[x][y] = '-';
                }

                if (cart != null) {
                    carts.add(cart);
                }
                x++;
            }
            y++;
        }

        for (Cart c : carts) {
            System.err.println(c.x + ", " + c.y + " : " + c.dir);
        }

        while (carts.size() > 1) {
            tick(carts, map);
            for (Cart c : carts) {
                System.err.println(c.x + ", " + c.y + " : " + c.dir);
            }
            System.err.println();
        }

    }

}
