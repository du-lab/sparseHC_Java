package org.dulab.clustering.sparcehc;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CompleteBinaryTreeVertex extends BinaryTreeVertex {



    public CompleteBinaryTreeVertex(int id) {
        super(id);
        numChildren = 1;
        outEdges = new HashMap<>();
    }

    public boolean isConnected(BinaryTreeVertex v) {
        return outEdges.containsKey(v);
    }

    public int getNumEdges() {
        return outEdges.size();
    }

    public void printOutEdges() {
        System.out.print(String.format("BinaryTreeVertex %d has %d edges: ", id, outEdges.size()));
        for (Map.Entry<BinaryTreeVertex, Integer> e : outEdges.entrySet())
            System.out.print(String.format("(%d, %d)", e.getKey().id, e.getValue()));
        System.out.println();
    }


}
