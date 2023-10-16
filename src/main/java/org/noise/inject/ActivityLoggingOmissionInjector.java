package org.noise.inject;

import java.util.List;
import java.util.Random;

public class ActivityLoggingOmissionInjector extends NoiseInjector {

    @Override
    public String injectNoise(List<String> traces) {
        Random random = new Random();
        String trace = traces.get(random.nextInt(traces.size()));
        StringBuilder modifiedTrace = new StringBuilder(trace);
        modifiedTrace.deleteCharAt(random.nextInt(trace.length()));
        return modifiedTrace.toString();
    }
}
