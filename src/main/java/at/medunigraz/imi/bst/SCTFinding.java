package at.medunigraz.imi.bst;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import at.medunigraz.imi.bst.TopLevelConcept.SUBHIERARCHY;

public class SCTFinding {
	public static final String TERMINOLOGY_FILE = "src/main/resources/data/SNOMED_ID_Ancestor.txt";
	private static SCTFinding singleton;
	private HashMap<String, String> map;
	
	public static SCTFinding getInstance(){
		if(singleton==null){
			singleton = new SCTFinding();
		}
		return singleton;
	}
	
	public SCTFinding(){
		map = new HashMap<String, String>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(TERMINOLOGY_FILE));
			String line = "";
			while((line = br.readLine()) != null){
				String[] tokens = line.split("\t");
				map.put(tokens[0], tokens[1]);
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public SUBHIERARCHY getAncestor(String sctid){
		String parent = map.get(sctid);
		for(SUBHIERARCHY value: TopLevelConcept.SUBHIERARCHY_MAPPINGS.keySet()){
			if(TopLevelConcept.SUBHIERARCHY_MAPPINGS.get(value).equals(parent)){
				return value;
			}
		}
		return null;
	}
}
