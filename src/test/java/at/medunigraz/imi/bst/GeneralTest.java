package at.medunigraz.imi.bst;

import org.junit.Test;

import java.util.Arrays;

public class GeneralTest {

    @Test
    public void minimalTest() throws Exception {

        PatternRightHand prh1 = new PatternRightHand("finding-site", TopLevelConcept.SUBHIERARCHY.BS);
        PatternRightHand prh2 = new PatternRightHand("recommended-procedure", TopLevelConcept.SUBHIERARCHY.PR);
        PatternRightHand prh3 = new PatternRightHand("recommended-procedure", TopLevelConcept.SUBHIERARCHY.BS);
        PatternRightHand prh4 = new PatternRightHand("finding-site", TopLevelConcept.SUBHIERARCHY.CF);
        PatternRightHand prh5 = new PatternRightHand("finding-site", TopLevelConcept.SUBHIERARCHY.BS);

        Pattern pattern = new Pattern(  TopLevelConcept.SUBHIERARCHY.CF,
                                        Arrays.asList(prh1, prh2, prh3, prh4, prh5));

        PatternFrequency patternFreq = new PatternFrequency(pattern, 1000);

        System.out.println(patternFreq);

    }

}
