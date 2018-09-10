package org.dulab.jsparcehc;

import java.util.ArrayList;
import java.util.List;

public class Dendogram {

    public static class Link {
        int p1, p2;
        float distance;
        int nump;
    }

    private final int numPoints;
    private List<Link> links;
    private List<Integer> pointsCount;  // number of points in the cluster

    public Dendogram(int numPoints) {
        this.numPoints = numPoints;
        links = new ArrayList<Link>(numPoints - 1);
        pointsCount = new ArrayList<Integer>(numPoints - 1);
    }

    public void add(int p1, int p2, float distance) {
        Link l = new Link();
        l.p1 = Math.min(p1, p2);
        l.p2 = Math.max(p1, p2);
        l.distance = distance;
        l.nump = 0;
        l.nump += p1 >= numPoints ? pointsCount.get(p1 - numPoints) : 1;
        l.nump += p2 >= numPoints ? pointsCount.get(p2 - numPoints) : 1;
        pointsCount.add(l.nump);
        links.add(l);
    }

    public Link getLink(int i) {
        return links.get(i);
    }

    public void print() {
        for (Link l : links)
            System.out.println(String.format("%d %d %f %d", l.p1, l.p2, l.distance, l.nump));
    }
}
