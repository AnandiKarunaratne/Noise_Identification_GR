package org.noise;

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

        List<String> noise = new StringDistanceBasedNoiseIdentifier(traces).identifyNoise();

        for (String noisyTrace : noise) {
            System.out.println(noisyTrace);
        }
    }
}