package Crawlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import com.SemiLD.Connector;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.sparql.resultset.ResultSetRewindable;

public class DBPedia {
	public static ArrayList<String> DBPedia_labels, DBPedia_urls;
	public static ArrayList<ArrayList<String>> DBPedia_properties= new ArrayList<ArrayList<String>>();;
	public static ArrayList<Result> DBPedia_results= new ArrayList<Result>();
	private static Result result;
	public DBPedia() {

	}

	public static void getMovies(String keyword, int limit) {
		DBPedia_labels = new ArrayList();
		DBPedia_urls = new ArrayList();
		String keywords[]= keyword.split(" ");
		String uri = ""; // this is just to store ?s
		String sparqlQueryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "   PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ " SELECT ?s WHERE"
				+ "{"
				+ "?s rdf:type <http://schema.org/Movie>. "
				+ " ?s rdfs:label ?f .";
		for(String key: keywords)
		{
			    sparqlQueryString+= "FILTER contains(?f,'"
				+ key.trim()
				+ "').";
		}
		 		sparqlQueryString+= "} limit " + limit;

		System.out.println("Rertrieving Movies");

		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query,
				Connector.model2);
		ResultSet s = qexec.execSelect();
		ResultSetRewindable resR = ResultSetFactory.copyResults(s);

