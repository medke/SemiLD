package SharedProcesses;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

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

public class WriteXML {
	public static Element[] elements;
	private static String element;

	public WriteXML() {
	}

	public static void convert2XML(ArrayList<String> properties,
			String classsource, int resultsNumber) {

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Global");
			doc.appendChild(rootElement);
			elements = new Element[properties.size()];
			int i = 0; // index of the element number
			for (String str : properties) {
				element = removeInvalidXMLCharacters(str).replaceAll("#", "_")
						.replaceAll("-", "_").replaceAll(" ", "_")
						.replaceAll("\\d", "_");
				element = element.replaceAll("___", "_");
				element = element.replaceAll("__", "_");
				element = element.replaceAll(",", "_");
				element = element.startsWith("_") ? element.substring(1)
						: element;
				element.replaceAll("Ã","").replaceAll("©", "");
				try {
					elements[i] = doc.createElement(element);
				} catch (DOMException e) {
					System.out.println(element);
					System.out.println(e.getMessage());

				}

				i++;

			}
			System.out.println(Arrays.toString(elements));
			i = 0;
			while (i < properties.size()) {
				try{
				rootElement.appendChild(elements[i]);
				
				}catch(Exception e)
				{
					System.out.println("ERROR: "+e.getMessage()+ "\n i="+i
							+" Element-1 "+ elements[i-1] +" size= "+properties.size());
				}
				i++;
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(
					"C:\\Users\\MSK137\\workspace\\SemiLD-Servlet\\WebContent\\"
							+ "WEB-INF\\files\\Global-Pe-" + resultsNumber
							+ ".xml"));
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	public static String removeInvalidXMLCharacters(String s) {
		StringBuilder out = new StringBuilder();

		int codePoint;
		int i = 0;

		while (i < s.length()) {
			// This is the unicode code of the character.
			codePoint = s.codePointAt(i);
			if ((codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
					|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
					|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
					|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF))) {
				out.append(Character.toChars(codePoint));
			}
			i += Character.charCount(codePoint);
		}
		return out.toString();
	}

}
