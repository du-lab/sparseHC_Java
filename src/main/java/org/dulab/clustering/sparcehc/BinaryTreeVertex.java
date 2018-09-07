package org.dulab.clustering.sparcehc;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class BinaryTreeVertex {

    int id;
    BinaryTreeVertex ancestor;
    BinaryTreeVertex left;
    BinaryTreeVertex right;
    boolean isActive;

    Map<BinaryTreeVertex, Integer> outEdges;
    int numChildren;

    public BinaryTreeVertex(int id) {
        this.id = id;
        ancestor = this;
        left = null;
        right = null;
        isActive = true;
    }

    public void updateAncestor(BinaryTreeVertex ancestor) {
        this.ancestor = ancestor;
        if (left != null)
            left.updateAncestor(ancestor);
        if (right != null)
            right.updateAncestor(ancestor);
    }

    public abstract boolean isConnected(BinaryTreeVertex v);

    public abstract int getNumEdges();

    public abstract void printOutEdges();

    @Override
    public String toString() {
        return String.format("BinaryTreeVertex %d has %d edges: ", id, outEdges.size())
                + outEdges.entrySet()
                .stream()
                .map(e -> String.format("(%d, %d)", e.getKey().id, e.getValue()))
                .collect(Collectors.joining(", "));
    }
}
