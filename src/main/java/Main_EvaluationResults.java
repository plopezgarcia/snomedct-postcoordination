import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main_EvaluationResults {
	public static final String EVALUATIONRESULTS = "C:/Users/JoseAntonio/Dropbox/BST_Papers/2016/MIE 2017/Postcoordination/pattern matching/evaluationTable.txt";
	public static final String MATCHEDPATTERNS = "C:/Users/JoseAntonio/Dropbox/BST_Papers/2016/MIE 2017/Postcoordination/pattern matching/matchingPatterns6-EvaluationFile.txt";
	public static void main(String[] args) {
		try{
			String line = "";
			BufferedReader br = new BufferedReader(new FileReader(EVALUATIONRESULTS));
			br.readLine();
			Map<String,List<Integer>> eval = new HashMap<String,List<Integer>>();
			int totalMatchedPatterns = 0;
			while((line=br.readLine())!=null){
				if(line.trim().isEmpty())continue;
				String[] tokens = line.split("\\t");
				if(tokens[8].equals("YES")) continue;
				int nMatchedPatterns = Integer.parseInt(tokens[2]);
				totalMatchedPatterns+=nMatchedPatterns;
				if(tokens[6].startsWith("1")){
					String id = tokens[0];
					String patternPositions = tokens[6].substring(tokens[6].indexOf("(")+1,tokens[6].indexOf(")"));
					List<Integer> listPositions = new ArrayList<Integer>();
					while(patternPositions.contains(",")){
						Integer position = Integer.parseInt(patternPositions.substring(0,patternPositions.indexOf(",")));
						listPositions.add(position);
						patternPositions = patternPositions.substring(patternPositions.indexOf(",")+1);
					}
					Integer position = Integer.parseInt(patternPositions);
					listPositions.add(position);
					eval.put(id, listPositions);
				}
			}
			br.close();
			
			br = new BufferedReader(new FileReader(MATCHEDPATTERNS));
			String currentId = "";
			Map<String,List<List<String[]>>> listAGs = new HashMap<String,List<List<String[]>>>();
			List<List<String[]>> listMPs = null;
			List<String[]> currentMP = null;
			String focusConcept = "";
			while((line=br.readLine())!=null){
				if(line.startsWith("Annotation group")){
					if(!currentId.isEmpty()){
						listMPs.add(currentMP);
						
					}
					currentId = line.substring(line.indexOf("group")+5, line.indexOf(":")).trim();
					listMPs = new ArrayList<List<String[]>>();
					listAGs.put(currentId, listMPs);
					currentMP = null;
					continue;
				}
				if(line.startsWith("Matching pattern:")){
					if(currentMP!=null && !currentMP.isEmpty()){
						listMPs.add(currentMP);
						focusConcept = "";
					}
					currentMP = new ArrayList<String[]>();
					continue;
				}
				if(line.contains("Full match:")) continue;
				if(line.endsWith("| :")){
					focusConcept = line.substring(0,line.indexOf("|")).trim();
				}else{
					if(line.contains("=")){
						String attribute = line.substring(0,line.indexOf("|")).trim();
						if(attribute.startsWith(",")) attribute = attribute.substring(1);
						String aux = line.substring(line.indexOf("=")+1);
						String object = aux.substring(0,aux.indexOf("|")).trim();
						
						String[] triple = new String[3];
						triple[0] = focusConcept;
						triple[1] = attribute;
						triple[2] = object;
						currentMP.add(triple);
					}
				}
			}
			if(!currentId.isEmpty()){
				listMPs.add(currentMP);
				listAGs.put(currentId, listMPs);
			}
			br.close();
			
			
			Map<String,List<String[]>> listGold = new HashMap<String,List<String[]>>();
			for(String id: eval.keySet()){
				List<Integer> listPositions = eval.get(id);
				if(listAGs.containsKey(id)){
					listGold.put(id, new ArrayList<String[]>());
					listMPs = listAGs.get(id);
					for(Integer pos: listPositions){
						List<String[]> listTriples = listMPs.get(pos-1);
						listGold.get(id).addAll(listTriples);
					}
				}else{
					System.out.println("It does not exist "+id);
				}
			}
			
			int nMatchedPatternsEval = 0;
			double avPosTotal = 0.0;
			int listMPsTotal = 0;
			int total = 0;
			for(String id: listAGs.keySet()){
				if(!eval.containsKey(id)) continue;
				listMPs = listAGs.get(id);
				if(listMPs == null || listMPs.isEmpty()){
					System.out.println("listMPs is EMPTY = "+id);
					continue;
				}
				int localEval = 0;
				int positions = 0;
				for(int i=0;i<listMPs.size();i++){
					List<String[]> MP = listMPs.get(i);
					//for(List<String[]> MP: listMPs){
					if(MP == null || MP.isEmpty()){
						System.out.println("MP is EMPTY = "+id);
						continue;
					}
					boolean isRedundant = true;
					List<String[]> MPGS = listGold.get(id);
					for(String[] triple: MP){
						boolean match = false;
						for(String[] triplegs: MPGS){
							if(triple[0].equals(triplegs[0]) && triple[1].equals(triplegs[1]) && triple[2].equals(triplegs[2])){
								match = true;
								break;
							}
						}
						if(!match){
							isRedundant = false;
							break;
						}
					}
					if(isRedundant){
						localEval++;
						positions+=i+1;
					}
				}
				
				double avPos = ((double)positions)/((double)localEval);
				avPosTotal+= avPos;
				listMPsTotal+= listMPs.size();
				total++;
				
				System.out.println(id+"->Matched patterns = "+localEval);
				System.out.println("\tAverage position matched patterns = "+avPos);
				System.out.println("\tTotal size of matched patterns list = "+listMPs.size());
				nMatchedPatternsEval+=localEval;
			}
			
			System.out.println("Meaningful patterns = "+nMatchedPatternsEval+"; Total matched patterns = "+totalMatchedPatterns);
			System.out.println("Average position matched patterns total -> "+avPosTotal/((double)total));
			System.out.println("Average position -> "+((double)listMPsTotal)/((double)total));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
