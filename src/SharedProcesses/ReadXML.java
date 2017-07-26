package SharedProcesses;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ReadXML {
	public ReadXML(){}
	public ArrayList<String> XML2ArrayList(char domain,int n) throws Exception
	{
		String path="";
		if(domain=='m')
			path="C:\\Users\\MSK137\\workspace\\SemiLD-Servlet\\WebContent\\WEB-INF\\files\\Global-"+n+".xml";
		else if(domain=='g')
			path="C:\\Users\\MSK137\\workspace\\SemiLD-Servlet\\WebContent\\WEB-INF\\files\\Global-P-"+n+".xml";
			else if(domain=='p')
				path="C:\\Users\\MSK137\\workspace\\SemiLD-Servlet\\WebContent\\WEB-INF\\files\\Global-Pe-"+n+".xml";
			else
				throw new Exception("Enter a Valid Character to choose a domain");
		System.out.println("--->Using"+path);
		ArrayList<String> output= new ArrayList<String>();
		Document doc;
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setValidating(true);
	    factory.setIgnoringElementContentWhitespace(true);
	    try {
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        File file = new File(path);
	        doc = builder.parse(file);
	        NodeList nList = doc.getElementsByTagName("Global").item(0).getChildNodes();
	        for(int i=0; i<nList.getLength();i++)
	        {
	        	if(nList.item(i).getNodeType()==Node.ELEMENT_NODE)
	        	output.add(nList.item(i).getNodeName());
	        }
	        // Do something with the document here.
	    } catch (ParserConfigurationException e) {
	    } catch (SAXException e) {
	    } catch (IOException e) { 
	    }
	    
		  return output;
		    
         



             

	}

}
