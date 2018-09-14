package org.dulab.jsparcehc;

import java.util.List;

public interface Linkage {

    /**
     * Checks if two vertices should be merged
     * @param v1 first vertex
     * @param v2 second vertex
     * @return true if the vertices should be merged, otherwise false
     */
    boolean check(BinaryTreeVertex v1, BinaryTreeVertex v2);

    /**
     * Performs merging of vertices v1 and v2 into vertex v
     * @param v1 first vertex to be merged
     * @param v2 second vertex to be merged
     * @param v result of merging vertices v1 and v2
     * @param vertices list of all vertices
     */
    void merge(BinaryTreeVertex v1, BinaryTreeVertex v2, BinaryTreeVertex v, List<BinaryTreeVertex> vertices);
}
