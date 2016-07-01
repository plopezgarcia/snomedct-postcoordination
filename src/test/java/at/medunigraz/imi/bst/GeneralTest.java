package at.medunigraz.imi.bst;

import org.junit.Test;

import java.util.List;

public class GeneralTest {

    @Test
    public void minimalTest() throws Exception {

        // Gets all patterns for CF subhierarchy, in descending order, which at least appear 1000 times

        List<PatternFrequency> aListOfFreqs = TopLevelConcept.getPatternFrequency(TopLevelConcept.SUBHIERARCHY.CF, 6000);

        System.out.println(aListOfFreqs);
    }

}
