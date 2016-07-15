package at.medunigraz.imi.bst;

/**
 * This class represents a code in an annotation group and provides information about the hierarchy of SNOMED CT the code belongs to.
 * 
 * @version 1.0
 * */
public class AnnotationCode {
	private String code;
	private TopLevelConcept.SUBHIERARCHY hierarchy; /*CF, BS, SU, OR, PR, PO, QV, OE, SO, EV, PB, EG, SI, SN, SP, RA, ...*/

	public AnnotationCode (String code){
		this.code = code;
		findHierarchyRoot();
	}
	
	private void findHierarchyRoot(){
		hierarchy = SCTFinding.getInstance().getAncestor(code);
	}
	
	public String getCode(){
		return code;
	}
	
	public TopLevelConcept.SUBHIERARCHY getHierarchy(){
		return hierarchy;
	}
	
	public String toString(){
		return code+"("+hierarchy+")";
	}
}
