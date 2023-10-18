package org.noise;

import org.noise.inject.ActivityLoggingOmissionInjector;
import org.noise.inject.ErroneousActivityInjector;
import org.noise.inject.OutOfOrderActivityReporter;
import org.utils.files.xes.XESGenerator;

import java.util.ArrayList;
import java.util.List;

public class LogGeneratorWithNoise {

    public void generateLog(String filePath) {
        List<String> log = new ArrayList<>();
        List<String> noiseTraces1 = new ArrayList<>();
        List<String> traceRemovedLog1 = new ArrayList<>();
        List<String> noiseTraces2 = new ArrayList<>();
        List<String> traceRemovedLog2 = new ArrayList<>();
        List<String> noiseTraces3 = new ArrayList<>();
        List<String> traceRemovedLog3 = new ArrayList<>();

        int[] logSizes = {100, 500, 1000, 5000, 10000};

        for (int logSize : logSizes) {
            // generate logs without noise
            int incrementalLogSize;
            int initialNoiseCount;
            if (logSize == 100) {
                incrementalLogSize = 100;
                initialNoiseCount = 5;
            } else {
                incrementalLogSize = logSize - log.size();
                initialNoiseCount = logSize * 5 / 100 - noiseTraces1.size();
            }

            List<String> newSystemTraces = new SystemTraceGenerator().generateLog(incrementalLogSize);
            log.addAll(newSystemTraces);
            traceRemovedLog1.addAll(newSystemTraces);
            traceRemovedLog2.addAll(newSystemTraces);
            traceRemovedLog3.addAll(newSystemTraces);
            new XESGenerator().generateXES(log, filePath + "/OmitActivity/Logs/log-" + logSize + "-0.xes");
            new XESGenerator().generateXES(log, filePath + "/OutOfOrder/Logs/log-" + logSize + "-0.xes");
            new XESGenerator().generateXES(log, filePath + "/InjectActivity/Logs/log-" + logSize + "-0.xes");

            int numOfNoise;
            for (double noise = 0.05; noise <= 0.2; noise = noise + 0.05) {
                if (noise == 0.05) {
                    numOfNoise = initialNoiseCount;
                } else {
                    numOfNoise = (int) (logSize * 0.05);
                }

                for (int i = 0; i < numOfNoise; i++) {
                    String[] noiseResult1 = new ActivityLoggingOmissionInjector().injectNoise(traceRemovedLog1);
                    String replacedTrace1 = noiseResult1[0];
                    String noiseTrace1 = noiseResult1[1];
                    System.out.println(noiseTrace1);
                    traceRemovedLog1.remove(replacedTrace1);
                    noiseTraces1.add(noiseTrace1);

                    String[] noiseResult2 = new OutOfOrderActivityReporter().injectNoise(traceRemovedLog2);
                    String replacedTrace2 = noiseResult2[0];
                    String noiseTrace2 = noiseResult2[1];
                    System.out.println(noiseTrace2);
                    traceRemovedLog2.remove(replacedTrace2);
                    noiseTraces2.add(noiseTrace2);

                    String[] noiseResult3 = new ErroneousActivityInjector().injectNoise(traceRemovedLog3);
                    String replacedTrace3 = noiseResult3[0];
                    String noiseTrace3 = noiseResult3[1];
                    System.out.println(noiseTrace3);
                    traceRemovedLog3.remove(replacedTrace3);
                    noiseTraces3.add(noiseTrace3);
                }
                List<String> logWithNoise1 = new ArrayList<>(traceRemovedLog1);
                logWithNoise1.addAll(noiseTraces1);
                new XESGenerator().generateXES(logWithNoise1, filePath + "/OmitActivity/Logs/log-" + logSize + "-" + (int)(noise * 100) + ".xes");
                System.out.println(logWithNoise1.size());

                List<String> logWithNoise2 = new ArrayList<>(traceRemovedLog2);
                logWithNoise2.addAll(noiseTraces2);
                new XESGenerator().generateXES(logWithNoise2, filePath + "/OutOfOrder/Logs/log-" + logSize + "-" + (int)(noise * 100) + ".xes");
                System.out.println(logWithNoise2.size());

                List<String> logWithNoise3 = new ArrayList<>(traceRemovedLog3);
                logWithNoise3.addAll(noiseTraces3);
                new XESGenerator().generateXES(logWithNoise3, filePath + "/InjectActivity/Logs/log-" + logSize + "-" + (int)(noise * 100) + ".xes");
                System.out.println(logWithNoise3.size());
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
