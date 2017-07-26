package com.SemiLD;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdtjena.HDTGraph;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.graph.Graph; 

import edu.umbc.web.Similarity;


public class Connector {
	public static Model tdb,model,model2,model3;
	public static HDT hdt,hdt2;
	public static Similarity sim;
	public static ArrayList<ResultSetRewindable> res= new ArrayList<ResultSetRewindable>();
	
	public Connector() throws IOException
	{
		//Getting the runtime reference from system
		
		
		System.out.print("Read HDT0");
		readHDT2();
		System.out.print("Read HDT1");
		readHDT3();
		System.out.print("Read HDT2");
		readHDT();        
		sim = new Similarity ();
	}
	
	
		public static void readHDT2() throws IOException {
			hdt = HDTManager.mapIndexedHDT("E:/HDT_Datasets/linkedmdb.hdt", null);

			// Create Jena Model on top of HDT.
			HDTGraph graph = new HDTGraph(hdt);
			tdb = ModelFactory.createModelForGraph(graph);
		}
		public static void readHDT3() throws IOException {
			hdt2 = HDTManager.mapIndexedHDT("E:/HDT_Datasets/linkedgeodata.hdt", null);

			// Create Jena Model on top of HDT.
			HDTGraph graph = new HDTGraph(hdt2);
			model3 = ModelFactory.createModelForGraph(graph);
		}

	public static void readHDT() throws IOException {
		hdt = HDTManager.mapIndexedHDT("E:/DBpedia/dbpedia3.hdt", null);

		// Create Jena Model on top of HDT.
		HDTGraph graph = new HDTGraph(hdt);
		model2 = ModelFactory.createModelForGraph(graph);
	}

}
