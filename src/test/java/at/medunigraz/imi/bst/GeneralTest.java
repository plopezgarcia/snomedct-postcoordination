package at.medunigraz.imi.bst;

import org.junit.Test;

import java.util.List;

public class GeneralTest {

    @Test
    public void minimalTest() throws Exception {

        // Gets all patterns for CF subhierarchy, in descending order, which at least appear 1000 times

        List<PatternFrequency> aListOfFreqs = TopLevelConcept.getPatternFrequency(TopLevelConcept.SUBHIERARCHY.CF, 6000);
        System.out.println(aListOfFreqs);

        System.out.println("The most frequent pattern for Clinical Finding is: " + aListOfFreqs.get(0).pattern);
        System.out.println("In SNOMED CT, its used by: " + aListOfFreqs.get(0).frequency + " concepts");
    }

}
