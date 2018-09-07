package org.dulab.clustering.sparcehc;

import java.util.HashMap;
import java.util.Map;

public class CompleteSparseHierarchicalClusterer extends SparseHierarchicalClusterer {

    int totalEdges;

    CompleteSparseHierarchicalClusterer(Matrix m) {
        super(m);
        totalEdges = 0;
    }

    public BinaryTreeVertex createVertex(int id) {
        return new CompleteBinaryTreeVertex(id);
    }

    public Dendogram cluster() {

        int maxEdges = matrix.getNumElements();
        int newId = matrix.getNumElements();

        for (MatrixElement element : matrix.getElements()) {

            if (element.value >= matrix.threshold) break;

            CompleteBinaryTreeVertex v1 = (CompleteBinaryTreeVertex) vertices.get(element.row).ancestor;
            CompleteBinaryTreeVertex v2 = (CompleteBinaryTreeVertex) vertices.get(element.col).ancestor;

            if (v1 == v2)
                return dendogram;

            if (v1.id < v2.id) {
                CompleteBinaryTreeVertex v = v1;
                v1 = v2;
                v2 = v;
            }

            // v1 is always larger then v2
            // Links are stored in v1

            int weight = 1 + v1.outEdges.getOrDefault(v2, 0);
            v1.outEdges.put(v2, weight);

            totalEdges += (weight == 1) ? 1 : 0;

            if (weight == v1.numChildren * v2.numChildren) {

                CompleteBinaryTreeVertex v = (CompleteBinaryTreeVertex) createVertex(newId);
                vertices.add(v);
                merge(v1, v2, v);
                ++newId;

                // For visualization purpose
                updateGraph(v1.id, v2.id, element.value);
            }

            if (totalEdges > maxEdges)
                maxEdges *= 1.1F;
        }

        return dendogram;
    }

    void merge(CompleteBinaryTreeVertex v1, CompleteBinaryTreeVertex v2, CompleteBinaryTreeVertex v) {

        // Set child vertices for the parent TreeNode
        v.left = v1;
        v.right = v2;

        // Set parent TreeNode for the child vertices
        v1.updateAncestor(v);
        v2.updateAncestor(v);

        // Set distance between two child vertices
        v.numChildren = v1.numChildren + v2.numChildren;

        // Clear the connection between v1 and v2
        v1.outEdges.remove(v2);

        // Pass the outEdges from v1 to the parentNode
        v.outEdges = new HashMap<>(v1.outEdges);

        // For each item in the outEdges of v2
        for (Map.Entry<BinaryTreeVertex, Integer> e : v2.outEdges.entrySet())
            v.outEdges.put(e.getKey(), v.outEdges.getOrDefault(e.getKey(), 0) + e.getValue());

        v1.outEdges.clear();
        v2.outEdges.clear();

        v1.isActive = false;
        v2.isActive = false;

        // For vertices with larger ID then v2 that hold the outEdges to v2
        for (int i = v2.id + 1; i < v1.id; ++i) {
            CompleteBinaryTreeVertex vertex = (CompleteBinaryTreeVertex) vertices.get(i);
            if (vertex.isActive && vertex.isConnected(v2)) {
                v.outEdges.put(vertex, v.outEdges.getOrDefault(vertex, 0) + vertex.outEdges.get(v2));
                vertex.outEdges.remove(v2);
            }
        }

        // For vertices with larger ID then v1 that hold the links to v1 or v2
        for (int i = v1.id + 1; i < v.id; ++i) {
            CompleteBinaryTreeVertex vertex = (CompleteBinaryTreeVertex) vertices.get(i);

            if (vertex.isActive && vertex.isConnected(v1)) {
                v.outEdges.put(vertex, v.outEdges.getOrDefault(vertex, 0) + vertex.outEdges.get(v1));
                vertex.outEdges.remove(v1);
            }

            if (vertex.isActive && vertex.isConnected(v2)) {
                v.outEdges.put(vertex, v.outEdges.getOrDefault(vertex, 0) + vertex.outEdges.get(v2));
                vertex.outEdges.remove(v2);
            }
        }

        totalEdges = getNumEdges();
    }

    int getNumEdges() {
        return vertices.stream()
                .filter(v -> v.isActive)
                .mapToInt(v -> ((CompleteBinaryTreeVertex) v).outEdges.size())
                .sum();
    }

    void printAllEdges() {
        for (BinaryTreeVertex v : vertices)
            if (v.isActive)
                v.printOutEdges();
    }
}
