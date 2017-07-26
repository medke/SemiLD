package Crawlers;

import java.net.URLDecoder;
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

public class LinkedGeoData {
	public static ArrayList<String> linkedgeo_labels, linkedgeo_urls;
	public static ArrayList<ArrayList<String>> linkedgeo_properties = new ArrayList<ArrayList<String>>(); ;
	public static ArrayList<Result> linkedgeo_results= new ArrayList<Result>();
	private static Result result;
	public LinkedGeoData() {

	}



	
	public static void getPlaces(String keyword, int limit) {
		linkedgeo_labels = new ArrayList();
		linkedgeo_urls = new ArrayList();
		
		String uri=""; //this is just to store ?s
		String sparqlQueryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "   PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ " SELECT ?s WHERE"
				+ "{"
				+ "?s rdf:type <http://linkedgeodata.org/ontology/Place>. "
				+ " ?s rdfs:label ?f ."
				+" FILTER ( REGEX(?f, '"+keyword+"', 'i') )"
				+ "} limit "+limit;

		System.out.println("\nLGD-Rertrieving Places");
		
		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, Connector.model3);
		ResultSet s = qexec.execSelect();
		ResultSetRewindable resR= ResultSetFactory.copyResults(s);
		
		int i=0;
		System.out.println("Rertrieving Properties");
		while (resR.hasNext()) {

			
			QuerySolution so = resR.nextSolution();
			uri=so.get("?s").toString();
			linkedgeo_urls.add(uri);
			//linkedgeo_labels.add(so.get("?f").toString());
			System.out.print((i++)+"-");
			getData(uri);

		}

	}

	public static void getData(String movieURI) {


		String sparqlQueryString = "SELECT  ?p (group_concat(distinct ?o ; separator = ' || ') AS ?obj) WHERE{" + "" + "<"
				+ movieURI + "> ?p ?o . " + "}group by ?p";

		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query,
				Connector.model3);
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
		linkedgeo_results.add(result);

	}
	
	public static void getProperties(String movieURI) {
		ArrayList<String> temp_properties = new ArrayList();
	
		String sparqlQueryString = "SELECT distinct ?p WHERE{" + "" + "<"
				+ movieURI + "> ?p ?o . " + "}";

		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, Connector.model3);
		ResultSet s = qexec.execSelect();
		ResultSetRewindable resR= ResultSetFactory.copyResults(s);
		
		while (resR.hasNext()) {
			QuerySolution so = resR.nextSolution();
			temp_properties.add(prepareLabel(so.get("?p").toString()));
		}
		linkedgeo_properties.add(temp_properties);
		//temp_properties.clear();
		
	}
	public static String getLastBitFromUrl(final String url){
	    // return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
	    return url.replaceFirst(".*/([^/?]+).*", "$1");
	}
	public static String prepareLabel(final String url){
	    // return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
	    return URLDecoder.decode(getLastBitFromUrl(url)).replace("#", "_").replaceAll("(.)([A-Z])", "$1_$2");
	}
	public static void reset()
	{
		linkedgeo_properties.clear();
		linkedgeo_results.clear();
		linkedgeo_urls.clear();
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
