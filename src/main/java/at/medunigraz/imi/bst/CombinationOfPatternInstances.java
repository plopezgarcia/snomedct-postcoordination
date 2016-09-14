package at.medunigraz.imi.bst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CombinationOfPatternInstances implements Comparable<CombinationOfPatternInstances>{
	private ArrayList<PatternCombination> listPC;
	private HashMap<String,ArrayList<String[]>> tripletsMap;
	private boolean isComplete;
	private boolean isFullMatch;
	private int frequency;
	
	public CombinationOfPatternInstances(ArrayList<PatternCombination> listPC){
		this.listPC	= listPC;
		isComplete	= true;
		frequency	= 0;
		tripletsMap = new HashMap<String, ArrayList<String[]>>();
		HashSet<String> listCodes = new HashSet<String>();
		for(PatternCombination pc: listPC){
			if(isComplete && !pc.isComplete()) isComplete = false;
			frequency += pc.getPattern().frequency;
			String focus = pc.getMatchingTopLevel().getCode();
			listCodes.add(focus);
			ArrayList<String[]> triplets = null;
			if(tripletsMap.containsKey(focus)){
				triplets = tripletsMap.get(focus);
			}else{
				triplets = new ArrayList<String[]>();
				tripletsMap.put(focus, triplets);
			}
			
			for(PatternRightHand prh: pc.getMatchingMap().keySet()){
				String relationship = prh.relationship;
				String target = pc.getMatchingMap().get(prh).getCode();
				listCodes.add(target);
				boolean newTriplet = true;
				for(String[] triplet: triplets){
					if(triplet[1].equals(relationship) && triplet[2].equals(target)){
						newTriplet = false;
						break;
					}
				}
				if(newTriplet){
					String[] triplet = new String[3];
					triplet[0] = focus;
					triplet[1] = relationship;
					triplet[2] = target;
					triplets.add(triplet);
				}
			}
		}
		
		AnnotationGroup ag = listPC.get(0).getAnnotationGroup();
		int nCodesGroup = ag.getListAnnotationCodes().size();
		isFullMatch = (nCodesGroup == listCodes.size());
	}

	public HashMap<String,ArrayList<String[]>> getTriplets(){
		return tripletsMap;
	}
	
	public int compareTo(CombinationOfPatternInstances copi) {
		if(this.isComplete && this.isFullMatch && (!copi.isComplete() || !copi.isFullMatch()))	return -1;
		if(!copi.isComplete() && !copi.isFullMatch() && (this.isComplete || this.isFullMatch))	return -1;
		if(this.isFullMatch && !copi.isFullMatch() && !this.isComplete && copi.isComplete())	return -1; 
		if(copi.isComplete() && copi.isFullMatch() && (!this.isComplete || !this.isFullMatch))	return 1;
		if(!this.isComplete && !this.isFullMatch && (copi.isComplete() || copi.isFullMatch()))	return 1;
		if(copi.isFullMatch() && !this.isFullMatch && !copi.isComplete() && this.isComplete)	return 1;
		
		//if(this.getListCodes().size() != pc.getListCodes().size()) return pc.getListCodes().size() - this.getListCodes().size();
		return copi.frequency - this.frequency;
	}

	public boolean isComplete(){
		return isComplete;
	}
	
	public boolean isFullMatch(){
		return isFullMatch;
	}
	
	public ArrayList<PatternCombination> getPatternCombinations(){
		return listPC;                                                                                                                                                                                                                                                                                                                                                                                                                                                      
	}
	
	public String toString(){
		String matching = "(\n";
		for(String key: tripletsMap.keySet()){
			String desc = "";
			for(String[] triplet: tripletsMap.get(key)){
				if(!desc.isEmpty()){
					desc+="\t, ";
				}else{
					desc+="\t  ";
				}
				desc+=triplet[1]+" | = "+triplet[2]+" |\n";
			}
			desc = key+" | :\n"+desc;
			matching+=desc;
		}
		matching += ")\n";
		
		String res = "Matching pattern:\n\tFull match:"+isFullMatch+"\tComplete pattern match:"+isComplete+"\n"+matching;
		return res;
	}
	
	public String toString(Code2FSN c2f){
		String matching = "(";
		for(String key: tripletsMap.keySet()){
			String desc = "";
			for(String[] triplet: tripletsMap.get(key)){
				if(!desc.isEmpty()){
					desc+="\t, ";
				}else{
					desc+="\t  ";
				}
				desc+=triplet[1]+" | "+c2f.getName(triplet[1])+" | = "+triplet[2]+" | "+c2f.getName(triplet[2])+" |\n";
			}
			desc = key+" | "+c2f.getName(key)+" | :\n"+desc;
			matching+="\n"+desc;
		}
		matching += ")\n";
		
		String res = "Matching pattern:\n\tFull match:"+isFullMatch+"\tComplete pattern match:"+isComplete+"\tFrequency:"+frequency+"\n"+matching;
		return res;
	}
}
