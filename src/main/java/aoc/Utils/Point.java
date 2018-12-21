package aoc.Utils;

import java.util.ArrayList;
import java.util.List;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int readingOrder(int length) {
        return y * length + x;
    }

    public Point left() {
        return new Point(x - 1, y);
    }

    public Point right() {
        return new Point(x + 1, y);
    }

    public Point up() {
        return new Point(x, y - 1);
    }

    public Point down() {
        return new Point(x, y + 1);
    }

    public List<Point> adjacent() {
        List<Point> list = new ArrayList<>(4);
        Point w = new Point(x - 1, y);
        Point e = new Point(x + 1, y);
        Point n = new Point(x, y - 1);
        Point s = new Point(x, y + 1);

        list.add(w);
        list.add(e);
        list.add(n);
        list.add(s);

        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Point point = (Point)o;

        if (x != point.x)
            return false;
        return y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y;
    }
}