		int i = 0;
		System.out.println("Rertrieving Properties");
		while (resR.hasNext()) {

			QuerySolution so = resR.nextSolution();
			uri = so.get("?s").toString();
			DBPedia_urls.add(uri);
			// DBPedia_labels.add(so.get("?f").toString());
			System.out.print((i++) + "-");
		//	getProperties(uri);
			getData(uri);

		}

	}	public static void getMovies(String[] keyword, int limit) {
		DBPedia_labels = new ArrayList();
		DBPedia_urls = new ArrayList();

		String uri = ""; // this is just to store ?s
		String sparqlQueryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "   PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ " SELECT ?s WHERE"
				+ "{"
				+ "?s rdf:type <http://schema.org/Movie>. "
				+ " ?s rdfs:label ?f ."
				+ "FILTER contains(?f,'"
				+ keyword[0]
				+ "')."

				+ "} limit " + limit;

		System.out.println("Rertrieving Movies");

		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query,
				Connector.model2);
		ResultSet s = qexec.execSelect();
		ResultSetRewindable resR = ResultSetFactory.copyResults(s);

		int i = 0;
		System.out.println("Rertrieving Properties");
		while (resR.hasNext()) {

			QuerySolution so = resR.nextSolution();
			uri = so.get("?s").toString();
			DBPedia_urls.add(uri);
			// DBPedia_labels.add(so.get("?f").toString());
			System.out.print((i++) + "-");
		//	getProperties(uri);
			getData(uri);

		}

	}

	public static void getPlaces(String keyword, int limit) {
		DBPedia_labels = new ArrayList();
		DBPedia_urls = new ArrayList();
		String uri = ""; // this is just to store ?s
		String sparqlQueryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "   PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ " SELECT ?s WHERE"
				+ "{"
				+ "?s rdf:type <http://schema.org/Place>. "
				+ " ?s rdfs:label ?f ."
				+ "FILTER contains(?f,'"
				+ keyword
				+ "')."

				+ "} limit " + limit;

		System.out.println("DBpedia - Rertrieving Places");

		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query,
				Connector.model2);
		ResultSet s = qexec.execSelect();
		ResultSetRewindable resR = ResultSetFactory.copyResults(s);

		int i = 0;
		System.out.println("Rertrieving Properties");
		while (resR.hasNext()) {

			QuerySolution so = resR.nextSolution();
			uri = so.get("?s").toString();
			DBPedia_urls.add(uri);
			// DBPedia_labels.add(so.get("?f").toString());
			System.out.print((i++) + "-");
			getData(uri);

		}

	}
	public static void getPeople(String keyword, int limit) {
		DBPedia_labels = new ArrayList();
		DBPedia_urls = new ArrayList();
		String uri = ""; // this is just to store ?s
		String sparqlQueryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "   PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ " SELECT ?s WHERE"
				+ "{"
				+ "?s rdf:type <http://schema.org/Person>. "
				+ " ?s rdfs:label ?f ."
				+ "FILTER contains(?f,'"
				+ keyword
				+ "')."

				+ "} limit " + limit;

		System.out.println("DBpedia - Rertrieving People");

		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query,
				Connector.model2);
		ResultSet s = qexec.execSelect();
		ResultSetRewindable resR = ResultSetFactory.copyResults(s);

		int i = 0;
		System.out.println("Rertrieving Properties");
		while (resR.hasNext()) {

			QuerySolution so = resR.nextSolution();
			uri = so.get("?s").toString();
			DBPedia_urls.add(uri);
			// DBPedia_labels.add(so.get("?f").toString());
			System.out.print((i++) + "-");
			getData(uri);

		}

	}

	public static void getProperties(String movieURI) {
		ArrayList<String> temp_properties = new ArrayList();

		String sparqlQueryString = "SELECT distinct ?p WHERE{" + "" + "<"
				+ movieURI + "> ?p ?o . " + "}";

		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query,
				Connector.model2);
		ResultSet s = qexec.execSelect();
		ResultSetRewindable resR = ResultSetFactory.copyResults(s);

		while (resR.hasNext()) {
			QuerySolution so = resR.nextSolution();
			temp_properties.add(prepareLabel(so.get("?p").toString()));
			
		}
		DBPedia_properties.add(temp_properties);
		//temp_properties.clear();

	}
	public static void getData(String movieURI) {


		String sparqlQueryString = "SELECT  ?p (group_concat(distinct ?o ; separator = ' || ') AS ?obj) WHERE{" + "" + "<"
				+ movieURI + "> ?p ?o . " + "}group by ?p";

		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query,
				Connector.model2);
		ResultSet s = qexec.execSelect();
		ResultSetRewindable resR = ResultSetFactory.copyResults(s);
		result= new Result(movieURI);
		while (resR.hasNext()) {
			QuerySolution so = resR.nextSolution();
			result.predicates.put(prepareLabel(so.get("?p").toString()), so.get("?obj").toString());

		}
		result.predicates.put("fromWhere", "DBpedia");
		result.setLink(movieURI);


		System.out.println("-----------");
		System.out.println(result.predicates.toString());
		DBPedia_results.add(result);

	}
	public static void getMoreData(String movieURI, String p) {
		ArrayList<String> temp_properties = new ArrayList();
		ArrayList<String> temp_results = new ArrayList();

		String sparqlQueryString = "SELECT  ?o WHERE{" + "" + "<"
				+ movieURI + ">"+"<"+ p + "> + ?o . " + "}";

		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query,
				Connector.model2);
		ResultSet s = qexec.execSelect();
		ResultSetRewindable resR = ResultSetFactory.copyResults(s);
		
		while (resR.hasNext()) {
			QuerySolution so = resR.nextSolution();
		}
       
		//temp_properties.clear();

	}

	public static String getLastBitFromUrl(final String url) {
		// return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
		return url.replaceFirst(".*/([^/?]+).*", "$1");
	}

	public static String prepareLabel(final String url) {
		// return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
		return getLastBitFromUrl(url).replace("#", "_").replaceAll(
				"(.)([A-Z])", "$1_$2");
	}

	public static void reset() {
		DBPedia_properties.clear();
		DBPedia_results.clear();
		DBPedia_urls= null;
		result =null;

	}

	public static void removeDuplicates2d() {

		Set<String> set = new LinkedHashSet<String>();
		for (ArrayList<String> list:DBPedia.DBPedia_properties) {
		    set.addAll (list);
		    
		}
		DBPedia.DBPedia_properties.clear();
		DBPedia.DBPedia_properties.add(new ArrayList<String>(set));
	}
}
