package system;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class PrefsHandler
{
	private final File prefsFile = new File("prefs.xml");

	public void importPrefs()
	{
		if(prefsFile.exists() == true && prefsFile.isDirectory() == false)
		{
			readXML();
		}
		else
		{
			createPrefsXML();
		}
	}

	private void createPrefsXML()
	{
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			GraphicCalculations graphicCalc = new GraphicCalculations();

			// ----- [START] Graphics -----

			Element graphicsRoot = doc.createElement("graphics");
			doc.appendChild(graphicsRoot);

			Element resSubRoot = doc.createElement("resolution");
			graphicsRoot.appendChild(resSubRoot);

			Element element = doc.createElement("screenWidth");
			element.appendChild(doc.createTextNode(graphicCalc.screenWidth + ""));
			resSubRoot.appendChild(element);

			element = doc.createElement("screenHeight");
			element.appendChild(doc.createTextNode(graphicCalc.screenHeight + ""));
			resSubRoot.appendChild(element);

			element = doc.createElement("fps");
			element.appendChild(doc.createTextNode(graphicCalc.fps + ""));
			graphicsRoot.appendChild(element);
			// ----- [END] Graphics -----

			// Write the content into the XML file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(prefsFile);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(source, result);

			readXML();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch(TransformerException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void readXML()
	{
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(prefsFile);  
	        XPath xPath = XPathFactory.newInstance().newXPath();
	        
	        Options.SCREEN_WIDTH = Integer.parseInt(((Node) (xPath.compile("graphics/resolution/screenWidth/text()").evaluate(doc, XPathConstants.NODE))).getTextContent());
	        Options.SCREEN_HEIGHT = Integer.parseInt(((Node) (xPath.compile("graphics/resolution/screenHeight/text()").evaluate(doc, XPathConstants.NODE))).getTextContent());
	        Options.FPS = Short.parseShort(((Node) (xPath.compile("graphics/fps/text()").evaluate(doc, XPathConstants.NODE))).getTextContent());
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private class GraphicCalculations
	{
		int screenWidth = -1;
		int screenHeight = -1;
		short fps = -1;
		
		public GraphicCalculations()
		{
			//Calculate the resolution
			if(Toolkit.getDefaultToolkit().getScreenSize().getWidth() > Toolkit.getDefaultToolkit().getScreenSize().getHeight())
			{
				screenHeight = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 300);
				screenWidth = screenHeight + 200;
			}
			else
			{
				screenWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 300);
				screenHeight = screenWidth + 200;
			}
			
			//Calculate the refresh rate		
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        GraphicsDevice[] devices = env.getScreenDevices();
	        
	        fps = (short) devices[0].getDisplayMode().getRefreshRate();
		}
	}
}
