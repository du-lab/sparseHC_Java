package org.dulab.jsparsehc;

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
    Map<BinaryTreeVertex, BinaryTreeEdge> edges;
    int numChildren;

    public BinaryTreeVertex(int id) {
        this.id = id;
        ancestor = this;
        left = null;
        right = null;
        isActive = true;

        numChildren = 1;
        edgeCounts = new HashMap<>();
        edges = new HashMap<>();
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

    public int getId() {
        return id;
    }

    public BinaryTreeVertex getLeft() {
        return left;
    }

    public BinaryTreeVertex getRight() {
        return right;
    }

    public void setAncestor(BinaryTreeVertex ancestor) {
        this.ancestor = ancestor;
        if (left != null)
            left.setAncestor(ancestor);
        if (right != null)
            right.setAncestor(ancestor);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof BinaryTreeVertex)) return false;
        BinaryTreeVertex that = (BinaryTreeVertex) obj;
        return this.id == that.id;
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
