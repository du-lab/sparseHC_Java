package org.dulab.jsparcehc;

import java.util.*;

public class MatrixImpl implements Matrix {

    private final SortedSet<MatrixElement> elements;
    private final float threshold;
    private final Integer dimension;

    private Iterator<MatrixElement> iterator;
    private int maxRow = 0;
    private int maxCol = 0;

    public MatrixImpl(float threshold, Integer dimension) {
        this.threshold = threshold;
        this.dimension = dimension;
        this.elements = new TreeSet<>(Comparator
                .comparing((MatrixElement e) -> e.value)
                .thenComparing((MatrixElement e) -> e.row)
                .thenComparing((MatrixElement e) -> e.col));
    }

    public MatrixImpl(float threshold) {
        this(threshold, null);
    }

    public MatrixImpl() {
        this(Float.MAX_VALUE);
    }

    public void add(int row, int col, float value) {
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
        return dimension != null ? dimension : Math.max(maxRow, maxCol) + 1;
    }

    @Override
    public int getNumElements() {
        return elements.size();
    }
}
