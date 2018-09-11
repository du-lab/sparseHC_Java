package org.dulab.jsparcehc;

import java.util.*;

public class MatrixImpl implements Matrix {

    private SortedSet<MatrixElement> elements;
    private Iterator<MatrixElement> iterator;

    private int maxRow = 0;
    private int maxCol = 0;

    float threshold;

    MatrixImpl(float threshold) {
        this.threshold = threshold;
        this.elements = new TreeSet<>(Comparator
                .comparing((MatrixElement e) -> e.value)
                .thenComparing((MatrixElement e) -> e.row)
                .thenComparing((MatrixElement e) -> e.col));
    }

    MatrixImpl() {
        this(Float.MAX_VALUE);
    }

    void add(int row, int col, float value) {
        if (value < threshold) {
            elements.add(new MatrixElement(row, col, value));
            if (row > maxRow) maxRow = row;
            if (col > maxCol) maxCol = col;
        }
    }

    @Override
    public void init() {
        iterator = elements.iterator();
    }

    @Override
    public MatrixElement getNext() {
        return iterator.hasNext() ? iterator.next() : null;
    }

    @Override
    public int getDimension() {
        return Math.max(maxRow, maxCol) + 1;
    }

    @Override
    public int getNumElements() {
        return elements.size();
    }
}
