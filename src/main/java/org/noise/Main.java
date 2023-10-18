package org.noise;

import org.noise.identify.ModelBasedNoiseIdentifier;
import org.utils.files.xes.XESReader;

import java.util.List;

public class Main {
    public static void main(String[] args) {

//        List<String> log = new XESReader().readXES("/Users/anandik/Documents/LogQuality/Logs/OOO-2/log-100/log-100-5.xes");
//        new ModelBasedNoiseIdentifier().identifyNoise(log);

        for (int i = 0; i < 10; i++) {
            String filePath = "/Users/anandik/OneDrive - The University of Melbourne/GR/Experiments/LogQualityV3/Log" + i;
            new LogGeneratorWithNoise().generateLog(filePath);
        }


    }
}