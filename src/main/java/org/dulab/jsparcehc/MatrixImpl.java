package org.dulab.jsparcehc;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MatrixImpl implements Matrix {

    private final static MatrixElementComparator COMPARATOR = new MatrixElementComparator();

    private final SortedSet<MatrixElement> elements;
    private final Map<Integer, Integer> idToIndexMap = new HashMap<>();
    private final AtomicInteger index = new AtomicInteger(0);
    private final float threshold;
    private final Integer dimension;

    private Iterator<MatrixElement> iterator;

    public MatrixImpl(float threshold, Integer dimension) {
        this.threshold = threshold;
        this.dimension = dimension;
        this.elements = new TreeSet<>(COMPARATOR);
    }

    public MatrixImpl(float threshold) {
        this(threshold, null);
    }

    public MatrixImpl() {
        this(Float.MAX_VALUE);
    }

    public void add(int row, int col, float value) {
        if (value < threshold) {
            int rowIndex = idToIndexMap.computeIfAbsent(row, k -> index.getAndIncrement());
            int columnIndex = idToIndexMap.computeIfAbsent(col, k -> index.getAndIncrement());
            elements.add(new MatrixElement(rowIndex, columnIndex, value));
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
        return dimension != null ? dimension : index.get();
    }

    @Override
    public int getNumElements() {
        return elements.size();
    }

    @Override
    public Map<Integer, Integer> convertIndicesToIds(Map<Integer, Integer> indexToLabelMap) {
        return idToIndexMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> indexToLabelMap.get(e.getValue())));
    }
}
