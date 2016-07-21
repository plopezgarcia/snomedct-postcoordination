package at.medunigraz.imi.bst;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This is the main class that obtains all matches between the patterns and the annotation groups.
 * 
 * @version 1.0
 * */
public class Main_MatchingPostcoordinationPatterns {
	public static final String MATCHING_PATTERNS_FILE = "src/main/resources/data/matchingPatterns2.txt";
	
	public static void main(String[] args) {
		
		try{
			List<PatternFrequency> allPatternFreq = PatternFrequency.fromFile();//We need to remove redundant patterns after extended version
			HashMap<String, List<String>> mapStringCodes = ParseAnnotationFile.getListOfAnnotationGroups();
			BufferedWriter bw = new BufferedWriter(new FileWriter(MATCHING_PATTERNS_FILE));
			for(String groupId: mapStringCodes.keySet()){
				AnnotationGroup ag = new AnnotationGroup(groupId, mapStringCodes.get(groupId));
				List<PatternCombination> listMatchingPatterns = ag.getListPatternCombinations(allPatternFreq);
				Collections.sort(listMatchingPatterns);
				
				bw.write("Annotation group "+groupId+":"+ag.getListAnnotationCodes()+"\n");
				for(PatternCombination pc: listMatchingPatterns){
					bw.write(pc+"\n");
				}
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
