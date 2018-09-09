package org.dulab.clustering.sparcehc;

import java.util.*;

public class Matrix {

    private SortedSet<MatrixElement> elements;

    float threshold;

    Matrix(float threshold) {
        this.threshold = threshold;
        this.elements = new TreeSet<>(Comparator
                .comparing((MatrixElement e) -> e.value)
                .thenComparing((MatrixElement e) -> e.row)
                .thenComparing((MatrixElement e) -> e.col));
    }

    Matrix() {
        this(Float.MAX_VALUE);
    }

    void add(int row, int col, float value) {
        if (value < threshold)
            elements.add(new MatrixElement(row, col, value));
    }

    SortedSet<MatrixElement> getElements() {
        return elements;
    }

    int getNumElements() {
        return elements.size();
    }
}
