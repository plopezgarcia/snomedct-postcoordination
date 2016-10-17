package at.medunigraz.imi.bst;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This is the main class that obtains all matches between the patterns and the annotation groups.
 * 
 * @version 1.0
 * */
public class Main_MatchingPostcoordinationPatterns {
	public static final String MATCHING_PATTERNS_FILE	= "src/main/resources/data/matchingPatterns6.txt";
	public static final String SNOMEDCT_TERM_FILE		= "src/main/resources/data/sct2_Description_Snapshot-en_INT_20160131.txt";
	
	public static void main(String[] args) {
		
		try{
			Code2FSN c2f = new Code2FSN(SNOMEDCT_TERM_FILE);
			
			List<PatternFrequency> allPatternFreq = PatternFrequency.fromFile();//We need to remove redundant patterns after extended version
			Map<String, List<String>> mapStringCodes = ParseAnnotationFile.getListOfAnnotationGroups();
			BufferedWriter bw = new BufferedWriter(new FileWriter(MATCHING_PATTERNS_FILE));
			for(String groupId: mapStringCodes.keySet()){
				AnnotationGroup ag = new AnnotationGroup(groupId, mapStringCodes.get(groupId));
				List<PatternCombination> listMatchingPatterns = ag.getListPatternCombinations(allPatternFreq);
				List<PatternCombination> listNonRedundant = ag.getListNonRedundantPatterns(listMatchingPatterns);
				Collections.sort(listNonRedundant);
				if(groupId.contains("EN17_139")){
					System.out.println("List Non Redundant");
					for(PatternCombination pc: listNonRedundant){
						System.out.println(pc);
					}
				}
				
				CombinePatternInstances cpi = new CombinePatternInstances(listNonRedundant);
				List<CombinationOfPatternInstances> groups = cpi.getCombinationsOfPatterns();
				Collections.sort(groups);
				if(groupId.contains("EN17_139")){
					System.out.println("Combine Pattern Instances");
					for(CombinationOfPatternInstances copi: groups){
						System.out.println(copi.toString(c2f));
					}
				}
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
