package org.noise;

import org.jbpt.graph.DirectedGraph;
import org.jbpt.hypergraph.abs.Vertex;
import org.noise.inject.*;
import org.utils.discovery.ProcessDiscovery;
import org.utils.log.EventLog;
import org.utils.log.Trace;
import org.utils.files.xes.XESReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LogGeneratorWithNoise {

    private String baseFilePath = null;
    private DirectedGraph directedGraph;

    public void  generateLog(double[] noiseLevels, String logFilePath) {
        EventLog eventLog = new XESReader().readXES(logFilePath);
        directedGraph = new ProcessDiscovery().discoverDG(eventLog);

        int lastIndex = logFilePath.lastIndexOf('.');
        baseFilePath = (lastIndex != -1) ? logFilePath.substring(0, lastIndex) : logFilePath;

        for (NoiseType noiseType : NoiseType.values()) {
            generateNoiseTraces(eventLog, noiseType, noiseLevels);
        }
    }

    private void generateNoiseTraces(EventLog eventLog, NoiseType noiseType, double[] noiseLevels) {
        List<Trace> noiseTraces = new ArrayList<>();
        EventLog traceRemovedLog = new EventLog(eventLog);

        NoiseInjector noiseInjector;
        switch (noiseType) {
            case OMIT_ACTIVITY:
                noiseInjector = new ActivityLoggingOmissionInjector();
                break;
            case INJECT_ACTIVITY:
                noiseInjector = new ErroneousActivityInjector();
                break;
            case OUT_OF_ORDER:
                noiseInjector = new OutOfOrderActivityReporter();
                break;
            default:
                noiseInjector = new MixedNoiseInjector();
                break;
        }

        int logSize = eventLog.size();

        for (double noiseLevel : noiseLevels) {
            double noise = noiseLevel / 100.0;
            int numOfNoise = (noiseLevel == noiseLevels[0]) ? (int) Math.round(logSize * noise) : (int) (Math.round(logSize * noise)) - noiseTraces.size();

            for (int i = 0; i < numOfNoise; i++) {
                Trace[] noiseResult;
                do noiseResult = noiseInjector.injectNoise(traceRemovedLog);
                while (checkConformance(noiseResult[1]));
                // add a do-while to check whether generated noise trace conforms to the system
                Trace replacedTrace = noiseResult[0];
                Trace noiseTrace = noiseResult[1];
                System.out.println(noiseTrace);
                traceRemovedLog.remove(replacedTrace);
                noiseTraces.add(noiseTrace);
            }

            EventLog logWithNoise = new EventLog(traceRemovedLog);
            logWithNoise.addAll(noiseTraces);
            logWithNoise.serializeEventLog(baseFilePath + "-" + getNoiseTypeShort(noiseType) + "-" + noiseLevel + ".xes");
            System.out.println(logWithNoise.size());
        }
    }

    private String getNoiseTypeShort(NoiseType noiseType) {
        switch (noiseType) {
            case OMIT_ACTIVITY:
                return "oa";
            case INJECT_ACTIVITY:
                return "ia";
            case OUT_OF_ORDER:
                return "ooo";
            case ALL:
                return "all";
        }
        return "";
    }

    private boolean checkConformance(Trace trace) {
        boolean isConforming = true;
        Collection<Vertex> vertices = directedGraph.getVertices();
        Trace checkTrace = new Trace(trace);
        // check for INPUT and OUTPUT
        Vertex input = null;
        Vertex output = null;
        for (Vertex vertex : vertices) {
            if (vertex.getName().equals("INPUT")) input = vertex;
            if (vertex.getName().equals("OUTPUT")) output = vertex;
        }
        if (input != null && output != null) {
            Vertex currentVertex = input;
            checkTrace.add("OUTPUT");
            int index = 0;
            while(currentVertex != output) {
                boolean isSuccessor = false;
                Collection<Vertex> successors = directedGraph.getDirectSuccessors(currentVertex);
                for (Vertex successor : successors) if (successor.getName().equals(checkTrace.get(index))) isSuccessor = true;
                if (!isSuccessor) return false;
                else {
                    for (Vertex vertex : vertices) if (vertex.getName().equals(checkTrace.get(index))) currentVertex = vertex;
                    index++;
                }
            }
        } else isConforming = false;
        return isConforming;
    }
}
