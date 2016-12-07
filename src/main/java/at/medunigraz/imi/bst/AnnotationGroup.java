package at.medunigraz.imi.bst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class represents the set of codes used in the annotation of a medical text with SNOMED CT.
 * It is able to find which patterns match the set of annotation codes.
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
	
	public List<PatternCombination> getListNonRedundantPatterns(List<PatternCombination> listPatterns){
		ArrayList<PatternCombination> listNonRedundantPatterns = new ArrayList<PatternCombination>();
		
		for(PatternCombination pc0: listPatterns){
			boolean toInsert = true;
			for(PatternCombination pc1: listPatterns){
				if(!pc0.equals(pc1)){
					if(areEqual(pc0,pc1)){
						if(pc0.getPattern().frequency < pc1.getPattern().frequency){
							toInsert = false;
							break;
						}
						if(pc0.getPattern().frequency == pc1.getPattern().frequency){
							if(!pc0.isComplete() && pc1.isComplete()){
								toInsert = false;
								break;
							}else{
								if(listPatterns.indexOf(pc0) > listPatterns.indexOf(pc1)){
									toInsert = false;
									break;
								}
							}
						}
					}else{
						if(isIncluded(pc0,pc1)){ // is pc0 C pc1
							toInsert = false;
							break;
						}
					}
				}
			}
			if(toInsert) listNonRedundantPatterns.add(pc0);
		}		
		return listNonRedundantPatterns;
	}
	
	private boolean areEqual(PatternCombination pc0, PatternCombination pc1){
		String source1 = pc1.getMatchingTopLevel().getCode();
		String source0 = pc0.getMatchingTopLevel().getCode();
		if(source1.equals(source0)){
			if(pc0.getMatchingMap().size() == pc1.getMatchingMap().size()){
				for(PatternRightHand prh0: pc0.getMatchingMap().keySet()){
					String relationship0 = prh0.relationship;
					String object0 = pc0.getMatchingMap().get(prh0).getCode();
					boolean isIncluded = false;
					for(PatternRightHand prh1: pc1.getMatchingMap().keySet()){
						String relationship1 = prh1.relationship;
						String object1 = pc1.getMatchingMap().get(prh1).getCode();
						if(relationship1.equals(relationship0) && object1.equals(object0)){
							isIncluded = true;
							break;
						}
					}
					if(!isIncluded) return false;
				}
				return true;
			}
			return false;
		}
		return false;
	}
	
	private boolean isIncluded(PatternCombination pc0, PatternCombination pc1){
		String source1 = pc1.getMatchingTopLevel().getCode();
		String source0 = pc0.getMatchingTopLevel().getCode();
		if(source1.equals(source0)){
			for(PatternRightHand prh0: pc0.getMatchingMap().keySet()){
				String relationship0 = prh0.relationship;
				String object0 = pc0.getMatchingMap().get(prh0).getCode();
				boolean isIncluded = false;
				for(PatternRightHand prh1: pc1.getMatchingMap().keySet()){
					String relationship1 = prh1.relationship;
					String object1 = pc1.getMatchingMap().get(prh1).getCode();
					if(relationship1.equals(relationship0) && object1.equals(object0)){
						isIncluded = true;
						break;
					}
				}
				if(!isIncluded) return false;
			}
			return true;
		}
		return false;
	}
	
	private void recursiveCombination(List<PatternCombination> listSelectedPatterns, List<PatternFrequency> listPatterns, Integer[] indexes, int current){
		if(current >= indexes.length){			
			List<AnnotationCode> listCodes = getCombinationCodes(indexes);
			/*for(AnnotationCode ac: listCodes){
				System.out.print(ac+", ");
			}
			System.out.println();*/
			List<PatternCombination> listMatchedPatterns = findMatchingPatterns(listPatterns, listCodes);
			for(PatternCombination pc: listMatchedPatterns){
				if(!listSelectedPatterns.contains(pc)) listSelectedPatterns.add(pc);
			}
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
	
	private List<PatternCombination> findMatchingPatterns(List<PatternFrequency> listPatterns, List<AnnotationCode> listCodes){
		List<PatternCombination> listMatchingPatterns = new ArrayList<PatternCombination>();
		//Find all matching patterns with the combination of annotation codes and create the corresponding PatternCombinations
		for(PatternFrequency pf: listPatterns){
			if(pf.pattern.patternRightHands.size()+1 < listCodes.size()) continue;
			List<PatternCombination> listBestMatching = getBestMatching(pf, listCodes);
			for(PatternCombination pc: listBestMatching){
				if(!listMatchingPatterns.contains(pc)){
					//System.out.println(pc);
					listMatchingPatterns.add(pc);
				}
			}
		}
		
		return listMatchingPatterns;
	}
	
	private List<PatternCombination> getBestMatching(PatternFrequency pf, List<AnnotationCode> listCodes){
		List<PatternCombination> results = new ArrayList<PatternCombination>();
		PatternRightHand prhTop = new PatternRightHand("Top", pf.pattern.topLevelConcept);
		for(int i=0;i<listCodes.size();i++){
			AnnotationCode ac  = listCodes.get(i);
			if(ac.getParents().contains(pf.pattern.topLevelConcept)){
				Map<PatternRightHand,AnnotationCode> matchingMap = new HashMap<PatternRightHand,AnnotationCode>();
				matchingMap.put(prhTop, ac);
				List<PatternCombination> listBestMatchingPatterns = getBestPatternMatching(recursiveMatchingConcepts(matchingMap, pf, listCodes, 1), pf, listCodes);
				for(PatternCombination pc: listBestMatchingPatterns){
					if(!results.contains(pc))	results.add(pc);
				}
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
				
				PatternCombination pc = new PatternCombination(pf, this, topCode, map);
				if(!results.contains(pc)){
					results.add(pc);
				}
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
