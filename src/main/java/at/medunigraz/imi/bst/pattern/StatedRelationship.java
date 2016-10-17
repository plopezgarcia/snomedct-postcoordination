package at.medunigraz.imi.bst.pattern;

import java.util.ArrayList;
import java.util.List;

import at.medunigraz.imi.bst.ConceptModelConcepts.SUBHIERARCHY;
import at.medunigraz.imi.bst.RelationshipConcept;
import at.medunigraz.imi.bst.RelationshipConcept.RELATIONSHIP;
import at.medunigraz.imi.bst.SCTFinding;

/**
 * This class represents the stated relationships of a particular precoordinated concept in SNOMED-CT.
 * 
 * @version 1.0
 * */
public class StatedRelationship {
	private String sourceCode;
	private List<RightHandStatedRel> lrhsr;
	
	public StatedRelationship(String sourceCode){
		this.sourceCode = sourceCode;
		lrhsr = new ArrayList<RightHandStatedRel>();
	}
	
	public String getSourceCode(){
		return sourceCode;
	}
	
	public void addRelObj(String id, String rel, String obj){
		boolean exists = false;
		for(RightHandStatedRel rhsr: lrhsr){
			if(rhsr.getId().equals(id)){
				rhsr.addRelObj(rel, obj);
				exists = true;
				break;
			}
		}
		if(!exists){
			RightHandStatedRel rhsr = new RightHandStatedRel(id);
			rhsr.addRelObj(rel, obj);
			lrhsr.add(rhsr);
		}
	}
	
	public PatternAdquisition generatePattern(){
		SCTFinding sctf = SCTFinding.getInstance();
		SCTMRCM sctm = SCTMRCM.getInstance();
		List<SUBHIERARCHY> domain = sctf.getAncestors(sourceCode);
		if(domain.contains(SUBHIERARCHY.SM)) domain.remove(SUBHIERARCHY.SM);
		PatternAdquisition pa = new PatternAdquisition(domain);
		for(RightHandStatedRel rhsr: lrhsr){
			for(String rel: rhsr.getStatedRels().keySet()){
				RELATIONSHIP relationship = RelationshipConcept.RELATIONSHIP_MAPPINGS.get(rel);
				List<SUBHIERARCHY> object = sctf.getAncestors(rhsr.getStatedRels().get(rel));
				List<SUBHIERARCHY> rel_range = sctm.getTarget(relationship);
				List<SUBHIERARCHY> range = new ArrayList<SUBHIERARCHY>();
				for(SUBHIERARCHY obj: rel_range){
					if(object.contains(obj)) range.add(obj);
				}
				
				if(range.contains(SUBHIERARCHY.SM)) range.remove(SUBHIERARCHY.SM);
				if(!range.isEmpty() && relationship != null)	pa.addPatternRightHand(rhsr.getId(), relationship, range);
			}
		}
			
		return pa;
	}
	
	public String toString(){
		String result = "[";
		String group = "";
		result+="116680003-"+sourceCode+",";
		group = "";
		for(RightHandStatedRel rhsr: lrhsr){
			if(!group.isEmpty()) group+=",";
			group+="{"+rhsr.toString()+"}";
		}
		result+=group;
		result+="]";
		return result;
	}
}
