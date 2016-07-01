package at.medunigraz.imi.bst;


import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatternFrequency implements Comparable<PatternFrequency>{

    public static final String PATTERNS_FILE = "src/main/resources/data/pattern-frequencies.csv";

    public ConceptPattern pattern;
    public int frequency;

    public PatternFrequency(ConceptPattern pattern, int frequency){
        this.pattern = pattern;
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Freq: " + frequency + " - ConceptPattern: " + pattern.toString();
    }

    public static List<PatternFrequency> fromFile() throws IOException {

        List<PatternFrequency> patternFrequencies = new ArrayList<PatternFrequency>();

        CSVReader reader = new CSVReader(new FileReader(PATTERNS_FILE), '\t');
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {

            int frequency = new Integer(nextLine[0]);

            ConceptPattern conceptPattern = ConceptPattern.fromString(nextLine[1]);

            if (!conceptPattern.patternRightHands.isEmpty())
                patternFrequencies.add(new PatternFrequency(conceptPattern, frequency));
        }

        // File is already sorted, but make sure just in case implementation is changed
        Collections.sort(patternFrequencies, Collections.<PatternFrequency>reverseOrder());

        return patternFrequencies;
    }

    public int compareTo(PatternFrequency other){
        return Integer.compare(this.frequency, other.frequency);
    }
}
