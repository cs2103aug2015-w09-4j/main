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
	private int currentIndex;
	File inputFile;

	/**
	 * Creates a TGStorageManager object
	 * initializes file, event caches, logger, and time block
	 * @param filePath
	 * @param fileName
	 */
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
	
	//sets the file path to be @param filePath
	public void setFilePath(String filePath) {
		this._filePath = filePath;
	}
	
	/**
	 * Iterates through the event caches to find an event of the same id
	 * @param id
	 * @return event which id == @param id, else null
	 */
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
	
	//returns task cache
	public ArrayList<Event> getTaskCache() {
		return this._taskCache;
	}

	//returns schedule cache
	public ArrayList<Event> getScheduleCache() {
		return this._scheduleCache;
	}

	//returns deadline cache
	public ArrayList<Event> getDeadlineCache() {
		return this._deadlineCache;
	}

	//returns the last index of events
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	//adds a task
	public int addTask(String name, String category, int priority){
		Event newTask = new Event(currentIndex, name, category, priority);
		addTaskToStorage(newTask);
		return newTask.getID();

	}
	
	//adds a task to the cache + storage
	public int addTaskToStorage(Event newTask){
		logger.writeLog("addTask: "+newTask.getName());
		_taskCache.add(newTask);
		currentIndex++;
		updateStorage();
		return newTask.getID();
	}

	//adds a deadline
	public int addDeadline(String name, Date endDate, String category, int priority){
		Event newDeadline = new Event(currentIndex, name, endDate, category, priority);
		addDeadlineToStorage(newDeadline);
		return newDeadline.getID();
	}
	
	//adds a deadline to the cache + storage
	public int addDeadlineToStorage (Event newDeadline){
		logger.writeLog("add deadline: "+newDeadline.getName());
		_deadlineCache.add(newDeadline);
		currentIndex++;
		updateStorage();
		return newDeadline.getID();
	}

	//adds a schedule
	public int addSchedule(String name, Date startDate, Date endDate, String category, int priority){
		Event newSchedule = new Event(currentIndex, name, startDate, endDate, category, priority);
		if (tb.canFitSchedule(newSchedule)) {
			addScheduleToStorage(newSchedule);
			return newSchedule.getID();
		} else {
			return -1;
		}
	}

	//adds a schedule to cache + storage
	public int addScheduleToStorage(Event newSchedule){
		logger.writeLog("add schedule: "+newSchedule.getName());
		_scheduleCache.add(newSchedule);
		currentIndex++;
		updateStorage();
		tb.updateCache(_scheduleCache);
		return newSchedule.getID();
	}
	
	/**
	 * precon:id exists
	 * Iterates through the event caches, removes event with the same 
	 * ID as @param id
	 * @param id
	 * @return deleted event
	 */
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
	}

	/**
	 * precon:id exists
	 * Iterates through the event caches, updates name of even with id == @param ID
	 * to @param name
	 * @param id
	 * @param name
	 */
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
	}

	/**
	 * precon:id exists
	 * Iterates through schedule cache, updates the start date of the
	 * event with ID == @param id to @param startDate
	 * @param id
	 * @param startDate
	 * @return true if start date is not blocked by the time block, false otherwise
	 */
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
	}

	/**
	 * precon:id exists
	 * Iterates through schedule and deadline cache, updates the end date of the
	 * event with ID == @param id to @param endDate
	 * @param id
	 * @param endDate
	 * @return true if end date is not blocked by the time block, false otherwise
	 */
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
	}

	/**
	 * precon:id exists
	 * Iterates through the event caches, updates the category of the
	 * event with ID == @param id to @param category
	 * @param id
	 * @param category
	 */
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
	}

	/**
	 * precon:id exists
	 * Iterates through the event caches, updates the priority of the
	 * event with ID == @param id to @param priority
	 * @param id
	 * @param priority
	 */
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
	}

	/**
	 * precon:id exists
	 * Iterates through the event caches, updates whether the
	 * event with ID == @param id is done with @param isDone
	 * @param id
	 * @param isDone
	 */
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
	}

	//sorts the caches according to the events' names
	public void sortName() {
		Collections.sort(_taskCache, Sorters.sortName());
		Collections.sort(_deadlineCache, Sorters.sortName());
		Collections.sort(_scheduleCache, Sorters.sortName());
	}

	//sorts the schedules according to their start times
	public void sortStart() {
		Collections.sort(_scheduleCache, Sorters.sortStart());
	}

	//sorts the deadlines and schedules according to their end times
	public void sortEnd() {
		Collections.sort(_deadlineCache, Sorters.sortEnd());
		Collections.sort(_scheduleCache, Sorters.sortEnd());
	}

	//sorts the caches according to the events' priorities
	public void sortPriority() {
		Collections.sort(_taskCache, Sorters.sortPriority());
		Collections.sort(_deadlineCache, Sorters.sortPriority());
		Collections.sort(_scheduleCache, Sorters.sortPriority());
	}

	/**
	 * Searches the task cache for all tasks that contain a key word
	 * @param key
	 * @return an ArrayList of all tasks which contain @param key
	 */
	public ArrayList<Event> searchTask(String key) {
		ArrayList<Event> result = new ArrayList<Event>();
		for (Event element:_taskCache){
			if (element.contains(key)) {
				result.add(element);
			}
		}
		return result;
	}

	/**
	 * Searches the deadline cache for all deadlines that contain a key word
	 * @param key
	 * @return an ArrayList of all deadlines which contain @param key
	 */
	public ArrayList<Event> searchDeadline(String key) {
		ArrayList<Event> result = new ArrayList<Event>();
		for (Event element:_deadlineCache){
			if (element.contains(key)) {
				result.add(element);
			}
		}
		return result;
	}

	/**
	 * Searches the schedule cache for all schedules that contain a key word
	 * @param key
	 * @return an ArrayList of all schedules which contain @param key
	 */
	public ArrayList<Event> searchSchedule(String key) {
		ArrayList<Event> result = new ArrayList<Event>();
		for (Event element:_scheduleCache){
			if (element.contains(key)) {
				result.add(element);
			}
		}
		return result;
	}

	//clears all event caches
	public void clear(){
		_scheduleCache.clear();
		_deadlineCache.clear();
		_taskCache.clear();
		updateStorage();
	}

	/**
	 * Initializes TangGuo by reading from the storage file and initializing
	 * the event caches with the data stored
	 */
	private void initialize() {
		try {
			File inputFile;
			
			if (_filePath.equals("")) {
				inputFile = new File(_fileName);
			} else {
				inputFile = new File(_filePath, _fileName);
			}
			
			if (!inputFile.exists()) {
				createStorageFile();
				return;
			}
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
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

	/**
	 * creates an XML file for storing user data
	 */
	private void createStorageFile() {
		currentIndex = 0;
		StringWriter stringWriter = new StringWriter();
		XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
		XMLStreamWriter xMLStreamWriter;
		
		try {
			xMLStreamWriter = xMLOutputFactory
					.createXMLStreamWriter(stringWriter);

			xMLStreamWriter.writeStartDocument();
			xMLStreamWriter.writeStartElement(Constants.CALENDAR);
			xMLStreamWriter.writeAttribute(Constants.ATTRIBUTE_CURRENT_INDEX, Constants.INITIALIZE_CURRENT_INDEX);
			xMLStreamWriter.writeEndElement();
			xMLStreamWriter.writeEndDocument();

			xMLStreamWriter.flush();
			xMLStreamWriter.close();

		} catch (XMLStreamException e) {
			e.printStackTrace();
			return;
		}
		writexmlStringToFile(stringWriter.getBuffer().toString());
		
		try {
			stringWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//parses and returns the current index from @param doc
	private int getCurrentIndexFromFile(Document doc) {
		return Integer.parseInt(doc.getDocumentElement().getAttribute(Constants.ATTRIBUTE_CURRENT_INDEX));
	}

	/**
	 * reads task data from @param doc and stores the task events into the task cache
	 * @param doc
	 */
	private void initializeTaskCache(Document doc) {	
		NodeList nodeList = getNodeList(doc, Constants.XML_TASK_EXPRESSION);		
		Event event;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				event = createTaskEvent(eElement);
				_taskCache.add(event);
			}
		}		
	}
	
	/**
	 * converts @param eElement into an Event object (task)
	 * @param eElement
	 * @return an Event object parsed from @param eElement
	 */
	private Event createTaskEvent(Element eElement) {
		int ID = Integer.parseInt(eElement.getAttribute(Constants.ATTRIBUTE_ID));
		String nameString = getPropertyFromElement(eElement, Constants.PROPERTY_NAME);;
		String categoryString = getPropertyFromElement(eElement, Constants.PROPERTY_CATEGORY);
		int priority = Integer.parseInt(getPropertyFromElement(eElement, Constants.PROPERTY_PRIORITY));
		boolean isDone = Boolean.parseBoolean(getPropertyFromElement(eElement, Constants.PROPERTY_IS_DONE));

		Event event = new Event(ID, nameString, categoryString, priority);
		event.setIsDone(isDone);
		
		return event;
	}
	
	/**
	 * @param doc
	 * @param expression
	 * @return a NodeList from @param doc by compiling @param expression
	 */
	private NodeList getNodeList(Document doc, String expression) {
		try{
			XPath xPath = XPathFactory.newInstance().newXPath();
			return (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e){
			System.out.println("Failed to compile xPath");
			return null;
		}
	}
	
	//returns a property of an Element object
	private String getPropertyFromElement(Element eElement, String property){
		return eElement.getElementsByTagName(property).item(0).getTextContent();
	}

	/**
	 * reads deadline data from @param doc and stores the deadline events into 
	 * the deadline cache
	 * @param doc
	 */
	private void initializeDeadlineCache(Document doc) {
		NodeList nodeList = getNodeList(doc, Constants.XML_DEADLINE_EXPRESSION);
		Event event;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				event = createDeadlineEvent(eElement);
				_deadlineCache.add(event);
			}
		}	
	}
	
	/**
	 * converts @param eElement into an Event object (deadline)
	 * @param eElement
	 * @return an Event object parsed from @param eElement
	 */
	private Event createDeadlineEvent(Element eElement) {
		Event deadline = createTaskEvent(eElement);
		DateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Date endDate = null;
		try {
			endDate = sdf.parse(getPropertyFromElement(eElement, Constants.PROPERTY_END));
		} catch (ParseException e) {
			System.out.println("Failed to parse date when reading from file");
			e.printStackTrace();
			return null;
		}
		deadline.setEnd(endDate);
		return deadline;
	}

	/**
	 * reads schedule data from @param doc and stores the schedule events into 
	 * the schedule cache
	 * @param doc
	 */
	private void initializeScheduleCache(Document doc) {
		NodeList nodeList = getNodeList(doc, Constants.XML_SCHEDULE_EXPRESSION);
		Event event;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				event = createScheduleEvent(eElement);
				_scheduleCache.add(event);
			}
		}
		
	}
	
	/**
	 * converts @param eElement into an Event object (schedule)
	 * @param eElement
	 * @return an Event object parsed from @param eElement
	 */
	private Event createScheduleEvent(Element eElement) {
		Event schedule = createTaskEvent(eElement);
		DateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Date endDate = null, startDate = null;
		try {
			startDate = sdf.parse(getPropertyFromElement(eElement, Constants.PROPERTY_START));
			endDate = sdf.parse(getPropertyFromElement(eElement, Constants.PROPERTY_END));
		} catch (ParseException e) {
			System.out.println("Failed to parse date when reading from file");
			e.printStackTrace();
			return null;
		}
		schedule.setStart(startDate);
		schedule.setEnd(endDate);
		return schedule;
	}
	
	/**
	 * Writes every event stored inside the event caches into the storage file
	 */
	private void updateStorage(){
		try {
	         StringWriter stringWriter = new StringWriter();
	         DateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
	         XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
	         XMLStreamWriter xmlStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);

	         xmlStreamWriter.writeStartDocument();
	         xmlStreamWriter.writeStartElement(Constants.CALENDAR);
	         xmlStreamWriter.writeAttribute(Constants.ATTRIBUTE_CURRENT_INDEX, String.valueOf(currentIndex));
	         
	         for (Event element:_taskCache){
	        	 xmlStreamWriter.writeStartElement(Constants.TASK_TYPE);
	        	 writeTaskProperties(xmlStreamWriter, element);
	             xmlStreamWriter.writeEndElement();
	         }

	         for (Event element:_deadlineCache){
	        	 xmlStreamWriter.writeStartElement(Constants.DEADLINE_TYPE);
	             writeTaskProperties(xmlStreamWriter, element);
	             writeEndProperty(xmlStreamWriter, element, sdf);
	             xmlStreamWriter.writeEndElement();
	         }

	         for (Event element:_scheduleCache){
	        	 xmlStreamWriter.writeStartElement(Constants.SCHEDULE_TYPE);
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
	         e.printStackTrace();
	      }
	}
	
	/**
	 * Writes in the task properties of an Event object
	 * @param xmlStreamWriter
	 * @param element
	 */
	private void writeTaskProperties(XMLStreamWriter xmlStreamWriter, Event element){
		try{
			xmlStreamWriter.writeAttribute(Constants.ATTRIBUTE_ID, String.valueOf(element.getID()));
	        xmlStreamWriter.writeStartElement(Constants.PROPERTY_NAME);
	        xmlStreamWriter.writeCharacters(element.getName());
	        xmlStreamWriter.writeEndElement();
	        xmlStreamWriter.writeStartElement(Constants.PROPERTY_CATEGORY);
	        xmlStreamWriter.writeCharacters(element.getCategory());
	        xmlStreamWriter.writeEndElement();
	        xmlStreamWriter.writeStartElement(Constants.PROPERTY_PRIORITY);
	        xmlStreamWriter.writeCharacters(String.valueOf(element.getPriority()));
	        xmlStreamWriter.writeEndElement();
	        xmlStreamWriter.writeStartElement(Constants.PROPERTY_IS_DONE);
	        xmlStreamWriter.writeCharacters(String.valueOf(element.isDone()));
	        xmlStreamWriter.writeEndElement();
		} catch (XMLStreamException e){
			System.out.println("Failed to write into file");
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes in the start time property of schedule Event objects
	 * @param xmlStreamWriter
	 * @param element
	 * @param sdf
	 */
	private void writeStartProperty(XMLStreamWriter xmlStreamWriter, Event element, DateFormat sdf){
		try {
			xmlStreamWriter.writeStartElement(Constants.PROPERTY_START);
            xmlStreamWriter.writeCharacters(sdf.format(element.getStart()));
            xmlStreamWriter.writeEndElement();
		} catch (XMLStreamException e) {
			System.out.println("Failed to write into file");
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes in the end time properties of deadline and schedule Event objects
	 * @param xmlStreamWriter
	 * @param element
	 * @param sdf
	 */
	private void writeEndProperty(XMLStreamWriter xmlStreamWriter, Event element, DateFormat sdf){
		try {
			xmlStreamWriter.writeStartElement(Constants.PROPERTY_END);
            xmlStreamWriter.writeCharacters(sdf.format(element.getEnd()));
            xmlStreamWriter.writeEndElement();
		} catch (XMLStreamException e) {
			System.out.println("Failed to write into file");
			e.printStackTrace();
		}
	}

	/**
	 * Writes in @param xmlString into the storage file
	 * @param xmlString
	 */
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
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}