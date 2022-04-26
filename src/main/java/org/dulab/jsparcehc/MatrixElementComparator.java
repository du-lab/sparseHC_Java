package org.dulab.jsparcehc;

import java.util.Comparator;

public class MatrixElementComparator implements Comparator<MatrixElement> {

    @Override
    public int compare(MatrixElement e1, MatrixElement e2) {
        int compare = Float.compare(e1.value, e2.value);
        if (compare == 0)
            compare = Integer.compare(e1.row, e2.row);
        if (compare == 0)
            compare = Integer.compare(e1.col, e2.col);
        return compare;
    }
}
