package org.dulab.jsparcehc;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This clustering algorithm is based on the paper
 *
 * Thuy-Diem Nguyen, Bertil Schmidt, Chee-Keong Kwoh,
 * SparseHC: A Memory-efficient Online Hierarchical Clustering Algorithm,
 * Procedia Computer Science,
 * Volume 29,
 * 2014,
 * Pages 8-19,
 */
public class SparseHierarchicalClustererV2 {

    final Matrix matrix;
    final Linkage linkage;

    private List<BinaryTreeVertex> vertices;
    private int newVertexId;
    private Dendogram dendogram;

    private final Set<BinaryTreeEdge> completeEdges = new HashSet<>();
    private final Set<BinaryTreeEdge> incompleteEdges = new HashSet<>();

    public SparseHierarchicalClustererV2(Matrix m, Linkage linkage) {
        if (m.getNumElements() == 0)
            throw new IllegalStateException("Matrix is empty");
        this.matrix = m;
        this.linkage = linkage;
    }

    public BinaryTreeVertex getVertex(int id) {
        return vertices.get(id);
    }

    public Map<Integer, Integer> getLabels() {
        Map<Integer, Integer> labels = new HashMap<>();
        for (int i = 0; i < matrix.getDimension(); ++i) {
            labels.put(i, vertices.get(i).ancestor.id);
        }
        return labels;
    }

    public Dendogram cluster(float threshold) {

        // Initialize
        matrix.init();
        vertices = IntStream.range(0, matrix.getDimension())
                .mapToObj(BinaryTreeVertex::new)
               .collect(Collectors.toList());
        dendogram = new Dendogram(matrix.getDimension());
        newVertexId = matrix.getDimension();

        int verticesMaxSize = 2 * matrix.getDimension() - 1;

        // Read distance values
        MatrixElement element;
        while ((element = matrix.getNext()) != null && element.value < threshold && vertices.size() <= verticesMaxSize) {
            float dxy = element.value;
            BinaryTreeVertex ci = vertices.get(element.row).ancestor;
            BinaryTreeVertex cj = vertices.get(element.col).ancestor;

            if (ci == cj) continue;

            BinaryTreeEdge eij = ci.edges.get(cj);

            if (eij == null) {
                eij = new BinaryTreeEdge(ci, cj);
                ci.edges.put(cj, eij);
                cj.edges.put(ci, eij);
            }

            eij.update(element);
            updateLists(eij);

            while ((eij = findMinimumEdge(completeEdges)) != null
                    && (linkage.calculateDistance(eij) <= findMinimum(incompleteEdges))) {
                BinaryTreeVertex vertexK = merge(newVertexId++, eij);
                vertices.add(vertexK);
                removeFromLists(eij);
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

    private BinaryTreeEdge findMinimumEdge(Set<BinaryTreeEdge> edges) {
        BinaryTreeEdge minimumEdge = null;
        float minimumDistance = Float.MAX_VALUE;
        for (BinaryTreeEdge edge : edges) {
            float distance = linkage.calculateDistance(edge);
            if (distance < minimumDistance) {
                minimumDistance = distance;
                minimumEdge = edge;
            }
        }
        return minimumEdge;
    }

    private float findMinimum(Set<BinaryTreeEdge> edges) {
        BinaryTreeEdge edge = findMinimumEdge(edges);
        return edge != null ? linkage.calculateDistance(edge) : Float.MAX_VALUE;
    }

    private BinaryTreeVertex merge(int id, BinaryTreeEdge eij) {
        BinaryTreeVertex vertexI = eij.getVertexI();
        BinaryTreeVertex vertexJ = eij.getVertexJ();
        BinaryTreeVertex vertexK = new BinaryTreeVertex(id);
        vertexK.left = vertexI;
        vertexK.right = vertexJ;
        vertexK.numChildren = vertexI.numChildren + vertexJ.numChildren;

        vertexI.setAncestor(vertexK);
        vertexI.edges.remove(vertexJ);
        vertexJ.setAncestor(vertexK);
        vertexJ.edges.remove(vertexI);
        mergeEdges(vertexK, eij);

        return vertexK;
    }

    private void mergeEdges(BinaryTreeVertex vertexK, BinaryTreeEdge eij) {

        Map<BinaryTreeVertex, BinaryTreeEdge> edgesI = eij.getVertexI().edges;
        Map<BinaryTreeVertex, BinaryTreeEdge> edgesJ = eij.getVertexJ().edges;

        Set<BinaryTreeVertex> vertices = new HashSet<>(edgesI.keySet());
        vertices.addAll(edgesJ.keySet());
        for (BinaryTreeVertex vertexM : vertices) {
            BinaryTreeEdge eim = edgesI.get(vertexM);
            BinaryTreeEdge ejm = edgesJ.get(vertexM);

            if (eim != null && ejm != null) {
                BinaryTreeEdge ekm = new BinaryTreeEdge(vertexM, vertexK);
                Set<MatrixElement> elements = new HashSet<>(eim.getMatrixElements());
                elements.addAll(ejm.getMatrixElements());
                ekm.update(elements);
                vertexK.edges.put(vertexM, ekm);
                vertexM.edges.put(vertexK, ekm);
                vertexM.edges.remove(eij.getVertexI());
                vertexM.edges.remove(eij.getVertexJ());
                edgesI.remove(vertexM);
                edgesJ.remove(vertexM);

                updateLists(ekm);
                removeFromLists(eim, ejm);

            } else if (eim != null) {
                vertexK.edges.put(vertexM, eim);
                vertexM.edges.put(vertexK, eim);
                vertexM.edges.remove(eij.getVertexI());
                edgesI.remove(vertexM);
                eim.replaceVertex(eij.getVertexI(), vertexK);
                updateLists(eim);

            } else if (ejm != null) {
                vertexK.edges.put(vertexM, ejm);
                vertexM.edges.put(vertexK, ejm);
                vertexM.edges.remove(eij.getVertexJ());
                edgesJ.remove(vertexM);
                ejm.replaceVertex(eij.getVertexJ(), vertexK);
                updateLists(ejm);
            }
        }
    }

    private void removeFromLists(BinaryTreeEdge... edges) {
        for (BinaryTreeEdge edge : edges) {
            completeEdges.remove(edge);
            incompleteEdges.remove(edge);
        }
    }

    private void updateLists(BinaryTreeEdge... edges) {
        for (BinaryTreeEdge edge : edges) {
            if (linkage.checkComplete(edge)) {
                completeEdges.add(edge);
                incompleteEdges.remove(edge);
            } else {
                completeEdges.remove(edge);
                incompleteEdges.add(edge);
            }
        }
    }
}
