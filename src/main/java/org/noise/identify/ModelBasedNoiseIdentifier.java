package org.noise.identify;

import java.util.*;

public class ModelBasedNoiseIdentifier extends NoiseIdentifier {

    @Override
    public List<String> identifyNoise(List<String> traces) {

        Map<String, HashSet<String>> model = getModel(traces, 1.0);

        for (Map.Entry<String, HashSet<String>> entry : model.entrySet()) {
            String key = entry.getKey();
            HashSet<String> values = entry.getValue();

            for (String value : values) {
                System.out.print("\"" + key + "->" + value + "\", ");
            }
        }

        return new ArrayList<>();
    }

    public List<String> filterTraces(List<String> traces, double filterThreshold) {
        Map<String, Integer> traceOccurrences = new HashMap<>();
        for (String str : traces) {
            traceOccurrences.put("i" + str + "o", traceOccurrences.getOrDefault("i" + str + "o", 0) + 1);
        }

        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(traceOccurrences.entrySet());
        sortedList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Calculate the total occurrences
        int totalOccurrences = sortedList.stream().mapToInt(Map.Entry::getValue).sum();

        // Calculate the threshold for the top 80%
        double threshold = filterThreshold * totalOccurrences;

        // Iterate through the sorted list and accumulate strings until reaching the top 80%
        int accumulatedOccurrences = 0;
        List<String> topStrings = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedList) {
            if (accumulatedOccurrences >= threshold) {
                break;
            }
            topStrings.add(entry.getKey());
            accumulatedOccurrences += entry.getValue();
        }
        return topStrings;
    }

    public Map<String, HashSet<String>> getModel(List<String> traces, double filterThreshold) {
        List<String> filteredTraces = filterTraces(traces, filterThreshold);
        // create a "model" using the maxOccurringList
        Map<String, HashSet<String>> model = new HashMap<>();
        for (String maxTrace : filteredTraces) {
            for (int i = 0; i < maxTrace.length() - 1; i++) {
                String currentActivity = String.valueOf(maxTrace.charAt(i));
                String nextActivity = String.valueOf(maxTrace.charAt(i+1));
                if (!model.containsKey(currentActivity)) {
                    model.put(currentActivity, new HashSet<>());
                }
                model.get(currentActivity).add(nextActivity);
            }
        }
        return model;
    }
}
