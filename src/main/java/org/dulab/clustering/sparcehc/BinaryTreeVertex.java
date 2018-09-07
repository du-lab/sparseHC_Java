package org.dulab.clustering.sparcehc;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BinaryTreeVertex {

    int id;
    BinaryTreeVertex ancestor;
    BinaryTreeVertex left;
    BinaryTreeVertex right;
    boolean isActive;

    Map<BinaryTreeVertex, Integer> edgeCounts;
    int numChildren;

    public BinaryTreeVertex(int id) {
        this.id = id;
        ancestor = this;
        left = null;
        right = null;
        isActive = true;

        numChildren = 1;
        edgeCounts = new HashMap<>();
    }

    public void updateAncestor(BinaryTreeVertex ancestor) {
        this.ancestor = ancestor;
        if (left != null)
            left.updateAncestor(ancestor);
        if (right != null)
            right.updateAncestor(ancestor);
    }

    public boolean isConnected(BinaryTreeVertex v) {
        return edgeCounts.containsKey(v);
    }

    public int getNumEdges() {
        return edgeCounts.size();
    }

    @Override
    public String toString() {
        return String.format("BinaryTreeVertex %d has %d edges: ", id, edgeCounts.size())
                + edgeCounts.entrySet()
                .stream()
                .map(e -> String.format("(%d, %d)", e.getKey().id, e.getValue()))
                .collect(Collectors.joining(", "));
    }
}
