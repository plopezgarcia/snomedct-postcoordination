package at.medunigraz.imi.bst;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents a selected pattern from the list that matches the codes from the annotation group.
 * 
 * @version 1.1
 * */
public class PatternCombination implements Comparable<PatternCombination>{
	private PatternFrequency pf;
	private AnnotationGroup annotationGroup;
	private boolean isComplete; //Indicates whether the selected pattern is filled with the concepts from the annotation group.
	private Map<PatternRightHand, AnnotationCode> matchingMap;
	private AnnotationCode matchingTopLevel;
	private boolean isFullMatch; //Indicates whether the full set of codes from the annotation group is matched within the selected pattern.
	
	/*public int compareTo(PatternCombination pc) {
		if(this.isComplete && this.isFullMatch && (!pc.isComplete() || !pc.isFullMatch()))	return -1;
		if(!pc.isComplete() && !pc.isFullMatch() && (this.isComplete || this.isFullMatch))	return -1;
		if(this.isFullMatch && !pc.isFullMatch() && !this.isComplete && pc.isComplete())	return -1; 
		if(pc.isComplete() && pc.isFullMatch() && (!this.isComplete || !this.isFullMatch))	return 1;
		if(!this.isComplete && !this.isFullMatch && (pc.isComplete() || pc.isFullMatch()))	return 1;
		if(pc.isFullMatch() && !this.isFullMatch && !pc.isComplete() && this.isComplete)	return 1;
			
		if(this.getListCodes().size() != pc.getListCodes().size()) return pc.getListCodes().size() - this.getListCodes().size();
		return pc.getPattern().frequency - this.getPattern().frequency;
	}*/
	
	public int compareTo(PatternCombination pc) {
		return pc.getPattern().frequency - this.getPattern().frequency;
	}	
	
	public PatternFrequency getPattern(){
		return pf;
	}
	
	public List<AnnotationCode> getListCodes(){
		List<AnnotationCode> listCodes = new ArrayList<AnnotationCode>();
		listCodes.add(matchingTopLevel);
		for(PatternRightHand prh: matchingMap.keySet()){
			listCodes.add(matchingMap.get(prh));
		}
		
		return listCodes;
	}
	
	public AnnotationGroup getAnnotationGroup(){
		return annotationGroup;
	}
	
	public boolean isComplete(){
		return isComplete;
	}
	
	public boolean isFullMatch(){
		return isFullMatch;
	}
	
	public int getNumberMatchingCodes(){
		return (matchingMap.size()+1);//We add +1 in order to count the top level concept of the pattern.
	}
	
	public PatternCombination(PatternFrequency pf, AnnotationGroup annotationGroup, AnnotationCode matchingTopLevel, Map<PatternRightHand, AnnotationCode> matchingMap){
		this.pf					= pf;
		this.annotationGroup	= annotationGroup;
		this.matchingTopLevel	= matchingTopLevel;
		this.matchingMap		= matchingMap;
		this.isComplete			= (pf.pattern.patternRightHands.size() == matchingMap.size());
		this.isFullMatch		= ((matchingMap.size()+1) == annotationGroup.getListAnnotationCodes().size());
	}
	
	public AnnotationCode getMatchingTopLevel(){
		return matchingTopLevel;
	}
	
	public Map<PatternRightHand, AnnotationCode> getMatchingMap(){
		return matchingMap;
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof PatternCombination))
			return false;
		if(this == o)
			return true;
		
		PatternCombination pc = (PatternCombination)o;
		if(pf.equals(pc.pf)){
			if(matchingTopLevel.equals(pc.getMatchingTopLevel())){
				if(matchingMap.size() == pc.matchingMap.size()){
					for(PatternRightHand prh: matchingMap.keySet()){
						if(!pc.matchingMap.containsKey(prh) || !matchingMap.get(prh).equals(pc.matchingMap.get(prh))){
							return false;
						}
					}
					return true;
				}
			}
		}
    	
		return false;
	}
	
	public String toString(){
		String matching = "";
		for(PatternRightHand prh: matchingMap.keySet()){
			if(!matching.isEmpty()){
				matching+=" + ";
			}
			matching+=prh.relationship+":"+matchingMap.get(prh).getCode();
		}
		matching = matchingTopLevel.getCode()+"->["+matching+"]";
		
		String res = "Matching pattern:\n\t"+pf+"\n\tFull match:"+isFullMatch+"\tComplete pattern match:"+isComplete+"\n\t"+matching;
		return res;
	}
	
	public String toString(Code2FSN c2f){
		String matching = "";
		for(PatternRightHand prh: matchingMap.keySet()){
			if(!matching.isEmpty()){
				matching+="\t,";
			}else{
				matching+="\t";
			}
			AnnotationCode ac = matchingMap.get(prh);
			matching+=prh.relationship+" | "+c2f.getName(prh.relationship)+" | = "+ac.getCode()+" | "+c2f.getName(ac.getCode())+" |\n";
		}
		matching = matchingTopLevel.getCode()+" | "+c2f.getName(matchingTopLevel.getCode())+" | :\n"+matching+"\n";
		
		String res = "Matching pattern:\n\tFull match:"+isFullMatch+"\tComplete pattern match:"+isComplete+"\tFrequency:"+pf.frequency+"\n"+matching;
		return res;
	}
}
