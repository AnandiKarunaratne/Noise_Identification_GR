package org.noise.inject;

import org.utils.log.EventLog;
import org.utils.log.Trace;

public abstract class NoiseInjector {

    public abstract Trace[] injectNoise(EventLog traces);

}
