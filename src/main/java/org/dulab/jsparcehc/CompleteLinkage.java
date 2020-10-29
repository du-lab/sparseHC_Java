package org.dulab.jsparcehc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompleteLinkage implements Linkage {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check(BinaryTreeVertex v1, BinaryTreeVertex v2) {

        int numEdges = 1 + v1.edgeCounts.getOrDefault(v2, 0);
        v1.edgeCounts.put(v2, numEdges);

        return numEdges == v1.numChildren * v2.numChildren;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(BinaryTreeVertex v1, BinaryTreeVertex v2, BinaryTreeVertex v, List<BinaryTreeVertex> vertices) {

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
    }

    @Override
    public float computeDij(BinaryTreeEdge edge, float dxy) {
        return checkComplete(edge) ? dxy : Float.MAX_VALUE;
    }

    @Override
    public boolean checkComplete(BinaryTreeEdge edge) {
        return edge.getMatrixElements().size() == edge.getVertexI().numChildren * edge.getVertexJ().numChildren;
    }

    @Override
    public float calculateDistance(BinaryTreeEdge edge) {

        if (edge.getMatrixElements().isEmpty())
            throw new IllegalStateException("Cannot calculate distance of an empty edge");

        if (!checkComplete(edge)) return Float.MAX_VALUE;

        float maxDistance = 0F;
        for (MatrixElement e : edge.getMatrixElements())
            if (e.value > maxDistance)
                maxDistance = e.value;
        return maxDistance;
    }
}
