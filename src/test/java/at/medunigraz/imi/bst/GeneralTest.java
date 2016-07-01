package at.medunigraz.imi.bst;

import org.junit.Test;

import java.util.Arrays;

public class GeneralTest {

    @Test
    public void minimalTest() throws Exception {

        PatternRightHand prh1 = new PatternRightHand("finding-site", TopLevelConcept.SUBHIERARCHY.BS);
        PatternRightHand prh2 = new PatternRightHand("recommended-procedure", TopLevelConcept.SUBHIERARCHY.PR);

        Pattern pattern = new Pattern(TopLevelConcept.SUBHIERARCHY.CF, Arrays.asList(prh1, prh2));

        System.out.println(pattern);

    }

}
