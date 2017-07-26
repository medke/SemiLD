package GlobalSchema;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.SemiLD.Connector;

import SharedProcesses.ReadXML;

public class InterlinkWithGS {
	public static ArrayList<String> GS_elements;
	public static ArrayList<String> input_elements;
	public static ArrayList<String> all_elements;
	public static ArrayList<String> notMatched_elements;
	public static ArrayList<String> previous_input_elements = null;
	public static int numberOfProperties;
	public static PrintStream out;
	public static char domain;
	public static int numberResults;
	public static int threshold;

	public InterlinkWithGS(char d, int n, int t,int gs) {
		domain = d;
		numberResults = n;
		threshold = t;
		all_elements = new ArrayList<String>();
	//	try {
			String nameOfFile = "";
			if (domain == 'm')
				nameOfFile += "M_";
			else if (domain == 'g')
				nameOfFile += "G_";
			else if (domain == 'p')
				nameOfFile += "P_";
			nameOfFile += numberResults + "_" + threshold + "_GS"+gs+".txt";
/*
			out = new PrintStream(
					new FileOutputStream(
							"C:\\Users\\MSK137\\workspace\\SemiLD-Servlet\\WebContent\\WEB-INF\\files\\Evaluation\\"
									+ nameOfFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.setOut(out);*/
	}

	public static void match() {
		System.out.println("      ----------------------");
		notMatched_elements = new ArrayList<String>();
		double similarity = 0;
		int notMatched = 0;
		boolean wasMatched = false;
		for (int i = 0; i < input_elements.size(); i++) {
			wasMatched = false;
			for (int j = 0; j < GS_elements.size(); j++) {
				similarity = Connector.sim.getSim(removeInvalidXMLCharacters(input_elements.get(i)),
						removeInvalidXMLCharacters(GS_elements.get(j)));
				if (similarity >  ((double) threshold / 100)) {
					System.out
							.println(removeInvalidXMLCharacters(input_elements
									.get(i))
									+ " <=>"
									+ removeInvalidXMLCharacters(GS_elements
											.get(j)));
					wasMatched = true;
				}
			}
			if (wasMatched == false) {
				notMatched_elements.add(input_elements.get(i));
				notMatched++;
			}

		}

		System.out.println("number of NON-MATCHES= " + notMatched);
		checkNotMatched();
		previous_input_elements = new ArrayList<>(input_elements);
		System.out.println("      ----------------------");
	}

	public void add2dInput(ArrayList<ArrayList<String>> input) {
		input_elements = Convert2dTo1d(input);
		if (previous_input_elements != null)
			input_elements.removeAll(previous_input_elements);
		match();

	}

	public void add1dInput(ArrayList<String> input) {
		input_elements = removeDuplicates(input);
		if (previous_input_elements != null)
			input_elements.removeAll(previous_input_elements);
		match();

	}

	public void getGS(char d, int n) {
		try {
			GS_elements = new ReadXML().XML2ArrayList(d, n);
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

	public static void checkNotMatched() {
		for (int i = 0; i < notMatched_elements.size(); i++) {
			System.out.println(removeInvalidXMLCharacters(notMatched_elements
					.get(i))
					+ " !!"
					+ Collections.frequency(all_elements,
							notMatched_elements.get(i)));
		}
		all_elements.clear();
		notMatched_elements.clear();
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
		input_elements = null;
		all_elements = null;
		notMatched_elements = null;
		previous_input_elements = null;
		numberOfProperties = 0;
		numberResults = 0;
		threshold = 0;
	}

}
