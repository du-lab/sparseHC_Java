package org.dulab.clustering.sparcehc;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CompleteSparseHierarchicalClustererTest {

    @Test
    public void test() {
        float[] values = new float[]{2F, 2F, 1.41F, 1F, 1F, 1F};
        int[] rows = new int[]{1, 1, 3, 0, 3, 4};
        int[] cols = new int[]{2, 3, 5, 1, 4, 5};

        Matrix matrix = new Matrix();

        for (int i = 0; i < values.length; ++i)
            matrix.add(rows[i], cols[i], values[i]);

        CompleteSparseHierarchicalClusterer clusterer = new CompleteSparseHierarchicalClusterer(matrix);
        clusterer.cluster();

        Map<Integer, Integer> labels = clusterer.getLabels();

        assertEquals(6, labels.get(0).intValue());
        assertEquals(6, labels.get(1).intValue());
        assertEquals(2, labels.get(2).intValue());
        assertEquals(8, labels.get(3).intValue());
        assertEquals(8, labels.get(4).intValue());
        assertEquals(8, labels.get(5).intValue());
    }
}