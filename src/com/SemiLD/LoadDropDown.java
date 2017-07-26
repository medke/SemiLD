package com.SemiLD;

import GlobalSchema.SourceInterlinker;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoadDropDown
 */
public class LoadDropDown extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Map<String, Set<String>> filter;
	public static SourceInterlinker interlinker;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoadDropDown() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {

		String message="";
		PrintWriter out = response.getWriter();
		try {
			String category = request.getParameter("category");
			System.out.println("this is a "+ category);
			HashSet<String> al = new HashSet<String>((Home.filter_info.get(category)));
			System.out.println(Arrays.toString(al.toArray()));
			for(String item: al)
			{
				message+="<option value='"+item+"'>"+item+"</option>";
				
			}
			message+="<option value='All'>All</option>";

			out.print(message);

		} catch (Exception ex) {
			out.print("Error getting product name..." + ex.toString());
		} finally {
			out.close();
		}

	}
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	
	String property= request.getParameter("properties");
	String value= request.getParameter("values");
	System.out.println(property+":"+value);
	PrintWriter out = response.getWriter();
	try {
		String message= searchForKeyValue(property,value, Home.current_d);
		System.out.println("=--> "+Home.current_d);
		System.out.println(message);
		out.print(message);
		

	} catch (Exception ex) {
		out.print("Error filtering the results" + ex.toString());
	} finally {
		out.close();
	}

	}
	public static void setFilter(Map<String, Set<String>> f) {
		filter = new HashMap<String, Set<String>>(f);
	}
	public static String searchForKeyValue(String key, String value, char d)
	{
		String message="",val="";
		String fromw = "", titleToDisplay = "", imagePath = "", overview = "", releaseDate = "", MovieLink = "", fromw_display = "";

			
		System.out.println("===>"+Home.interlinker.final_results.size()+"\n"+Home.interlinker.final_results.getClass().getName()+"-\n"+Home.interlinker.final_results.toString());
		for (int ii = 0; ii < Home.interlinker.final_results.size(); ii++) {
		
			System.out.println("t"+ii);
			val= Home.interlinker.final_results.get(ii).get(key);
			if((Home.interlinker.final_results.get(ii).containsKey(key) && val.contains(value)) || value.toLowerCase().trim()=="all")
			{
				System.out.println("  true");
			
			
			/*
			if(Home.interlinker.final_results.get(ii).get(key).contains(value))
				{
				v2=false;
				System.out.println("v2 true");
				}
				*/

			if (d=='m')
			{
				fromw = interlinker.final_results.get(ii).get("fromWhere");

				if (fromw == "TheMovieDB") {
					titleToDisplay = interlinker.final_results.get(ii).get("title");
					imagePath = "http://image.tmdb.org/t/p/original//"
							+ interlinker.final_results.get(ii).get("poster_path");
					overview = interlinker.final_results.get(ii).get("overview");
					releaseDate = interlinker.final_results.get(ii).get(
							"release_date");
					MovieLink = "https://www.themoviedb.org/movie/"
							+ interlinker.final_results.get(ii).get("id");
					fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightslategray;'>"
							+ fromw + "</span>";

				} else if (fromw == "IMDB") {
					titleToDisplay = interlinker.final_results.get(ii).get("title");
					if (interlinker.final_results.get(ii).get("Poster").length() > 10)
						imagePath = interlinker.final_results.get(ii).get("Poster");
					overview = interlinker.final_results.get(ii).get("overview");
					releaseDate = interlinker.final_results.get(ii).get("Year");
					MovieLink = "http://www.imdb.com/title/"
							+ interlinker.final_results.get(ii).get("imdbID");
					fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightcyan;'>"
							+ fromw + "</span>";

				} else if (fromw == "LinkedMDB") {
					titleToDisplay = interlinker.final_results.get(ii).get(
							"rdf_schema_label");
					imagePath = "http://localhost:185/SemiLD-Servlet/C:/Users/MSK137/workspace/SemiLD-Servlet/WebContent/images/default-thumbnail.jpg";
					overview = "Same as "
							+ interlinker.final_results.get(ii).get("owl_same_As");
					releaseDate = interlinker.final_results.get(ii).get(
							"release_date");
					MovieLink = interlinker.final_results.get(ii).get("urrri");
					fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightcoral;'>"
							+ fromw + "</span>";

				} else if (fromw == "DBpedia") {
					titleToDisplay = interlinker.final_results.get(ii).get("name");
					imagePath = Home.PrepareIMG(interlinker.final_results.get(ii).get(
							"thumbnail"));
					overview = interlinker.final_results.get(ii).get(
							"rdf_schema_comment");
					releaseDate = interlinker.final_results.get(ii).get(
							"release_date");
					MovieLink = interlinker.final_results.get(ii).get("urrri");
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
						+ interlinker.final_results.get(ii).get("producer")
						+ "</span></li>"
						+ "<li><span class='candidate__years'>Description</span>"
						+ " <span class='candidate__title'>"
						+ overview
						+ "</li>"
						+ "</ul>"
						+ "<br>"
						+ "<div class='moreInfo'>"
						+ "<ul 'list-inline2'>";
				for (String key2 : interlinker.final_results.get(ii).keySet()) {
					if (key2 != "producer" && key2 != "rdf_schema_comment"
							&& key2 != "release_date" && key2 != "name"
							&& key2 != "fromWhere" && key2 != "urrri") {
						message += "<li><span class='candidate__years'>"
								+ key2
								+ "</span>"
								+ " <div class='candidate__title'>"
								+ Home.getLastBitFromUrl(interlinker.final_results
										.get(ii).get(key2)) + "<div></li>";
					}
				}
				message += "</ul>"
						+ "</div><div class='accordion'>"
						+ "<img href='#' src='https://www.cohen-west.com/wp-content/themes/metro/images/menu-button.png' "
						+ "style='width: 20px;'>" + "</div></div>" + "</div>";
			}else if(d=='p')
			{
				
			} 
			else if (d=='g')
			{		

				fromw = interlinker.final_results.get(ii).get("fromWhere");
				imagePath="http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld="+ii+"|FFFFFF|000000";
				if (fromw == "Geonames") {
					titleToDisplay = interlinker.final_results.get(ii).get("name");
					overview = interlinker.final_results.get(ii).get("lng") + "/"
							+ interlinker.final_results.get(ii).get("lat");
					
					releaseDate = interlinker.final_results.get(ii).get("country")
							+ ", "
							+ interlinker.final_results.get(ii).get("country_Code");
					MovieLink = "#";
					fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightslategray;'>"
							+ fromw + "</span>";

					
					
				} else if (fromw == "Google") {
					titleToDisplay = interlinker.final_results.get(ii).get("name");
					if (interlinker.final_results.get(ii).get("icon").length() > 10)
						
					overview = interlinker.final_results.get(ii).get("lng") + "/"
							+ interlinker.final_results.get(ii).get("lat");
					releaseDate = interlinker.final_results.get(ii).get(
							"formatted_address");
					MovieLink = "#";
					fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightcyan;'>"
							+ fromw + "</span>";

					
					
				} else if (fromw == "LinkedGeoData") {
					titleToDisplay = interlinker.final_results.get(ii).get(
							"rdf_schema_label");
					overview = interlinker.final_results.get(ii).get("wgs_pos_long")
							+ "/"
							+ interlinker.final_results.get(ii).get("wgs_pos_lat");
					releaseDate = interlinker.final_results.get(ii).get(
							"rdf_schema_label");
					MovieLink = "http://linkedgeodata.org/page/triplify/"
							+ interlinker.final_results.get(ii).get("urrri");
					fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightcoral;'>"
							+ fromw + "</span>";

					
					
				} else if (fromw == "DBpedia") {
					titleToDisplay = interlinker.final_results.get(ii).get(
							"rdf_schema_label");
					
					overview = interlinker.final_results.get(ii).get("wgs_pos_long")
							+ "/"
							+ interlinker.final_results.get(ii).get("wgs_pos_lat");

					if (interlinker.final_results.get(ii).get("is_Part_Of") != null
							&& interlinker.final_results.get(ii).get("is_Part_Of") != null) {
						releaseDate = (Home.getLastBitFromUrl(interlinker.final_results
								.get(ii).get("is_Part_Of")).replace("_", ""))
								.replace("||", ",")
								+ ", "
								+ interlinker.final_results.get(ii).get("country");
					}
					MovieLink = interlinker.final_results.get(ii).get("urrri");
					fromw_display = "        <span class='candidate__badge badge badge-default' style='background: lightblue;'>"
							+ fromw + "</span>";
				}


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
				for (String key2 : interlinker.final_results.get(ii).keySet()) {
					// if (key != "producer" && key != "rdf_schema_comment"
					// && key != "release_date" && key != "name"
					// && key != "fromWhere" && key != "urrri") {
					message += "<li><span class='candidate__years'>"
							+ key2
							+ "</span>"
							+ " <div class='candidate__title'>"
							+ Home.getLastBitFromUrl(interlinker.final_results.get(ii)
									.get(key2)) + "<div></li>";
					// }
				}
				message += "</ul>"
						+ "</div><div class='accordion'>"
						+ "<img href='#' src='https://www.cohen-west.com/wp-content/themes/metro/images/menu-button.png' "
						+ "style='width: 20px;'>" + "</div></div>" + "</div>";

			}
			}
			

		}
		
		
		return message;
	}
	public static Boolean checkSetValues(String val, Set<String> set) {
		
		for(String str:set)
		{
			if(str.contains(val))
				return true;
		}
		return false;
		
	}
	public static void setInterlinker(SourceInterlinker interlink) {
		// TODO Auto-generated method stub
		interlinker= new SourceInterlinker(interlink);
		
	}

}
