package org.dulab.jsparcehc;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Launcher {

    private static final String[] OUTPUT_HEADER = new String[]{"Id", "Label"};


    /**
     * Reads CSV file and returns a matrix with distance values
     *
     * @param file      input file
     * @return distance matrix
     */
    private static Matrix readFile(File file) {

        MatrixImpl distanceMatrix = new MatrixImpl();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] header = reader.readNext();
            if (header.length < 3)
                throw new IOException("File must contain at least 3 columns");

            String[] values;
            while ((values = reader.readNext()) != null) {
                int rowId = Integer.parseInt(values[0]);
                int columnId = Integer.parseInt(values[1]);
                float distance = Float.parseFloat(values[2]);
                distanceMatrix.add(rowId, columnId, distance);
            }

        } catch (IOException | CsvValidationException | NumberFormatException e) {
            throw new IllegalStateException(String.format("Reading file %s failed: %s",
                    file, e.getMessage()), e);
        }

        return distanceMatrix;
    }

    /**
     * Saves the clustering labels into a file
     *
     * @param file   output file
     * @param labels labels in format (Sample ID, Label ID)
     */
    private static void saveLabels(File file, Map<Integer, Integer> labels) {
        SortedMap<Integer, Integer> sortedLabels = new TreeMap<>(labels);

        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            writer.writeNext(OUTPUT_HEADER);
            for (Map.Entry<Integer, Integer> e : sortedLabels.entrySet()) {
                String id = e.getKey().toString();
                String label = e.getValue().toString();
                writer.writeNext(new String[]{id, label}, false);
            }

        } catch (IOException e) {
            throw new IllegalStateException(String.format("Writing to file %s failed: %s",
                    file, e.getMessage()), e);
        }
    }

    /**
     * Reads distance from the input file, performs the complete-linkage hierarchical clustering, and writes labels into the output file
     *
     * @param threshold  clustering threshold
     * @param inputFile  input file
     * @param outputFile output file
     */
    private static void cluster(float threshold, File inputFile, File outputFile) {

        Matrix distanceMatrix = readFile(inputFile);
        SparseHierarchicalClustererV2 clusterer = new SparseHierarchicalClustererV2(distanceMatrix, new CompleteLinkage());
        clusterer.cluster(threshold);
        Map<Integer, Integer> idToLabelMap = clusterer.getLabels();
        saveLabels(outputFile, idToLabelMap);
    }

    public static void main(String[] args) {

        Options options = new Options();
        options.addRequiredOption("t", "threshold", true, "Value of the clustering threshold");
        options.addRequiredOption("i", "input", true, "Input CSV file with distances");
        options.addRequiredOption("o", "output", true, "Output CSV file with cluster labels");

        CommandLineParser parser = new DefaultParser();
        File inputFile;
        File outputFile;
        float threshold;
        try {
            CommandLine commandLine = parser.parse(options, args);
            inputFile = new File(commandLine.getOptionValue("input"));
            if (!inputFile.exists())
                throw new IOException(String.format("File %s not found", inputFile.getAbsolutePath()));
            outputFile = new File(commandLine.getOptionValue("output"));
            threshold = Float.parseFloat(commandLine.getOptionValue("threshold"));

        } catch (ParseException | IOException | NumberFormatException e) {
            System.err.println("Parsing arguments failed: " + e.getMessage());
            new HelpFormatter().printHelp("jsparsehc", options);
            return;
        }

        cluster(threshold, inputFile, outputFile);
    }
}
