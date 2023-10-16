package org.noise;

import org.noise.inject.ActivityLoggingOmissionInjector;
import org.noise.inject.ErroneousActivityInjector;
import org.noise.inject.OutOfOrderActivityReporter;
import org.utils.files.xes.XESGenerator;

import java.util.ArrayList;
import java.util.List;

public class LogGeneratorWithNoise {

    public void generateLog() {
        List<String> log = new ArrayList<>();
        List<String> noiseTraces = new ArrayList<>();

        for (int logSize = 100; logSize <= 100000; logSize = logSize * 10) {
            // for log size 100 - create an initial log

            int incrementalLogSize;
            if (logSize == 100) {
                incrementalLogSize = 100;
            } else {
                incrementalLogSize = logSize - log.size();
            }

            log.addAll(new SystemTraceGenerator().generateLog(incrementalLogSize));
            new XESGenerator().generateXES(log, "/Users/anandik/Documents/LogQuality/Logs/OOO-2/log-" + logSize + "/log-" + logSize + "-0.xes");

            int numOfNoise = (int) (logSize * 0.05) - noiseTraces.size();
            for (double noise = 0.05; noise <= 0.2; noise = noise + 0.05) {
                for (int i = 0; i < numOfNoise; i++) {
                    String noiseTrace = new OutOfOrderActivityReporter().injectNoise(log);
//                    System.out.println(noiseTrace);
                    noiseTraces.add(noiseTrace);
                }
                List<String> logWithNoise = new ArrayList<>(log);
                logWithNoise.addAll(noiseTraces);
                new XESGenerator().generateXES(logWithNoise, "/Users/anandik/Documents/LogQuality/Logs/OOO-2/log-" + logSize + "/log-" + logSize + "-" + (int) (noise * 100) + ".xes");
                System.out.println(logWithNoise.size());
                numOfNoise = (int) (logSize * 0.05);
            }
        }











//        Random random = new Random();
//        for (int i = 0; i < numOfNoise; i++) {
//            int noiseTypeSelector = random.nextInt(3);
//            String noiseTrace = null;
//            switch (noiseTypeSelector) {
//                case 0:
//                    noiseTrace = new ErroneousActivityInjector(new SystemTraceGenerator().generateLog(100)).injectNoise();
//                    break;
//                case 1:
//                    noiseTrace = new ActivityLoggingOmissionInjector(new SystemTraceGenerator().generateLog(100)).injectNoise();
//                    break;
//                case 2:
//                    noiseTrace = new OutOfOrderActivityReporter(new SystemTraceGenerator().generateLog(100)).injectNoise();
//                    break;
//            }
//            noise.add(noiseTrace);
//        }

    }
}
