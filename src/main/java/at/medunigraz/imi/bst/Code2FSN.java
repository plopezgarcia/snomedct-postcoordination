package at.medunigraz.imi.bst;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Code2FSN {
	private HashMap<String,String> map;
	private String inFile;
	
	public Code2FSN(String termFile){
		this.inFile = termFile;
		initializeTermMap();
	}
	
	public String getName(String code){
		return map.get(code);
	}
	
	private void initializeTermMap(){
		
		try{
			String line = "";
			HashMap<String,HashSet<String>> codes_synonyms = new HashMap<String,HashSet<String>>();
			HashMap<String,String> code_fsn = new HashMap<String,String>();
			
			BufferedReader br = new BufferedReader(new FileReader(inFile));
			while((line=br.readLine())!=null){
				if(line.isEmpty())continue;
				String[] tokens = line.split("\t",-1);
				if(codes_synonyms.containsKey(tokens[4])){
					HashSet<String> listSynonyms = codes_synonyms.get(tokens[4]);
					if(listSynonyms.contains(tokens[7])&&tokens[2].equals("0")){
						listSynonyms.remove(tokens[7]);//REMOVE INACTIVE
					}else{
						listSynonyms.add(tokens[7]);
					}
				}else{
					if(tokens[2].equals("1")){
						HashSet<String> listSynonyms = new HashSet<String>();
						listSynonyms.add(tokens[7]);
						codes_synonyms.put(tokens[4],listSynonyms);
					}
				}
			}
			
			for(String key: codes_synonyms.keySet()){
				HashSet<String> list = codes_synonyms.get(key);
				String fsn = "";
				String current = "";
				Iterator<String> listSyn = list.iterator();
				while(listSyn.hasNext()){
					current = listSyn.next();
					if(fsn.isEmpty()){
						fsn = current;
						continue;
					}
					if(fsn.contains("(") && current.contains("(")){
						if(fsn.length() < current.length()) fsn = current;
						continue;
					}
					if(fsn.contains("(")) continue;
					if(current.contains("(")) fsn=current;
					else{
						if(fsn.length()<current.length()) fsn = current;
					}
				}
				code_fsn.put(key,fsn);
			}
			br.close();
			map = code_fsn;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
