package TGStorage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.print.Doc;
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

import TGUtils.Constants;
import TGUtils.Event;
import TGUtils.Logger;
import TGUtils.Sorters;
import TGUtils.TimeBlock;

public class TGStorageManager {
	private String _filePath;
	private String _fileName;
	private ArrayList<Event> _taskCache;
	private ArrayList<Event> _deadlineCache;
	private ArrayList<Event> _scheduleCache;
	private Logger logger;
	private TimeBlock tb;
	File inputFile;
	private int currentIndex;

	public TGStorageManager(String filePath, String fileName) {
		this._filePath = filePath;
		this._fileName = fileName;
		this._taskCache = new ArrayList<Event>();
		this._deadlineCache = new ArrayList<Event>();
		this._scheduleCache = new ArrayList<Event>();
		this.tb = new TimeBlock();
		try {
			this.logger = new Logger("Tangguo.log");
		} catch (IOException e) {
			e.printStackTrace();
		}

		initialize();
	}
	
	public void setFilePath(String filePath) {
		this._filePath = filePath;
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

	public int addTaskToStorage(Event newTask){
		logger.writeLog("addTask: "+newTask.getName());
		_taskCache.add(newTask);
		currentIndex++;
		updateStorage();
		return newTask.getID();
	}
	
	public int addTask(String name, String category, int priority){
		Event newTask = new Event(currentIndex, name, category, priority);
		addTaskToStorage(newTask);
		return newTask.getID();

	}

	public int addDeadlineToStorage (Event newDeadline){
		logger.writeLog("add deadline: "+newDeadline.getName());
		_deadlineCache.add(newDeadline);
		currentIndex++;
		updateStorage();
		return newDeadline.getID();
	}
	
	public int addDeadline(String name, Date endDate, String category, int priority){
		Event newDeadline = new Event(currentIndex, name, endDate, category, priority);
		addDeadlineToStorage(newDeadline);
		return newDeadline.getID();
	}

	public int addScheduleToStorage(Event newSchedule){
		logger.writeLog("add schedule: "+newSchedule.getName());
		_scheduleCache.add(newSchedule);
		currentIndex++;
		updateStorage();
		tb.updateCache(_scheduleCache);
		return newSchedule.getID();
	}
	
	public int addSchedule(String name, Date startDate, Date endDate, String category, int priority){
		Event newSchedule = new Event(currentIndex, name, startDate, endDate, category, priority);
		if (tb.canFitSchedule(newSchedule)) {
			addScheduleToStorage(newSchedule);
			return newSchedule.getID();
		} else {
			return -1;
		}
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
				tb.updateCache(_scheduleCache);
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
				tb.updateCache(_scheduleCache);
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

	//precon:id exists
	public boolean updateStartByID(int id, Date startDate){
		for (Event element:_scheduleCache){
			if (element.getID() == id){
				if (startDate.before(element.getEnd()) && tb.updateStart(id, startDate)) {
					element.setStart(startDate);
					updateStorage();
					tb.updateCache(_scheduleCache);
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
		//System.out.println("not found");
	}

	//precon:id exists
	public boolean updateEndByID(int id, Date endDate){
		for (Event element:_scheduleCache){
			if (element.getID() == id){
				if (endDate.after(element.getStart()) && tb.updateEnd(id, endDate)) {
					element.setEnd(endDate);
					updateStorage();
					tb.updateCache(_scheduleCache);
					return true;
				} else {
					return false;
				}
			}
		}

		for (Event element:_deadlineCache){
			if (element.getID() == id){
				element.setEnd(endDate);
				updateStorage();
				return true;
			}
		}
		return false;
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
				tb.updateCache(_scheduleCache);
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
				tb.updateCache(_scheduleCache);
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
				tb.updateCache(_scheduleCache);
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

	public void sortName() {
		Collections.sort(_taskCache, Sorters.sortName());
		Collections.sort(_deadlineCache, Sorters.sortName());
		Collections.sort(_scheduleCache, Sorters.sortName());
	}

	public void sortStart() {
		Collections.sort(_scheduleCache, Sorters.sortStart());
	}

	public void sortEnd() {
		Collections.sort(_deadlineCache, Sorters.sortEnd());
		Collections.sort(_scheduleCache, Sorters.sortEnd());
	}

	public void sortPriority() {
		Collections.sort(_taskCache, Sorters.sortPriority());
		Collections.sort(_deadlineCache, Sorters.sortPriority());
		Collections.sort(_scheduleCache, Sorters.sortPriority());
	}

	public ArrayList<Event> searchTask(String key) {
		ArrayList<Event> result = new ArrayList<Event>();
		for (Event element:_taskCache){
			if (element.contains(key)) {
				result.add(element);
			}
		}
		return result;
	}

	public ArrayList<Event> searchDeadline(String key) {
		ArrayList<Event> result = new ArrayList<Event>();
		for (Event element:_deadlineCache){
			if (element.contains(key)) {
				result.add(element);
			}
		}
		return result;
	}

	public ArrayList<Event> searchSchedule(String key) {
		ArrayList<Event> result = new ArrayList<Event>();
		for (Event element:_scheduleCache){
			if (element.contains(key)) {
				result.add(element);
			}
		}
		return result;
	}

	public void clear(){
		_scheduleCache.clear();
		_deadlineCache.clear();
		_taskCache.clear();
		updateStorage();
	}

	private void initialize() {
		try {
			File inputFile;
			if (_filePath.equals("")) {
				inputFile = new File(_fileName);
			} else {
				inputFile = new File(_filePath, _fileName);
			}
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
			tb.updateCache(this._scheduleCache);

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
		NodeList nodeList = getNodeList(doc, Constants.XML_TASK_EXPRESSION);
		String nameString, categoryString;
		int ID, priority;
		boolean isDone;
		Event event;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				ID = Integer.parseInt(eElement.getAttribute("id"));
				nameString = getPropertyFromElement(eElement, "name");;
				categoryString = getPropertyFromElement(eElement, "category");
				priority = Integer.parseInt(getPropertyFromElement(eElement, "priority"));
				isDone = Boolean.parseBoolean(getPropertyFromElement(eElement, "isDone"));
				
				event = new Event(ID, nameString, categoryString, priority);
				event.setIsDone(isDone);

				_taskCache.add(event);
			}
		}		
	}
	
	private NodeList getNodeList(Document doc, String expression) {
		try{
			XPath xPath = XPathFactory.newInstance().newXPath();
			return (NodeList) xPath.compile(expression)
				.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e){
			System.out.println("Fail to compile xPath");
			return null;
		}
	}
	
	private String getPropertyFromElement(Element eElement, String property){
		return eElement.getElementsByTagName(property).item(0).getTextContent();
	}

	private void initializeDeadlineCache(Document doc) {
		DateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		try {
			NodeList nodeList = getNodeList(doc, Constants.XML_DEADLINE_EXPRESSION);
			String nameString, categoryString;
			Date endDate;
			int ID, priority;
			boolean isDone;
			Event event;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					ID = Integer.parseInt(eElement.getAttribute("id"));
					nameString = getPropertyFromElement(eElement, "name");
					endDate = sdf.parse(getPropertyFromElement(eElement, "endDate"));
					categoryString = getPropertyFromElement(eElement, "category");
					priority = Integer.parseInt(getPropertyFromElement(eElement, "priority"));
					isDone = Boolean.parseBoolean(getPropertyFromElement(eElement, "isDone"));
					
					event = new Event(ID, nameString, endDate, categoryString, priority);
					event.setIsDone(isDone);

					_deadlineCache.add(event);
				}
			}
		} catch (ParseException e) {
			System.out.println("Failed to parse date when reading from file");
			e.printStackTrace();
		}
	}

	private void initializeScheduleCache(Document doc) {

		DateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		try {
			NodeList nodeList = getNodeList(doc, Constants.XML_SCHEDULE_EXPRESSION);
			String nameString, categoryString;
			Date startDate, endDate;
			int ID, priority;
			boolean isDone;
			Event event;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					ID = Integer.parseInt(eElement.getAttribute("id"));
					nameString = getPropertyFromElement(eElement, "name");
					startDate = sdf.parse(getPropertyFromElement(eElement, "startDate"));
					endDate = sdf.parse(getPropertyFromElement(eElement, "endDate"));
					categoryString = getPropertyFromElement(eElement, "category");
					priority = Integer.parseInt(getPropertyFromElement(eElement, "priority"));
					isDone = Boolean.parseBoolean(getPropertyFromElement(eElement, "isDone"));
					event = new Event(ID, nameString, startDate, endDate, categoryString, priority);
					event.setIsDone(isDone);
					
					_scheduleCache.add(event);
				}
			}
		} catch (ParseException e) {
			System.out.println("Failed to parse date when reading from file");
			e.printStackTrace();
		}
	}
	private void updateStorage(){
		try {
	         StringWriter stringWriter = new StringWriter();
	         DateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
	         XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
	         XMLStreamWriter xmlStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);

	         xmlStreamWriter.writeStartDocument();
	         xmlStreamWriter.writeStartElement("calendar");
	         xmlStreamWriter.writeAttribute("current", String.valueOf(currentIndex));
	         for (Event element:_taskCache){
	        	 xmlStreamWriter.writeStartElement("task");
	        	 writeTaskProperties(xmlStreamWriter, element);
	             xmlStreamWriter.writeEndElement();
	         }

	         for (Event element:_deadlineCache){
	        	 xmlStreamWriter.writeStartElement("deadline");
	             writeTaskProperties(xmlStreamWriter, element);
	             writeEndProperty(xmlStreamWriter, element, sdf);
	             xmlStreamWriter.writeEndElement();
	         }

	         for (Event element:_scheduleCache){
	        	 xmlStreamWriter.writeStartElement("schedule");
	             writeTaskProperties(xmlStreamWriter, element);
	             writeStartProperty(xmlStreamWriter, element, sdf);
	             writeEndProperty(xmlStreamWriter, element, sdf);
	             xmlStreamWriter.writeEndElement();
	         }

	         xmlStreamWriter.writeEndElement();
	         xmlStreamWriter.writeEndDocument();

	         xmlStreamWriter.flush();
	         xmlStreamWriter.close();

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
	
	private void writeTaskProperties(XMLStreamWriter xmlStreamWriter, Event element){
		try{
			xmlStreamWriter.writeAttribute("id", String.valueOf(element.getID()));
	        xmlStreamWriter.writeStartElement("name");
	        xmlStreamWriter.writeCharacters(element.getName());
	        xmlStreamWriter.writeEndElement();
	        xmlStreamWriter.writeStartElement("category");
	        xmlStreamWriter.writeCharacters(element.getCategory());
	        xmlStreamWriter.writeEndElement();
	        xmlStreamWriter.writeStartElement("priority");
	        xmlStreamWriter.writeCharacters(String.valueOf(element.getPriority()));
	        xmlStreamWriter.writeEndElement();
	        xmlStreamWriter.writeStartElement("isDone");
	        xmlStreamWriter.writeCharacters(String.valueOf(element.isDone()));
	        xmlStreamWriter.writeEndElement();
		} catch (XMLStreamException e){
			System.out.println("Failed to write into file");
			e.printStackTrace();
		}
	}
	
	private void writeStartProperty(XMLStreamWriter xmlStreamWriter, Event element, DateFormat sdf){
		try {
			xmlStreamWriter.writeStartElement("startDate");
            xmlStreamWriter.writeCharacters(sdf.format(element.getStart()));
            xmlStreamWriter.writeEndElement();
		} catch (XMLStreamException e) {
			System.out.println("Failed to write into file");
			e.printStackTrace();
		}
	}
	
	private void writeEndProperty(XMLStreamWriter xmlStreamWriter, Event element, DateFormat sdf){
		try {
			xmlStreamWriter.writeStartElement("endDate");
            xmlStreamWriter.writeCharacters(sdf.format(element.getEnd()));
            xmlStreamWriter.writeEndElement();
		} catch (XMLStreamException e) {
			System.out.println("Failed to write into file");
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
			File outputFile;
			if (_filePath.equals("")) {
				outputFile = new File(_fileName);
			} else {
				outputFile = new File(_filePath, _fileName);
			}
			FileWriter fw = new FileWriter(outputFile);
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
}