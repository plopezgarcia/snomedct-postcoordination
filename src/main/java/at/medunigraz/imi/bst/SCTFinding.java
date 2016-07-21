package at.medunigraz.imi.bst;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import at.medunigraz.imi.bst.TopLevelConcept.SUBHIERARCHY;

public class SCTFinding {
	public static final String TERMINOLOGY_FILE = "src/main/resources/data/SNOMED_ID_Ancestor.txt";
	private static SCTFinding singleton;
	//private HashMap<String, String> map;
	private HashMap<String, List<String>> map;
	
	public static SCTFinding getInstance(){
		if(singleton==null){
			singleton = new SCTFinding();
		}
		return singleton;
	}
	
	/*
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
	*/	
	 
	public SCTFinding(){
		map = new HashMap<String, List<String>>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(TERMINOLOGY_FILE));
			String line = "";
			while((line = br.readLine()) != null){
				String[] tokens = line.split("\t");
				ArrayList<String> listParents = new ArrayList<String>();
				for(int i=1;i<tokens.length;i++){
					listParents.add(tokens[i].trim());
				}
				map.put(tokens[0].trim(), listParents);
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<SUBHIERARCHY> getAncestors(String sctid){
		ArrayList<SUBHIERARCHY>	values = new ArrayList<SUBHIERARCHY>();
		List<String> listParents = map.get(sctid);
		for(String parent: listParents){
			for(SUBHIERARCHY value: TopLevelConcept.SUBHIERARCHY_MAPPINGS.keySet()){
				if(TopLevelConcept.SUBHIERARCHY_MAPPINGS.get(value).equals(parent)){
					values.add(value);
				}
			}
		}
		return values;
	}
	
}
