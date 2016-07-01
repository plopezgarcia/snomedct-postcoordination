package at.medunigraz.imi.bst;

import au.com.bytecode.opencsv.CSVReader;
import org.junit.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneralTest {

    @Test
    public void minimalTest() throws Exception {

        String PATTERNS_FILE = "src/main/resources/data/pattern-frequencies.csv";

        List<PatternFrequency> patternFrequencies = new ArrayList<PatternFrequency>();

        CSVReader reader = new CSVReader(new FileReader(PATTERNS_FILE), '\t');
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {

            int frequency = new Integer(nextLine[0]);

            ConceptPattern conceptPattern = ConceptPattern.fromString(nextLine[1]);

            if (!conceptPattern.patternRightHands.isEmpty())
                patternFrequencies.add(new PatternFrequency(conceptPattern, frequency));
        }

        System.out.println(patternFrequencies);
    }

}
