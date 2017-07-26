package Crawlers;

import java.util.ArrayList;

import com.SemiLD.Connector;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.sparql.resultset.ResultSetRewindable;

public class LinkedMDB {
	public static ArrayList<String> LMDB_labels, LMDB_urls;
	public static ArrayList<ArrayList<String>> LMDB_properties = new ArrayList<ArrayList<String>>();
	public static ArrayList<Result> LMDB_results= new ArrayList<Result>();
	private static Result result;
	public static void getMovies(String keyword, int limit) {
		LMDB_labels = new ArrayList();
		LMDB_urls = new ArrayList();
		String keywords[]= keyword.split(" ");
		String uri = ""; // this is just to store ?s
		String sparqlQueryString = "PREFIX mdb: <http://data.linkedmdb.org/resource/movie/film>"
				+ " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ " PREFIX dc: <http://purl.org/dc/terms/>"
				+ "   PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				
				+ " SELECT ?s WHERE {"
				+ "   ?s rdf:type mdb: . "
				+ "   ?s dc:title ?l . ";
				for(String key: keywords)
				{
					    sparqlQueryString+= "FILTER contains(?l,'"
						+ key.trim()
						+ "').";
				}
				 		sparqlQueryString+= "} limit " + limit;
		System.out.println(sparqlQueryString);
		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, Connector.tdb);
		ResultSet s = qexec.execSelect();
		ResultSetRewindable resR= ResultSetFactory.copyResults(s);
		int i=0;
		System.out.println("Rertrieving Properties");
		while (resR.hasNext()) {

			
			QuerySolution so = resR.nextSolution();
			uri=so.get("?s").toString();
			LMDB_urls.add(uri);
			//getProperties(uri);
			getData(uri);
		}

	}
	public static void getData(String movieURI) {



		String sparqlQueryString = "SELECT  ?p (group_concat(distinct ?o ; separator = ' || ') AS ?obj) WHERE{" + "" + "<"
				+ movieURI + "> ?p ?o . " + "}group by ?p";

		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query,
				Connector.tdb);
		ResultSet s = qexec.execSelect();
		ResultSetRewindable resR = ResultSetFactory.copyResults(s);
		result= new Result(movieURI);
		while (resR.hasNext()) {
			QuerySolution so = resR.nextSolution();
			result.predicates.put(prepareLabel(so.get("?p").toString()), so.get("?obj").toString());

		}
		result.predicates.put("fromWhere", "LinkedMDB");

		result.setLink(movieURI);

		System.out.println("-----------");
		System.out.println(result.predicates.toString());
		LMDB_results.add(result);
	}
	public static void getPeople(String keyword, int limit) {
		LMDB_labels = new ArrayList();
		LMDB_urls = new ArrayList();
		String keywords[]= keyword.split(" ");
		String uri = ""; // this is just to store ?s
		String sparqlQueryString = "PREFIX mdb: <http://data.linkedmdb.org/resource/movie/>"
				+ " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ " PREFIX dc: <http://purl.org/dc/terms/>"
				+ "   PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				
				+ " SELECT distinct ?s WHERE {"
				+"{"
				+ "{ ?s rdf:type mdb:actor . "
				+ " ?s mdb:actor_name ?l."
				+ "}"
				+ "UNION"
				+"{?s rdf:type mdb:writer ."
				+ "?s mdb:writer_name ?l."
				+ "}"
				+ "UNION"
				+"{?s rdf:type mdb:producer ."
				+ "?s mdb:producer_name ?l.}"
				+ "UNION"
				+"{?s rdf:type mdb:editor ."
				+ "?s mdb:editor_name ?l."
				+ "}"
				+"}";
				for(String key: keywords)
				{
					    sparqlQueryString+= "FILTER contains(?l,'"
						+ key.trim()
						+ "').";
				}
				 		sparqlQueryString+= "} limit " + limit;
		System.out.println(sparqlQueryString);
		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, Connector.tdb);
		ResultSet s = qexec.execSelect();
		ResultSetRewindable resR= ResultSetFactory.copyResults(s);
		int i=0;
		System.out.println("Rertrieving Properties");
		while (resR.hasNext()) {

			
			QuerySolution so = resR.nextSolution();
			uri=so.get("?s").toString();
			LMDB_urls.add(uri);
			//DBPedia_labels.add(so.get("?f").toString());
			System.out.print((i++)+")"+ uri+"-");
			getData(uri);

		}

	}

	
	public static void getProperties(String movieURI) {
		ArrayList<String> temp_properties = new ArrayList();
	
		String sparqlQueryString = "SELECT distinct ?p WHERE{" + "" + "<"
				+ movieURI + "> ?p ?o . " + "}";

		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, Connector.tdb);
		ResultSet s = qexec.execSelect();
		ResultSetRewindable resR= ResultSetFactory.copyResults(s);
		
		while (resR.hasNext()) {
			QuerySolution so = resR.nextSolution();
			temp_properties.add(prepareLabel(so.get("?p").toString()));
		}
		LMDB_properties.add(temp_properties);

		
	}
	public static String getLastBitFromUrl(final String url){
	    // return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
	    return url.replaceFirst(".*/([^/?]+).*", "$1");
	}
	public static String prepareLabel(final String url){
	    // return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
	    return getLastBitFromUrl(url).replace("#", "_").replaceAll("(.)([A-Z])", "$1_$2");
	}
	public static void reset()
	{
		LMDB_properties.clear();
		LMDB_results.clear();
		result=null;
	}
}
