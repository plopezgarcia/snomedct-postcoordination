package at.medunigraz.imi.bst;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

/**
 * This class parses the list of annotations from the reference standard and obtains a list of set of codes
 * 
 * @version 1.0
 * */
public class ParseAnnotationFile {
	public static final String ANNOTATIONS_FILE = "src/main/resources/data/referenceStandard.csv";
	
	public static HashMap<String, List<String>> getListOfAnnotationGroups(){
		HashMap<String, List<String>> results = new HashMap<String, List<String>>();
		try{
			CSVReader reader = new CSVReader(new FileReader(ANNOTATIONS_FILE), ';');
	        String[] nextLine;
	        while ((nextLine = reader.readNext()) != null) {
	        	if(nextLine[0].equals("Doc ID")) break;
	        }
	        
	        while ((nextLine = reader.readNext()) != null) {
	        	String docId = nextLine[0];
	        	String chunkId = nextLine[2];
	        	String key = docId+"_"+chunkId;
	        	if(chunkId.isEmpty()) continue;
	        	String codesValue = nextLine[9];
	        	if(codesValue.isEmpty())continue;
	        	List<String> listCodes = new ArrayList<String>();
	        	
	        	if(codesValue.contains(";")){
	        		String[] tokens = codesValue.split(";");
	        		
	        		for(String code:tokens){
	        			if(code.contains("|")){
		        			code = code.substring(0,code.indexOf("|"));
		        			listCodes.add(code.trim());
		        		}else{
		        			listCodes.add(code.trim());
		        		}
	        		}
	        	}else{
	        		if(codesValue.contains("|")){
		        		codesValue = codesValue.substring(0,codesValue.indexOf("|"));
		        		listCodes.add(codesValue.trim());
		        	}else{
		        		listCodes.add(codesValue.trim());
		        	}
	        	}
	        	if(results.containsKey(key)){
	        		List<String> previousCodes = results.get(key);
	        		for(String code: listCodes){
	        			if(!previousCodes.contains(code)) previousCodes.add(code);
	        		}
	        	}else{
	        		results.put(key, listCodes);
	        	}
	        }
	        
	        reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return results;
	}
}
