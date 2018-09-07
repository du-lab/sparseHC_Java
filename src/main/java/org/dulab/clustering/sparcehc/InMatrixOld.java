package org.dulab.clustering.sparcehc;

public class InMatrixOld extends Matrix {
//    int pos = 0;
//
//    InMatrixOld(float threshold) {
//        this.threshold = threshold;
//    }
//
//    InMatrixOld() {
//        this(Float.MAX_VALUE);
//    }
//
//    void add(int row, int col, int value) {
//        add(new MatrixElement(row, col, value));
//    }
//
//    void add(MatrixElement e) {
//        threshold = threshold < e.value ? e.value : threshold;
//        elements.add(e);
//    }
//
//    void sort() {
//        elements.sort((e1, e2) -> e1.lessThan(e2) ? -1 : 1);
//        numPoints = (int) (1L + Math.sqrt(1L + elements.size() * 8L)) / 2;
//    }
//
//    int getNumElements() {
//        return elements.size();
//    }
//
//    boolean isEmpty() {
//        return (pos >= elementsCount() && elements.isEmpty());
//    }
//
//    MatrixElement getNext() {
//        if (pos < elements.size())
//            return elements.get(pos++);
//        else
//            return null;
//    }
}
