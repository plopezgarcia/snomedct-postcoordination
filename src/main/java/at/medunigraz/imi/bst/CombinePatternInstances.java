package at.medunigraz.imi.bst;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CombinePatternInstances {
	private List<PatternCombination> listCombinePatterns;
	
	public CombinePatternInstances (List<PatternCombination> listPatterns){
		listCombinePatterns = listPatterns;
	}
	
	public ArrayList<CombinationOfPatternInstances> getCombinationsOfPatterns(){
		ArrayList<CombinationOfPatternInstances> resultList = new ArrayList<CombinationOfPatternInstances>();
		ArrayList<Integer> indexPatterns = new ArrayList<Integer>();
		for(int i=0;i<listCombinePatterns.size();i++){
			indexPatterns.add(i);
		}
		while(!indexPatterns.isEmpty()){
			int i = indexPatterns.get(0);
			indexPatterns.remove(0);
			ArrayList<PatternCombination> group = new ArrayList<PatternCombination>();
			group.add(listCombinePatterns.get(i));
			
			for(int j=0;j<listCombinePatterns.size();j++){
				if(isCompatible(group,listCombinePatterns.get(j))){
					indexPatterns.remove((Object)j);
					group.add(listCombinePatterns.get(j));
				}
			}
			
			CombinationOfPatternInstances copi = new CombinationOfPatternInstances(group);
			resultList = insertInto(resultList,copi);
			//resultList.add(copi);
		}
		
		return resultList;
	}
	
	private ArrayList<CombinationOfPatternInstances> insertInto(ArrayList<CombinationOfPatternInstances> listCOPI, CombinationOfPatternInstances copi){
		boolean insert = true;
		Map<String,ArrayList<String[]>> tripletsOut = copi.getTriplets();
		for(int i=0;i<listCOPI.size();i++){
			boolean containNewTriplet = false;
			Map<String,ArrayList<String[]>> tripletsIn = listCOPI.get(i).getTriplets();
			for(String key: tripletsOut.keySet()){
				if(!tripletsIn.containsKey(key)){
					containNewTriplet = true;
					break;
				}
				for(String[] tripletOut: tripletsOut.get(key)){
					boolean newTriplet = true;
					for(String[] tripletIn: tripletsIn.get(key)){
						if(tripletOut[0].equals(tripletIn[0]) && tripletOut[1].equals(tripletIn[1]) && tripletOut[2].equals(tripletIn[2])){
							newTriplet = false;
							break;
						}
					}
					if(newTriplet){
						containNewTriplet = true;
						break;
					}
				}
				if(containNewTriplet) break;
			}
			if(!containNewTriplet){
				insert = false;
				break;
			}
		}
		
		if(insert){
			ArrayList<CombinationOfPatternInstances> listRemove = new ArrayList<CombinationOfPatternInstances>();
			for(int i=0;i<listCOPI.size();i++){
				boolean containDifferentTriplets = false;
				Map<String,ArrayList<String[]>> tripletsIn = listCOPI.get(i).getTriplets();
				for(String key: tripletsOut.keySet()){
					if(!tripletsIn.containsKey(key)){
						continue;
					}
					for(String[] tripletIn: tripletsIn.get(key)){
						boolean differentTriplet = true;
						for(String[] tripletOut: tripletsOut.get(key)){
							if(tripletOut[0].equals(tripletIn[0]) && tripletOut[1].equals(tripletIn[1]) && tripletOut[2].equals(tripletIn[2])){
								differentTriplet = false;
								break;
							}
						}
						if(differentTriplet){
							containDifferentTriplets = true;
							break;
						}
					}
					if(containDifferentTriplets) break;
				}
				if(!containDifferentTriplets){
					listRemove.add(listCOPI.get(i));
				}
			}
			for(CombinationOfPatternInstances instance: listRemove){
				listCOPI.remove(instance);
			}
			listCOPI.add(copi);
		}
		
		
		return listCOPI;
	}
	
	
	private boolean isCompatible(ArrayList<PatternCombination> group, PatternCombination pattern){
		String focus = pattern.getMatchingTopLevel().getCode();
		Map<PatternRightHand, AnnotationCode> map = pattern.getMatchingMap();
		for(PatternRightHand prh: map.keySet()){
			String rel		= prh.relationship;
			String target	= map.get(prh).getCode();
			for(PatternCombination pat: group){
				String focusGroup = pat.getMatchingTopLevel().getCode();
				Map<PatternRightHand, AnnotationCode> mapGroup = pat.getMatchingMap();
				for(PatternRightHand prhGroup: mapGroup.keySet()){
					String relGroup		= prhGroup.relationship;
					String targetGroup	= mapGroup.get(prhGroup).getCode();
					if(focus.equals(focusGroup) && target.equals(targetGroup) && !rel.equals(relGroup)) return false;
					if(focus.equals(targetGroup) && target.equals(focusGroup)) return false;
				}
			}	
		}
		return true;
	}
}
