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
import java.util.Arrays;
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

public class LastFM {
	public static String lastfm_jsonText;
	public static ArrayList<ArrayList<String>> lastfm_elements = new ArrayList<ArrayList<String>>();
	public static JSONObject lastfm_jsonObject;
	public static String json_array_key = "";

	public LastFM() {
	}

	public static void readPeopleFromUrl(String titre) throws IOException,
			JSONException {

		titre = titre.replaceAll(" ", "+").toLowerCase();

		// URL urli = new URL("http://www.omdbapi.com/?r=json&s=" +
		// titre+"&page=1");
		URL urli = new URL("http://ws.audioscrobbler.com/2.0/?method=artist.search&artist="+titre+"&api_key=00ed5a16390c4f519e08a1868df205df&format=json");
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

			lastfm_jsonText = jsonText;
			// inputArray = new JSONArray(jsonText.substring(10,
			// jsonText.length()));
			lastfm_jsonObject = new JSONObject(jsonText);
			json_array_key = "artist";
			// return output;
		} finally {
			is.close();
		}
	}

	public static void readPeopleFromUrl(String titre, int limit) throws IOException,
	JSONException {

titre = titre.replaceAll(" ", "+").toLowerCase();

//URL urli = new URL("http://www.omdbapi.com/?r=json&s=" + titre+"&page=1");
URL urli = new URL("http://ws.audioscrobbler.com/2.0/?method=artist.search&artist="+titre+"&limit="+limit+"&api_key=00ed5a16390c4f519e08a1868df205df&format=json");
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
	lastfm_jsonText = jsonText;
	// inputArray = new JSONArray(jsonText.substring(10,
	// jsonText.length()));
	lastfm_jsonObject = new JSONObject(jsonText);
	json_array_key="artist";
	// return output;
} finally {
	is.close();
}
}

	public static void getElements() {

		ArrayList<String> temp_properties = new ArrayList();
		JSONArray jsonArray = lastfm_jsonObject.getJSONObject("results").getJSONObject("artistmatches").getJSONArray(json_array_key);
		for (int i = 0, size = jsonArray.length(); i < size; i++) {
			JSONObject objectInArray = jsonArray.getJSONObject(i);
			temp_properties.clear();
			// "...and get thier component and thier value."
			String[] elementNames = JSONObject.getNames(objectInArray);
			for (String elementName : elementNames) {
				try
				{
				String value = objectInArray.getString(elementName);
				}catch(Exception e)
				{
			    
				}
				temp_properties.add(elementName);

			}
			lastfm_elements.add(temp_properties);
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
		lastfm_jsonText="";
		lastfm_elements = new ArrayList<ArrayList<String>>();
		lastfm_jsonObject=null;
		
	}

}
