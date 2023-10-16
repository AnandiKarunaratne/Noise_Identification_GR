package org.noise.identify;

import java.util.*;
import java.util.stream.Collectors;

public class StringDistanceBasedNoiseIdentifier extends NoiseIdentifier {

    @Override
    public List<String> identifyNoise(List<String> traces) {
        // calculate the threshold for the max occurring list
        int threshold = (int) (0.10 * traces.size());
        Map<String, Integer> traceOccurrences = new HashMap<>();
        for (String str : traces) {
            traceOccurrences.put(str, traceOccurrences.getOrDefault(str, 0) + 1);
        }

        // separate the strings into max occurring and min occurring lists
        List<String> maxOccurringList = traceOccurrences.entrySet().stream()
                .filter(entry -> entry.getValue() >= threshold)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<String> minOccurringList = traceOccurrences.entrySet().stream()
                .filter(entry -> entry.getValue() < threshold)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // calculate string distance of each trace in minOccurringList with traces in maxOccurringList and see whether if all the values are higher than all the distance values
        // if so add as noise
        HashSet<String> noise = new HashSet<>();
        int distanceThreshold = 2;
        for (String minTrace : minOccurringList) {
            boolean isNoise = true;
            for (String maxTrace : maxOccurringList) {
                if (StringDistanceCalculator.calculateLevenshteinDistance(maxTrace, minTrace) < distanceThreshold) {
                    isNoise = false;
                    break;
                }
            }
            if (isNoise) {
                noise.add(minTrace);
            }
        }

        return new ArrayList<>(noise);
    }
}
