package org.dulab.clustering.sparcehc;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class CompleteSparseHierarchicalClustererTest {

    @Test
    public void test1() {
        float[] values = new float[]{2F, 2F, 1.41F, 1F, 1F, 1F};
        int[] rows = new int[]{1, 1, 3, 0, 3, 4};
        int[] cols = new int[]{2, 3, 5, 1, 4, 5};

        MatrixImpl matrix = new MatrixImpl();

        for (int i = 0; i < values.length; ++i)
            matrix.add(rows[i], cols[i], values[i]);

        CompleteSparseHierarchicalClusterer clusterer = new CompleteSparseHierarchicalClusterer(matrix);
        clusterer.cluster(2f);

        Map<Integer, Integer> labels = clusterer.getLabels();

        assertEquals(6, labels.get(0).intValue());
        assertEquals(6, labels.get(1).intValue());
        assertEquals(2, labels.get(2).intValue());
        assertEquals(8, labels.get(3).intValue());
        assertEquals(8, labels.get(4).intValue());
        assertEquals(8, labels.get(5).intValue());
    }

    @Test
    public void test2() {

        final int numPoints = 1000;

        MatrixImpl matrix = new MatrixImpl(1F);
        for (int i = 0; i < numPoints; ++i)
            for (int j = i + 1; j < numPoints; ++j)
                matrix.add(i, j, 10F * (j - i) / numPoints);

        CompleteSparseHierarchicalClusterer clusterer = new CompleteSparseHierarchicalClusterer(matrix);
        clusterer.cluster(1F);

        Map<Integer, Integer> labels = clusterer.getLabels();

        int[] uniqueLabels = labels.values()
                .stream()
                .mapToInt(Integer::intValue)
                .distinct()
                .toArray();

        // Expect 16 clusters
        assertEquals(16, uniqueLabels.length);

        Map<Long, AtomicInteger> numClustersBySize = new HashMap<>();
        for (int label : uniqueLabels) {

            long clusterSize = labels.values()
                    .stream()
                    .filter(l -> l == label)
                    .count();

            numClustersBySize
                    .computeIfAbsent(clusterSize, size -> new AtomicInteger())
                    .incrementAndGet();
        }

        // Expect one cluster of size 40 and fifteen clusters of size 64
        assertEquals(1, numClustersBySize.get(40L).intValue());
        assertEquals(15, numClustersBySize.get(64L).intValue());
    }
}