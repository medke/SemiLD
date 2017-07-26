package SimiMatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.SemiLD.Connector;

public class SchemaMatching {

	public static ArrayList<ArrayList<ArrayList<String>>> sources = new ArrayList<ArrayList<ArrayList<String>>>();
	public static ArrayList<String> globalSchema = new ArrayList<String>();
	public static String currentSource="";

	public SchemaMatching() {}

	public static void addSource(ArrayList<ArrayList<String>> s_n,String c) {
		//sources.add(s_n);
		updateGlobalSchema(InternalSemanticDistinction(s_n));
		currentSource=c;
	}

	public static void updateGlobalSchema(ArrayList<String> s_n) {

		if (globalSchema.isEmpty()) {
			globalSchema.addAll(s_n);
		} else {
			ArrayList<String> temp_globalSchema = new ArrayList<String>();
			temp_globalSchema.addAll(removeDuplicates(globalSchema));
			temp_globalSchema.addAll(s_n);
			globalSchema.clear();
			globalSchema
					.addAll(SemanticDistinction(removeDuplicates(temp_globalSchema)));

		}
	}

	public static ArrayList<String> InternalSemanticDistinction(
			ArrayList<ArrayList<String>> s_n) {
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String> inputs = new ArrayList<String>();
		inputs.addAll(Convert2dTo1d(s_n));
		results.addAll(SemanticDistinction(inputs));
		System.out.println(currentSource+" | size of Semantically distinct properties= "+results.size()+" =>"+ Arrays.toString(results.toArray()));
		return results;
	}



	public static ArrayList<String> SemanticDistinction(ArrayList<String> s) {
		double similarity;
		ArrayList<String> results = new ArrayList<String>();
		List<Integer> index_toBe_Removed = new ArrayList<>();

		
		boolean beRemoved = false;
		
		
		for (int i = 0; i < s.size(); i++) {
			beRemoved = false;
			for (int j = 0; j < s.size(); j++) {
				if (i != j) {
					similarity = Connector.sim.getSim(s.get(i), s.get(j));
					if (similarity > 0.70) {
						beRemoved = true;
						System.out.println("similarity || "+s.get(i)+" | "+ s.get(j)+" = "+ similarity);
					}
				} else {

				}

			}
			if (beRemoved)
				index_toBe_Removed.add(i);

		}
		

		for (int i = 0; i < s.size(); i++) {
			if (!index_toBe_Removed.contains(i)) {
				results.add(s.get(i));
			} 

		}
		
		return results;
	}

	public static ArrayList<String> Convert2dTo1d(ArrayList<ArrayList<String>> s) {
		ArrayList<String> results = new ArrayList<String>();
		for (int i = 0; i < s.size(); i++)
			for (int j = 0; j < s.get(i).size(); j++)
				results.add(s.get(i).get(j));
		System.out.println(currentSource+" | size of Syntactically distinct properties= "+removeDuplicates(results).size()+" => "+Arrays.toString(removeDuplicates(results).toArray()));
		return removeDuplicates(results);
	}

	public static ArrayList<String> removeDuplicates(ArrayList<String> al) {
		Set<String> hs = new HashSet<>();
		hs.addAll(al);
		al.clear();
		al.addAll(hs);
		return al;
	}
	public static void reset()
	{
		globalSchema.clear();
		sources.clear();
	}
}
