package org.noise.inject;

import java.util.*;

public class ErroneousActivityInjector extends NoiseInjector {

    private final Set<Character> activities = new HashSet<>();

    @Override
    public String injectNoise(List<String> traces) {
        Random random = new Random();
        getDistinctActivities(traces);

        //for (int i = 0; i < numOfNoiseTraces; i++) {
            // select random trace
            String trace = traces.get(random.nextInt(traces.size()));

            // define a probability to add activity not in the system
             double p1 = 0.45;
             double p2 = 0.1;

            String modifiedTrace;
            if (random.nextDouble() < p1) {
                // duplicate activities
                modifiedTrace = logDuplicateActivity(trace);
            } else if (random.nextDouble() < (p1+p2)){
                // activities in the system, but not executed
                modifiedTrace = logNotExecutedSystemActivities(trace);
            } else {
                // activities not in the system
                modifiedTrace = logActivitiesNotInSystem(trace);
            }
            System.out.println(trace + " " + modifiedTrace);

        return modifiedTrace;
    }

    private String logDuplicateActivity(String trace) {
        Random random = new Random();
        int randomIndex = random.nextInt(trace.length());
        char randomChar = trace.charAt(randomIndex);
        StringBuilder builder = new StringBuilder(trace);
        builder.insert(randomIndex, randomChar);
        return builder.toString();
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

    private void getDistinctActivities(List<String> traces) {
        for (String trace : traces) {
            for (char activity : trace.toCharArray()) {
                activities.add(activity);
            }
        }
    }
}
