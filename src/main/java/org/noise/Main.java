package org.noise;

import org.jbpt.graph.DirectedGraph;
import org.noise.identify.ModelBasedNoiseIdentifier;
import org.utils.discovery.ProcessDiscovery;
import org.utils.files.xes.XESReader;

import org.utils.log.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {

//        List<String> log = new XESReader().readXES("/Users/anandik/Documents/LogQuality/Logs/OOO-2/log-100/log-100-5.xes");
//        new ModelBasedNoiseIdentifier().identifyNoise(log);

//        for (int i = 0; i < 3; i++) {
//            String filePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV4/Log" + i;
//            new LogGeneratorWithNoise().generateLog(filePath);
//        }

        String logReadFilePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV4/Log2/OmitActivity/Logs/log-512-0.0.xes";
        EventLog eventLog = new EventLog().readXES(logReadFilePath);
        DirectedGraph directedGraph = new ProcessDiscovery().discoverDG(eventLog);

        for (int log = 0; log < 3; log++) {
            for (int logSize = 100; logSize <= 1000; logSize = logSize + 100) {
                String logWriteFilePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV5/Logs/" + log + "-" + logSize + ".xes";
//                new LogGenerator().generateLog(directedGraph, logSize, logWriteFilePath);
                double[] noiseLevels = {0.25, 0.5, 0.75, 1, 1.25, 1.5, 1.75, 2};
                new LogGeneratorWithNoise().generateLog(noiseLevels, logWriteFilePath);
            }
        }






//
//        double[] noiseLevels = {0.25, 0.5, 0.75, 1, 1.25, 1.5, 1.75, 2};
//        new LogGeneratorWithNoise().generateLog(noiseLevels, logFilePath);

    }
}