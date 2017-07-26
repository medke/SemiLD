package Crawlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Geonames {
	public static String geonames_jsonText;
	public static ArrayList<ArrayList<String>> geonames_elements = new ArrayList<ArrayList<String>>();
	public static JSONObject geonames_jsonObject;
	public static ArrayList<Result> geonames_results= new ArrayList<Result>();
	private static Result result;

	public Geonames() {
	}

	public static void readJsonFromUrl(String titre, int limit) throws IOException,
			JSONException {

		String year = "";
		titre = titre.replaceAll(" ", "+").toLowerCase();



		URL urli = new URL(
				"http://api.geonames.org/searchJSON?q="+titre+"&maxRows="+limit+"&username=mk21");

		URLConnection con = urli.openConnection();
		;
		con.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.237 Safari/534.10");

		InputStream is = con.getInputStream();

		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			// JSONArray inputArray=null ;
			if (jsonText.contains("error")
					&& jsonText.contains("Film not found"))
				System.out.println("error");
			// return inputArray;
			System.out.println(jsonText);
			geonames_jsonText = jsonText;
			// inputArray = new JSONArray(jsonText.substring(10,
			// jsonText.length()));
			JSONObject output = new JSONObject(jsonText);
			
			geonames_jsonObject = output;


			// return output;
		} finally {
			is.close();
		}
	}

	public static void readJsonFromUrl(String titre) throws IOException,
			JSONException {

		
		titre = titre.replaceAll(" ", "+").toLowerCase();

		
				URL urli = urli = new URL(
						"http://api.geonames.org/searchJSON?q="+titre+"&maxRows=4&username=mk21");


		URLConnection con = urli.openConnection();

		con.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.237 Safari/534.10");

		InputStream is = con.getInputStream();

		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			// JSONArray inputArray=null ;
			if (jsonText.contains("error")
					&& jsonText.contains("Film not found"))
				System.out.println("error");
			// return inputArray;
			System.out.println(jsonText);

			geonames_jsonText = jsonText;
			// inputArray = new JSONArray(jsonText.substring(10,
			// jsonText.length()));
			JSONObject output = new JSONObject(jsonText);
			geonames_jsonObject = output;

			// return output;
		} finally {
			is.close();
		}
	}

	public static void getElements() {

		ArrayList<String> temp_properties = new ArrayList();
	  
		JSONArray jsonArray = geonames_jsonObject.getJSONArray("geonames");
		for (int i = 0, size = jsonArray.length(); i < size; i++) {
			JSONObject objectInArray = jsonArray.getJSONObject(i);
			temp_properties.clear();
			// "...and get thier component and thier value."
			String[] elementNames = JSONObject.getNames(objectInArray);
			for (String elementName : elementNames) {
				//String value = objectInArray.get(elementName);
				temp_properties.add(prepareLabel(elementName));
			
			}
			geonames_elements.clear();
			geonames_elements.add(temp_properties);
		}
		
	}

	public static void getPlaces(String movieURI, int limit) throws IOException {
		movieURI = movieURI.replaceAll(" ", "+").toLowerCase();
		if(movieURI.endsWith("+"))
			movieURI = movieURI.substring(0, movieURI.length()-1);
		

		// URL urli = new URL("http://www.omdbapi.com/?r=json&s=" +
		// titre+"&page=1");
		URL urli = new URL(
				"http://api.geonames.org/searchJSON?q="+movieURI+"&maxRows="+limit+"&username=mk21");

		URLConnection con = urli.openConnection();
		System.out.println(urli.toString());
		con.setDoOutput(true);
		con.setRequestProperty("Accept", "application/json");

		InputStream is = con.getInputStream();

		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			// JSONArray inputArray=null ;
			if (jsonText.contains("error")
					&& jsonText.contains("Film not found"))
				System.out.println("error");
			// return inputArray;
			System.out.println(jsonText);
			geonames_jsonText = jsonText;
			// inputArray = new JSONArray(jsonText.substring(10,
			// jsonText.length()));
			JSONObject output = new JSONObject(jsonText);
			
			geonames_jsonObject = output;

			// return output;
		} finally {
			is.close();
		}
		System.out.println(geonames_jsonText);
		String element="",value="";
		JSONArray jsonArray = geonames_jsonObject.getJSONArray("geonames");

		

		for (int i = 0, size = jsonArray.length(); i < size; i++) {
			result= new Result(movieURI);
			JSONObject objectInArray = jsonArray.getJSONObject(i);
			Iterator it = objectInArray.keys();
			// "...and get thier component and thier value."
			   while (it.hasNext()) {
				      element = (String) it.next();
				      value= objectInArray.get(element).toString();
					result.predicates.put(element, value);
					System.out.println(element+":"+value);

				  			} 
				result.predicates.put("fromWhere", "IMDB");
				result.predicates.put("movieURI",  movieURI);
			   geonames_results.add(result);
			   }
			
		}
	
	
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}

		return sb.toString();
	}

	public static String prepareLabel(final String url){
	    // return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
	    return URLDecoder.decode(url).replace("#", "_").replaceAll("(.)([A-Z])", "$1_$2").replaceAll("\\d+.*", "");
	}
	public static void reset() {
		geonames_elements.clear();
		geonames_results= new ArrayList<Result>();;
	}

}
