package org.dulab.jsparsehc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SparseHierarchicalClusterer {

    final List<BinaryTreeVertex> vertices;
    final Matrix matrix;
    final Linkage linkage;
    final Dendogram dendogram;

    private int newVertexId;

    public SparseHierarchicalClusterer(Matrix m, Linkage linkage) {

        if (m.getNumElements() == 0)
            throw new IllegalStateException("Matrix is empty");

        matrix = m;
        this.linkage = linkage;
        vertices = IntStream.range(0, m.getDimension())
                .mapToObj(BinaryTreeVertex::new)
                .collect(Collectors.toList());

        dendogram = new Dendogram(m.getDimension());

        newVertexId = m.getDimension();
    }

    public BinaryTreeVertex getVertex(int id) {
        return vertices.get(id);
    }

    public Map<Integer, Integer> getLabels() {
        Map<Integer, Integer> labels = new HashMap<>();
        for (int i = 0; i < matrix.getDimension(); ++i) {
            labels.put(i, vertices.get(i).ancestor.id);
        }
//        matrix.init();
//        MatrixElement element;
//        while ((element = matrix.getNext()) != null) {
//            labels.put(element.row, vertices.get(element.row).ancestor.id);
//            labels.put(element.col, vertices.get(element.col).ancestor.id);
//        }
        return matrix.convertIndicesToIds(labels);
    }

    public Dendogram cluster(float threshold) {

        matrix.init();
        MatrixElement element;
        while ((element = matrix.getNext()) != null && element.value < threshold) {

            BinaryTreeVertex v1 = vertices.get(element.row).ancestor;
            BinaryTreeVertex v2 = vertices.get(element.col).ancestor;

            if (v1 == v2) continue;

            if (v1.id < v2.id) {
                BinaryTreeVertex v = v1;
                v1 = v2;
                v2 = v;
            }

            // v1 is always larger then v2

            if (linkage.check(v1, v2)) {

                BinaryTreeVertex v = new BinaryTreeVertex(getNewVertexId());
                vertices.add(v);
                linkage.merge(v1, v2, v, vertices);

                // For visualization purpose
                updateGraph(v1.id, v2.id, element.value);
            }
        }

        return dendogram;
    }

    public void updateGraph(int id1, int id2, float distance) {
        dendogram.add(id1, id2, distance);
    }

    public BinaryTreeVertex getVertexById(int id) {
        for (BinaryTreeVertex vertex : vertices) {
            if (vertex.id == id)
                return vertex;
        }
        return null;
    }

    protected int getNewVertexId() {
        return newVertexId++;
    }
}
