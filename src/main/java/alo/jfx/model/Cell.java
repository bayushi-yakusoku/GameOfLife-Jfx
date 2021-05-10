package alo.jfx.model;

import java.util.Arrays;

/**
 * Simple class to store coordinates
 */
public class Cell {
    private int x;
    private int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int hashCode() {
        final int[] numbers = {x, y};

        return Arrays.hashCode(numbers);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Cell))
            return false;

        final Cell other = (Cell) obj;

        return this.x == other.x && this.y == other.y;
    }
}
