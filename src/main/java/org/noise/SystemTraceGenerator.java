package org.noise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SystemTraceGenerator {
    HashMap<String, List<ActivityWithProbability>> system = new HashMap<>();

    public SystemTraceGenerator() {
        createSampleSystem();
    }

    private void createSampleSystem() {
        system.put("i", new ArrayList<>());
        system.put("a", new ArrayList<>());
        system.put("b", new ArrayList<>());
        system.put("c", new ArrayList<>());
        system.put("d", new ArrayList<>());
        system.put("e", new ArrayList<>());
        system.put("f", new ArrayList<>());
        system.put("o", new ArrayList<>());

        system.get("i").add(new ActivityWithProbability("a", 1));
        system.get("a").add(new ActivityWithProbability("b", 0.5));
        system.get("a").add(new ActivityWithProbability("d", 0.5));
        system.get("b").add(new ActivityWithProbability("b", 0.2));
        system.get("b").add(new ActivityWithProbability("c", 0.8));
        system.get("c").add(new ActivityWithProbability("f", 1));
        system.get("d").add(new ActivityWithProbability("e", 1));
        system.get("e").add(new ActivityWithProbability("f", 1));
        system.get("f").add(new ActivityWithProbability("a", 0.4));
        system.get("f").add(new ActivityWithProbability("o", 0.6));
    }

    public List<String> generateLog(int numOfTraces) {
        List<String> log = new ArrayList<>();
        for (int i = 0; i < numOfTraces; i++) {
            log.add(generateSystemTrace());
        }
        return log;
    }

    private String generateSystemTrace() {
        String trace = "";
        String currentActivity = "i";
        do {
            trace = trace + currentActivity;
            currentActivity = getActivityWithProbability(system.get(currentActivity));
        } while(!currentActivity.equals("o"));
        return trace.substring(1);
    }

    private String getActivityWithProbability(List<ActivityWithProbability> activitiesWithProbabilities) {
        double randomValue = Math.random();
        double cumulativeProbability = 0.0;

        for (ActivityWithProbability activityWithProbability : activitiesWithProbabilities) {
            cumulativeProbability += activityWithProbability.probability;

            // If the random value is less than or equal to the cumulative probability,
            // select the corresponding event and return it.
            if (randomValue <= cumulativeProbability) {
                return activityWithProbability.activity;
            }
        }
        throw new IllegalStateException("Invalid probability distribution.");
    }

    private class ActivityWithProbability {
        String activity;
        double probability;

        ActivityWithProbability(String activity, double probability) {
            this.activity = activity;
            this.probability = probability;
        }
    }

}
