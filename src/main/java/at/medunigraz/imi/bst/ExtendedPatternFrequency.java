package at.medunigraz.imi.bst;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import at.medunigraz.imi.bst.RelationshipConcept.RELATIONSHIP;
import au.com.bytecode.opencsv.CSVReader;

public class ExtendedPatternFrequency {
	
	public static final String MRCM_FILE = "src/main/resources/data/MRCM.csv";

	public static Map<String,RelationshipConcept> fromFile() throws Exception {
		Map<String, RelationshipConcept> relationshipsRange = new HashMap<String, RelationshipConcept>();
		
		CSVReader reader = new CSVReader(new FileReader(MRCM_FILE), '\t');
		
		String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
        	String relationship = nextLine[0];
        	if(!RelationshipConcept.RELATIONSHIP_MAPPINGS.containsKey(relationship)) throw new Exception();
        	String [] rangesPerRelationship = RelationshipConcept.fromString(nextLine[1]);      	
        	for (String string : rangesPerRelationship) {
        		if(!TopLevelConcept.SUBHIERARCHY_MAPPINGS.containsKey(string))throw new Exception();
        	}
        	RelationshipConcept relationshipConcept = new RelationshipConcept(relationship,Arrays.asList(rangesPerRelationship)); 
        	relationshipsRange.put(relationship,relationshipConcept);
        }
        reader.close();
		return relationshipsRange;
	}
	
	public  List<PatternFrequency> extendPatterns(List<PatternFrequency> allPatternFreq, Map<String, RelationshipConcept>relationshipsRange){
		
		List<PatternFrequency> extendPatternFreq = new ArrayList<PatternFrequency>();
		for (PatternFrequency patternFrequency : allPatternFreq) {
			int frequency = patternFrequency.frequency;
			List<PatternRightHand> pattern = patternFrequency.pattern.patternRightHands;
			Map<String,List<String>> extendedPattern = extendPattern(pattern,relationshipsRange);	
			List<List<PatternRightHand>> rightHandpatterns = combinePattern(extendedPattern);
			for (List<PatternRightHand> list : rightHandpatterns) {
				ConceptPattern cp = new ConceptPattern(patternFrequency.pattern.topLevelConcept, list);
				PatternFrequency pf = new PatternFrequency(cp,frequency);
				extendPatternFreq.add(pf);
			}
		}
		return extendPatternFreq;
	}

	public  Map<String,List<String>> extendPattern(List<PatternRightHand> pattern, Map<String, RelationshipConcept> relationshipsRange){
		String IS_A = "116680003";
		Map<String,List<String>> extendedPatterns = new HashMap<String,List<String>>();
		for (PatternRightHand patternRightHand : pattern) {
			String relationship = patternRightHand.relationship;
			if(relationship.equals(IS_A)) continue;
			List<String> ranges =relationshipsRange.get(relationship).ranges;
			extendedPatterns.put(relationship, ranges);
		}
		return extendedPatterns;
		
	}
	
	public   List<List<PatternRightHand>> combinePattern(Map<String,List<String>> extendedPattern){
	    List<List<PatternRightHand>> extendedList = new ArrayList<List<PatternRightHand>>();
	    List<String> keyIndexes = new ArrayList<String>();
	    Integer[] rangeIndexes = new Integer[extendedPattern.keySet().size()];
	    for(String key: extendedPattern.keySet()){
	        keyIndexes.add(key);
	    }
	    List<Integer[]> results = new ArrayList<Integer[]>();
	    for(int i=0;i<extendedPattern.get(keyIndexes.get(0)).size();i++){
	        rangeIndexes[0] = i;
	        results.addAll(recursiveCombinePattern(extendedPattern, keyIndexes, rangeIndexes, 1));
	    }
	    
	    for(Integer[] pattern: results){
	        List<PatternRightHand> listPRH = new ArrayList<PatternRightHand>();
	        for(int i=0;i<pattern.length;i++){
	            String relationship = keyIndexes.get(i);
	            TopLevelConcept.SUBHIERARCHY range = TopLevelConcept.SUBHIERARCHY_MAPPINGS.get(extendedPattern.get(relationship).get(pattern[i]));
	            PatternRightHand prh = new PatternRightHand(relationship,range);
	            listPRH.add(prh);
	        }
	        extendedList.add(listPRH);
	    }
	    
	    return extendedList;
	}

	public  List<Integer[]> recursiveCombinePattern(Map<String,List<String>> extendedPattern, List<String> keyIndexes, Integer[] rangeIndexes, int relationship){
	    if(relationship == rangeIndexes.length){
	        List<Integer[]> results = new ArrayList<Integer[]>();
	        Integer[] clone = new Integer[rangeIndexes.length];
	        for(int i=0;i<rangeIndexes.length;i++){
	        	clone[i] = rangeIndexes[i]; 
	        }
	        results.add(clone);
	        return results;
	    }
	    
	    List<Integer[]> results = new ArrayList<Integer[]>();
	    for(int i=0;i<extendedPattern.get(keyIndexes.get(relationship)).size();i++){
	        rangeIndexes[relationship] = i;
	        results.addAll(recursiveCombinePattern(extendedPattern, keyIndexes, rangeIndexes, relationship+1));
	    }
	    return results;
	}
	
}
