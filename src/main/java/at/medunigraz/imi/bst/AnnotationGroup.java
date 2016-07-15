package at.medunigraz.imi.bst;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the set of codes used in the annotation of a medical text with SNOMED CT. 
 * 
 * @version 1.0
 * */
public class AnnotationGroup {
	private List<AnnotationCode> annotationGroup;
	private String id;
	
	public AnnotationGroup(String id, List<String> listAnnotationCodes){
		this.id = id;
		annotationGroup = new ArrayList<AnnotationCode>();
		for(String code: listAnnotationCodes){
			annotationGroup.add(new AnnotationCode(code));
		}
	}
	
	public String getId(){
		return id;
	}
	
	public List<AnnotationCode> getListAnnotationCodes(){
		return annotationGroup;
	}
	
	public List<PatternCombination> getListPatternCombinations(List<PatternFrequency> listPatterns){
		List<PatternCombination> listSelectedPatterns = new ArrayList<PatternCombination>();
		if(annotationGroup.size()<2) return listSelectedPatterns;
		
		for(int i=annotationGroup.size();i>1;i--){//For each combination size get the matching patterns
			//'i' represents the number of elements for each combination
			Integer[] indexes = new Integer[i];
			for(int j=0;j<=(annotationGroup.size() - indexes.length);j++){
				indexes[0] = j;
				recursiveCombination(listSelectedPatterns, listPatterns, indexes, 1);
			}
		}
		
		return listSelectedPatterns;
	}
	
	
	private void recursiveCombination(List<PatternCombination> listSelectedPatterns, List<PatternFrequency> listPatterns, Integer[] indexes, int current){
		if(current >= indexes.length){
			List<AnnotationCode> listCodes = getCombinationCodes(indexes);
			List<PatternCombination> listMatchedPatterns = findMatchingPatterns(listPatterns, listCodes);
			listSelectedPatterns.addAll(listMatchedPatterns);//Probably we should avoid adding a patterns which is already in the list but with a larger set of codes than the new matching pattern.
			return;
		}
		
		for(int i = (indexes[(current - 1)]+1);i<=((annotationGroup.size() - indexes.length) + current);i++){
			indexes[current] = i;
			recursiveCombination(listSelectedPatterns, listPatterns, indexes, current+1);
		}
	}
	
	private List<AnnotationCode> getCombinationCodes(Integer[] indexes){
		List<AnnotationCode> listCodes = new ArrayList<AnnotationCode>();
		
		for(int i=0;i<indexes.length;i++){
			listCodes.add(annotationGroup.get(indexes[i]));
		}
		
		return listCodes;
	}
	
	public List<PatternCombination> findMatchingPatterns(List<PatternFrequency> listPatterns, List<AnnotationCode> listCodes){
		List<PatternCombination> listMatchingPatterns = new ArrayList<PatternCombination>();
		//Find all matching patterns with the combination of annotation codes and create the corresponding PatternCombinations
		for(PatternFrequency pf: listPatterns){
			List<PatternRightHand> listHierarchyConcepts = pf.pattern.patternRightHands;
			ArrayList<Integer> listMatchingCodes = new ArrayList<Integer>();
			for(PatternRightHand prh: listHierarchyConcepts){
				for(int h=0;h<listCodes.size();h++){
					if(listMatchingCodes.contains(h)) continue;
					AnnotationCode ac = listCodes.get(h);
					if(prh.range.equals(ac.getHierarchy())){
						listMatchingCodes.add(h);
					}
				}
			}
			if(listMatchingCodes.size()==listCodes.size()){
				PatternCombination pc = new PatternCombination(pf, listCodes, listHierarchyConcepts.size()==listCodes.size());
				listMatchingPatterns.add(pc);
			}
		}
		
		return listMatchingPatterns;
	}
}
