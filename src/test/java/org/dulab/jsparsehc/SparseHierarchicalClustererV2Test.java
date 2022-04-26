package org.dulab.jsparsehc;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class SparseHierarchicalClustererV2Test {

    private final MatrixImpl matrix;

    public SparseHierarchicalClustererV2Test() throws IOException, CsvValidationException {
        // Read data
        List<double[]> data = new ArrayList<>();
        InputStream stream = this.getClass().getResourceAsStream("rand-data.csv");
        try(CSVReader reader = new CSVReader(new InputStreamReader(stream))) {
            String[] header = reader.readNext();
            String[] values;
            int count = 0;
            while ((values = reader.readNext()) != null && count < 10) {
                double[] xs = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
                data.add(xs);
                ++count;
            }
        }

        // Calculate distances
        matrix = new MatrixImpl();
        for (int i = 0; i < data.size(); ++i) {
            double[] vector1 = data.get(i);
            for (int j = i + 1; j < data.size(); ++j) {
                double[] vector2 = data.get(j);
                double distance =  Math.sqrt(IntStream.range(0, 2)
                        .mapToDouble(k -> vector1[k] - vector2[k])
                        .map(x -> x * x)
                        .sum());
                matrix.add(i, j, new Double(distance).floatValue());
            }
        }
    }

    @Test
    public void testCompleteLinkage() {
        SparseHierarchicalClustererV2 clusterer = new SparseHierarchicalClustererV2(matrix, new CompleteLinkage());
        assertNumClusters(1, clusterer, Float.MAX_VALUE);
        assertNumClusters(2, clusterer,0.8F);
        assertNumClusters(3, clusterer, 0.65F);
        assertNumClusters(4, clusterer, 0.5F);
        assertNumClusters(5, clusterer, 0.4F);
    }

    @Test
    public void testSingleLinkage() {
        SparseHierarchicalClustererV2 clusterer = new SparseHierarchicalClustererV2(matrix, new SingleLinkage());
        assertNumClusters(1, clusterer, Float.MAX_VALUE);
        assertNumClusters(2, clusterer, 0.4F);
        assertNumClusters(3, clusterer, 0.37F);
        assertNumClusters(4, clusterer, 0.3F);
        assertNumClusters(5, clusterer, 0.27F);
    }

    private void assertNumClusters(int expectedNumClusters, SparseHierarchicalClustererV2 clusterer, float threshold) {
        clusterer.cluster(threshold);
        Map<Integer, Integer> labelMap = clusterer.getLabels();
        Set<Integer> labelSet = new HashSet<>(labelMap.values());
        assertEquals(expectedNumClusters, labelSet.size());
    }
}