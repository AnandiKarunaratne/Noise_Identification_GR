package org.noise.inject;

import org.utils.log.EventLog;
import org.utils.log.Trace;

import java.util.Random;

public class OutOfOrderActivityReporter extends NoiseInjector {

    @Override
    public Trace[] injectNoise(EventLog traces) {
        Random random = new Random();
        Trace trace = traces.get(random.nextInt(traces.size()));
        Trace modifiedTrace = new Trace(trace);

        // select indices to swap the activities
        int index1 = random.nextInt(modifiedTrace.size());
        int index2;
        do { index2 = random.nextInt(modifiedTrace.size()); } while (index1 == index2); // ensure two indices are different

        if (index1 > index2) {
            int temp = index1;
            index1 = index2;
            index2 = temp;
        }

        String temp = modifiedTrace.get(index1);
        modifiedTrace.set(index1, modifiedTrace.get(index2));
        modifiedTrace.set(index2, temp);

        Trace[] result = new Trace[2];
        result[0] = trace;
        result[1] = modifiedTrace;
        return result;
    }
}
