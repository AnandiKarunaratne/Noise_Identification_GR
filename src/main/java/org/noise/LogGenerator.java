package org.noise;

import org.jbpt.graph.DirectedGraph;
import org.jbpt.hypergraph.abs.Vertex;
import org.noise.log.EventLog;
import org.noise.log.Trace;

import java.util.*;

public class LogGenerator {

    /**
     * Generates an XES file of a possible event log based on a given directed graph.
     * No probabilities of the activity occurring is considered.
     *
     * @param directedGraph
     * @param logSize
     * @param logFilePath
     */
    public void generateLog(DirectedGraph directedGraph, int logSize, String logFilePath) {
        EventLog eventLog = new EventLog();
        for (int i = 0; i < logSize; i++) {
            eventLog.add(generateTrace(directedGraph));
        }
        eventLog.serializeEventLog(logFilePath);
    }

    private Trace generateTrace(DirectedGraph directedGraph) {
        Trace trace = new Trace();
        Collection<Vertex> vertices = directedGraph.getVertices();

        // check for INPUT and OUTPUT
        Vertex input = null;
        Vertex output = null;
        for (Vertex vertex : vertices) {
            if (vertex.getName().equals("INPUT")) input = vertex;
            if (vertex.getName().equals("OUTPUT")) output = vertex;
        }

        if (input != null && output != null) {
            Vertex currentVertex = input;
            Random random = new Random();

            while (!currentVertex.equals(output)) {
                if (currentVertex != input) trace.add(currentVertex.getName()); // add the activity to the trace
                Collection<Vertex> adjacentVertices = directedGraph.getDirectSuccessors(currentVertex);
                if (!adjacentVertices.isEmpty()) {
                    List<Vertex> adjacentList = new ArrayList<>(adjacentVertices);
                    int randomIndex = random.nextInt(adjacentList.size());
                    currentVertex = adjacentList.get(randomIndex);
                } else {
                    break;
                }
            }
            System.out.println(trace);
        }
        return trace;
    }
}
