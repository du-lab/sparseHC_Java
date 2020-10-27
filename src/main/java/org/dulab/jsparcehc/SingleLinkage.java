package org.dulab.jsparcehc;

import java.util.List;

public class SingleLinkage implements Linkage {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check(BinaryTreeVertex v1, BinaryTreeVertex v2) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(BinaryTreeVertex v1, BinaryTreeVertex v2, BinaryTreeVertex v, List<BinaryTreeVertex> vertices) {

        v.left = v1;
        v.right = v2;

        v1.updateAncestor(v);
        v2.updateAncestor(v);
    }

    @Override
    public float computeDij(BinaryTreeEdge edge, float dxy) {
        return checkComplete(edge) ? dxy : Float.MAX_VALUE;
    }

    @Override
    public boolean checkComplete(BinaryTreeEdge edge) {
        return edge.getNumberOfDistances() == 1;
    }
}
