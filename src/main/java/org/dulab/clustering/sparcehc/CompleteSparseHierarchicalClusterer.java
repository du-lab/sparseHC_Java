package org.dulab.clustering.sparcehc;

import java.util.HashMap;
import java.util.Map;

public class CompleteSparseHierarchicalClusterer extends SparseHierarchicalClusterer {

    private int totalNumEdges;

    CompleteSparseHierarchicalClusterer(Matrix m) {
        super(m);
        totalNumEdges = 0;
    }

    public Dendogram cluster(float threshold) {

        int newId = matrix.getNumElements();

        matrix.init();
        MatrixElement element;
        while ((element = matrix.getNext()) != null && element.value < threshold) {

            BinaryTreeVertex v1 = vertices.get(element.row).ancestor;
            BinaryTreeVertex v2 = vertices.get(element.col).ancestor;

            if (v1 == v2) break;

            if (v1.id < v2.id) {
                BinaryTreeVertex v = v1;
                v1 = v2;
                v2 = v;
            }

            // v1 is always larger then v2
            // Links are stored in v1

            int numEdges = 1 + v1.edgeCounts.getOrDefault(v2, 0);
            v1.edgeCounts.put(v2, numEdges);

            totalNumEdges += (numEdges == 1) ? 1 : 0;

            if (numEdges == v1.numChildren * v2.numChildren) {

                BinaryTreeVertex v = new BinaryTreeVertex(newId);
                vertices.add(v);
                merge(v1, v2, v);
                ++newId;

                // For visualization purpose
                updateGraph(v1.id, v2.id, element.value);
            }

//            if (totalNumEdges > maxEdges)
//                maxEdges *= 1.1F;
        }

        return dendogram;
    }

    void merge(BinaryTreeVertex v1, BinaryTreeVertex v2, BinaryTreeVertex v) {

        // Set child vertices for the parent TreeNode
        v.left = v1;
        v.right = v2;

        // Set parent TreeNode for the child vertices
        v1.updateAncestor(v);
        v2.updateAncestor(v);

        // Set number of children
        v.numChildren = v1.numChildren + v2.numChildren;

        // Clear the connection between v1 and v2
        v1.edgeCounts.remove(v2);

        // Pass the edgeCounts from v1 to the parentNode
        v.edgeCounts = new HashMap<>(v1.edgeCounts);

        // For each item in the edgeCounts of v2
        for (Map.Entry<BinaryTreeVertex, Integer> e : v2.edgeCounts.entrySet())
            v.edgeCounts.put(e.getKey(), v.edgeCounts.getOrDefault(e.getKey(), 0) + e.getValue());

        v1.edgeCounts.clear();
        v2.edgeCounts.clear();

        v1.isActive = false;
        v2.isActive = false;

        // For vertices with larger ID then v2 that hold the edgeCounts to v2
        for (int i = v2.id + 1; i < v1.id; ++i) {
            BinaryTreeVertex vertex = vertices.get(i);
            if (vertex.isActive && vertex.isConnected(v2)) {
                v.edgeCounts.put(vertex, v.edgeCounts.getOrDefault(vertex, 0) + vertex.edgeCounts.get(v2));
                vertex.edgeCounts.remove(v2);
            }
        }

        // For vertices with larger ID then v1 that hold the links to v1 or v2
        for (int i = v1.id + 1; i < v.id; ++i) {
            BinaryTreeVertex vertex = vertices.get(i);

            if (vertex.isActive && vertex.isConnected(v1)) {
                v.edgeCounts.put(vertex, v.edgeCounts.getOrDefault(vertex, 0) + vertex.edgeCounts.get(v1));
                vertex.edgeCounts.remove(v1);
            }

            if (vertex.isActive && vertex.isConnected(v2)) {
                v.edgeCounts.put(vertex, v.edgeCounts.getOrDefault(vertex, 0) + vertex.edgeCounts.get(v2));
                vertex.edgeCounts.remove(v2);
            }
        }

        totalNumEdges = getNumEdges();
    }

    private int getNumEdges() {
        return vertices.stream()
                .filter(v -> v.isActive)
                .mapToInt(v -> v.edgeCounts.size())
                .sum();
    }
}
