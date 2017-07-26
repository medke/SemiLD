package Crawlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class IMDB {
	public static String imdb_jsonText;
	public static ArrayList<ArrayList<String>> imdb_elements = new ArrayList<ArrayList<String>>();
	public static JSONObject imdb_jsonObject;
	public static String json_array_key = "";
	public static ArrayList<Result> IMDB_results= new ArrayList<Result>();
	private static Result result;

	public IMDB() {
	}

	public static void readJsonFromUrl(String titre) throws IOException,
			JSONException {

		titre = titre.replaceAll(" ", "+").toLowerCase();

		// URL urli = new URL("http://www.omdbapi.com/?r=json&s=" +
		// titre+"&page=1");
		URL urli = new URL("http://www.omdbapi.com/?r=json&s="+titre);
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

			imdb_jsonText = jsonText;
			// inputArray = new JSONArray(jsonText.substring(10,
			// jsonText.length()));
			imdb_jsonObject = new JSONObject(jsonText);
			json_array_key= "Search";
			// return output;
		} finally {
			is.close();
		}
	}

	public static void readPeopleFromUrl(String titre) throws IOException,
	JSONException {

titre = titre.replaceAll(" ", "+").toLowerCase();

//URL urli = new URL("http://www.omdbapi.com/?r=json&s=" + titre+"&page=1");
URL urli = new URL("http://www.imdb.com/xml/find?json=1&nr=1&nm=on&q=Mohamed");
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

	imdb_jsonText = jsonText;
	// inputArray = new JSONArray(jsonText.substring(10,
	// jsonText.length()));
	imdb_jsonObject = new JSONObject(jsonText);
	json_array_key="name_exact";
	// return output;
} finally {
	is.close();
}
}
	public static void getElements() {

		ArrayList<String> temp_properties = new ArrayList();
		JSONArray jsonArray = imdb_jsonObject.getJSONArray(json_array_key);
		for (int i = 0, size = jsonArray.length(); i < size; i++) {
			JSONObject objectInArray = jsonArray.getJSONObject(i);
			temp_properties.clear();
			// "...and get thier component and thier value."
			String[] elementNames = JSONObject.getNames(objectInArray);
			for (String elementName : elementNames) {
				String value = objectInArray.getString(elementName);
				temp_properties.add(elementName);

			}
			imdb_elements.add(temp_properties);
		}
	}
	public static void getMovies(String movieURI) throws IOException {
		movieURI = movieURI.replaceAll(" ", "+").toLowerCase();
		if(movieURI.endsWith("+"))
			movieURI = movieURI.substring(0, movieURI.length()-1);

		// URL urli = new URL("http://www.omdbapi.com/?r=json&s=" +
		// titre+"&page=1");
		URL urli = new URL("http://www.omdbapi.com/?r=json&s="+movieURI);
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

			imdb_jsonText = jsonText;
			// inputArray = new JSONArray(jsonText.substring(10,
			// jsonText.length()));
			imdb_jsonObject = new JSONObject(jsonText);

			// return output;
		} finally {
			is.close();
		}
		System.out.println(imdb_jsonText);
		String element="",value="";
		JSONArray jsonArray = imdb_jsonObject.getJSONArray("Search");

		

		for (int i = 0, size = jsonArray.length(); i < size; i++) {
			result= new Result(movieURI);
			JSONObject objectInArray = jsonArray.getJSONObject(i);
			Iterator it = objectInArray.keys();
			// "...and get thier component and thier value."
			   while (it.hasNext()) {
				      element = (String) it.next();
				      value= objectInArray.getString(element);
					result.predicates.put(element, value);
					System.out.println(element+":"+value);

				  			} 
				result.predicates.put("fromWhere", "IMDB");
				result.predicates.put("movieURI",  movieURI);
			   IMDB_results.add(result);
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
	public static void reset() {
		imdb_jsonText="";
		imdb_elements = new ArrayList<ArrayList<String>>();
		imdb_jsonObject=null;
		IMDB_results= new ArrayList<Result>();;
		result=null;
	}

}
