package org.dulab.jsparcehc;

import java.util.Map;

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
     * Returns the number of rows (columns) of the matrix
     * @return number of rows(columns)
     */
    int getDimension();

    /**
     * Returns the number of elements in a sparse distance matrix
     * @return number of elements
     */
    int getNumElements();

    /**
     * Converts indices to IDs
     * @param indexToLabelMap mapping from indices to labels
     * @return mapping from IDs to labels
     */
    Map<Integer, Integer> convertIndicesToIds(Map<Integer, Integer> indexToLabelMap);
}
