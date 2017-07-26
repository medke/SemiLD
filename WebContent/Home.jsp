<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="Home.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<title>SemiLD</title>
        <script>
        var myFunction = function() {
        	$.get("Home", function(responseText) {  
        		$("#firstDiv").html("");
        		var current= responseText.charAt(0);
        		if(current=='g')
        			{
        				$("#firstDiv").html("<div style='width: 300px;height: 200px;position: absolute;left: 50%;top: 50%;margin: -100px 0 0 -150px;'>"
        				+"<img style='display:block; margin:auto;width:100%; max-width:200px;'"
        				+" src='https://gisgba.geologie.ac.at/geonames/css/img/logoGeoNames.png'/><br>"+
        				"<img style='display:block; margin:auto;width:100%; max-width:50px;'"
        				+" src='https://s-media-cache-ak0.pinimg.com/originals/01/8d/2a/018d2ac351d2f2de9b77e870f29894e1.gif'/></div>");
        			}
        		if(current=='l')
    			{$("#firstDiv").html("<div style='width: 300px;height: 200px;position: absolute;left: 50%;top: 50%;margin: -100px 0 0 -150px;'>"
        				+"<img style='display:block; margin:auto;width:100%; max-width:200px;'"
        				+" src='http://linkedgeodata.org/files/lgdlogo.png'/><br>"+
        				"<img style='display:block; margin:auto;width:100%; max-width:50px;'"
        				+" src='https://s-media-cache-ak0.pinimg.com/originals/01/8d/2a/018d2ac351d2f2de9b77e870f29894e1.gif'/></div>");
    			
    			}
        		if(current=='o')
    			{
        			$("#firstDiv").html("<div style='width: 300px;height: 200px;position: absolute;left: 50%;top: 50%;margin: -100px 0 0 -150px;'>"
            				+"<img style='display:block; margin:auto;width:100%; max-width:200px;'"
            				+" src='https://www.gstatic.com/images/branding/googlelogo/2x/googlelogo_color_284x96dp.png'/><br>"+
            				"<img style='display:block; margin:auto;width:100%; max-width:50px;'"
            				+" src='https://s-media-cache-ak0.pinimg.com/originals/01/8d/2a/018d2ac351d2f2de9b77e870f29894e1.gif'/></div>");
        			
    				}
        		if(current=='d')
    			{
        			$("#firstDiv").html("<div style='width: 300px;height: 200px;position: absolute;left: 50%;top: 50%;margin: -100px 0 0 -150px;'>"
            				+"<img style='display:block; margin:auto;width:100%; max-width:200px;'"
            				+" src='http://wiki.dbpedia.org/sites/default/files/dbpedia_plain.png'/><br>"+
            				"<img style='display:block; margin:auto;width:100%; max-width:50px;'"
            				+" src='https://s-media-cache-ak0.pinimg.com/originals/01/8d/2a/018d2ac351d2f2de9b77e870f29894e1.gif'/></div>");
        			
    				}
        		if(current=='t')
    			{
        			$("#firstDiv").html("<div style='width: 300px;height: 200px;position: absolute;left: 50%;top: 50%;margin: -100px 0 0 -150px;'>"
            				+"<img style='display:block; margin:auto;width:100%; max-width:200px;'"
            				+" src='http://wiki.dbpedia.org/sites/default/files/dbpedia_plain.png'/><br>"+
            				"<img style='display:block; margin:auto;width:100%; max-width:50px;'"
            				+" src='https://s-media-cache-ak0.pinimg.com/originals/01/8d/2a/018d2ac351d2f2de9b77e870f29894e1.gif'/></div>");
    			}
        		if(current=='i')
    			{
        			$("#firstDiv").html("<div style='width: 300px;height: 200px;position: absolute;left: 50%;top: 50%;margin: -100px 0 0 -150px;'>"
            				+"<img style='display:block; margin:auto;width:100%; max-width:200px;'"
            				+" src='http://vignette3.wikia.nocookie.net/eastereggs/images/f/f8/IMDB_Logo.png'/><br>"+
            				"<img style='display:block; margin:auto;width:100%; max-width:50px;'"
            				+" src='https://s-media-cache-ak0.pinimg.com/originals/01/8d/2a/018d2ac351d2f2de9b77e870f29894e1.gif'/></div>");
    			}
        		if(current=='t')
    			{
        			$("#firstDiv").html("<div style='width: 300px;height: 200px;position: absolute;left: 50%;top: 50%;margin: -100px 0 0 -150px;'>"
            				+"<img style='display:block; margin:auto;width:100%; max-width:200px;'"
            				+" src='https://www.programmableweb.com/sites/default/files/styles/facebook_scale_width_200/public/the-movie-db-api.png'/><br>"+
            				"<img style='display:block; margin:auto;width:100%; max-width:50px;'"
            				+" src='https://s-media-cache-ak0.pinimg.com/originals/01/8d/2a/018d2ac351d2f2de9b77e870f29894e1.gif'/></div>");
    			}
        		if(current=='b')
    			{
        			$("#firstDiv").html("<div style='width: 300px;height: 200px;position: absolute;left: 50%;top: 50%;margin: -100px 0 0 -150px;'>"
            				+"<h1 style='display:block; margin:auto;width:100%; max-width:200px;'"
            				+"> LinkedMDB </h1><br>"+
            				"<img style='display:block; margin:auto;width:100%; max-width:50px;'"
            				+" src='https://s-media-cache-ak0.pinimg.com/originals/01/8d/2a/018d2ac351d2f2de9b77e870f29894e1.gif'/></div>");
    			}


            
        });};
        

        </script>
</head>
<body>
<div id="firstDiv">
	<div id="title">
		<h1>SemiLD</h1>
	</div>

	<section class="searchSection">
	<form action="Home" method="post">
		<input type="search" name="search"
			placeholder="What are you looking for?">
		<button onclick="setInterval(myFunction, 1000);">Search</button>

		<div id="radiopoints">

			<label><input type="radio" name="type" id="rd1" value="movie"
				checked><label for="rd1"> Movies</label><br> 
				<label><input
					type="radio" id="rd2" name="type" value="glocation"> <label
					for="rd2">Locations</label><br> 
					<label><input
						type="radio" id="rd3" name="type" value="person"> <label
						for="rd3">People </label>
		</div>
		<div id="other">
			<label for="Results"> Number of Results: </label> <input id="Results"
				name="Results" type="number" placeholder="How many results?">
		</div>
		<div id="other2">
			<label for="GS"> Global Schema Version: </label> <select id="GS"
				name="GS">
				<option value="10">10</option>
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="200">200</option>
				<option value="300">300</option>
				<option value="400">400</option>
				<option value="500">500</option>
			</select>

		</div>
	</form>

	</section></div>

</body>
</html>