package org.dulab.clustering.sparcehc;

/**
 * This interface is used to acquire values of the distance matrix. The values should be sorted in the ascending order.
 */
public interface Matrix {

    /**
     * Initializes a distance matrix
     */
    void init();

    /**
     * Iterates over elements of a distance matrix sorted in the ascending order by (value, row, column)
     * @return an element of the distance matrix or null if the end of the matrix is reached.
     */
    MatrixElement getNext();

    /**
     * Returns the number of elements in a sparse distance matrix
     * @return number of elements
     */
    int getNumElements();
}
