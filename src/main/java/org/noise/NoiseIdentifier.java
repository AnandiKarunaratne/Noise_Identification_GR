package org.noise;

import java.util.*;

public abstract class NoiseIdentifier {

    final List<String> traces;

    NoiseIdentifier(List<String> traces) {
        this.traces = traces;
    }

    abstract List<String> identifyNoise();

}
