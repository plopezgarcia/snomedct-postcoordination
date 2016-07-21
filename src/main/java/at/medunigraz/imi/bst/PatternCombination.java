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
	
	public int compareTo(PatternCombination pc) {
		if(this.isComplete == true && pc.isComplete != this.isComplete) return -1;
		if(this.isComplete == false && pc.isComplete!= this.isComplete) return 1;
		if(this.isComplete == pc.isComplete){
			if(this.getListCodes().size() != pc.getListCodes().size()) return pc.getListCodes().size() - this.getListCodes().size();
			return pc.getPattern().frequency - this.getPattern().frequency;
		}
		return 0;
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
	
	public int getNumberMatchingCodes(){
		return (matchingMap.size()+1);//We add +1 in order to count the top level concept of the pattern.
	}
	
	public PatternCombination(PatternFrequency pf, AnnotationGroup annotationGroup, AnnotationCode matchingTopLevel, Map<PatternRightHand, AnnotationCode> matchingMap){
		this.pf					= pf;
		this.annotationGroup	= annotationGroup;
		this.matchingTopLevel	= matchingTopLevel;
		this.matchingMap		= matchingMap;
		this.isComplete			= ((pf.pattern.patternRightHands.size()+1) == annotationGroup.getListAnnotationCodes().size());
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
		
		String res = "Matching pattern:\n\t"+pf+"\n\tFull match:"+isComplete+"\n\t"+matching;
		return res;
	}
}
