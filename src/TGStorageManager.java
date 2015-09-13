import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class TGStorageManager {
	private String _fileDirectory;
	private ArrayList<Event> _taskCache;
	private ArrayList<Event> _deadlineCache;
	private ArrayList<Event> _scheduleCache;
	File inputFile;

	public TGStorageManager(String fileDirectory) {
		this._fileDirectory = fileDirectory;
		this._taskCache = new ArrayList<Event>();
		this._deadlineCache = new ArrayList<Event>();
		this._scheduleCache = new ArrayList<Event>();
		initialize();
	}
	
	public ArrayList<Event> getTaskCache(){
		return this._taskCache;
	}
	
	public ArrayList<Event> getScheduleCache(){
		return this._scheduleCache;
	}
	
	public ArrayList<Event> getDeadlineCache(){
		return this._deadlineCache;
	}
	
	private void initialize() {
		try {
			File inputFile = new File(_fileDirectory);
			//TODO file existence
			//System.out.println(inputFile.exists());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			initializeTaskCache(doc);
			initializeDeadlineCache(doc);
			initializeScheduleCache(doc);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initializeTaskCache(Document doc) {

		NodeList nodeList;
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			nodeList = (NodeList) xPath.compile(Constants.XML_TASK_EXPRESSION).evaluate(doc,
					XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					_taskCache.add(new Event(eElement.getElementsByTagName("name").item(0)
									.getTextContent()));
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initializeDeadlineCache(Document doc) {
		DateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		NodeList nodeList;
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			nodeList = (NodeList) xPath.compile(Constants.XML_DEADLINE_EXPRESSION).evaluate(doc,
					XPathConstants.NODESET);
			String nameString,startDateString;
			Date startDate;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					nameString = eElement.getElementsByTagName("name").item(0).getTextContent();
					startDateString = eElement.getElementsByTagName("startDate").item(0).getTextContent();
					startDate = sdf.parse(startDateString);
					_deadlineCache.add(new Event(nameString,startDate));
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initializeScheduleCache(Document doc) {
		DateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		NodeList nodeList;
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			nodeList = (NodeList) xPath.compile(Constants.XML_SCHEDULE_EXPRESSION).evaluate(doc,
					XPathConstants.NODESET);
			String nameString,startDateString,endDateString;
			Date startDate, endDate;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					nameString = eElement.getElementsByTagName("name").item(0).getTextContent();
					startDateString = eElement.getElementsByTagName("startDate").item(0).getTextContent();
					endDateString = eElement.getElementsByTagName("endDate").item(0).getTextContent();
					startDate = sdf.parse(startDateString);
					endDate = sdf.parse(endDateString);
					_scheduleCache.add(new Event(nameString,startDate,endDate));
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		TGStorageManager tm = new TGStorageManager("test");
		for (Event element:tm.getTaskCache()){
			System.out.println(element.name);
		}
		System.out.println();
		for (Event element:tm.getDeadlineCache()){
			System.out.println(element.name+":"+element.startDate);	
		}
		System.out.println();
		for (Event element:tm.getScheduleCache()){
			System.out.println(element.name+":"+element.startDate+"-"+element.endDate);
			
		}
	}
}