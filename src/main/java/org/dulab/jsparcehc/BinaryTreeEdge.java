package org.dulab.jsparcehc;

import java.util.*;

public class BinaryTreeEdge {

    private BinaryTreeVertex vertexI;
    private BinaryTreeVertex vertexJ;
    private final Set<MatrixElement> matrixElements;


    public BinaryTreeEdge(BinaryTreeVertex vertexI, BinaryTreeVertex vertexJ) {
        this.vertexI = vertexI;
        this.vertexJ = vertexJ;
        this.matrixElements = new HashSet<>();
    }

    public void update(Collection<MatrixElement> elements) {
        matrixElements.addAll(elements);
    }

    public void update(MatrixElement element) {
        update(Collections.singleton(element));
    }

    public void setVertexI(BinaryTreeVertex vertexI) {
        this.vertexI = vertexI;
    }

    public void setVertexJ(BinaryTreeVertex vertexJ) {
        this.vertexJ = vertexJ;
    }

    public BinaryTreeVertex getVertexI() {
        return vertexI;
    }

    public BinaryTreeVertex getVertexJ() {
        return vertexJ;
    }

    public Set<MatrixElement> getMatrixElements() {
        return matrixElements;
    }

    public void replaceVertex(BinaryTreeVertex from, BinaryTreeVertex to) {
        if (vertexI.equals(from))
            vertexI = to;
        else if (vertexJ.equals(from))
            vertexJ = to;
        else
            throw new IllegalStateException("No matching vertex found for " + from);
    }
}
