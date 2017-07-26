package GlobalSchema;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.SemiLD.Connector;

import Crawlers.Result;
import SharedProcesses.ReadXML;

public class SourceInterlinker {
	public static ArrayList<String> GS_elements;
	public static ArrayList<String> GS_elements_results;
	public static ArrayList<String> input_elements;
	public static ArrayList<String> all_elements;
	public static ArrayList<String> notMatched_elements;
	public static ArrayList<String> previous_input_elements = null;
	public static int numberOfProperties;
	public static ArrayList<Map<String, String>> final_results;
	public static PrintStream out;
	public static char domain;
	public static int numberResults;
	public static int threshold;

	public SourceInterlinker(SourceInterlinker si) {
		this.final_results = si.final_results;
		
	}
	public SourceInterlinker(char d, int n, int t, int gs) {
		domain = d;
		numberResults = n;
		threshold = t;
		GS_elements_results = new ArrayList<String>();
		final_results = new ArrayList<Map<String, String>>();
		all_elements = new ArrayList<String>();
		// try {
		String nameOfFile = "";
		if (domain == 'm')
			nameOfFile += "M_";
		else if (domain == 'g')
			nameOfFile += "G_";
		else if (domain == 'p')
			nameOfFile += "P_";
		nameOfFile += numberResults + "_" + threshold + "_GS" + gs + ".txt";
		/*
		 * out = new PrintStream( new FileOutputStream(
		 * "C:\\Users\\MSK137\\workspace\\SemiLD-Servlet\\WebContent\\WEB-INF\\files\\Evaluation\\"
		 * + nameOfFile)); } catch (FileNotFoundException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } System.setOut(out);
		 */
	}

	public static void match(ArrayList<Result> input,String fromWhere) {
		Map<String, String> map = new HashMap<String, String>();
		double similarity = 0;

		for (int x = 0; x < input.size(); x++) {
			map = new HashMap<String, String>();

			for (String key : input.get(x).predicates.keySet()) {

				for (int j = 0; j < GS_elements.size(); j++) {
					similarity = Connector.sim.getSim(
							removeInvalidXMLCharacters(key),
							removeInvalidXMLCharacters(GS_elements.get(j)));
						//	System.out.println(removeInvalidXMLCharacters(key)+"  - -  "+
							//		removeInvalidXMLCharacters(GS_elements.get(j))+"=>"+similarity);
					if (similarity > ((double) 70 / 100)) {
						
						map.put(GS_elements.get(j),
								input.get(x).predicates.get(key));

					}
				}

			}
		map.put("fromWhere", fromWhere);
		map.put("urrri", input.get(x).link);
			final_results.add(map);
		}

	//	System.out.println("------------");
	//	System.out.println("------------");

		}

	

	public void getGS(char d, int n) {
		try {
			GS_elements = new ReadXML().XML2ArrayList(d, n);

		//	System.out.println(Arrays.toString(GS_elements.toArray()));
			intialiseGS_elements_results();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<String> Convert2dTo1d(ArrayList<ArrayList<String>> s) {
		ArrayList<String> results = new ArrayList<String>();
		for (int i = 0; i < s.size(); i++) {
			for (int j = 0; j < s.get(i).size(); j++) {
				results.add(s.get(i).get(j));
			}
		}

		return removeDuplicates(results);
	}

	public static ArrayList<String> removeDuplicates(ArrayList<String> al) {
		all_elements.addAll(al);

		numberOfProperties += al.size();
		System.out.println("Number of Properties= " + numberOfProperties);
		Set<String> hs = new HashSet<>();
		hs.addAll(al);
		al.clear();
		al.addAll(hs);
		return al;
	}

	public static void intialiseGS_elements_results() {
		for (int i = 0; i < GS_elements.size(); i++) {
			GS_elements_results.add("");
		}
	}

	public static String removeInvalidXMLCharacters(String s) {
		StringBuilder out = new StringBuilder();
		String output = "";
		int codePoint;
		int i = 0;

		while (i < s.length()) {
			// This is the unicode code of the character.
			codePoint = s.codePointAt(i);
			if ((codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
					|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
					|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
					|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF))) {
				out.append(Character.toChars(codePoint));
			}
			i += Character.charCount(codePoint);
		}
		output = out.toString();
		output = output.replaceAll("-", " ");
		output = output.replaceAll("_", " ");
		output = output.replaceAll("[^A-Za-z]", " ");

		return output;
	}

	public void destroy() {

		GS_elements = null;
		GS_elements_results = null;
		input_elements = null;
		all_elements = null;
		notMatched_elements = null;
		previous_input_elements = null;
		numberOfProperties = 0;
		numberResults = 0;
		threshold = 0;
		 final_results=null;
	}

}
