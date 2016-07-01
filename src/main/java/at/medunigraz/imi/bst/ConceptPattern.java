package at.medunigraz.imi.bst;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConceptPattern {

    public TopLevelConcept.SUBHIERARCHY topLevelConcept;

    public List<PatternRightHand> patternRightHands;

    public ConceptPattern(TopLevelConcept.SUBHIERARCHY topLevelConcept, List<PatternRightHand> patternRightHands) {
        this.topLevelConcept = topLevelConcept;
        Collections.sort(patternRightHands);
        this.patternRightHands = patternRightHands;
    }

    public static ConceptPattern fromString(String fullString) throws IOException {

        String IS_A = "116680003";

        String rightHandPattern = "(\\d+)-(..)";

        Pattern r = Pattern.compile(rightHandPattern);

        Matcher m = r.matcher(fullString);

        List<ConceptPattern> pattern = new ArrayList<ConceptPattern>();

        List<PatternRightHand> prhs = new ArrayList<PatternRightHand>();

        // FIXME: 01/07/16
        TopLevelConcept.SUBHIERARCHY currentTopLevelSH = null;

        while (m.find()) {
            String relationship = m.group(1);
            TopLevelConcept.SUBHIERARCHY range = TopLevelConcept.SUBHIERARCHY.valueOf(m.group(2));

            //IS-A triple should not be added, but determines which SH we are talking about
            if (relationship.equals(IS_A)) {
                currentTopLevelSH = range;
            } else {
                prhs.add(new PatternRightHand(relationship, range));
            }
        }

        return new ConceptPattern(currentTopLevelSH, prhs);
    }

    @Override
    public String toString() {
        return topLevelConcept + "->" + patternRightHands;
    }

}
