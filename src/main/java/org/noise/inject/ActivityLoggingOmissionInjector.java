package org.noise.inject;

import org.utils.log.EventLog;
import org.utils.log.Trace;

import java.util.Random;

public class ActivityLoggingOmissionInjector extends NoiseInjector {

    @Override
    public Trace[] injectNoise(EventLog traces) {
        Random random = new Random();
        Trace trace = traces.get(random.nextInt(traces.size()));

        Trace modifiedTrace = new Trace(trace);

        int randomIndex = random.nextInt(modifiedTrace.size());
        modifiedTrace.remove(randomIndex);

        Trace[] result = new Trace[2];
        result[0] = trace;
        result[1] = modifiedTrace;
        return result;
    }
}
