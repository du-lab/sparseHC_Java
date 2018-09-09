package org.dulab.clustering.sparcehc;

import java.util.Objects;

public class MatrixElement {

    final int row;
    final int col;
    final float value;

    MatrixElement(int row, int col, float value) {
        this.row = row;
        this.col = col;
        this.value = value;
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
