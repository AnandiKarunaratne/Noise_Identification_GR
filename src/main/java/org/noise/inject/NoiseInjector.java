package org.noise.inject;

import java.util.List;

public abstract class NoiseInjector {

    final List<String> traces;
    final double noiseRatio;
    final int numOfNoiseTraces;

    NoiseInjector(List<String> traces, double noiseRatio) {
        this.traces = traces;
        this.noiseRatio = noiseRatio;
        this.numOfNoiseTraces = (int) (traces.size() * noiseRatio / (1 - noiseRatio)); // Noise/(Traces+Noise) = noiseRatio
    }

    public abstract List<String> injectNoise();
}
