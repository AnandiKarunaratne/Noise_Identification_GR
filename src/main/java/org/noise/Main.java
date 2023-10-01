package org.noise;

import org.noise.identify.ClusterBasedNoiseIdentifier;
import org.noise.identify.ModelBasedNoiseIdentifier;
import org.noise.identify.StringDistanceBasedNoiseIdentifier;
import org.noise.inject.ActivityLoggingOmissionInjector;
import org.noise.inject.ErroneousActivityInjector;
import org.noise.inject.OutOfOrderActivityReporter;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<String> traces = Arrays.asList(
                "abbbcf", "abbbcf", "abbbcf", "abbbcf", "abbbcf",
                "abcf", "abcf", "abcf", "abcf", "abcf", "abcf", "abcf", "abcf", "abcf", "abcf", "abcf", "abcf", "abcf", "abcf", "abcf", "abcf", "abcf", "abcf", "abcf", "abcf",
                "adeef", "adeef", "adeef", "adeef", "adeef", "adeef", "adeef", "adeef", "adeef", "adeef",
                "adefabcfadef", "adefabcfadef", "adefabcfadef", "adefabcfadef", "adefabcfadef", "adefabcfadef", "adefabcfadef", "adefabcfadef", "adefabcfadef", "adefabcfadef",
                "adef", "adef", "adef", "adef", "adef", "adef", "adef", "adef", "adef", "adef", "adef", "adef", "adef", "adef", "adef", "adef", "adef", "adef", "adef", "adef",
                "addef"
        );

        List<String> tracesWithNoise = new ErroneousActivityInjector(traces, 0.1).injectNoise();

        List<String> noise = new ModelBasedNoiseIdentifier(tracesWithNoise).identifyNoise();

        for (String noisyTrace : noise) {
            System.out.println(noisyTrace);
        }
    }
}