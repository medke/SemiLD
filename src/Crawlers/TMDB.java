package Crawlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TMDB {
	public static String tmdb_jsonText;
	public static ArrayList<ArrayList<String>> tmdb_elements = new ArrayList<ArrayList<String>>();
	public static JSONObject tmdb_jsonObject;
	public static ArrayList<Result> TMDB_results= new ArrayList<Result>();
	private static Result result;
	public TMDB() {
	}

	public static void readJsonFromUrl(String titre, int y) throws IOException,
			JSONException {

		String year = "";
		titre = titre.replaceAll(" ", "+").toLowerCase();

		if (y > 1900 && y < 2030) {
			year = "&yg=1&year=" + y + "";
		}

		URL urli = urli = new URL(
				"http://api.themoviedb.org/3/search/movie?api_key=b307abc388114f177ae36f2c2cc52f41"
						+ "&query=" + titre + "&year=" + year);

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

			tmdb_jsonText = jsonText;
			// inputArray = new JSONArray(jsonText.substring(10,
			// jsonText.length()));
			JSONObject output = new JSONObject(jsonText);

			// return output;
		} finally {
			is.close();
		}
	}

	public static void readJsonFromUrl(String titre) throws IOException,
			JSONException {

		String year = "";
		titre = titre.replaceAll(" ", "+").toLowerCase();

		URL urli = urli = new URL(
				"http://api.themoviedb.org/3/search/movie?api_key=b307abc388114f177ae36f2c2cc52f41"
						+ "&query=" + titre);

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

			tmdb_jsonText = jsonText;
			// inputArray = new JSONArray(jsonText.substring(10,
			// jsonText.length()));
			JSONObject output = new JSONObject(jsonText);
			tmdb_jsonObject = output;

			// return output;
		} finally {
			is.close();
		}
	}

	public static void getElements() {

		ArrayList<String> temp_properties = new ArrayList();
	  
		JSONArray jsonArray = tmdb_jsonObject.getJSONArray("results");
		for (int i = 0, size = jsonArray.length(); i < size; i++) {
			JSONObject objectInArray = jsonArray.getJSONObject(i);
			temp_properties.clear();
			// "...and get thier component and thier value."
			String[] elementNames = JSONObject.getNames(objectInArray);
			for (String elementName : elementNames) {
				//String value = objectInArray.get(elementName);
				temp_properties.add(elementName);
			
			}
			tmdb_elements.add(temp_properties);
		}
	}
	
	public static void getMovies(String movieURI) throws IOException {
		movieURI = movieURI.replaceAll(" ", "+").toLowerCase();
		if(movieURI.endsWith("+"))
			movieURI = movieURI.substring(0, movieURI.length()-1);
		// URL urli = new URL("http://www.omdbapi.com/?r=json&s=" +
		// titre+"&page=1");
		URL urli = urli = new URL(
				"http://api.themoviedb.org/3/search/movie?api_key=b307abc388114f177ae36f2c2cc52f41"
						+ "&query=" + movieURI);
		URLConnection con = urli.openConnection();
		;
		con.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.237 Safari/534.10");

		InputStream is = con.getInputStream();
		String json_array_key="";
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			// JSONArray inputArray=null ;
			if (jsonText.contains("error")
					&& jsonText.contains("Film not found"))
				System.out.println("error");
			// return inputArray;

			tmdb_jsonText = jsonText;
			// inputArray = new JSONArray(jsonText.substring(10,
			// jsonText.length()));
			tmdb_jsonObject = new JSONObject(jsonText);
			json_array_key= "results";
			// return output;
		} finally {
			is.close();
		}
		System.out.println(tmdb_jsonText);
		String element="",value="";
		JSONArray jsonArray = tmdb_jsonObject.getJSONArray(json_array_key);

		

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
				result.predicates.put("fromWhere", "TMDB");
				result.predicates.put("movieURI",  movieURI);
			   TMDB_results.add(result);
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
		tmdb_jsonText="";
		tmdb_elements = new ArrayList<ArrayList<String>>();
		tmdb_jsonObject=null;
		TMDB_results = new ArrayList<Result>();;
	}
}
