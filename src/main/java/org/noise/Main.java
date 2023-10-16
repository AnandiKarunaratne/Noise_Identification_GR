package org.noise;

import org.noise.identify.ClusterBasedNoiseIdentifier;
import org.noise.identify.ModelBasedNoiseIdentifier;
import org.noise.identify.StringDistanceBasedNoiseIdentifier;
import org.noise.inject.ActivityLoggingOmissionInjector;
import org.noise.inject.ErroneousActivityInjector;
import org.noise.inject.OutOfOrderActivityReporter;
import org.utils.files.xes.XESGenerator;
import org.utils.files.xes.XESReader;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

//        List<String> log = new XESReader().readXES("/Users/anandik/Documents/LogQuality/Logs/OOO-2/log-100/log-100-5.xes");
//        new ModelBasedNoiseIdentifier().identifyNoise(log);

         new LogGeneratorWithNoise().generateLog();

    }
}