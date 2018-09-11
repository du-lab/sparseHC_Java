package org.dulab.jsparcehc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class SparseHierarchicalClusterer {

    final List<BinaryTreeVertex> vertices;
    final Matrix matrix;
    final Dendogram dendogram;

    public SparseHierarchicalClusterer(Matrix m) {

        if (m.getNumElements() == 0)
            throw new IllegalStateException("Matrix is empty");

        matrix = m;
        vertices = IntStream.range(0, m.getDimension())
                .mapToObj(BinaryTreeVertex::new)
                .collect(Collectors.toList());
        dendogram = new Dendogram(m.getDimension());
    }

    public BinaryTreeVertex getVertex(int id) {
        return vertices.get(id);
    }

    public Map<Integer, Integer> getLabels() {
        Map<Integer, Integer> labels = new HashMap<>();

        matrix.init();
        MatrixElement element;
        while ((element = matrix.getNext()) != null) {
            labels.put(element.row, vertices.get(element.row).ancestor.id);
            labels.put(element.col, vertices.get(element.col).ancestor.id);
        }
        return labels;
    }

    public abstract Dendogram cluster(float threshold);

    public void updateGraph(int id1, int id2, float distance) {
        dendogram.add(id1, id2, distance);
    }
}
