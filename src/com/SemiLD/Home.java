package com.SemiLD;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.xml.sax.SAXException;

import Metadata.IndexGlobalSchema;
import SharedProcesses.WriteXML;
import SimiMatch.SchemaMatching;
import Crawlers.DBPedia;
import Crawlers.Geonames;
import Crawlers.GoogleMap;
import Crawlers.IMDB;
import Crawlers.LastFM;
import Crawlers.LinkedGeoData;
import Crawlers.LinkedMDB;
import Crawlers.TMDB;
import GlobalSchema.InterlinkWithGS;
import GlobalSchema.SourceInterlinker;

/**
 * Servlet implementation class Home
 */
@WebServlet("/login")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Map<String, Set<String>> filter_info;
	private static HashSet<String> filter;
	public static SourceInterlinker interlinker;
	public static double[][] locations;
	public static char current_t = 'a';
	public static char current_d;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Home() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void init() {
		try {
			new Connector();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String text = current_t + " now";
		response.setContentType("text/plain"); // Set content type of the
												// response so that jQuery knows
												// what it can expect.
		response.setCharacterEncoding("UTF-8"); // You want world domination,
												// huh?
		response.getWriter().write(text);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*
		 * response.setContentType("text/html;charset=UTF-8");
		 * request.setAttribute("var", "Test");
		 * if(request.getParameterMap().containsKey("search"))
		 * request.setAttribute("var", "<h2> Welcome " +
		 * request.getParameter("search") + "</h2>");
		 */
		current_t = 'a';
		int ResultsNumber, threshold = 0, gsVersion = 0;
		char domain = 'm';
		String paramAuteur;

		if (request.getParameterMap().containsKey("search")) {
			ResultsNumber = 20;
			paramAuteur = request.getParameter("search");
			if (request.getParameterMap().containsKey("Results"))
				ResultsNumber = Integer.parseInt(request
						.getParameter("Results"));
			if (request.getParameterMap().containsKey("o")) {
				createGS4People(paramAuteur, ResultsNumber);
				return;
			}
			if (request.getParameterMap().containsKey("type")) {
				domain = request.getParameter("type").charAt(0);
				current_d = domain;
			}
			if (request.getParameterMap().containsKey("t")) {
				// threshold = Integer.parseInt(request.getParameter("t"));
				threshold = 90;
			}
			if (request.getParameterMap().containsKey("GS"))
				gsVersion = Integer.parseInt(request.getParameter("GS"));

			if (domain == 'm') {
				request.setAttribute(
						"var",
						InterlinkMovies(paramAuteur, domain, ResultsNumber,
								threshold, gsVersion));

			} else if (domain == 'g') {
				request.setAttribute(
						"var",
						InterlinkPlaces(paramAuteur, domain, ResultsNumber,
								threshold, gsVersion));
				request.setAttribute("locations", locations);

			} else if (domain == 'p')
				request.setAttribute(
						"var",
						InterlinkPeople(paramAuteur, domain, ResultsNumber,
								threshold, gsVersion));
			request.setAttribute("filter", filter);

			// DBPedia.reset();
			// LinkedMDB.reset();
			SchemaMatching.reset();
		}
		/*
		 * int[] n_res= {10,50,100,200,300,400,500,600,700}; for(int
		 * i=0;i<n_res.length; i++) for(int j=70;j<100; j+=5) for(int
		 * w=0;w<n_res.length; w++)
		 * request.setAttribute("var",InterlinkGS4Places("London", 'g',
		 * n_res[i],j, n_res[w]));
		 */

		this.getServletContext().getRequestDispatcher("/Search.jsp")
				.forward(request, response);
	}

	public static String createGS4Movies(String title, int ResultsNumber)
			throws JSONException, IOException {
		String message = " ";
		boolean update = false;

		LinkedMDB.getMovies(title, ResultsNumber);// SPACES TO BE FILLED
		DBPedia.getMovies(title, ResultsNumber); // WITH paramAuteur
		// IMDB.readJsonFromUrl(title);
		// IMDB.getElements();
		TMDB.readJsonFromUrl(title);
		TMDB.getElements();

		SchemaMatching.addSource(DBPedia.DBPedia_properties, "DBPedia");
		SchemaMatching.addSource(LinkedMDB.LMDB_properties, "LinkedMDB");
		// SchemaMatching.addSource(IMDB.imdb_elements);
		SchemaMatching.addSource(TMDB.tmdb_elements, "TMDB");

		try {
			update = IndexGlobalSchema.checkIndexGS(
					new ArrayList<>(Arrays
							.asList("DBP", "IMDB", "TMDB", "LMDB")),
					ResultsNumber);
			if (update) {
				IndexGlobalSchema.indexGS(
						new ArrayList<>(Arrays.asList("DBP", "IMDB", "TMDB",
								"LMDB")), ResultsNumber);
				WriteXML.convert2XML(SchemaMatching.globalSchema, "",
						ResultsNumber);
			}

			else
				System.out.println("No Update Needed");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// SchemaMatching.addSource(TMDB.tmdb_elements);
		message = Arrays.toString(DBPedia.DBPedia_urls.toArray()) + " <br>"
				+ " <br> <h2> DBPedia Properties </h2>"
				+ Arrays.toString(DBPedia.DBPedia_properties.toArray())
				+ " <br> <h2> LinkedMDB URIs </h2>"
				+ Arrays.toString(LinkedMDB.LMDB_urls.toArray())
				+ " <br> <h2> LinkedMDB Properties </h2>"
				+ Arrays.toString(LinkedMDB.LMDB_properties.toArray())
				// + " <br> <h2> IMDB </h2>" + IMDB.imdb_elements
				+ " <br> <h2> TMDB </h2>" + TMDB.tmdb_elements
				+ " <br> <h2> GlobalSchema </h2>" + SchemaMatching.globalSchema;

		return message;
	}

	public static String createGS4Places(String title, int ResultsNumber)
			throws JSONException, IOException {
		String message = " ";
		boolean update = false;
		Geonames.readJsonFromUrl(title, 2);
		Geonames.getElements();
		GoogleMap.readJsonFromUrl(title);
		GoogleMap.getElements();
		// LinkedMDB.getMovies(title, ResultsNumber);// SPACES TO BE FILLED
		DBPedia.getPlaces(title, ResultsNumber); // WITH paramAuteur
		LinkedGeoData.getPlaces(title, ResultsNumber); // WITH paramAuteur

		// IMDB.readJsonFromUrl(title);
		// IMDB.getElements();
		// TMDB.readJsonFromUrl(title);
		// TMDB.getElements();

		SchemaMatching.addSource(Geonames.geonames_elements, "Geonames");
		SchemaMatching.addSource(GoogleMap.google_elements, "GoogleMap");
		SchemaMatching.addSource(DBPedia.DBPedia_properties, "DBPedia");
		SchemaMatching.addSource(LinkedGeoData.linkedgeo_properties,
				"LinkedGeoData");

		try {
			update = IndexGlobalSchema.checkIndexGS(
					new ArrayList<>(Arrays.asList("GEO", "GOO", "LGD", "DBP")),
					ResultsNumber);
			if (update) {
				IndexGlobalSchema.indexGS(
						new ArrayList<>(Arrays.asList("GEO", "GOO", "LGD",
								"DBP")), ResultsNumber);
				WriteXML.convert2XML(SchemaMatching.globalSchema, "",
						ResultsNumber);
			}

			else
				System.out.println("No Update Needed");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// SchemaMatching.addSource(TMDB.tmdb_elements);
		message = " <br> <h2> Geonames </h2>" + Geonames.geonames_elements
				+ " <br> <h2> Google MAP API </h2>" + GoogleMap.google_elements
				+ " <br> <h2> DBpedia </h2>" + DBPedia.DBPedia_properties
				+ " <br> <h2> LinkedGeoData </h2>"
				+ LinkedGeoData.linkedgeo_properties
				+ " <br> <h2> GlobalSchema </h2>" + SchemaMatching.globalSchema;

		DBPedia.reset();
		LinkedGeoData.reset();
		Geonames.reset();
		GoogleMap.reset();
		return message;
	}

	public static String createGS4People(String title, int ResultsNumber)
			throws JSONException, IOException {
		String message = " ";
		boolean update = false;

		LinkedMDB.getPeople(title, ResultsNumber);// SPACES TO BE FILLED
		DBPedia.getPeople(title, ResultsNumber); // WITH paramAuteur
		// WITH paramAuteur

		IMDB.readPeopleFromUrl(title);
		IMDB.getElements();
		LastFM.readPeopleFromUrl(title, 2);
		LastFM.getElements();
		// TMDB.readJsonFromUrl(title);
		// TMDB.getElements();

		SchemaMatching.addSource(LinkedMDB.LMDB_properties, "LinkedMDB");
		SchemaMatching.addSource(DBPedia.DBPedia_properties, "DBpedia");
		SchemaMatching.addSource(IMDB.imdb_elements, "IMDB");
		SchemaMatching.addSource(LastFM.lastfm_elements, "LastFM");

		try {
			update = IndexGlobalSchema
					.checkIndexGS(
							new ArrayList<>(Arrays.asList("LMDB", "DBP",
									"IMDB", "LFM")), ResultsNumber);
			if (update) {
				IndexGlobalSchema.indexGS(
						new ArrayList<>(Arrays.asList("LMDB", "DBP", "IMDB",
								"LFM")), ResultsNumber);
				WriteXML.convert2XML(SchemaMatching.globalSchema, "",
						ResultsNumber);
			}

			else
				System.out.println("No Update Needed");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// SchemaMatching.addSource(TMDB.tmdb_elements);
		message = " <br> <h2> DBpedia </h2>" + DBPedia.DBPedia_properties
				+ " <br> <h2> LinkedMDB </h2>" + LinkedMDB.LMDB_properties
				+ " <br> <h2> IMDB </h2>" + IMDB.imdb_elements
				+ " <br> <h2> LastFM </h2>" + LastFM.lastfm_elements
				+ " <br> <h2> GlobalSchema </h2>" + SchemaMatching.globalSchema;

		DBPedia.reset();
		LinkedMDB.reset();

		return message;
	}

	// ------------------------------------------------------

	public static String InterlinkGS4Movies(String title, char domain,
			int ResultsNumber, int threshold, int GS_version)
			throws JSONException, IOException {
		String message = " ";
		int totalResults = 0;
		LinkedMDB.getMovies(title, ResultsNumber);// SPACES TO BE FILLED
		DBPedia.getMovies(title, ResultsNumber); // WITH paramAuteur
		IMDB.readJsonFromUrl(title);
		IMDB.getElements();
		TMDB.readJsonFromUrl(title);
		TMDB.getElements();

		InterlinkWithGS interlinker = new InterlinkWithGS(domain,
				ResultsNumber, threshold, GS_version);
		interlinker.getGS(domain, GS_version);
		totalResults += DBPedia.DBPedia_properties.size();
		System.out.println("N of Datasets DBpedia==> " + totalResults);
		interlinker.add2dInput(DBPedia.DBPedia_properties);

		totalResults += LinkedMDB.LMDB_properties.size();
		System.out.println("N of Datasets LMDB==> " + totalResults);
		interlinker.add2dInput(LinkedMDB.LMDB_properties);

		totalResults += IMDB.imdb_elements.size();
		System.out.println("N of Datasets IMDB==> " + totalResults);
		interlinker.add2dInput(IMDB.imdb_elements);

		totalResults += TMDB.tmdb_elements.size();
		System.out.println("N of Datasets TMDB==> " + totalResults);
		interlinker.add2dInput(TMDB.tmdb_elements);

		System.out.flush();

		// SchemaMatching.addSource(TMDB.tmdb_elements);
		message = Arrays.toString(interlinker.input_elements.toArray())
				+ " <br>"
				+ " <br> <h2> Number Properties </h2>"
				+ interlinker.numberOfProperties
				+ " <br> <h2> Input Elements </h2>"
				+ Arrays.toString(interlinker.previous_input_elements.toArray())
				+ " <br> <h2> Global Schema Elements </h2>"
				+ Arrays.toString(interlinker.GS_elements.toArray());

		System.out.println("N of Datasets==> " + totalResults);
		totalResults = 0;
		DBPedia.reset();
		LinkedMDB.reset();
		IMDB.reset();
		TMDB.reset();
		interlinker.destroy();
		return message;
	}

	public static String InterlinkGS4Places(String title, char domain,
			int ResultsNumber, int threshold, int GS_version)
			throws JSONException, IOException {
		String message = " ";
		int totalResults = 0;
		Geonames.readJsonFromUrl(title, 2);
		Geonames.getElements();
		GoogleMap.readJsonFromUrl(title);
		GoogleMap.getElements();
		// LinkedMDB.getMovies(title, ResultsNumber);// SPACES TO BE FILLED
		DBPedia.getPlaces(title, ResultsNumber); // WITH paramAuteur
		LinkedGeoData.getPlaces(title, ResultsNumber); // WITH paramAuteur

		// IMDB.readJsonFromUrl(title);
		// IMDB.getElements();
		// TMDB.readJsonFromUrl(title);
		// TMDB.getElements();
		InterlinkWithGS interlinker = new InterlinkWithGS(domain,
				ResultsNumber, threshold, GS_version);
		interlinker.getGS(domain, GS_version);

		totalResults += DBPedia.DBPedia_properties.size();
		System.out.println("N of Datasets DBpedia==> " + totalResults);
		interlinker.add2dInput(DBPedia.DBPedia_properties);

		totalResults += LinkedGeoData.linkedgeo_properties.size();
		System.out.println("N of Datasets LinkedGeoData==> " + totalResults);
		interlinker.add2dInput(LinkedGeoData.linkedgeo_properties);

		totalResults += Geonames.geonames_elements.size();
		System.out.println("N of Datasets Geonames==> " + totalResults);
		interlinker.add2dInput(Geonames.geonames_elements);

		totalResults += GoogleMap.google_elements.size();
		System.out.println("N of Datasets GoogleMap==> " + totalResults);
		interlinker.add2dInput(GoogleMap.google_elements);

		System.out.flush();

		// SchemaMatching.addSource(TMDB.tmdb_elements);
		message = " <br> <h2> Geonames </h2>" + Geonames.geonames_elements
				+ " <br> <h2> Google MAP API </h2>" + GoogleMap.google_elements
				+ " <br> <h2> DBpedia </h2>" + DBPedia.DBPedia_properties
				+ " <br> <h2> LinkedGeoData </h2>"
				+ LinkedGeoData.linkedgeo_properties
				+ " <br> <h2> GlobalSchema </h2>" + interlinker.GS_elements;

		System.out.println("N of Datasets==> " + totalResults);
		totalResults = 0;
		DBPedia.reset();
		LinkedGeoData.reset();
		Geonames.reset();
		GoogleMap.reset();
		interlinker.destroy();
		return message;

	}

	public static String InterlinkGS4People(String title, char domain,
			int ResultsNumber, int threshold, int GS_version)
			throws JSONException, IOException {
		String message = " ";
		int totalResults = 0;
		LinkedMDB.getPeople(title, ResultsNumber);// SPACES TO BE FILLED
		DBPedia.getPeople(title, ResultsNumber); // WITH paramAuteur
		IMDB.readPeopleFromUrl(title);
		IMDB.getElements();
		LastFM.readPeopleFromUrl(title, 2);
		LastFM.getElements();
		// TMDB.readJsonFromUrl(title);
		// TMDB.getElements();
		InterlinkWithGS interlinker = new InterlinkWithGS(domain,
				ResultsNumber, threshold, GS_version);
		interlinker.getGS(domain, GS_version);

		totalResults += DBPedia.DBPedia_properties.size();
		System.out.println("N of Datasets DBpedia==> " + totalResults);
		interlinker.add2dInput(DBPedia.DBPedia_properties);

		totalResults += LinkedMDB.LMDB_properties.size();
		System.out.println("N of Datasets LMDB==> " + totalResults);
		interlinker.add2dInput(LinkedMDB.LMDB_properties);

		totalResults += IMDB.imdb_elements.size();
		System.out.println("N of Datasets IMDB==> " + totalResults);
		interlinker.add2dInput(IMDB.imdb_elements);

		totalResults += LastFM.lastfm_elements.size();
		System.out.println("N of Datasets LastFM==> " + totalResults);
		interlinker.add2dInput(LastFM.lastfm_elements);

		// SchemaMatching.addSource(TMDB.tmdb_elements);
		message = " <br> <h2> DBpedia </h2>"
				+ DBPedia.DBPedia_properties.size()
				+ " <br> <h2> LinkedMDB </h2>"
				+ LinkedMDB.LMDB_properties.size() + " <br> <h2> IMDB </h2>"
				+ IMDB.imdb_elements.size() + " <br> <h2> LastFM </h2>"
				+ LastFM.lastfm_elements.size()
				+ " <br> <h2> GlobalSchema </h2>"
				+ interlinker.GS_elements.size();

		DBPedia.reset();
		LinkedMDB.reset();
		IMDB.reset();
		LastFM.reset();
		interlinker.destroy();
		return message;
	}

	// -----------------------------------------

	public static String InterlinkMovies(String title, char domain,
			int ResultsNumber, int threshold, int GS_version)
			throws JSONException, IOException {
		String message = " ";
		int totalResults = 0;
		filter_info = new HashMap<String, Set<String>>();
		filter = new HashSet<String>();
		interlinker = new SourceInterlinker(domain, ResultsNumber, threshold,
				GS_version);
		interlinker.getGS(domain, GS_version);

		// LinkedMDB.getMovies(title, ResultsNumber);// SPACES TO BE FILLED
		try {
			current_t = 'i';
			IMDB.getMovies(title);
			interlinker.match(IMDB.IMDB_results, "IMDB");
		} catch (JSONException je) {

		}
		current_t = 't';
		TMDB.getMovies(title);
		interlinker.match(TMDB.TMDB_results, "TheMovieDB");

		current_t = 'd';
		DBPedia.getMovies(title, ResultsNumber);
		interlinker.match(DBPedia.DBPedia_results, "DBpedia");

		current_t = 'b';
		LinkedMDB.getMovies(title, ResultsNumber);
		interlinker.match(LinkedMDB.LMDB_results, "LinkedMDB");

		// WITH paramAuteur
		/*
		 * IMDB.getElements(); TMDB.readJsonFromUrl(title); TMDB.getElements();
		 */

		Collections.shuffle(interlinker.final_results, new Random(4));
		String fromw = "", titleToDisplay = "", imagePath = "", overview = "", releaseDate = "", MovieLink = "", fromw_display = "";
		for (int i = 0; i < interlinker.final_results.size(); i++) {

			fromw = interlinker.final_results.get(i).get("fromWhere");

			if (fromw == "TheMovieDB") {
				titleToDisplay = interlinker.final_results.get(i).get("title");
				imagePath = "http://image.tmdb.org/t/p/original//"
						+ interlinker.final_results.get(i).get("poster_path");
				overview = interlinker.final_results.get(i).get("overview");
				releaseDate = interlinker.final_results.get(i).get(
						"release_date");
				MovieLink = "https://www.themoviedb.org/movie/"
						+ interlinker.final_results.get(i).get("id");
				fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightslategray;'>"
						+ fromw + "</span>";

			} else if (fromw == "IMDB") {
				titleToDisplay = interlinker.final_results.get(i).get("title");
				if (interlinker.final_results.get(i).get("Poster").length() > 10)
					imagePath = interlinker.final_results.get(i).get("Poster");
				overview = interlinker.final_results.get(i).get("overview");
				releaseDate = interlinker.final_results.get(i).get("Year");
				MovieLink = "http://www.imdb.com/title/"
						+ interlinker.final_results.get(i).get("imdbID");
				fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightcyan;'>"
						+ fromw + "</span>";

			} else if (fromw == "LinkedMDB") {
				titleToDisplay = interlinker.final_results.get(i).get(
						"rdf_schema_label");
				imagePath = "http://localhost:185/SemiLD-Servlet/C:/Users/MSK137/workspace/SemiLD-Servlet/WebContent/images/default-thumbnail.jpg";
				overview = "Same as "
						+ interlinker.final_results.get(i).get("owl_same_As");
				releaseDate = interlinker.final_results.get(i).get(
						"release_date");
				MovieLink = interlinker.final_results.get(i).get("urrri");
				fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightcoral;'>"
						+ fromw + "</span>";

			} else if (fromw == "DBpedia") {
				titleToDisplay = interlinker.final_results.get(i).get("name");
				imagePath = PrepareIMG(interlinker.final_results.get(i).get(
						"thumbnail"));
				overview = interlinker.final_results.get(i).get(
						"rdf_schema_comment");
				releaseDate = interlinker.final_results.get(i).get(
						"release_date");
				MovieLink = interlinker.final_results.get(i).get("urrri");
				fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightblue;'>"
						+ fromw + "</span>";
			}

			message += "<div class='row mb20'>" + "<div class='col-md-12'>"
					+ "<div class='candidate'>"
					+ fromw_display
					+ " <div class='candidate__header clearfix mb20'>"
					+ "<img class='candidate__img' src='"
					+ imagePath
					+ "' onError=\"this.onerror=null;this.src='https://www.gumtree.com/static/1/resources/assets/rwd/images/orphans/a37b37d99e7cef805f354d47.noimage_thumbnail.png';\">"
					+ "<div class='candidate__details'>"
					+ "<h1 class='candidate__fullname'><a href='"
					+ MovieLink
					+ "'>"
					+ titleToDisplay
					+ "</a></h1>"
					+ "<h2 class='candidate__location'>"
					+ releaseDate
					+ ""
					+ "</h2>"
					+ "</div></div>"
					+ "<ul class='list-inline'>"
					+ "<li class='mb10'><span class='candidate__years'>Producer:</span> <span class='candidate__title'>"
					+ interlinker.final_results.get(i).get("producer")
					+ "</span></li>"
					+ "<li><span class='candidate__years'>Description</span>"
					+ " <span class='candidate__title'>"
					+ overview
					+ "</li>"
					+ "</ul>"
					+ "<br>"
					+ "<div class='moreInfo'>"
					+ "<ul 'list-inline2'>";
			for (String key : interlinker.final_results.get(i).keySet()) {
				if (key != "producer" && key != "rdf_schema_comment"
						&& key != "release_date" && key != "name"
						&& key != "fromWhere" && key != "urrri") {
					message += "<li><span class='candidate__years'>"
							+ key
							+ "</span>"
							+ " <div class='candidate__title'>"
							+ getLastBitFromUrl(interlinker.final_results
									.get(i).get(key)) + "<div></li>";
				}
			}
			message += "</ul>"
					+ "</div><div class='accordion'>"
					+ "<img href='#' src='https://www.cohen-west.com/wp-content/themes/metro/images/menu-button.png' "
					+ "style='width: 20px;'>" + "</div></div>" + "</div>";
			for (String key : interlinker.final_results.get(i).keySet()) {
				filter.add(key.trim());
				AppendToKey(key.trim(), splitIntoSet(interlinker.final_results
						.get(i).get(key)));

			}
			LoadDropDown.setInterlinker(interlinker);
			/*
			 * 
			 * + DBPedia.DBPedia_results.get(i).title + "</p><br><p>" +
			 * interlinker.final_results.get(i).keySet().toArray()[0] + ":" +
			 * interlinker.final_results.get(i).values().toArray()[0]
			 */
		}

		DBPedia.reset();

		LinkedMDB.reset();
		IMDB.reset();
		TMDB.reset();

		return message;
	}

	public static String InterlinkPlaces(String title, char domain,
			int ResultsNumber, int threshold, int GS_version)
			throws JSONException, IOException {

		String message = " ";

		filter_info = new HashMap<String, Set<String>>();
		filter = new HashSet<String>();
		interlinker = new SourceInterlinker(domain, ResultsNumber, threshold,
				GS_version);
		interlinker.getGS(domain, GS_version);
		try {
			current_t = 'g';
			Geonames.getPlaces(title, ResultsNumber);
			interlinker.match(Geonames.geonames_results, "Geonames");

			current_t = 'o';
			interlinker.match(GoogleMap.google_results, "Google");
			GoogleMap.getPlaces(title);

			current_t = 'd';
			DBPedia.getPlaces(title, ResultsNumber); // WITH paramAuteur
			interlinker.match(DBPedia.DBPedia_results, "DBpedia");

			current_t = 'l';
			LinkedGeoData.getPlaces(title, ResultsNumber); // WITH paramAuteur
			interlinker.match(LinkedGeoData.linkedgeo_results, "LinkedGeoData");

			DBPedia.reset();
			LinkedGeoData.reset();
			GoogleMap.reset();
			Geonames.reset();
		} catch (Exception e) {
		}
		Collections.shuffle(interlinker.final_results, new Random(4));
		String fromw = "", titleToDisplay = "", imagePath = "", overview = "", releaseDate = "", MovieLink = "", fromw_display = "";
		locations = new double[interlinker.final_results.size()][4];
		message += "<button class='showMap' onclick='eval(displayMapEvent(this));'>Show Results in a Map</button>";

		int index_separator, index_separator2;
		String lat;
		String longt;
		for (int i = 0; i < interlinker.final_results.size(); i++) {

			fromw = interlinker.final_results.get(i).get("fromWhere");
			imagePath = "http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld="
					+ i + "|FFFFFF|000000";
			if (fromw == "Geonames") {
				titleToDisplay = interlinker.final_results.get(i).get("name");
				overview = interlinker.final_results.get(i).get("lng") + "/"
						+ interlinker.final_results.get(i).get("lat");
				locations[i][0] = Double.parseDouble(interlinker.final_results
						.get(i).get("lat"));
				locations[i][1] = Double.parseDouble(interlinker.final_results
						.get(i).get("lng"));
				locations[i][2] = 0;
				locations[i][3] = i;
				releaseDate = interlinker.final_results.get(i).get("country")
						+ ", "
						+ interlinker.final_results.get(i).get("country_Code");
				MovieLink = "#";
				fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightslategray;'>"
						+ fromw + "</span>";

			} else if (fromw == "Google") {
				titleToDisplay = interlinker.final_results.get(i).get("name");
				if (interlinker.final_results.get(i).get("icon").length() > 10)

					overview = interlinker.final_results.get(i).get("lng")
							+ "/" + interlinker.final_results.get(i).get("lat");
				locations[i][0] = Double.parseDouble(interlinker.final_results
						.get(i).get("lat"));
				locations[i][1] = Double.parseDouble(interlinker.final_results
						.get(i).get("lng"));
				locations[i][2] = 1;
				locations[i][3] = i;
				releaseDate = interlinker.final_results.get(i).get(
						"formatted_address");
				MovieLink = "#";
				fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightcyan;'>"
						+ fromw + "</span>";

			} else if (fromw == "LinkedGeoData") {
				titleToDisplay = interlinker.final_results.get(i).get(
						"rdf_schema_label");
				overview = interlinker.final_results.get(i).get("wgs_pos_long")
						+ "/"
						+ interlinker.final_results.get(i).get("wgs_pos_lat");
				locations[i][0] = Double.parseDouble(interlinker.final_results
						.get(i).get("wgs_pos_lat").toString());
				locations[i][1] = Double.parseDouble(interlinker.final_results
						.get(i).get("wgs_pos_long").toString());
				locations[i][2] = 2;
				locations[i][3] = i;
				releaseDate = interlinker.final_results.get(i).get(
						"rdf_schema_label");
				MovieLink = "http://linkedgeodata.org/page/triplify/"
						+ interlinker.final_results.get(i).get("urrri");
				fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightcoral;'>"
						+ fromw + "</span>";

			} else if (fromw == "DBpedia") {
				titleToDisplay = interlinker.final_results.get(i).get(
						"rdf_schema_label");

				overview = interlinker.final_results.get(i).get("wgs_pos_long")
						+ "/"
						+ interlinker.final_results.get(i).get("wgs_pos_lat");

				if (interlinker.final_results.get(i).get("wgs_pos_lat") != null
						&& interlinker.final_results.get(i).get("wgs_pos_long") != null) {

					lat = interlinker.final_results.get(i).get("wgs_pos_lat")
							.toString();
					index_separator = lat.indexOf("||");
					longt = interlinker.final_results.get(i)
							.get("wgs_pos_long").toString();
					index_separator2 = longt.indexOf("||");
					if (index_separator > 0)
						lat = lat.substring(0, index_separator);
					if (index_separator2 > 0)
						longt = longt.substring(0, index_separator2);

					locations[i][0] = Double.parseDouble(lat.toString());
					locations[i][1] = Double.parseDouble(longt.toString());
					locations[i][2] = 3;
					locations[i][3] = i;
				}
				if (interlinker.final_results.get(i).get("is_Part_Of") != null
						&& interlinker.final_results.get(i).get("is_Part_Of") != null) {
					releaseDate = (getLastBitFromUrl(interlinker.final_results
							.get(i).get("is_Part_Of")).replace("_", ""))
							.replace("||", ",")
							+ ", "
							+ interlinker.final_results.get(i).get("country");
				}
				MovieLink = interlinker.final_results.get(i).get("urrri");
				fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightblue;'>"
						+ fromw + "</span>";
			}
			current_t += 100;

			message += "<div class='row mb20'>" + "<div class='col-md-12'>"
					+ "<div class='candidate'>"
					+ fromw_display
					+ " <div class='candidate__header clearfix mb20'>"
					+ "<img style='width:38px' class='candidate__img' src='"
					+ imagePath
					+ "' onError=\"this.onerror=null;this.src='https://www.gumtree.com/static/1/resources/assets/rwd/images/orphans/a37b37d99e7cef805f354d47.noimage_thumbnail.png';\">"
					+ "<div class='candidate__details'>"
					+ "<h1 class='candidate__fullname'><a href='"
					+ MovieLink
					+ "'>"
					+ titleToDisplay
					+ "</a></h1>"
					+ "<h2 class='candidate__location'>"
					+ releaseDate
					+ ""
					+ "</h2>"
					+ "</div></div>"
					+ "<ul class='list-inline'>"
					+ "<li class='mb10'><span class='candidate__years'>Longtitude/Latitude:</span> <span class='candidate__title'>"
					+ overview
					+ "</span></li>"
					+ "</ul>"
					+ "<br>"
					+ "<div class='moreInfo'>" + "<ul 'list-inline2'>";
			for (String key : interlinker.final_results.get(i).keySet()) {
				// if (key != "producer" && key != "rdf_schema_comment"
				// && key != "release_date" && key != "name"
				// && key != "fromWhere" && key != "urrri") {
				message += "<li><span class='candidate__years'>"
						+ key
						+ "</span>"
						+ " <div class='candidate__title'>"
						+ getLastBitFromUrl(interlinker.final_results.get(i)
								.get(key)) + "<div></li>";
				// }
			}
			message += "</ul>"
					+ "</div><div class='accordion'>"
					+ "<img href='#' src='https://www.cohen-west.com/wp-content/themes/metro/images/menu-button.png' "
					+ "style='width: 20px;'>" + "</div></div>" + "</div>";
			for (String key : interlinker.final_results.get(i).keySet()) {
				filter.add(key.trim());
				AppendToKey(key.trim(), splitIntoSet(interlinker.final_results
						.get(i).get(key)));

			}
			LoadDropDown.setInterlinker(interlinker);
			/*
			 * 
			 * + DBPedia.DBPedia_results.get(i).title + "</p><br><p>" +
			 * interlinker.final_results.get(i).keySet().toArray()[0] + ":" +
			 * interlinker.final_results.get(i).values().toArray()[0]
			 */
		}

		return message;
	}

	public static String InterlinkPeople(String title, char domain,
			int ResultsNumber, int threshold, int GS_version)
			throws JSONException, IOException {
		String message = " ";
		int totalResults = 0;
		filter_info = new HashMap<String, Set<String>>();
		filter = new HashSet<String>();
		interlinker = new SourceInterlinker(domain, ResultsNumber, threshold,
				GS_version);
		interlinker.getGS(domain, GS_version);

		// LinkedMDB.getMovies(title, ResultsNumber);// SPACES TO BE FILLED

		/*
		 * current_t='d'; DBPedia.getPeople(title, ResultsNumber);
		 * interlinker.match(DBPedia.DBPedia_results, "DBpedia");
		 */
		current_t = 'o';
		GoogleMap.getPeopleIDs(title);
		GoogleMap.getPeople();
		interlinker.match(GoogleMap.google_results, "Google");
		try {
			/*
			 * current_t='i'; IMDB.getMovies(title);
			 * interlinker.match(IMDB.IMDB_results, "IMDB");
			 */
		} catch (JSONException je) {

		}
		/*
		 * current_t='b'; LinkedMDB.getPeople(title, ResultsNumber);
		 * interlinker.match(LinkedMDB.LMDB_results, "LinkedMDB");
		 */
		// WITH paramAuteur
		/*
		 * IMDB.getElements(); TMDB.readJsonFromUrl(title); TMDB.getElements();
		 */

		Collections.shuffle(interlinker.final_results, new Random(4));
		String fromw = "", titleToDisplay = "", imagePath = "", overview = "", releaseDate = "", MovieLink = "", fromw_display = "";
		for (int i = 0; i < interlinker.final_results.size(); i++) {

			fromw = interlinker.final_results.get(i).get("fromWhere");

			if (fromw == "LinkedMDB") {
				titleToDisplay = interlinker.final_results.get(i).get(
						"rdf_schema_label");
				// imagePath = "http://image.tmdb.org/t/p/original//"
				// + interlinker.final_results.get(i).get("poster_path");
				// overview = interlinker.final_results.get(i).get("overview");
				releaseDate = interlinker.final_results.get(i)
						.get("birth_Year");
				// MovieLink = "https://www.themoviedb.org/movie/"
				// + interlinker.final_results.get(i).get("id");
				fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightslategray;'>"
						+ fromw + "</span>";

			} else if (fromw == "IMDB") {
				titleToDisplay = interlinker.final_results.get(i).get("title");
				// if (interlinker.final_results.get(i).get("Poster").length() >
				// 10)
				// imagePath = interlinker.final_results.get(i).get("Poster");
				// overview = interlinker.final_results.get(i).get("overview");
				releaseDate = interlinker.final_results.get(i)
						.get("birth_Year");
				// MovieLink = "http://www.imdb.com/title/"
				// + interlinker.final_results.get(i).get("imdbID");
				fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightcyan;'>"
						+ fromw + "</span>";

			} else if (fromw == "LastFM") {
				// titleToDisplay = interlinker.final_results.get(i).get(
				// "rdf_schema_label");
				// imagePath =
				// "http://localhost:185/SemiLD-Servlet/C:/Users/MSK137/workspace/SemiLD-Servlet/WebContent/images/default-thumbnail.jpg";
				// overview = "Same as "
				// + interlinker.final_results.get(i).get("owl_same_As");
				// releaseDate = interlinker.final_results.get(i).get(
				// "release_date");
				// MovieLink = interlinker.final_results.get(i).get("urrri");
				fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightcoral;'>"
						+ fromw + "</span>";

			} else if (fromw == "DBpedia") {
				titleToDisplay = interlinker.final_results.get(i).get(
						"rdf_schema_label");
				imagePath = PrepareIMG(interlinker.final_results.get(i).get(
						"thumbnail"));
				// overview = interlinker.final_results.get(i).get(
				// "rdf_schema_comment");
				releaseDate = interlinker.final_results.get(i)
						.get("birth_Year");
				// MovieLink = interlinker.final_results.get(i).get("urrri");
				fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightgreen;'>"
						+ fromw + "</span>";
			} else if (fromw == "Google") {
				titleToDisplay = interlinker.final_results.get(i).get(
						"displayName");
				imagePath = PrepareIMG(interlinker.final_results.get(i).get(
						"image"));
				// overview = interlinker.final_results.get(i).get(
				// "rdf_schema_comment");
				releaseDate = interlinker.final_results.get(i)
						.get("occupation");
				MovieLink = interlinker.final_results.get(i).get("urrri");
				fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightblue;'>"
						+ fromw + "</span>";
			}

			message += "<div class='row mb20'>"
					+ "<div class='col-md-12'>"
					+ "<div class='candidate'>"
					+ fromw_display
					+ " <div class='candidate__header clearfix mb20'>"
					+ "<img class='candidate__img' src='"
					+ imagePath
					+ "' onError=\"this.onerror=null;this.src='https://www.gumtree.com/static/1/resources/assets/rwd/images/orphans/a37b37d99e7cef805f354d47.noimage_thumbnail.png';\">"
					+ "<div class='candidate__details'>"
					+ "<h1 class='candidate__fullname'><a href='" + MovieLink
					+ "'>" + titleToDisplay + "</a></h1>"
					+ "<h2 class='candidate__location'>" + releaseDate + ""
					+ "</h2>" + "</div></div>"

					+ "<br>" + "<div class='moreInfo'>" + "<ul 'list-inline2'>";
			for (String key : interlinker.final_results.get(i).keySet()) {
				// if (key != "producer" && key != "rdf_schema_comment"
				// && key != "release_date" && key != "name"
				// && key != "fromWhere" && key != "urrri") {
				message += "<li><span class='candidate__years'>"
						+ key
						+ "</span>"
						+ " <div class='candidate__title'>"
						+ getLastBitFromUrl(interlinker.final_results.get(i)
								.get(key)) + "<div></li>";
				// }
			}
			message += "</ul>"
					+ "</div><div class='accordion'>"
					+ "<img href='#' src='https://www.cohen-west.com/wp-content/themes/metro/images/menu-button.png' "
					+ "style='width: 20px;'>" + "</div></div>" + "</div>";
			for (String key : interlinker.final_results.get(i).keySet()) {
				filter.add(key.trim());
				AppendToKey(key.trim(), splitIntoSet(interlinker.final_results
						.get(i).get(key)));

			}
			LoadDropDown.setInterlinker(interlinker);
			/*
			 * 
			 * + DBPedia.DBPedia_results.get(i).title + "</p><br><p>" +
			 * interlinker.final_results.get(i).keySet().toArray()[0] + ":" +
			 * interlinker.final_results.get(i).values().toArray()[0]
			 */
		}

		// DBPedia.reset();
		GoogleMap.reset();
		// IMDB.reset();

		/*
		 * LinkedMDB.reset(); IMDB.reset(); TMDB.reset();
		 */
		return message;
	}

	// ---------------------------------------

	public static String getLast4char(String word) {
		word = word.trim();

		if (word.length() == 4 || word.length() < 4) {
			return word;
		} else {
			return word.substring(word.length() - 4);
		}
	}

	public static String PrepareIMG(String word) {
		if (word == null) {
			return null;
		} else if (word.length() > 8) {
			word = word.trim();
			word = word.replace("commons", "en");
			word = word.replaceAll("(jpg/)[^&]*(px)", "$1100$2");
		}
		return word.trim();

	}

	public static HashSet<String> splitIntoSet(String str) {
		if (str == null)
			return null;

		HashSet<String> myHashSet = new HashSet(100); // Or a more realistic
														// size
		StringTokenizer st = new StringTokenizer(str, "||");
		while (st.hasMoreTokens())
			myHashSet.add(st.nextToken());

		return myHashSet;
	}

	public static void AppendToKey(String key, HashSet<String> myHashSet) {
		if (key != null && myHashSet != null) {

			Set<String> oldHashSet = new HashSet(myHashSet.size());

			if (filter_info.containsKey(key)) {
				oldHashSet = filter_info.get(key);
				oldHashSet.addAll(myHashSet);
				filter_info.put(key, oldHashSet);

			} else {

				filter_info.put(key, myHashSet);
			}
		}

	}

	public static String getLastBitFromUrl(final String url) {
		if (url != null) {
			String[] urls = url.split(Pattern.quote("||"));
			String output = "";
			// return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <--
			// incorrect
			for (String str : urls)
				output += str.replaceFirst(".*/([^/?]+).*", "$1") + ", ";

			return output.substring(0, output.length() - 2);
		} else
			return null;
	}

}
