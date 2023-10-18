package org.noise.inject;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public abstract class NoiseInjector {

    public abstract String[] injectNoise(List<String> traces);

}
