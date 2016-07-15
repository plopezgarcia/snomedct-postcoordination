package at.medunigraz.imi.bst;

import java.util.List;

/**
 * This class represents a selected pattern from the list that matches the codes from the annotation group.
 * 
 * @version 1.0
 * */
public class PatternCombination implements Comparable<PatternCombination>{
	private PatternFrequency pf;
	private List<AnnotationCode> listCodes;
	private boolean isComplete; //Indicates whether the selected pattern is filled with the concepts from the annotation group.
	public PatternCombination(PatternFrequency pf, List<AnnotationCode> listCodes, boolean isComplete){
		this.pf = pf;
		this.listCodes = listCodes;
		this.isComplete = isComplete;
	}
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
		return listCodes;
	}
	
	public boolean isComplete(){
		return isComplete;
	}
	
	public int getNumberCodes(){
		return listCodes.size();
	}
	
	public String toString(){
		String res = "Matching pattern:\n\t"+pf+"\n\tFull match:"+isComplete+"\n\tCodes:"+listCodes;
		return res;
	}
}
