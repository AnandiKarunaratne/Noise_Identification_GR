package org.noise;

import java.util.*;
import java.util.stream.Collectors;

public class ModelBasedNoiseIdentifier extends NoiseIdentifier{
    ModelBasedNoiseIdentifier(List<String> traces) {
        super(traces);
    }

    @Override
    List<String> identifyNoise() {
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

        // create a "model" using the maxOccurringList
        Map<String, HashSet<String>> model = new HashMap<>();
        for (String maxTrace : maxOccurringList) {
            for (int i = 0; i < maxTrace.length() - 1; i++) {
                String currentActivity = String.valueOf(maxTrace.charAt(i));
                String nextActivity = String.valueOf(maxTrace.charAt(i+1));
                if (!model.containsKey(currentActivity)) {
                    model.put(currentActivity, new HashSet<>());
                }
                model.get(currentActivity).add(nextActivity);
            }
        }

        // check if traces in minOccurringList aligns with the model
        HashSet<String> noise = new HashSet<>();
        for (String minTrace : minOccurringList) {
            for (int i = 0; i < minTrace.length() - 1; i++) {
                String currentActivity = String.valueOf(minTrace.charAt(i));
                String nextActivity = String.valueOf(minTrace.charAt(i+1));

                // if current activity is not a key or the next activity is not in the current activity's hashset add it as noise
                if (!model.containsKey(currentActivity) || !model.get(currentActivity).contains(nextActivity)) {
                    noise.add(minTrace);
                    break;
                }
            }
        }

        return new ArrayList<>(noise);
    }
}
