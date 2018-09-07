package org.dulab.clustering.sparcehc;

import java.util.ArrayList;
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
        matrix = m;
        vertices = IntStream.range(0, m.getNumElements())
                .mapToObj(this::createVertex)
                .collect(Collectors.toList());
        dendogram = new Dendogram(m.getNumElements());
    }

    public BinaryTreeVertex getVertex(int id) {
        return vertices.get(id);
    }

    public Map<Integer, Integer> getLabels() {
        Map<Integer, Integer> labels = new HashMap<>();
        for (MatrixElement element : matrix.getElements()) {
            labels.put(element.row, vertices.get(element.row).ancestor.id);
            labels.put(element.col, vertices.get(element.col).ancestor.id);
        }
        return labels;
    }

    public abstract BinaryTreeVertex createVertex(int id);

    public abstract Dendogram cluster();

    public void updateGraph(int id1, int id2, float distance) {
        dendogram.add(id1, id2, distance);
    }
}
