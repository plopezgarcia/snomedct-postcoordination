package at.medunigraz.imi.bst;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
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
	public static final String MATCHING_PATTERNS_FILE2	= "src/main/resources/data/matchingPatterns7.txt";
	public static final String MATCHING_PATTERNS_FILE3	= "src/main/resources/data/matchingPatterns8.txt";
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
				
				
				for(PatternCombination pc: listMatchingPatterns){
					if(pc.isComplete()){
						boolean insert = true;
						for(PatternCombination pcr: listNonRedundant){
							if(pc.equals(pcr)){
								insert = false;
								break;
							}
						}
						if(insert) listNonRedundant.add(pc);
					}
				}
				
				Collections.sort(listNonRedundant);
				
				int nFullMatch = 0;
				int nCompleteMatch = 0;
				
				for(PatternCombination pc: listNonRedundant){
					if(pc.isComplete()) nCompleteMatch++;
					if(pc.isFullMatch()) nFullMatch++;
				}
				
				bw.write("Annotation group "+groupId+":"+ag.getListAnnotationCodes()+" Total matching pattern = "+listNonRedundant.size()+"; Full Match = "+nFullMatch+"; Complete pattern = "+nCompleteMatch+"\n");
				for(PatternCombination pc: listNonRedundant){
					bw.write(pc.toString(c2f)+"\n");
				}
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	/*public static void main(String[] args) {
		
		try{
			Code2FSN c2f = new Code2FSN(SNOMEDCT_TERM_FILE);
			
			List<PatternFrequency> allPatternFreq = PatternFrequency.fromFile();//We need to remove redundant patterns after extended version
			Map<String, List<String>> mapStringCodes = ParseAnnotationFile.getListOfAnnotationGroups();
			BufferedWriter bw = new BufferedWriter(new FileWriter(MATCHING_PATTERNS_FILE));
			//BufferedWriter bw2 = new BufferedWriter(new FileWriter(MATCHING_PATTERNS_FILE2));
			//BufferedWriter bw3 = new BufferedWriter(new FileWriter(MATCHING_PATTERNS_FILE3));
			for(String groupId: mapStringCodes.keySet()){
				AnnotationGroup ag = new AnnotationGroup(groupId, mapStringCodes.get(groupId));
				List<PatternCombination> listMatchingPatterns = ag.getListPatternCombinations(allPatternFreq);
				//if(groupId.contains("EN17_139")) break;
				
				//bw3.write("Annotation group "+groupId+":"+ag.getListAnnotationCodes()+" Total matching pattern = "+listMatchingPatterns.size()+"\n");
				//for(PatternCombination pc: listMatchingPatterns){
				//	bw3.write(pc.toString()+"\n");
				//}
				
				List<PatternCombination> listNonRedundant = ag.getListNonRedundantPatterns(listMatchingPatterns);
				Collections.sort(listNonRedundant);
				
				//bw2.write("Annotation group "+groupId+":"+ag.getListAnnotationCodes()+" Total matching pattern = "+listNonRedundant.size()+"\n");
				//for(PatternCombination pc: listNonRedundant){
				//	bw2.write(pc.toString()+"\n");
				//}
				
				CombinePatternInstances cpi = new CombinePatternInstances(listNonRedundant);
				List<CombinationOfPatternInstances> groups = cpi.getCombinationsOfPatterns();
				//Collections.sort(groups);
				
				for(PatternCombination pc: listMatchingPatterns){
					if(pc.isComplete()){
						List<PatternCombination> listPC = new ArrayList<PatternCombination>();
						listPC.add(pc);
						CombinationOfPatternInstances copi = new CombinationOfPatternInstances(listPC);
						boolean insert = true;
						for(CombinationOfPatternInstances ci: groups){
							if(copi.equals(ci)){
								insert = false;
								break;
							}
						}
						if(insert) groups.add(copi);
					}
				}
				
				Collections.sort(groups);
				
				int nFullMatch = 0;
				int nCompleteMatch = 0;
				
				for(CombinationOfPatternInstances copi: groups){
					if(copi.isComplete()) nCompleteMatch++;
					if(copi.isFullMatch()) nFullMatch++;
				}
				
				bw.write("Annotation group "+groupId+":"+ag.getListAnnotationCodes()+" Total matching pattern = "+groups.size()+"; Full Match = "+nFullMatch+"; Complete pattern = "+nCompleteMatch+"\n");
				for(CombinationOfPatternInstances copi: groups){
					bw.write(copi.toString(c2f)+"\n");
				}
			}
			bw.close();
			//bw2.close();
			//bw3.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
}
