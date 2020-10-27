package org.dulab.jsparcehc;

public class BinaryTreeEdge {

    private BinaryTreeVertex vertexI;
    private BinaryTreeVertex vertexJ;
    private int numberOfDistances;
    private float sumOfDistances;
//    private boolean complete;
    private float distance;

    public BinaryTreeEdge(BinaryTreeVertex vertexI, BinaryTreeVertex vertexJ) {
        this.vertexI = vertexI;
        this.vertexJ = vertexJ;
        this.numberOfDistances = 0;
        this.sumOfDistances = 0F;
//        this.complete = false;
    }

    public void update(int n, float distance, Linkage linkage) {
        numberOfDistances += n;
        sumOfDistances += distance;
        distance = linkage.computeDij(this, distance);
//        complete = numberOfDistances == vertexI.numChildren * vertexJ.numChildren;
    }

    public void update(float distance, Linkage linkage) {
        update(1, distance, linkage);
    }

//    public boolean isComplete() {
//        return complete;
//    }

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

    public float getDistance() {
        return distance;
    }

    public int getNumberOfDistances() {
        return numberOfDistances;
    }

    public float getSumOfDistances() {
        return sumOfDistances;
    }
}
