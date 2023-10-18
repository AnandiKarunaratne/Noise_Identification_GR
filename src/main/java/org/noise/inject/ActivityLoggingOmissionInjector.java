package org.noise.inject;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Random;

public class ActivityLoggingOmissionInjector extends NoiseInjector {

    @Override
    public String[] injectNoise(List<String> traces) {
        Random random = new Random();
        String trace = traces.get(random.nextInt(traces.size()));
        StringBuilder modifiedTrace = new StringBuilder(trace);
        modifiedTrace.deleteCharAt(random.nextInt(trace.length()));
        String[] result = new String[2];
        result[0] = trace;
        result[1] = modifiedTrace.toString();
        return result;
    }
}
