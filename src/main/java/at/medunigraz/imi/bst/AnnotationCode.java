package at.medunigraz.imi.bst;

import java.util.List;

/**
 * This class represents a code in an annotation group and provides information about the hierarchy of SNOMED CT the code belongs to.
 * 
 * @version 1.0
 * */
public class AnnotationCode {
	private String code;
	//private TopLevelConcept.SUBHIERARCHY hierarchy; /*CF, BS, SU, OR, PR, PO, QV, OE, SO, EV, PB, EG, SI, SN, SP, RA, ...*/
	private List<TopLevelConcept.SUBHIERARCHY> listParents;
	
	
	public AnnotationCode (String code){
		this.code = code;
		findHierarchyRoot();
	}
	
	/*
	private void findHierarchyRoot(){
		hierarchy = SCTFinding.getInstance().getAncestor(code);
	}
		
	public TopLevelConcept.SUBHIERARCHY getHierarchy(){
		return hierarchy;
	}
	
	public String toString(){
		return code+"("+hierarchy+")";
	}
	*/

	public String getCode(){
		return code;
	}
	
	public String toString(){
		String parentsCode = "";
		for(TopLevelConcept.SUBHIERARCHY hierarchy: listParents){
			if(!parentsCode.isEmpty()) parentsCode+=",";
			parentsCode+=hierarchy+"";
		}
		return code+"("+parentsCode+")";
	}
		
	public List<TopLevelConcept.SUBHIERARCHY> getParents(){
		return listParents;
	}
	
	public void findHierarchyRoot(){
		listParents = SCTFinding.getInstance().getAncestors(code);
	}
	
}
