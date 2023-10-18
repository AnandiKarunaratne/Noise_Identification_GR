package org.noise.inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OutOfOrderActivityReporter extends NoiseInjector {

    @Override
    public String[] injectNoise(List<String> traces) {
        //List<String> tracesWithNoise = new ArrayList<>(traces);

        Random random = new Random();
//        for (int i = 0; i < numOfNoiseTraces; i++) {
            // select random trace
            String trace = traces.get(random.nextInt(traces.size()));
            // select indices to swap the activities
            int index1 = random.nextInt(trace.length());
            int index2;
            do { index2 = random.nextInt(trace.length()); } while (index1 == index2); // ensure two indices are different
            // swap
            char[] activities = trace.toCharArray();
            char[] modifiedActivities = new char[trace.length()];

            if (index1 > index2) {
                int temp = index1;
                index1 = index2;
                index2 = temp;
            }

            for (int j = 0; j < trace.length(); j++) {
                if (j == index1) {
                    modifiedActivities[index2] = activities[j];
                } else if (j > index1 && j <= index2) {
                    modifiedActivities[j - 1] = activities[j];
                } else {
                    modifiedActivities[j] = activities[j];
                }
            }

            String modifiedTrace = new String(modifiedActivities);
            System.out.println(trace + " " + modifiedTrace);
            //tracesWithNoise.add(modifiedTrace);
//        }

        String[] result = new String[2];
        result[0] = trace;
        result[1] = modifiedTrace;
        return result;
    }
}
