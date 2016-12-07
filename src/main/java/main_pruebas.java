import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class main_pruebas {
	public static final String StatedRelationshipsSCT = "src/main/resources/data/sct2_StatedRelationship_Snapshot_INT_20160131.txt";
	public static void main(String[] args) {
		/*
		try{
			String line = "";
			BufferedReader br = new BufferedReader(new FileReader(StatedRelationshipsSCT));
			br.readLine();//header of the file
			HashSet<String> uniqueSources = new HashSet<String>();
			while((line=br.readLine())!=null){
				String[] tokens = line.split("\t");
				uniqueSources.add(tokens[4]);
			}
			br.close();
			System.out.println("Size = "+uniqueSources.size());
		}catch(Exception e){
			e.printStackTrace();
		}*/
		
		Random rn = new Random();
		rn.setSeed(System.currentTimeMillis());
		int n = 224;//Max		
		int remove = 224/4;
		HashSet<Integer> listRemove = new HashSet<Integer>();
		while(listRemove.size()<remove){
			int i = (rn.nextInt() % n) + 2;
			if(i<0) i = i*-1;
			listRemove.add(i);
		}
		List<Integer> listRes = new ArrayList<Integer>();
		listRes.addAll(listRemove);
		Collections.sort(listRes);
		for(Integer i: listRes){
			System.out.println(i);
		}
	}
}
