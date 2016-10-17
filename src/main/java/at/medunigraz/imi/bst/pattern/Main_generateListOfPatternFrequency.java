package at.medunigraz.imi.bst.pattern;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class generate the list of expanded patterns and their frequencies based on the precoordination of concept in SNOMED CT.
 * 
 * @version 1.0
 * */
public class Main_generateListOfPatternFrequency {
	public static final String StatedRelationshipsSCT = "src/main/resources/data/sct2_StatedRelationship_Snapshot_INT_20160131.txt";
	public static final String newPatternsFile = "src/main/resources/data/new-pattern-frequencies.txt";
	public static final String newExtendedPatternsFile = "src/main/resources/data/new-extended-pattern-frequencies.txt";
	
	public static void main(String[] args) {
		ParseSCTStatedRels psctsr = new ParseSCTStatedRels(StatedRelationshipsSCT);
		List<StatedRelationship> listSR = psctsr.getListStatedRelationships();
		
		List<PatternAdquisition> listPA = new ArrayList<PatternAdquisition>();
		for(StatedRelationship sr: listSR){
			PatternAdquisition pa = sr.generatePattern();
			boolean exists = false;
			for(PatternAdquisition pattern: listPA){
				if(pattern.compareTo(pa) == 0){
					exists = true;
					pattern.incrementFrequency();
					break;
				}
			}
			if(!exists) listPA.add(pa);
		}
		ExpandedPatterns ep = new ExpandedPatterns(listPA);
		List<PatternAdquisition> listEPA = ep.getExtendedPatterns();
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(newPatternsFile));
			for(PatternAdquisition pa: listPA){
				bw.write(pa.toString()+"\n");
			}
			bw.close();
			
			bw = new BufferedWriter(new FileWriter(newExtendedPatternsFile));
			for(PatternAdquisition pa: listEPA){
				bw.write(pa.getDescription()+"\n");
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		/*List<PatternAdquisition> listPA = psctsr.getListPatterns();
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(newPatternsFile));
			for(PatternAdquisition pa: listPA){
				bw.write(pa.getFrequency()+"\t[116680003-"+pa.getSourceCode());
				HashMap<RELATIONSHIP,SUBHIERARCHY> prh = pa.getPatternRightHand();
				if(!prh.isEmpty()){
					for(RELATIONSHIP rel: prh.keySet()){
						SUBHIERARCHY o_sub = prh.get(rel);
						String code = RelationshipConcept.getCode(rel);
						bw.write(", "+code+"-"+o_sub);
					}
				}
				bw.write("]\n");
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}*/
	}
}
