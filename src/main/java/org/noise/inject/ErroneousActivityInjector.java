package org.noise.inject;

import java.util.*;

public class ErroneousActivityInjector extends NoiseInjector {

    private Set<Character> activities = new HashSet<>();
    public ErroneousActivityInjector(List<String> traces, double noiseRatio) {
        super(traces, noiseRatio);
    }

    @Override
    public List<String> injectNoise() {
        // could be either activities in the system, but not executed OR activities not in the system

        List<String> tracesWithNoise = new ArrayList<>(traces);
        Random random = new Random();
        getDistinctActivities();

        for (int i = 0; i < numOfNoiseTraces; i++) {
            // select random trace
            String trace = traces.get(random.nextInt(traces.size()));

            // define a probability to add activity not in the system
            double p = 0.1;

            String modifiedTrace;
            if (random.nextDouble() < p) {
                // activities not in the system
                modifiedTrace = logActivitiesNotInSystem(trace);
            } else {
                // activities in the system, but not executed
                modifiedTrace = logNotExecutedSystemActivities(trace);
            }
            tracesWithNoise.add(modifiedTrace);
            System.out.println(trace + " " + modifiedTrace);
        }

        return tracesWithNoise;
    }

    private String logNotExecutedSystemActivities(String trace) {
        Random random = new Random();
        // select a random activity from the activity list to inject
        Character[] activitiesArray = activities.toArray(new Character[0]);
        Character injectActivity = activitiesArray[random.nextInt(activitiesArray.length)];
        // inject
        return injectActivity(trace, injectActivity);
    }

    private String logActivitiesNotInSystem(String trace) {
        Random random = new Random();
        // select a random activity not in the activity list to inject
        char activityNotInSystem;
        do {
            activityNotInSystem = (char) ('a' + random.nextInt(26));
        } while (activities.contains(Character.toString(activityNotInSystem)));
        // inject
        return injectActivity(trace, activityNotInSystem);
    }

    private String injectActivity(String trace, char activity) {
        Random random = new Random();
        int injectIndex = random.nextInt(trace.length());
        StringBuilder builder = new StringBuilder(trace);
        builder.insert(injectIndex, activity);
        return builder.toString();
    }

    private void getDistinctActivities() {
        for (String trace : traces) {
            for (char activity : trace.toCharArray()) {
                activities.add(activity);
            }
        }
    }
}
