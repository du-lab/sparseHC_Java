package org.dulab.clustering.sparcehc;

import java.util.Objects;

public class MatrixElement {

    int row;
    int col;
    float value;

    MatrixElement(int row, int col, float value) {
        update(row, col, value);
    }

    void update(int row, int col, float value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    boolean isAboveThreshold(float threshold) {
        return value > threshold;
    }

    boolean lessThan(final MatrixElement e) {
        if (value == e.value)
            if (row == e.row)
                return col < e.col;
            else
                return row < e.row;
        else
            return value < e.value;
    }

    boolean equalTo(final MatrixElement e) {
        return value == e.value;
    }

    static MatrixElement minValue() {
        return new MatrixElement(0, 0, Float.MIN_VALUE);
    }

    static MatrixElement maxValue() {
        return new MatrixElement(Integer.MAX_VALUE, Integer.MAX_VALUE, Float.MAX_VALUE);
    }

    static int size() {
        return 32 * 2 + 32;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %f)", row, col, value);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof MatrixElement)) return false;
        MatrixElement that = (MatrixElement) other;
        return this.row == that.row && this.col == that.col && this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, value);
    }
}
