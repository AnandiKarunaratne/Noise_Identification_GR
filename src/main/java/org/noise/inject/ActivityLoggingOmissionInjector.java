package org.noise.inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ActivityLoggingOmissionInjector extends NoiseInjector {

    public ActivityLoggingOmissionInjector(List<String> traces, double noiseRatio) {
        super(traces, noiseRatio);
    }

    @Override
    public List<String> injectNoise() {
        List<String> tracesWithNoise = new ArrayList<>(traces);

        Random random = new Random();
        for (int i = 0; i < numOfNoiseTraces; i++) {
            // select random trace
            String trace = traces.get(random.nextInt(traces.size()));
            // remove a random activity from the trace
            StringBuilder modifiedTrace = new StringBuilder(trace);
            modifiedTrace.deleteCharAt(random.nextInt(trace.length()));
            // add the modified trace to the traces
            tracesWithNoise.add(modifiedTrace.toString());
        }

        return tracesWithNoise;
    }
}
