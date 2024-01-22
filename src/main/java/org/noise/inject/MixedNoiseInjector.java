package org.noise.inject;

import org.utils.log.EventLog;
import org.utils.log.Trace;

import java.util.Random;

public class MixedNoiseInjector extends NoiseInjector{

    @Override
    public Trace[] injectNoise(EventLog traces) {
        Random random = new Random();
        int selector = random.nextInt(3);
        Trace[] result = null;

        switch (selector) {
            case 0:
                result = new ActivityLoggingOmissionInjector().injectNoise(traces);
                break;
            case 1:
                result = new ErroneousActivityInjector().injectNoise(traces);
                break;
            case 2:
                result = new OutOfOrderActivityReporter().injectNoise(traces);
                break;
        }
        return result;
    }

}
