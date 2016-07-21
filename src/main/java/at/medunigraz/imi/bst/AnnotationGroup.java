package at.medunigraz.imi.bst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class represents the set of codes used in the annotation of a medical text with SNOMED CT. 
 * 
 * @version 1.1
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
	/*
	public List<PatternCombination> findMatchingPatterns(List<PatternFrequency> listPatterns, List<AnnotationCode> listCodes){
		List<PatternCombination> listMatchingPatterns = new ArrayList<PatternCombination>();
		//Find all matching patterns with the combination of annotation codes and create the corresponding PatternCombinations
		for(PatternFrequency pf: listPatterns){
			List<TopLevelConcept.SUBHIERARCHY> listConcepts = new ArrayList<TopLevelConcept.SUBHIERARCHY>();
			listConcepts.add(pf.pattern.topLevelConcept);
			List<PatternRightHand> listHierarchyConcepts = pf.pattern.patternRightHands;
			for(PatternRightHand prh: listHierarchyConcepts){
				listConcepts.add(prh.range);
			}
			ArrayList<Integer> listMatchingCodes = new ArrayList<Integer>();
			for(TopLevelConcept.SUBHIERARCHY prh: listConcepts){
				for(int h=0;h<listCodes.size();h++){
					if(listMatchingCodes.contains(h)) continue;
					AnnotationCode ac = listCodes.get(h);
					if(prh.equals(ac.getHierarchy())){
						listMatchingCodes.add(h);
					}
				}
			}
			if(listMatchingCodes.size()==listCodes.size()){
				PatternCombination pc = new PatternCombination(pf, listCodes, listConcepts.size()==listCodes.size());
				listMatchingPatterns.add(pc);
			}
		}
		
		return listMatchingPatterns;
	}*/
	
	
	//OPCION 1: PatternFrequency tiene solo un posible concepto como rango para cada relación
	public List<PatternCombination> findMatchingPatterns(List<PatternFrequency> listPatterns, List<AnnotationCode> listCodes){
		List<PatternCombination> listMatchingPatterns = new ArrayList<PatternCombination>();
		//Find all matching patterns with the combination of annotation codes and create the corresponding PatternCombinations
		for(PatternFrequency pf: listPatterns){
			List<PatternCombination> listBestMatching = getBestMatching(pf, listCodes);
			listMatchingPatterns.addAll(listBestMatching);
		}
		
		return listMatchingPatterns;
	}
	
	
	public List<PatternCombination> getBestMatching(PatternFrequency pf, List<AnnotationCode> listCodes){
		List<PatternCombination> results = new ArrayList<PatternCombination>();
		PatternRightHand prhTop = new PatternRightHand("Top", pf.pattern.topLevelConcept);
		for(int i=0;i<listCodes.size();i++){
			AnnotationCode ac  = listCodes.get(i);
			if(ac.getParents().contains(pf.pattern.topLevelConcept)){
				Map<PatternRightHand,AnnotationCode> matchingMap = new HashMap<PatternRightHand,AnnotationCode>();
				matchingMap.put(prhTop, ac);
				List<PatternCombination> listBestMatchingPatterns = getBestPatternMatching(recursiveMatchingConcepts(matchingMap, pf, listCodes, 1), pf, listCodes);
				results.addAll(listBestMatchingPatterns);
			}
		}
				
		return results;
	}
	
	private List<PatternCombination> getBestPatternMatching(List<Map<PatternRightHand,AnnotationCode>> listMatchingPatterns, PatternFrequency pf, List<AnnotationCode> listCodes){
		List<PatternCombination> results = new ArrayList<PatternCombination>();
		int maxSize = 0;
		for(Map<PatternRightHand, AnnotationCode> map: listMatchingPatterns){
			if(map.size()> maxSize) maxSize = map.size();
		}
		
		for(Map<PatternRightHand, AnnotationCode> map: listMatchingPatterns){
			if(map.size()== maxSize){
				AnnotationCode topCode = null;
				PatternRightHand topKey = null;
				for(PatternRightHand prh: map.keySet()){
					if(prh.relationship.equals("Top")){
						topKey = prh;
						topCode = map.get(prh);
						break;
					}
				}
				map.remove(topKey);
				PatternCombination pc = new PatternCombination(pf, listCodes, map.size() == listCodes.size(), topCode, map);
				results.add(pc);
			}
		}
		
		return results;
	}
	
	private List<Map<PatternRightHand,AnnotationCode>> recursiveMatchingConcepts(Map<PatternRightHand,AnnotationCode> matchingMap, PatternFrequency pf, List<AnnotationCode> listCodes, int depth){
		
		if((depth == listCodes.size()) || (depth == (pf.pattern.patternRightHands.size()+1))){
			List<Map<PatternRightHand, AnnotationCode>> results = new ArrayList<Map<PatternRightHand, AnnotationCode>>();
			if(matchingMap.size()>1){
				Map<PatternRightHand, AnnotationCode> res = new HashMap<PatternRightHand, AnnotationCode>();
				for(PatternRightHand prh: matchingMap.keySet()){
					res.put(prh, matchingMap.get(prh));
				}				
				results.add(res);
			}
			return results;
		}
		
		List<Map<PatternRightHand,AnnotationCode>> results = new ArrayList<Map<PatternRightHand,AnnotationCode>>();
		for(int i=0;i<listCodes.size();i++){
			boolean cont = false;
			for(PatternRightHand prh: matchingMap.keySet()){
				if(listCodes.get(i).equals(matchingMap.get(prh))){
					cont = true;
					break;
				}
			}
			if(cont) continue;
			for(int j=0;j<pf.pattern.patternRightHands.size();j++){
				if(matchingMap.keySet().contains(pf.pattern.patternRightHands.get(j))) continue;
				if(listCodes.get(i).getParents().contains(pf.pattern.patternRightHands.get(j).range)){
					matchingMap.put(pf.pattern.patternRightHands.get(j), listCodes.get(i));
					results.addAll(recursiveMatchingConcepts(matchingMap, pf, listCodes, depth+1));
					matchingMap.remove(pf.pattern.patternRightHands.get(j));
				}
			}
		}
		return results;
	}
}
