package at.medunigraz.imi.bst;

import java.util.Collections;
import java.util.List;

public class Pattern {

    public TopLevelConcept.SUBHIERARCHY topLevelConcept;

    public List<PatternRightHand> patternRightHands;

    public Pattern(TopLevelConcept.SUBHIERARCHY topLevelConcept, List<PatternRightHand> patternRightHands) {
        this.topLevelConcept = topLevelConcept;
        Collections.sort(patternRightHands);
        this.patternRightHands = patternRightHands;
    }

    @Override
    public String toString() {
        return topLevelConcept + " - " + patternRightHands;
    }

}
