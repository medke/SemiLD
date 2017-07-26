package Metadata;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import SharedProcesses.WriteXML;

public class IndexGlobalSchema {

	public static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");;
	public static Date currentDate = new Date();
	static final String FILE_PATH = "C:\\Users\\MSK137\\workspace\\SemiLD-Servlet\\WebContent\\"
			+ "WEB-INF\\files\\IndexGS.xml";

	public static void indexGS(ArrayList<String> sources, int number_of_results)
			throws SAXException, IOException {
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			String sourcesList = "";
			// root elements
			Document doc = docBuilder.parse(FILE_PATH);
			Element rootElement = doc.getDocumentElement();
			Element GS = doc.createElement("GS");
			/*
			 * Element[] elements = new Element[3]; int i = 0; // index of the
			 * element number elements[0] = doc.createElement("sources");
			 */
			GS.setAttribute("Date", dateFormat.format(currentDate));
			for (String str : sources) {
				sourcesList += str + ",";
			}
			sourcesList = (sourcesList.endsWith(",")) ? sourcesList.substring(
					0, sourcesList.lastIndexOf(",")) : sourcesList;
			GS.setAttribute("Sources", sourcesList);
			GS.setAttribute("Results", number_of_results + "");

			rootElement.appendChild(GS);
			// doc.appendChild(rootElement);
			// write the content into xml file
			DOMSource source = new DOMSource(doc);
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			StreamResult result = new StreamResult(new File(FILE_PATH));

			// Output to console for testing
			// StreamResult result = new StreamResult(//System.out);

			transformer.transform(source, result);

			//System.out.println("IndexGS Updated!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	public static boolean checkIndexGS(ArrayList<String> sources,
			int number_of_results) throws ParserConfigurationException,
			SAXException, IOException {
		boolean needUpdating = true;

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document document = docBuilder.parse(new File(FILE_PATH));

		String sources_new;
		String[] listOfSources_new;
		String date_new;
		int resultsNumber_new;

		NodeList nodeList = document.getElementsByTagName("GS");
		if(nodeList.getLength()>0)
		{
		for (int i = 0; i < nodeList.getLength(); i++) {
			boolean local_update1 = false, local_update2 = false, local_update3 = false;
			Node currentNode = nodeList.item(i);

			sources_new = currentNode.getAttributes().getNamedItem("Sources")
					.getNodeValue();
			
			listOfSources_new = sources_new.split(",");
			local_update1 = sources.containsAll(Arrays.asList(listOfSources_new));;
			//System.out.println("metadata 1 -"+ sources_new+" = "+local_update1);

			date_new = currentNode.getAttributes().getNamedItem("Date")
					.getNodeValue();
			if (dateDifference(date_new) < 7)
				local_update2 = true;
			//System.out.println("metadata 2 -"+ dateDifference(date_new)+" = "+local_update2);


			resultsNumber_new = Integer.parseInt(currentNode.getAttributes()
					.getNamedItem("Results").getNodeValue());
			if (resultsNumber_new == number_of_results)
				local_update3 = true;
			//System.out.println("metadata 3 -"+ resultsNumber_new +" <=>"+number_of_results +" = "+local_update3);

			if(local_update1 && local_update2 &&local_update3)needUpdating=false;

		}
		}
		//System.out.println("Updating F:: " + needUpdating);

		return needUpdating;
	}

	public static int dateDifference(String date) {
		try {

			Date indexDate = dateFormat.parse(date);
			long diff = currentDate.getTime() - indexDate.getTime();
			return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
