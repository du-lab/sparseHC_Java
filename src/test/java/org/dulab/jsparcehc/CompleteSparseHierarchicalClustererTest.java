package org.dulab.jsparcehc;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

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


        SparseHierarchicalClusterer clusterer = new SparseHierarchicalClusterer(matrix, new CompleteLinkage());
        clusterer.cluster(2f);

        Map<Integer, Integer> labels = clusterer.getLabels();

        assertEquals(6, labels.get(0).intValue());
        assertEquals(6, labels.get(1).intValue());
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

        SparseHierarchicalClusterer clusterer = new SparseHierarchicalClusterer(matrix, new CompleteLinkage());
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

    @Test
    public void test3() throws IOException, CsvValidationException {

        // Read data
        List<double[]> data = new ArrayList<>();
        InputStream stream = this.getClass().getResourceAsStream("iris-data.csv");
        try(CSVReader reader = new CSVReader(new InputStreamReader(stream))) {
            String[] header = reader.readNext();
            String[] values;
            int count = 0;
            while ((values = reader.readNext()) != null && count < 10) {
                double[] xs = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
                System.out.printf("%f %f %f %f\n", xs[0], xs[1], xs[2], xs[3]);
                data.add(xs);
                ++count;
            }
        }

        // Calculate distances
        MatrixImpl matrix = new MatrixImpl();
        for (int i = 0; i < data.size(); ++i) {
            double[] vector1 = data.get(i);
            for (int j = i + 1; j < data.size(); ++j) {
                double[] vector2 = data.get(j);
                double distance =  Math.sqrt(IntStream.range(0, 4)
                        .mapToDouble(k -> vector1[k] - vector2[k])
                        .map(x -> x * x)
                        .sum());
                matrix.add(i, j, new Double(distance).floatValue());
            }
        }

        // Cluster
        SparseHierarchicalClusterer clusterer = new SparseHierarchicalClusterer(matrix, new CompleteLinkage());
        clusterer.cluster(Float.MAX_VALUE);

        Map<Integer, Integer> labels = clusterer.getLabels();
        for (Map.Entry<Integer, Integer> e : labels.entrySet())
            assertEquals(18, e.getValue().intValue());

        BinaryTreeVertex vertex = clusterer.getVertex(18);
        System.out.println(vertex);

    }
}