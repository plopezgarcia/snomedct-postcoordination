package at.medunigraz.imi.bst.pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import at.medunigraz.imi.bst.ConceptModelConcepts;
import at.medunigraz.imi.bst.ConceptModelConcepts.SUBHIERARCHY;
import at.medunigraz.imi.bst.RelationshipConcept;
import at.medunigraz.imi.bst.RelationshipConcept.RELATIONSHIP;

/**
 * This class parses the content of the file that gathers the ranges of each relationship in the SNOMED-CT concept model.
 * 
 * @version 1.0
 * */
public class SCTMRCM {
	public static final String MRCM_FILE = "src/main/resources/data/MRCM.csv";
	private static SCTMRCM singleton;
	private Map<RELATIONSHIP, List<SUBHIERARCHY>> map;
	public static SCTMRCM getInstance(){
		if(singleton==null){
			singleton = new SCTMRCM();
		}
		return singleton;
	}
	
	public SCTMRCM(){
		map = new HashMap<RELATIONSHIP, List<SUBHIERARCHY>>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(MRCM_FILE));
			String line = "";
			while((line=br.readLine()) != null){
				String rel = line.substring(0,line.indexOf("[")).trim();
				RELATIONSHIP relation = RelationshipConcept.RELATIONSHIP_MAPPINGS.get(rel);
				String range = line.substring(line.indexOf("[")+1,line.indexOf("]")).trim();
				List<SUBHIERARCHY> listSUB = new ArrayList<SUBHIERARCHY>();
				String[] concs = range.split(",");
				for(String conc: concs){
					listSUB.add(ConceptModelConcepts.SUBHIERARCHY_MAPPINGS.get(conc));
				}
				map.put(relation, listSUB);
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<SUBHIERARCHY> getTarget(RELATIONSHIP rel){
		ArrayList<SUBHIERARCHY>	values = new ArrayList<SUBHIERARCHY>();
		List<SUBHIERARCHY> targets = map.get(rel);
		if(targets!=null){
			values.addAll(targets);
		}
		return values;
	}
}
