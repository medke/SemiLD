
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
        <title>SO question 4112686</title>
        <script src="http://code.jquery.com/jquery-latest.min.js"></script>
        <script>
        var myFunction = function() {
        	$.get("someservlet", function(responseText) {  
        		$("#somediv").text(responseText);
            
        });};
        

        </script>
    </head>
    <body>
    <div id="firstDiv">
    	<form action="SomeServlet" method="post">		    
    
        <button id="somebutton" onclick="setInterval(myFunction, 1000);" >press here</button>
    	</form>		    
    </div>
        <div id="somediv"></div>
    </body>
</html>