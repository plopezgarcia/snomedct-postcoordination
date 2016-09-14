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
	public static final String MATCHING_PATTERNS_FILE	= "src/main/resources/data/matchingPatterns4.txt";
	public static final String SNOMEDCT_TERM_FILE		= "src/main/resources/data/sct2_Description_Snapshot-en_INT_20160131.txt";
	
	public static void main(String[] args) {
		
		try{
			Code2FSN c2f = new Code2FSN(SNOMEDCT_TERM_FILE);
			
			List<PatternFrequency> allPatternFreq = PatternFrequency.fromFile();//We need to remove redundant patterns after extended version
			HashMap<String, List<String>> mapStringCodes = ParseAnnotationFile.getListOfAnnotationGroups();
			BufferedWriter bw = new BufferedWriter(new FileWriter(MATCHING_PATTERNS_FILE));
			for(String groupId: mapStringCodes.keySet()){
				AnnotationGroup ag = new AnnotationGroup(groupId, mapStringCodes.get(groupId));
				List<PatternCombination> listMatchingPatterns = ag.getListPatternCombinations(allPatternFreq);
				Collections.sort(listMatchingPatterns);
				
				CombinePatternInstances cpi = new CombinePatternInstances(listMatchingPatterns);
				
				List<CombinationOfPatternInstances> groups = cpi.getCombinationsOfPatterns();
				Collections.sort(groups);
				
				bw.write("Annotation group "+groupId+":"+ag.getListAnnotationCodes()+"\n");
				for(CombinationOfPatternInstances copi: groups){
					bw.write(copi.toString(c2f)+"\n");
				}
				
				
				/*for(PatternCombination pc: listMatchingPatterns){
					bw.write(pc+"\n");
				}*/
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
