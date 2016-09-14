package at.medunigraz.imi.bst;

import java.util.List;

/**
 * This class represents a code in an annotation group and provides information about its ancestors is SNOMED CT.
 * 
 * @version 1.1
 * */
public class AnnotationCode {
	private String code;
	private List<ConceptModelConcepts.SUBHIERARCHY> listParents;
	
	
	public AnnotationCode (String code){
		this.code = code;
		findHierarchyRoot();
	}
	
	public String getCode(){
		return code;
	}
	
	public String toString(){
		String parentsCode = "";
		for(ConceptModelConcepts.SUBHIERARCHY hierarchy: listParents){
			if(!parentsCode.isEmpty()) parentsCode+=",";
			parentsCode+=hierarchy+"";
		}
		return code+"("+parentsCode+")";
	}
		
	public List<ConceptModelConcepts.SUBHIERARCHY> getParents(){
		return listParents;
	}
	
	private void findHierarchyRoot(){
		listParents = SCTFinding.getInstance().getAncestors(code);
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof AnnotationCode))
			return false;
		if(this == o)
			return true;
		
		AnnotationCode ac = (AnnotationCode)o;
		
		return this.code.equals(ac.getCode());
	}
}