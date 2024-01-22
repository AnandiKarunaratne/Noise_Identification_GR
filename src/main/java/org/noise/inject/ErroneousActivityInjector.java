package org.noise.inject;

import org.utils.log.EventLog;
import org.utils.log.Trace;

import java.util.*;

public class ErroneousActivityInjector extends NoiseInjector {

    private final Set<String> activities = new HashSet<>();

    @Override
    public Trace[] injectNoise(EventLog traces) {
        Random random = new Random();
        getDistinctActivities(traces);

        Trace trace = traces.get(random.nextInt(traces.size()));
        Trace selectedTrace = new Trace(trace);

        // define a probability to add activity not in the system
        double p1 = 0.45;
        double p2 = 0.45;

//        Trace modifiedTrace;
        if (random.nextDouble() < p1) {
            // duplicate activities
            logDuplicateActivity(selectedTrace);
        } else if (random.nextDouble() < (p1+p2)){
            // activities in the system, but not executed
            logNotExecutedSystemActivities(selectedTrace);
        } else {
            // activities not in the system
            logActivitiesNotInSystem(selectedTrace);
        }

        Trace[] result = new Trace[2];
        result[0] = trace;
        result[1] = selectedTrace;
        return result;
    }

    private void logDuplicateActivity(Trace trace) {
        Random random = new Random();
        int randomIndex = random.nextInt(trace.size());
        String randomActivity = trace.get(randomIndex);

        trace.add(randomIndex, randomActivity);
    }

    private void logNotExecutedSystemActivities(Trace trace) {
        List<String> activityList = new ArrayList<>(activities);
        Random random = new Random();
        String injectActivity = activityList.get(random.nextInt(activityList.size()));

        injectActivity(trace, injectActivity);
    }

    private void logActivitiesNotInSystem(Trace trace) {
        Set<String> activitiesWithIO = new HashSet<>(activities);
        activitiesWithIO.add("i");
        activitiesWithIO.add("o");
        Random random = new Random();
        // select a random activity not in the activity list to inject
        String activityNotInSystem;
        do activityNotInSystem = String.valueOf('a' + random.nextInt(26));
        while (activitiesWithIO.contains(activityNotInSystem));
        // inject
        injectActivity(trace, activityNotInSystem);
    }

    private void injectActivity(Trace trace, String activity) {
        Random random = new Random();
        int injectIndex = random.nextInt(trace.size());
        trace.add(injectIndex, activity);
    }

    private void getDistinctActivities(EventLog traces) {
        for (Trace trace : traces) {
            activities.addAll(trace);
        }
    }
}
