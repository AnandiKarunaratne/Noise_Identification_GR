package org.noise.log;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class EventLog extends ArrayList<Trace> {

    public EventLog() {
        super();
    }

    public EventLog(Collection<? extends Trace> c) {
        super(c);
    }

    public void serializeEventLog(String fileName) {
        try {
            FileWriter myWriter = new FileWriter(fileName);

            myWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            myWriter.write("<log xes.version=\"1.0\" xes.features=\"nested-attributes\" openxes.version=\"1.0RC7\">\n");
            myWriter.write("<extension name=\"Lifecycle\" prefix=\"lifecycle\" uri=\"http://www.xes-standard.org/lifecycle.xesext\"/>\n");
            myWriter.write("<extension name=\"Organizational\" prefix=\"org\" uri=\"http://www.xes-standard.org/org.xesext\"/>\n");
            myWriter.write("<extension name=\"Time\" prefix=\"time\" uri=\"http://www.xes-standard.org/time.xesext\"/>\n");
            myWriter.write("<extension name=\"Concept\" prefix=\"concept\" uri=\"http://www.xes-standard.org/concept.xesext\"/>\n");
            myWriter.write("<extension name=\"Semantic\" prefix=\"semantic\" uri=\"http://www.xes-standard.org/semantic.xesext\"/>\n");
            myWriter.write("<global scope=\"trace\">\n");
            myWriter.write("<string key=\"concept:name\" value=\"UNKNOWN\"/>\n");
            myWriter.write("</global>\n");
            myWriter.write("<global scope=\"event\">\n");
            myWriter.write("<string key=\"concept:name\" value=\"UNKNOWN\"/>\n");
            myWriter.write("</global>\n");
            myWriter.write("<classifier name=\"Activity classifier\" keys=\"concept:name\"/>\n");
            myWriter.write("<string key=\"concept:name\" value=\"log\"/>\n");

            int z=1;
            for (List<String> t : this) {
                myWriter.write("<trace>\n");
                myWriter.write("<string key=\"concept:name\" value=\"" + z + "\"/>\n");

                for (String s : t) {
                    myWriter.write("<event><string key=\"concept:name\" value=\"" + s + "\"/></event>\n");
                }

                myWriter.write("</trace>\n");

                z++;
            }

            myWriter.write("</log>\n");

            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
