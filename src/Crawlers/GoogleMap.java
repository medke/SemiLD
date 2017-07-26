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

public class GoogleMap {
	public static String google_jsonText;
	public static ArrayList<ArrayList<String>> google_elements = new ArrayList<ArrayList<String>>();
	public static JSONObject google_jsonObject;
	public static ArrayList<Result> google_results= new ArrayList<Result>();
	private static Result result;
	public static ArrayList<String> ids= new ArrayList<String>();

	public GoogleMap() {
	}

	public static void readJsonFromUrl(String titre, int limit) throws IOException,
			JSONException {

		titre = titre.replaceAll(" ", "+").toLowerCase();



		URL urli = new URL(
				"https://maps.googleapis.com/maps/api/place/textsearch/json?query="+titre+"&sensor=false&key=AIzaSyDcktYJwWJbeivSuLdyJ8n0TTuHN7Sztt8");

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
			// return inputArray;

			google_jsonText = jsonText;
			// inputArray = new JSONArray(jsonText.substring(10,
			// jsonText.length()));
			JSONObject output = new JSONObject(jsonText);
			google_jsonObject = output;
			// return output;
		} finally {
			is.close();
		}
	}

	public static void readJsonFromUrl(String titre) throws IOException,
			JSONException {

		
		titre = titre.replaceAll(" ", "+").toLowerCase();

		
				URL urli = urli = new URL(
						"https://maps.googleapis.com/maps/api/place/textsearch/json?query="+titre+"&sensor=false&key=AIzaSyDcktYJwWJbeivSuLdyJ8n0TTuHN7Sztt8");


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
			// return inputArray;

			google_jsonText = jsonText;
			// inputArray = new JSONArray(jsonText.substring(10,
			// jsonText.length()));
			JSONObject output = new JSONObject(jsonText);
			google_jsonObject = output;

			// return output;
		} finally {
			is.close();
		}
	}

	public static void getElements() {

		ArrayList<String> temp_properties = new ArrayList();
	  
		JSONArray jsonArray = google_jsonObject.getJSONArray("results");
		for (int i = 0, size = jsonArray.length(); i < size; i++) {
			JSONObject objectInArray = jsonArray.getJSONObject(i);
			temp_properties.clear();
			// "...and get thier component and thier value."
			String[] elementNames = JSONObject.getNames(objectInArray);
			for (String elementName : elementNames) {
				//String value = objectInArray.get(elementName);
				temp_properties.add(prepareLabel(elementName));
			
			}
			google_elements.clear();
			google_elements.add(temp_properties);
		}
		
	}
	public static void getPlaces(String movieURI) throws IOException {
		movieURI = movieURI.replaceAll(" ", "+").toLowerCase();
		if(movieURI.endsWith("+"))
			movieURI = movieURI.substring(0, movieURI.length()-1);
		// URL urli = new URL("http://www.omdbapi.com/?r=json&s=" +
		// titre+"&page=1");
		URL urli = new URL(
				"https://maps.googleapis.com/maps/api/place/textsearch/json?query="+movieURI+"&sensor=false&key=AIzaSyDcktYJwWJbeivSuLdyJ8n0TTuHN7Sztt8");

		URLConnection con = urli.openConnection();
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

			google_jsonText = jsonText;
			// inputArray = new JSONArray(jsonText.substring(10,
			// jsonText.length()));
			JSONObject output = new JSONObject(jsonText);
			google_jsonObject = output;

			// return output;
		} finally {
			is.close();
		}
		String element="",element2="",element3="",value="";
		JSONArray jsonArray = google_jsonObject.getJSONArray("results");

		Iterator it,it2,it3;

		for (int i = 0, size = jsonArray.length(); i < size; i++) {
			result= new Result(movieURI);
			JSONObject objectInArray = jsonArray.getJSONObject(i);
			 it= objectInArray.keys();
			// "...and get thier component and thier value."
			   while (it.hasNext()) {
				      element = (String) it.next();
				      if ( objectInArray.get(element) instanceof JSONObject ) {
				    	  JSONObject jsonobj=((JSONObject)objectInArray.get(element));
					      it2=jsonobj.keys();
					      while (it2.hasNext()){
					    	  element2=(String) it2.next();
					    	  it3= ((JSONObject) jsonobj.get(element2)).keys();
					    			  while(it3.hasNext())
					    			  {	 
					    				  element3=(String) it3.next();

									      result.predicates.put(element3, ((JSONObject)jsonobj.get(element2)).get(element3).toString());
					    			  }
					      }
					      
				      }
				      else{
				      value= objectInArray.get(element).toString();
				      result.predicates.put(element, value);
				      }

				  			} 
				result.predicates.put("fromWhere", "IMDB");
				result.predicates.put("movieURI",  movieURI);
			   google_results.add(result);
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
	public static ArrayList<String> removeDuplicates(ArrayList<String> al) {
		Set<String> hs = new HashSet<>();
		hs.addAll(al);
		al.clear();
		al.addAll(hs);
		return al;
	}
	public static String prepareLabel(final String url){
	    // return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
	    return URLDecoder.decode(url).replace("#", "_").replaceAll("(.)([A-Z])", "$1_$2");
	}
	public static void reset() {
		google_elements.clear();
		google_results= new ArrayList<Result>();
		ids = new ArrayList<String>();
	}
	
	public static void getPeople() throws IOException{
		Set<String> user_ids = new HashSet<String>(ids);
		for(String user_id: user_ids)
		{
		// URL urli = new URL("http://www.omdbapi.com/?r=json&s=" +
		// titre+"&page=1");
		URL urli = new URL(
				"https://www.googleapis.com/plus/v1/people/"+user_id+"?key=AIzaSyDgpBy6-pRPU7VfSrYGGzuQseJsQ9uoSTw");

		URLConnection con = urli.openConnection();
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

			google_jsonText = jsonText;
			// inputArray = new JSONArray(jsonText.substring(10,
			// jsonText.length()));
			JSONObject output = new JSONObject(jsonText);
			//System.out.println(jsonText);
			google_jsonObject = output;

			// return output;
		} finally {
			is.close();
		}
		String element="",element2="",value="";
		

		Iterator it,it2;

	
			result= new Result(user_id);
			
			 it= google_jsonObject.keys();
			// "...and get thier component and thier value."
			   while (it.hasNext()) {
				      element = (String) it.next();
				      if ( google_jsonObject.get(element) instanceof JSONObject ) {
				    	  JSONObject jsonobj=((JSONObject)google_jsonObject.get(element));
					      it2=jsonobj.keys();
					      while (it2.hasNext()){
					    	  element2=(String) it2.next();

							result.predicates.put(element2, jsonobj.get(element2).toString());

					      }
							result.predicates.put("image", google_jsonObject.getJSONObject("image").getString("url").toString());

				      }
				      else{
				      value= google_jsonObject.get(element).toString();
				      result.predicates.put(element, value);
				      }

				  			} 
				result.predicates.put("fromWhere", "IMDB");
				result.predicates.put("movieURI",  user_id);
			   google_results.add(result);
			   }
		
			
	}
	public static void getPeopleIDs(String movieURI) throws IOException {
		getPeopleIDs2(movieURI);
		movieURI = movieURI.replaceAll(" ", "+").toLowerCase();
		if(movieURI.endsWith("+"))
			movieURI = movieURI.substring(0, movieURI.length()-1);
		// URL urli = new URL("http://www.omdbapi.com/?r=json&s=" +
		// titre+"&page=1");
		URL urli = new URL(
				"https://www.googleapis.com/plus/v1/people?query="+movieURI+"&key=AIzaSyDgpBy6-pRPU7VfSrYGGzuQseJsQ9uoSTw");

		URLConnection con = urli.openConnection();
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
			// return inputArray;

			google_jsonText = jsonText;
			// inputArray = new JSONArray(jsonText.substring(10,
			// jsonText.length()));
			JSONObject output = new JSONObject(jsonText);
			google_jsonObject = output;

			// return output;
		} finally {
			is.close();
		}

		JSONArray jsonArray = google_jsonObject.getJSONArray("items");


		for (int i = 0, size = jsonArray.length(); i < size; i++) {
			JSONObject objectInArray = jsonArray.getJSONObject(i);


					ids.add(objectInArray.get("id").toString());


				      

				  	} 
		
	 }	
	public static void getPeopleIDs2(String movieURI) throws IOException {
		movieURI = movieURI.replaceAll(" ", "+").toLowerCase();
		if(movieURI.endsWith("+"))
			movieURI = movieURI.substring(0, movieURI.length()-1);
		// URL urli = new URL("http://www.omdbapi.com/?r=json&s=" +
		// titre+"&page=1");
		URL urli = new URL(
				"https://www.googleapis.com/plus/v1/activities?query="+movieURI+"&key=AIzaSyDgpBy6-pRPU7VfSrYGGzuQseJsQ9uoSTw");

		URLConnection con = urli.openConnection();
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
			// return inputArray;

			google_jsonText = jsonText;
			// inputArray = new JSONArray(jsonText.substring(10,
			// jsonText.length()));
			JSONObject output = new JSONObject(jsonText);
			google_jsonObject = output;

			// return output;
		} finally {
			is.close();
		}
		JSONArray jsonArray = google_jsonObject.getJSONArray("items");

		for (int i = 0, size = jsonArray.length(); i < size; i++) {
			JSONObject objectInArray = jsonArray.getJSONObject(i);


					ids.add(objectInArray.getJSONObject("actor").get("id").toString());


				      

				  	} 

			   }
}
