package at.medunigraz.imi.bst;

import org.junit.Test;

import java.util.List;

public class GeneralTest {

    @Test
    public void minimalTest() throws Exception {

        // Gets all patterns for CF subhierarchy, in descending order, with at least 6000 concepts
        // Retains frequency information

    	ExtendedPatternFrequency.fromFile();
    	
        List<PatternFrequency> aListOfFreqs = ConceptModelConcepts.getPatternFrequency(ConceptModelConcepts.SUBHIERARCHY.CF, 6000);
        System.out.println(aListOfFreqs);

        System.out.println("The most frequent pattern for Clinical Finding is: " + aListOfFreqs.get(0).pattern);
        System.out.println("In SNOMED CT, its used by: " + aListOfFreqs.get(0).frequency + " concepts");

        // Easier version: gets only the most frequent pattern

        ConceptPattern cp = ConceptModelConcepts.getMostFrequentPattern(ConceptModelConcepts.SUBHIERARCHY.CF);

        System.out.println("The most frequent pattern for Clinical Finding is: " + cp);
    }

}
