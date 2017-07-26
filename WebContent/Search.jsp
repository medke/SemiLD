<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="Search.css?v=1.0">
  <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDcktYJwWJbeivSuLdyJ8n0TTuHN7Sztt8&callback=initMap&sensor=false" 
          type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search results</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>

</head>
<body>

	<form role="search" class="search-form" id="search-form"
		action="LoadDropDown" method="post">

		<section class="search-terms">
		<div>
			<span class="search-term-wrap"> <label for="properties">Properties:
			</label> <select id="properties" name="properties"
				onchange='showModel(this.value)'>
					<c:forEach var="item" items="${filter}" varStatus="row">
						<option value="${item}"><c:out value="${item}" /></option>
					</c:forEach>
			</select> <br /> <label for="values">Values: </label> <select id="values"
				name="values">
			</select> <br />
			</span> <span class="search-term-button-wrap"> <input type="submit"
				value="Filter" class="search-button">
			</span>
		</div>
		</section>


	</form>
	<div class="container" id="container" name="container"><div class="map" id="map" style="width:700px; height:500px; margin:auto;" ></div>${var}</div>

</body>


<script type="text/javascript">

        function showModel(el) {
        	 if (el === "") {
             } else {


                $.ajax({
                    type: "GET",
                    url: "LoadDropDown",
                    data: {category: el },
                    success: function(data){
                        $("#values").html(data)
                    }
                });
            }} ;
            
            $(document).on("submit", "#search-form", function(event) {
                var $form = $(this);
                $.post($form.attr("action"), $form.serialize(), function(response) {
                	$("#container").html(response);
                    eval(addChangeEvent());

                });

                event.preventDefault(); 
                // Important! Prevents submitting the form.
            });
            function addChangeEvent() {
            	var acc = document.getElementsByClassName("accordion");
                var i;

                for (i = 0; i < acc.length; i++) {
                    acc[i].onclick = function(){
                        this.classList.toggle("active");
                        var panel = this.previousElementSibling;
                        if (panel.style.display === "block") {
                            panel.style.display = "none";
                        } else {
                            panel.style.display = "block";
                        }
                    }
                }
            }
            var acc = document.getElementsByClassName("accordion");
            var i;

            for (i = 0; i < acc.length; i++) {
                acc[i].onclick = function(){
                    this.classList.toggle("active");
                    var panel = this.previousElementSibling;
                    if (panel.style.display === "block") {
                        panel.style.display = "none";
                    } else {
                        panel.style.display = "block";
                    }
                }
            }
           
           


    	
            function displayMapEvent(showMapButton) {
            	var mapDiv=showMapButton.previousElementSibling;

    			if (mapDiv.style.display === "block") {
    				mapDiv.style.display = "none";
                    showMapButton.innerText ="Show Results in a Map";
                } else {
                	mapDiv.style.display = "block";
                	showMapButton.innerText ="Hide the Map";
                }

        	    var baseurl;

    			
            	var locations = new Array();
            	var iconsArray = new Array();

            	<c:forEach var="row" items="${locations}">
            	if('${row[0]}'!=0){
            	    locations.push(['${row[0]}', '${row[1]}']);
            	    baseurl="http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld="+parseInt('${row[3]}')+"|";
                	if(parseInt('${row[2]}')==0){
                		baseurl+= "778899";
                	}else if(parseInt('${row[2]}')==1){
                		baseurl+= "e0ffff";
                	}else if(parseInt('${row[2]}')==2){
                		baseurl+= "f08080";
                	}else if(parseInt('${row[2]}')==3){
                		baseurl+= "add8e6";
                	}
                	
                	iconsArray.push(baseurl);
            	}
            	</c:forEach>
            	locations.toString();
			
            var map = new google.maps.Map(mapDiv, {
              zoom: 1,
              center: new google.maps.LatLng(0, 0),
              mapTypeId: google.maps.MapTypeId.ROADMAP
            });

            var infowindow = new google.maps.InfoWindow();

  
            var marker, i;

            for (i = 0; i < locations.length; i++) {  
              marker = new google.maps.Marker({
                position: new google.maps.LatLng(locations[i][0], locations[i][1]),
                map: map,
                icon: iconsArray[i]
              });

              google.maps.event.addListener(marker, 'click', (function(marker, i) {
                return function() {
                  infowindow.open(map, marker);
                }
              })(marker, i));
            }}

    </script>


</html>