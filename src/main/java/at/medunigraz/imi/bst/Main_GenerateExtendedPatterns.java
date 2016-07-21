package at.medunigraz.imi.bst;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;


/**
 * This is the main class that was used to produce the list of extended version of the postcoordination patterns.
 * 
 * @version 1.0
 * */
public class Main_GenerateExtendedPatterns {
	
	public static final String EXTENDED_PATTERNS_FILE = "src/main/resources/data/pattern-extended-frequencies.csv";

	public static void main(String[] args) {
		try {
			List<PatternFrequency> allPatternFreq = PatternFrequency.fromFile();
			Map<String, RelationshipConcept> relationshipsRange = ExtendedPatternFrequency.fromFile();
			ExtendedPatternFrequency extendPatternFrequency = new ExtendedPatternFrequency();
			List<PatternFrequency> allExtendedPatternFreq =extendPatternFrequency.extendPatterns(allPatternFreq,relationshipsRange);
			
			String IS_A = "116680003";
			BufferedWriter bw = new BufferedWriter(new FileWriter(EXTENDED_PATTERNS_FILE));
			for (PatternFrequency patternFrequency : allExtendedPatternFreq) {
				List<PatternRightHand> patterns= patternFrequency.pattern.patternRightHands;
				bw.write(patternFrequency.frequency+"\t"+"["+IS_A+"-"+patternFrequency.pattern.topLevelConcept);
				for (PatternRightHand patternRightHand : patterns) {
					bw.write(","+patternRightHand.relationship+"-"+patternRightHand.range);
				}
				bw.write("]\n");
			}
			bw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
