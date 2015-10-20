package main;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
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
	private Logger logger;
	File inputFile;
	private int currentIndex;

	public TGStorageManager(String fileDirectory) {
		this._fileDirectory = fileDirectory;
		this._taskCache = new ArrayList<Event>();
		this._deadlineCache = new ArrayList<Event>();
		this._scheduleCache = new ArrayList<Event>();
		try {
			this.logger = new Logger("Tangguo.log");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		initialize();
	}
	public Event getEventByID(int id){
		for (Event element:_taskCache){
			if (element.getID() == id){
				return element;
			}
		}
		
		for (Event element:_scheduleCache){
			if (element.getID() == id){
				return element;
			}
		}
		
		for (Event element:_deadlineCache){
			if (element.getID() == id){
				return element;
			}
		}
		return null;
	}
	public ArrayList<Event> getTaskCache() {
		return this._taskCache;
	}

	public ArrayList<Event> getScheduleCache() {
		return this._scheduleCache;
	}

	public ArrayList<Event> getDeadlineCache() {
		return this._deadlineCache;
	}
	
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	public int addTask(Event newTask){
		logger.writeLog("addTask: "+newTask.getName());
		_taskCache.add(newTask);
		currentIndex++;
		updateStorage();
		return newTask.getID();
	}
	public int addTask(String name){
		Event newTask = new Event(currentIndex,name);
		addTask(newTask);
		return newTask.getID();
		
	}	
	
	public int addDeadline (Event newDeadline){
		logger.writeLog("add deadline: "+newDeadline.getName());
		_deadlineCache.add(newDeadline);
		currentIndex++;
		updateStorage();
		return newDeadline.getID();
	}
	public int addDeadline(String name, Date endDate){
		Event newDeadline = new Event(currentIndex, name, endDate);
		addDeadline(newDeadline);
		return newDeadline.getID();
	}
	
	public int addSchedule(Event newSchedule){
		logger.writeLog("add schedule: "+newSchedule.getName());
		_scheduleCache.add(newSchedule);
		currentIndex++;
		updateStorage();
		return newSchedule.getID();
	}
	public int addSchedule(String name, Date startDate, Date endDate){
		Event newSchedule = new Event(currentIndex,name, startDate, endDate);
		addSchedule(newSchedule);
		return newSchedule.getID();
	}
	
	//precon:id exists
	public Event deleteEventByID(int id){
		for (Event element:_taskCache){
			if (element.getID() == id){
				logger.writeLog("delete Task: "+element.getName());
				_taskCache.remove(element);
				updateStorage();
				return element;
			}
		}
		
		for (Event element:_scheduleCache){
			if (element.getID() == id){
				logger.writeLog("delete schedule: "+element.getName());
				_scheduleCache.remove(element);
				updateStorage();
				return element;
			}
		}
		
		for (Event element:_deadlineCache){
			if (element.getID() == id){
				logger.writeLog("delete deadline: "+element.getName());
				_deadlineCache.remove(element);
				updateStorage();
				return element;
			}
		}
		assert false:"no matched ID found";
		return null;
		//System.out.println("not found");
	}
	
	//precon:id exists
	public void updateNameByID(int id, String name){
		for (Event element:_taskCache){
			if (element.getID() == id){
				element.setName(name);
				updateStorage();
				return;
			}
		}
		
		for (Event element:_scheduleCache){
			if (element.getID() == id){
				element.setName(name);
				updateStorage();
				return;
			}
		}
		
		for (Event element:_deadlineCache){
			if (element.getID() == id){
				element.setName(name);
				updateStorage();
				return;
			}
		}
		//System.out.println("not found");
	}
	
	public void updateIsDoneByID(int id, boolean isDone){
		for (Event element:_taskCache){
			if (element.getID() == id){
				element.setIsDone(isDone);
				updateStorage();
				return;
			}
		}
		
		for (Event element:_scheduleCache){
			if (element.getID() == id){
				element.setIsDone(isDone);
				updateStorage();
				return;
			}
		}
		
		for (Event element:_deadlineCache){
			if (element.getID() == id){
				element.setIsDone(isDone);
				updateStorage();
				return;
			}
		}
		//System.out.println("not found");
	}
	//precon:id exists
	public void updateStartByID(int id, Date startDate){
		for (Event element:_taskCache){
			if (element.getID() == id){
				element.setStart(startDate);
				updateStorage();
				return;
			}
		}
		
		for (Event element:_scheduleCache){
			if (element.getID() == id){
				element.setStart(startDate);
				updateStorage();
				return;
			}
		}
		
		for (Event element:_deadlineCache){
			if (element.getID() == id){
				element.setStart(startDate);
				updateStorage();
				return;
			}
		}
		//System.out.println("not found");
	}
	
	//precon:id exists
	public void updateEndByID(int id, Date endDate){
		for (Event element:_taskCache){
			if (element.getID() == id){
				element.setEnd(endDate);
				updateStorage();
				return;
			}
		}
		
		for (Event element:_scheduleCache){
			if (element.getID() == id){
				element.setEnd(endDate);
				updateStorage();
				return;
			}
		}
		
		for (Event element:_deadlineCache){
			if (element.getID() == id){
				element.setEnd(endDate);
				updateStorage();
				return;
			}
		}
		//System.out.println("not found");
	}
	
	//precon:id exists
	public void updateCategoryByID(int id, String category){
		for (Event element:_taskCache){
			if (element.getID() == id){
				element.setCategory(category);
				updateStorage();
				return;
			}
		}
		
		for (Event element:_scheduleCache){
			if (element.getID() == id){
				element.setCategory(category);
				updateStorage();
				return;
			}
		}
		
		for (Event element:_deadlineCache){
			if (element.getID() == id){
				element.setCategory(category);
				updateStorage();
				return;
			}
		}
		//System.out.println("not found");
	}
	
	//precon:id exists
	public void updatePriorityByID(int id, int priority){
		for (Event element:_taskCache){
			if (element.getID() == id){
				element.setPriority(priority);
				updateStorage();
				return;
			}
		}
		
		for (Event element:_scheduleCache){
			if (element.getID() == id){
				element.setPriority(priority);
				updateStorage();
				return;
			}
		}
		
		for (Event element:_deadlineCache){
			if (element.getID() == id){
				element.setPriority(priority);
				updateStorage();
				return;
			}
		}
		//System.out.println("not found");
	}
	
	public void clear(){
		_scheduleCache.clear();
		_deadlineCache.clear();
		_taskCache.clear();
		updateStorage();
	}
	private void initialize() {
		try {
			File inputFile = new File(_fileDirectory);
			// TODO file existence
			// System.out.println(inputFile.exists());
			if (!inputFile.exists()) {
				createStorageFile();
				return;
			}
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			currentIndex = getCurrentIndexFromFile(doc);
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

	private void createStorageFile() {
		currentIndex = 0;
		StringWriter stringWriter = new StringWriter();

		XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
		XMLStreamWriter xMLStreamWriter;
		try {
			xMLStreamWriter = xMLOutputFactory
					.createXMLStreamWriter(stringWriter);

			xMLStreamWriter.writeStartDocument();
			xMLStreamWriter.writeStartElement("calendar");
			xMLStreamWriter.writeAttribute("current", "0");
			xMLStreamWriter.writeEndElement();
			xMLStreamWriter.writeEndDocument();

			xMLStreamWriter.flush();
			xMLStreamWriter.close();

		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
			writexmlStringToFile(stringWriter.getBuffer().toString());
			try {
				stringWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

	}

	private int getCurrentIndexFromFile(Document doc) {
		return Integer.parseInt(doc.getDocumentElement()
				.getAttribute("current"));
	}

	private void initializeTaskCache(Document doc) {

		NodeList nodeList;
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			nodeList = (NodeList) xPath.compile(Constants.XML_TASK_EXPRESSION)
					.evaluate(doc, XPathConstants.NODESET);
			String nameString, categoryString;
			int ID, priority;
			Event event;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				// System.out.println("\nCurrent Element :" +
				// nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					ID = Integer.parseInt(eElement.getAttribute("id"));
					nameString = eElement.getElementsByTagName("name").item(0)
							.getTextContent();
					categoryString = eElement.getElementsByTagName("category").item(0)
							.getTextContent();
					priority = Integer.parseInt(eElement.getElementsByTagName("priority").item(0)
							.getTextContent());
					event = new Event(ID, nameString);
					
					if (!categoryString.equals(Constants.DEFAULT_CATEGORY))
						event.setCategory(categoryString);
					if (priority != Constants.DEFAULT_PRIORITY)
						event.setPriority(priority);
					_taskCache.add(event);
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
			nodeList = (NodeList) xPath.compile(
					Constants.XML_DEADLINE_EXPRESSION).evaluate(doc,
					XPathConstants.NODESET);
			String nameString, endDateString, categoryString;
			Date endDate;
			int ID, priority;
			Event event;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				// System.out.println("\nCurrent Element :" +
				// nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					ID = Integer.parseInt(eElement.getAttribute("id"));
					nameString = eElement.getElementsByTagName("name").item(0)
							.getTextContent();
					endDateString = eElement
							.getElementsByTagName("endDate").item(0)
							.getTextContent();
					endDate = sdf.parse(endDateString);
					categoryString = eElement.getElementsByTagName("category").item(0)
							.getTextContent();
					priority = Integer.parseInt(eElement.getElementsByTagName("priority").item(0)
							.getTextContent());
					event = new Event(ID, nameString, endDate);
					
					if (!categoryString.equals(Constants.DEFAULT_CATEGORY))
						event.setCategory(categoryString);
					if (priority != Constants.DEFAULT_PRIORITY)
						event.setPriority(priority);
					_deadlineCache.add(event);
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
			nodeList = (NodeList) xPath.compile(
					Constants.XML_SCHEDULE_EXPRESSION).evaluate(doc,
					XPathConstants.NODESET);
			String nameString, startDateString, endDateString, categoryString;
			Date startDate, endDate;
			int ID, priority;
			Event event;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					ID = Integer.parseInt(eElement.getAttribute("id"));
					nameString = eElement.getElementsByTagName("name").item(0)
							.getTextContent();
					startDateString = eElement
							.getElementsByTagName("startDate").item(0)
							.getTextContent();
					endDateString = eElement.getElementsByTagName("endDate")
							.item(0).getTextContent();
					startDate = sdf.parse(startDateString);
					endDate = sdf.parse(endDateString);
					categoryString = eElement.getElementsByTagName("category").item(0)
							.getTextContent();
					priority = Integer.parseInt(eElement.getElementsByTagName("priority").item(0)
							.getTextContent());
					event = new Event(ID, nameString, startDate, endDate);
					
					if (!categoryString.equals(Constants.DEFAULT_CATEGORY))
						event.setCategory(categoryString);
					if (priority != Constants.DEFAULT_PRIORITY)
						event.setPriority(priority);
					_scheduleCache.add(event);
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
	private void updateStorage(){
		try {
	         StringWriter stringWriter = new StringWriter();
	         DateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
	         XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();	
	         XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);
	         
	         xMLStreamWriter.writeStartDocument();
	         xMLStreamWriter.writeStartElement("calendar");
	         xMLStreamWriter.writeAttribute("current", String.valueOf(currentIndex));
	         for (Event element:_taskCache){
	        	 xMLStreamWriter.writeStartElement("task");			
	             xMLStreamWriter.writeAttribute("id", String.valueOf(element.getID()));
	             xMLStreamWriter.writeStartElement("name");	
	             xMLStreamWriter.writeCharacters(element.getName());
	             xMLStreamWriter.writeEndElement();
	             xMLStreamWriter.writeStartElement("category");
	             xMLStreamWriter.writeCharacters(element.getCategory());
	             xMLStreamWriter.writeEndElement();
	             xMLStreamWriter.writeStartElement("priority");
	             xMLStreamWriter.writeCharacters(String.valueOf(element.getPriority()));
	             xMLStreamWriter.writeEndElement();
	             xMLStreamWriter.writeEndElement();
	         }
	         
	         for (Event element:_deadlineCache){
	        	 xMLStreamWriter.writeStartElement("deadline");			
	             xMLStreamWriter.writeAttribute("id", String.valueOf(element.getID()));
	             xMLStreamWriter.writeStartElement("name");	
	             xMLStreamWriter.writeCharacters(element.getName());
	             xMLStreamWriter.writeEndElement();
	             xMLStreamWriter.writeStartElement("endDate");	
	             xMLStreamWriter.writeCharacters(sdf.format(element.getEnd()));
	             xMLStreamWriter.writeEndElement();
	             xMLStreamWriter.writeStartElement("category");
	             xMLStreamWriter.writeCharacters(element.getCategory());
	             xMLStreamWriter.writeEndElement();
	             xMLStreamWriter.writeStartElement("priority");
	             xMLStreamWriter.writeCharacters(String.valueOf(element.getPriority()));
	             xMLStreamWriter.writeEndElement();
	             xMLStreamWriter.writeEndElement();
	         }
	         
	         for (Event element:_scheduleCache){
	        	 xMLStreamWriter.writeStartElement("schedule");			
	             xMLStreamWriter.writeAttribute("id", String.valueOf(element.getID()));
	             xMLStreamWriter.writeStartElement("name");	
	             xMLStreamWriter.writeCharacters(element.getName());
	             xMLStreamWriter.writeEndElement();
	             xMLStreamWriter.writeStartElement("startDate");	
	             xMLStreamWriter.writeCharacters(sdf.format(element.getStart()));
	             xMLStreamWriter.writeEndElement();
	             xMLStreamWriter.writeStartElement("endDate");	
	             xMLStreamWriter.writeCharacters(sdf.format(element.getEnd()));
	             xMLStreamWriter.writeEndElement();
	             xMLStreamWriter.writeStartElement("category");
	             xMLStreamWriter.writeCharacters(element.getCategory());
	             xMLStreamWriter.writeEndElement();
	             xMLStreamWriter.writeStartElement("priority");
	             xMLStreamWriter.writeCharacters(String.valueOf(element.getPriority()));
	             xMLStreamWriter.writeEndElement();
	             xMLStreamWriter.writeEndElement();
	         }

	         xMLStreamWriter.writeEndElement();
	         xMLStreamWriter.writeEndDocument();

	         xMLStreamWriter.flush();
	         xMLStreamWriter.close();

	         String xmlString = stringWriter.getBuffer().toString();
	         
	         writexmlStringToFile(xmlString);
	         stringWriter.close();
	         

	      } catch (XMLStreamException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	}
	
	private void writexmlStringToFile(String xmlString){
		try {
            Source xmlInput = new StreamSource(new StringReader(xmlString));
            StringWriter outputStringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(outputStringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer(); 
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
			FileWriter fw = new FileWriter(_fileDirectory);
			fw.write(xmlOutput.getWriter().toString());
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TGStorageManager tm = new TGStorageManager("test");
		tm.addTask("yo");
		for (Event element : tm.getTaskCache()) {
			System.out.println(element.getID()+" "+element.getCategory());
		}
		
		System.out.println();
		for (Event element : tm.getDeadlineCache()) {
			System.out.println(element.getID()+" "+element.getCategory() + ":" + element.getEnd());
		}
		System.out.println();
		for (Event element : tm.getScheduleCache()) {
			System.out.println(element.getID()+" "+element.getCategory() + ":" + element.getStart() + "-"
					+ element.getEnd());

		}
	}
}
