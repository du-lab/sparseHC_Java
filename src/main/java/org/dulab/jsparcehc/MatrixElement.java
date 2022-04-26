package org.dulab.jsparcehc;

import java.util.Objects;

public class MatrixElement {

    final int row;
    final int col;
    final float value;

    public MatrixElement(int row, int col, float value) {
        if (col < row) {
            int i = row;
            row = col;
            col = i;
        }

        this.row = row;
        this.col = col;
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
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
        return this.row == that.row && this.col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
