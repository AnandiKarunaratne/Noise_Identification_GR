package org.noise.inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OutOfOrderActivityReporter extends NoiseInjector {
    public OutOfOrderActivityReporter(List<String> traces, double noiseRatio) {
        super(traces, noiseRatio);
    }

    @Override
    public List<String> injectNoise() {
        List<String> tracesWithNoise = new ArrayList<>(traces);

        Random random = new Random();
        for (int i = 0; i < numOfNoiseTraces; i++) {
            // select random trace
            String trace = traces.get(random.nextInt(traces.size()));
            // select indices to swap the activities
            int index1 = random.nextInt(trace.length());
            int index2;
            do { index2 = random.nextInt(trace.length()); } while (index1 == index2); // ensure two indices are different
            // swap
            char[] activities = trace.toCharArray();
            char temp = activities[index1];
            activities[index1] = activities[index2];
            activities[index2] = temp;
            String modifiedTrace = new String(activities);
            System.out.println(trace + " " + modifiedTrace);
            tracesWithNoise.add(modifiedTrace);
        }

        return tracesWithNoise;
    }
}
