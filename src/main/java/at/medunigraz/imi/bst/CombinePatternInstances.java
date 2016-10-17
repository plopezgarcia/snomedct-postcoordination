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
		/*ArrayList<Integer> indexPatterns = new ArrayList<Integer>();
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
		}*/
		
		/*if(listCombinePatterns.size()>20){
			for(PatternCombination pc: listCombinePatterns){
				String code = pc.getMatchingTopLevel().getCode();
				for(PatternRightHand prh: pc.getMatchingMap().keySet()){
					System.out.println(code+";"+prh.relationship+";"+pc.getMatchingMap().get(prh).getCode());
				}
				System.out.println();
			}
		}*/
		
		if(listCombinePatterns.size() > 0){
			ArrayList<PatternCombination> perm = new ArrayList<PatternCombination>();
			int nMax = 5;
			if(listCombinePatterns.size()<nMax) nMax = listCombinePatterns.size();
			perm2(resultList, listCombinePatterns, perm, nMax);
		}
		return resultList;
	}
	
	
	private void perm2(List<CombinationOfPatternInstances> resultList, List<PatternCombination> source, List<PatternCombination> perm, int n){
		if(n==0){
			/*ArrayList<PatternCombination> group = new ArrayList<PatternCombination>();
			for(PatternCombination pc: perm){
				if(isCompatible(group, pc)){
					group.add(pc);
				}
			}
			
			for(PatternCombination pc: perm){
				System.out.print(listCombinePatterns.indexOf(pc)+" ");
			}
			System.out.println();*/
			
			CombinationOfPatternInstances copi = new CombinationOfPatternInstances(perm);
			resultList = insertInto(resultList,copi);
		}else{
			for(PatternCombination pc: source){
				if(!perm.contains(pc) && isCompatible(perm, pc)){
					ArrayList<PatternCombination> currentPerm = new ArrayList<PatternCombination>();
					currentPerm.addAll(perm);
					currentPerm.add(pc);
					ArrayList<PatternCombination> newSource = new ArrayList<PatternCombination>();
					for(PatternCombination aux: source){
						if(!currentPerm.contains(aux) && isCompatible(newSource,aux)){
							newSource.add(aux);
						}
					}
					perm2(resultList, newSource, currentPerm, n-1);
				}
			}
			if(!perm.isEmpty()){
				CombinationOfPatternInstances copi = new CombinationOfPatternInstances(perm);
				resultList = insertInto(resultList,copi);
			}
		}
	}
	
	private List<CombinationOfPatternInstances> insertInto(List<CombinationOfPatternInstances> listCOPI, CombinationOfPatternInstances copi){
		boolean insert = true;
		Map<String,ArrayList<String[]>> tripletsOut = copi.getTriplets();
		for(int i=0;i<listCOPI.size();i++){
			boolean hasNewTriplet = false;
			Map<String,ArrayList<String[]>> tripletsIn = listCOPI.get(i).getTriplets();
			for(String key: tripletsOut.keySet()){
				if(!tripletsIn.containsKey(key)){
					hasNewTriplet = true;
					break;
				}else{
					boolean tripletExistsInSet = true;
					for(String[] tripletOut: tripletsOut.get(key)){
						boolean tripletExists = false;
						for(String[] tripletIn: tripletsIn.get(key)){
							if(tripletOut[0].equals(tripletIn[0]) && tripletOut[1].equals(tripletIn[1]) && tripletOut[2].equals(tripletIn[2])){
								tripletExists = true;
								break;
							}
						}
						if(!tripletExists){
							tripletExistsInSet = false;
							break;
						}
					}
					if(!tripletExistsInSet){
						hasNewTriplet = true;
						break;
					}
				}
			}
			if(!hasNewTriplet){
				insert = false;
				break;
			}
		}
		if(insert){
			listCOPI.add(copi);
		}
		return listCOPI;
	}
	
	/*
	private List<CombinationOfPatternInstances> insertInto(List<CombinationOfPatternInstances> listCOPI, CombinationOfPatternInstances copi){
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
	*/
	
	private boolean isCompatible(List<PatternCombination> group, PatternCombination pattern){
		if(group.isEmpty()) return true;
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
					if(!focus.equals(focusGroup) && target.equals(targetGroup) && !rel.equals(relGroup)) return false;
				}
			}
		}
		return true;
	}
}
