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
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
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
import TGUtils.TimeClash;

public class TGStorageManager {
	private String filePath;
	private String fileName;
	private ArrayList<Event> taskCache;
	private ArrayList<Event> deadlineCache;
	private ArrayList<Event> scheduleCache;
	private Logger logger;
	private TimeClash tb;
	private int currentIndex;
	File inputFile;

	/**
	 * Creates a TGStorageManager object initializes file, event caches, logger,
	 * and time block
	 *
	 * @param filePath
	 * @param fileName
	 */
	public TGStorageManager(String filePath, String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;
		this.taskCache = new ArrayList<Event>();
		this.deadlineCache = new ArrayList<Event>();
		this.scheduleCache = new ArrayList<Event>();
		this.tb = new TimeClash();
		try {
			//initialize logger
			this.logger = new Logger(Constants.LOG_FILE);
		} catch (IOException e) {
			//if the logger fails to initialize, there's no choice but to print it on console
			System.out.println(Constants.FAILED_TO_INITIALIZE_LOGGER);
		}
		initialize();
	}

	// sets the file path to be @param filePath
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Iterates through the event caches to find an event of the same id
	 *
	 * @param id
	 * @return event which id == @param id, else null
	 */
	public Event getEventByID(int id) {
		for (Event element : taskCache) {
			if (element.getID() == id) {
				return element;
			}
		}

		for (Event element : scheduleCache) {
			if (element.getID() == id) {
				return element;
			}
		}

		for (Event element : deadlineCache) {
			if (element.getID() == id) {
				return element;
			}
		}
		return null;
	}

	// returns task cache
	public ArrayList<Event> getTaskCache() {
		return this.taskCache;
	}

	// returns schedule cache
	public ArrayList<Event> getScheduleCache() {
		return this.scheduleCache;
	}

	// returns deadline cache
	public ArrayList<Event> getDeadlineCache() {
		return this.deadlineCache;
	}

	/**
	 * Creates an Event object with given params, and stores the task event into
	 * storage
	 *
	 * @param name
	 * @param category
	 * @param priority
	 * @return id of the task event being added
	 */
	public int addTask(String name, String category, int priority) {
		Event newTask = new Event(currentIndex, name, category, priority);
		addTaskToStorage(newTask);
		return newTask.getID();
	}

	/**
	 * Creates an Event object with given params, and stores the deadline event
	 * into storage
	 *
	 * @param name
	 * @param endDate
	 * @param category
	 * @param priority
	 * @return id of the deadline event being added
	 */
	public int addDeadline(String name, Date endDate, String category, int priority) {
		Event newDeadline = new Event(currentIndex, name, endDate, category, priority);
		addDeadlineToStorage(newDeadline);
		return newDeadline.getID();
	}

	/**
	 * Creates an Event object with given params, and stores the schedule event
	 * into storage
	 *
	 * @param name
	 * @param startDate
	 * @param endDate
	 * @param category
	 * @param priority
	 * @return id of the schedule event being added
	 */
	public int addSchedule(String name, Date startDate, Date endDate, String category, int priority) {
		Event newSchedule = new Event(currentIndex, name, startDate, endDate, category, priority);
		addScheduleToStorage(newSchedule);
		return newSchedule.getID();
	}

	/**
	 * Adds an Event object into storage and writes this action into the logger
	 *
	 * @param newEvent
	 * @param eventCache
	 * @param logMsg
	 * @return id of @newEvent
	 */
	private int addEventToStorage(Event newEvent, ArrayList<Event> eventCache) {
		logger.writeAddEventLog(newEvent.getName());
		eventCache.add(newEvent);
		currentIndex++;
		updateStorage();
		return newEvent.getID();
	}

	// adds a task Event object to storage
	public int addTaskToStorage(Event newTask) {
		return addEventToStorage(newTask, taskCache);
	}

	// adds a deadline Event object to storage
	public int addDeadlineToStorage(Event newDeadline) {
		return addEventToStorage(newDeadline, deadlineCache);
	}

	// adds a schedule Event object to storage
	public int addScheduleToStorage(Event newSchedule) {
		int id = addEventToStorage(newSchedule, scheduleCache);
		tb.updateCache(scheduleCache);
		scheduleCache = tb.getCache();
		return id;
	}

	/**
	 * precon:id exists Iterates through the Event caches, removes Event object
	 * with the same ID as @param id
	 *
	 * @param id
	 * @return deleted event
	 */
	public Event deleteEventByID(int id) {
		for (Event element : taskCache) {
			if (element.getID() == id) {
				logger.writeDeleteEventLog(element.getName());
				taskCache.remove(element);
				updateStorage();
				return element;
			}
		}

		for (Event element : scheduleCache) {
			if (element.getID() == id) {
				logger.writeDeleteEventLog(element.getName());
				scheduleCache.remove(element);
				updateStorage();
				tb.updateCache(scheduleCache);
				return element;
			}
		}

		for (Event element : deadlineCache) {
			if (element.getID() == id) {
				logger.writeDeleteEventLog(element.getName());
				deadlineCache.remove(element);
				updateStorage();
				return element;
			}
		}
		return null; //event is not found in storage, return null
	}

	/**
	 * precon:id exists Iterates through the Event caches, updates name of Event
	 * object with id == @param ID to @param name
	 *
	 * @param id
	 * @param name
	 */
	public void updateNameByID(int id, String name) {
		for (Event element : taskCache) {
			if (element.getID() == id) {
				element.setName(name);
				updateStorage();
				return;
			}
		}

		for (Event element : scheduleCache) {
			if (element.getID() == id) {
				element.setName(name);
				updateStorage();
				tb.updateCache(scheduleCache);
				return;
			}
		}

		for (Event element : deadlineCache) {
			if (element.getID() == id) {
				element.setName(name);
				updateStorage();
				return;
			}
		}
	}

	/**
	 * precon:id exists Iterates through schedule cache, updates the start date
	 * of the Event object with ID == @param id to @param startDate
	 *
	 * @param id
	 * @param startDate
	 * @return true if start date is not blocked by the time block, false
	 *         otherwise
	 */
	public boolean updateStartByID(int id, Date startDate) {
		for (Event element : scheduleCache) {
			if (element.getID() == id) {
				if (startDate.before(element.getEnd())) {
					element.setStart(startDate);
					updateStorage();
					tb.updateCache(scheduleCache);
					scheduleCache = tb.getCache();
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * precon:id exists Iterates through schedule and deadline cache, updates
	 * the end date of the Event object with ID == @param id to @param endDate
	 *
	 * @param id
	 * @param endDate
	 * @return true if end date is not blocked by the time block, false
	 *         otherwise
	 */
	public boolean updateEndByID(int id, Date endDate) {
		for (Event element : scheduleCache) {
			if (element.getID() == id) {
				if (endDate.after(element.getStart())) {
					element.setEnd(endDate);
					updateStorage();
					tb.updateCache(scheduleCache);
					scheduleCache = tb.getCache();
					return true;
				} else {
					return false;
				}
			}
		}

		for (Event element : deadlineCache) {
			if (element.getID() == id) {
				element.setEnd(endDate);
				updateStorage();
				return true;
			}
		}
		return false;
	}

	/**
	 * precon:id exists Iterates through the Event caches, updates the category
	 * of the Event object with ID == @param id to @param category
	 *
	 * @param id
	 * @param category
	 */
	public void updateCategoryByID(int id, String category) {
		for (Event element : taskCache) {
			if (element.getID() == id) {
				element.setCategory(category);
				updateStorage();
				return;
			}
		}

		for (Event element : scheduleCache) {
			if (element.getID() == id) {
				element.setCategory(category);
				updateStorage();
				tb.updateCache(scheduleCache);
				return;
			}
		}

		for (Event element : deadlineCache) {
			if (element.getID() == id) {
				element.setCategory(category);
				updateStorage();
				return;
			}
		}
	}

	/**
	 * precon:id exists Iterates through the Event caches, updates the priority
	 * of the Event object with ID == @param id to @param priority
	 *
	 * @param id
	 * @param priority
	 */
	public void updatePriorityByID(int id, int priority) {
		for (Event element : taskCache) {
			if (element.getID() == id) {
				element.setPriority(priority);
				updateStorage();
				return;
			}
		}

		for (Event element : scheduleCache) {
			if (element.getID() == id) {
				element.setPriority(priority);
				updateStorage();
				tb.updateCache(scheduleCache);
				return;
			}
		}

		for (Event element : deadlineCache) {
			if (element.getID() == id) {
				element.setPriority(priority);
				updateStorage();
				return;
			}
		}
	}

	/**
	 * precon:id exists Iterates through the Event caches, updates whether the
	 * Event object with ID == @param id is done with @param isDone
	 *
	 * @param id
	 * @param isDone
	 */
	public void updateIsDoneByID(int id, boolean isDone) {
		for (Event element : taskCache) {
			if (element.getID() == id) {
				element.setIsDone(isDone);
				updateStorage();
				return;
			}
		}

		for (Event element : scheduleCache) {
			if (element.getID() == id) {
				element.setIsDone(isDone);
				updateStorage();
				tb.updateCache(scheduleCache);
				return;
			}
		}

		for (Event element : deadlineCache) {
			if (element.getID() == id) {
				element.setIsDone(isDone);
				updateStorage();
				return;
			}
		}
	}

	// sorts the caches according to event names
	public void sortName() {
		Collections.sort(taskCache, Sorters.sortName());
		Collections.sort(deadlineCache, Sorters.sortName());
		Collections.sort(scheduleCache, Sorters.sortName());
	}

	// sorts the schedules according to start times
	public void sortStart() {
		Collections.sort(scheduleCache, Sorters.sortStart());
	}

	// sorts the deadlines and schedules according to end times
	public void sortEnd() {
		Collections.sort(deadlineCache, Sorters.sortEnd());
		Collections.sort(scheduleCache, Sorters.sortEnd());
	}

	// sorts the caches according to event priorities
	public void sortPriority() {
		Collections.sort(taskCache, Sorters.sortPriority());
		Collections.sort(deadlineCache, Sorters.sortPriority());
		Collections.sort(scheduleCache, Sorters.sortPriority());
	}

	/**
	 * Searches the task cache for all tasks that contain a key word
	 *
	 * @param key
	 * @return an ArrayList of all tasks which contain @param key
	 */
	public ArrayList<Event> searchTask(String key) {
		return searchEventCache(key, taskCache);
	}

	/**
	 * Searches the deadline cache for all deadlines that contain a key word
	 *
	 * @param key
	 * @return an ArrayList of all deadlines which contain @param key
	 */
	public ArrayList<Event> searchDeadline(String key) {
		return searchEventCache(key, deadlineCache);
	}

	/**
	 * Searches the schedule cache for all schedules that contain a key word
	 *
	 * @param key
	 * @return an ArrayList of all schedules which contain @param key
	 */
	public ArrayList<Event> searchSchedule(String key) {
		return searchEventCache(key, scheduleCache);
	}

	/**
	 * Searches @param cache for all Event objects containing keyword @param key
	 *
	 * @param key
	 * @param cache
	 * @return an ArrayList of Event objects that match the criteria
	 */
	private ArrayList<Event> searchEventCache(String key, ArrayList<Event> cache) {
		ArrayList<Event> result = new ArrayList<Event>();
		for (Event element : cache) {
			if (element.contains(key)) {
				result.add(element);
			}
		}
		return result;
	}

	// clears all event caches
	public void clear() {
		scheduleCache.clear();
		deadlineCache.clear();
		taskCache.clear();
		updateStorage();
	}

	/**
	 * Initializes TangGuo by reading from the storage file and initializing the
	 * event caches with the data stored
	 */
	private void initialize() {
		File inputFile;
		if (filePath.equals(Constants.NULL)) {
			inputFile = new File(fileName);
		} else {
			inputFile = new File(filePath, fileName);
		}
		if (!inputFile.exists()) {
			createStorageFile();
			return;
		}
		Document doc = parseFile(inputFile);
		currentIndex = getCurrentIndexFromFile(doc);
		initializeCaches(doc);
	}

	/**
	 * Parses @param inputFile into a Document object
	 *
	 * @param inputFile
	 * @return Document object of @param inputFile
	 */
	private Document parseFile(File inputFile) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			return doc;
		} catch (ParserConfigurationException e) {
			logger.writeException(Constants.LOG_FAILED_PARSE_FILE);
			e.printStackTrace();
		} catch (SAXException e) {
			logger.writeException(Constants.LOG_FAILED_PARSE_FILE);
			e.printStackTrace();
		} catch (IOException e) {
			logger.writeException(Constants.LOG_FAILED_PARSE_FILE);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Initializes task, deadline, and schedule caches from storage file
	 *
	 * @param doc
	 */
	private void initializeCaches(Document doc) {
		initializeTaskCache(doc);
		initializeDeadlineCache(doc);
		initializeScheduleCache(doc);
		tb.updateCache(this.scheduleCache);
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
			xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);
			xMLStreamWriter.writeStartDocument();
			xMLStreamWriter.writeStartElement(Constants.CALENDAR);
			xMLStreamWriter.writeAttribute(Constants.ATTRIBUTE_CURRENT_INDEX, Constants.INITIALIZE_CURRENT_INDEX);
			xMLStreamWriter.writeEndElement();
			xMLStreamWriter.writeEndDocument();
			xMLStreamWriter.flush();
			xMLStreamWriter.close();
		} catch (XMLStreamException e) {
			logger.writeException(Constants.LOG_FAILED_CREATE_FILE);
			e.printStackTrace();
			return;
		}
		writeXMLStringToFile(stringWriter.getBuffer().toString());
		try {
			stringWriter.close();
		} catch (IOException e) {
			logger.writeException(Constants.LOG_FAILED_CLOSE_STRINGWRITER);
			e.printStackTrace();
		}
	}

	// parses and returns the current index from @param doc
	private int getCurrentIndexFromFile(Document doc) {
		return Integer.parseInt(doc.getDocumentElement().getAttribute(Constants.ATTRIBUTE_CURRENT_INDEX));
	}

	/**
	 * reads task data from @param doc and stores the task events into the task
	 * cache
	 *
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
				taskCache.add(event);
			}
		}
	}

	/**
	 * converts @param eElement into an Event object (task)
	 *
	 * @param eElement
	 * @return an Event object parsed from @param eElement
	 */
	private Event createTaskEvent(Element eElement) {
		int ID = Integer.parseInt(eElement.getAttribute(Constants.ATTRIBUTE_ID));
		String nameString = getPropertyFromElement(eElement, Constants.PROPERTY_NAME);
		;
		String categoryString = getPropertyFromElement(eElement, Constants.PROPERTY_CATEGORY);
		int priority = Integer.parseInt(getPropertyFromElement(eElement, Constants.PROPERTY_PRIORITY));
		boolean isDone = Boolean.parseBoolean(getPropertyFromElement(eElement, Constants.PROPERTY_IS_DONE));
		boolean hasClash = Boolean.parseBoolean(getPropertyFromElement(eElement, Constants.PROPERTY_HAS_CLASH));

		Event event = new Event(ID, nameString, categoryString, priority);
		event.setIsDone(isDone);
		event.setHasClash(hasClash);
		return event;
	}

	/**
	 * @param doc
	 * @param expression
	 * @return a NodeList from @param doc by compiling @param expression
	 */
	private NodeList getNodeList(Document doc, String expression) {
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			return (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			logger.writeException(Constants.LOG_FAILED_COMPILATION_XPATH);
			e.printStackTrace();
			return null;
		}
	}

	// returns specified property of an Element object
	private String getPropertyFromElement(Element eElement, String property) {
		return eElement.getElementsByTagName(property).item(0).getTextContent();
	}

	/**
	 * reads deadline data from @param doc and stores the deadline events into
	 * the deadline cache
	 *
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
				deadlineCache.add(event);
			}
		}
	}

	/**
	 * converts @param eElement into an Event object (deadline)
	 *
	 * @param eElement
	 * @return an Event object parsed from @param eElement
	 */
	private Event createDeadlineEvent(Element eElement) {
		int ID = Integer.parseInt(eElement.getAttribute(Constants.ATTRIBUTE_ID));
		String nameString = getPropertyFromElement(eElement, Constants.PROPERTY_NAME);
		;
		String categoryString = getPropertyFromElement(eElement, Constants.PROPERTY_CATEGORY);
		int priority = Integer.parseInt(getPropertyFromElement(eElement, Constants.PROPERTY_PRIORITY));
		boolean isDone = Boolean.parseBoolean(getPropertyFromElement(eElement, Constants.PROPERTY_IS_DONE));
		boolean hasClash = Boolean.parseBoolean(getPropertyFromElement(eElement, Constants.PROPERTY_HAS_CLASH));

		DateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Date endDate = null;
		try {
			endDate = sdf.parse(getPropertyFromElement(eElement, Constants.PROPERTY_END));
		} catch (ParseException e) {
			logger.writeException(Constants.LOG_FAILED_PARSE_DATE_FROM_FILE);
			e.printStackTrace();
			return null;
		}
		Event event = new Event(ID, nameString, endDate, categoryString, priority);
		event.setIsDone(isDone);
		event.setHasClash(hasClash);
		return event;
	}

	/**
	 * reads schedule data from @param doc and stores the schedule events into
	 * the schedule cache
	 *
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
				scheduleCache.add(event);
			}
		}
	}

	/**
	 * converts @param eElement into an Event object (schedule)
	 *
	 * @param eElement
	 * @return an Event object parsed from @param eElement
	 */
	private Event createScheduleEvent(Element eElement) {
		int ID = Integer.parseInt(eElement.getAttribute(Constants.ATTRIBUTE_ID));
		String nameString = getPropertyFromElement(eElement, Constants.PROPERTY_NAME);
		;
		String categoryString = getPropertyFromElement(eElement, Constants.PROPERTY_CATEGORY);
		int priority = Integer.parseInt(getPropertyFromElement(eElement, Constants.PROPERTY_PRIORITY));
		boolean isDone = Boolean.parseBoolean(getPropertyFromElement(eElement, Constants.PROPERTY_IS_DONE));
		boolean hasClash = Boolean.parseBoolean(getPropertyFromElement(eElement, Constants.PROPERTY_HAS_CLASH));

		DateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Date endDate = null, startDate = null;
		try {
			startDate = sdf.parse(getPropertyFromElement(eElement, Constants.PROPERTY_START));
			endDate = sdf.parse(getPropertyFromElement(eElement, Constants.PROPERTY_END));
		} catch (ParseException e) {
			logger.writeException(Constants.LOG_FAILED_PARSE_DATE_FROM_FILE);
			e.printStackTrace();
			return null;
		}
		Event event = new Event(ID, nameString, startDate, endDate, categoryString, priority);
		event.setIsDone(isDone);
		event.setHasClash(hasClash);
		return event;
	}

	/**
	 * Writes every event stored inside the Event caches into the storage file
	 */
	private void updateStorage() {
		try {
			StringWriter stringWriter = new StringWriter();
			DateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
			XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xmlStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);

			xmlStreamWriter.writeStartDocument();
			xmlStreamWriter.writeStartElement(Constants.CALENDAR);
			xmlStreamWriter.writeAttribute(Constants.ATTRIBUTE_CURRENT_INDEX, String.valueOf(currentIndex));
			for (Event element : taskCache) {
				xmlStreamWriter.writeStartElement(Constants.TASK_TYPE);
				writeTaskProperties(xmlStreamWriter, element);
				xmlStreamWriter.writeEndElement();
			}
			for (Event element : deadlineCache) {
				xmlStreamWriter.writeStartElement(Constants.DEADLINE_TYPE);
				writeTaskProperties(xmlStreamWriter, element);
				writeEndProperty(xmlStreamWriter, element, sdf);
				xmlStreamWriter.writeEndElement();
			}
			for (Event element : scheduleCache) {
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
			writeXMLStringToFile(xmlString);
			stringWriter.close();
		} catch (XMLStreamException e) {
			logger.writeException(Constants.LOG_FAILED_WRITE_TO_FILE);
			e.printStackTrace();
		} catch (IOException e) {
			logger.writeException(Constants.LOG_FAILED_CLOSE_STRINGWRITER);
			e.printStackTrace();
		}
	}

	/**
	 * Writes in the task properties of an Event object
	 *
	 * @param xmlStreamWriter
	 * @param element
	 */
	private void writeTaskProperties(XMLStreamWriter xmlStreamWriter, Event element) {
		try {
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
			xmlStreamWriter.writeStartElement(Constants.PROPERTY_HAS_CLASH);
			xmlStreamWriter.writeCharacters(String.valueOf(element.hasClash()));
			xmlStreamWriter.writeEndElement();
		} catch (XMLStreamException e) {
			logger.writeException(Constants.LOG_FAILED_WRITE_TO_FILE);
			e.printStackTrace();
		}
	}

	/**
	 * Writes in the start time property of schedule Event objects
	 *
	 * @param xmlStreamWriter
	 * @param element
	 * @param sdf
	 */
	private void writeStartProperty(XMLStreamWriter xmlStreamWriter, Event element, DateFormat sdf) {
		try {
			xmlStreamWriter.writeStartElement(Constants.PROPERTY_START);
			xmlStreamWriter.writeCharacters(sdf.format(element.getStart()));
			xmlStreamWriter.writeEndElement();
		} catch (XMLStreamException e) {
			logger.writeException(Constants.LOG_FAILED_WRITE_TO_FILE);
			e.printStackTrace();
		}
	}

	/**
	 * Writes in the end time property of deadline and schedule Event objects
	 *
	 * @param xmlStreamWriter
	 * @param element
	 * @param sdf
	 */
	private void writeEndProperty(XMLStreamWriter xmlStreamWriter, Event element, DateFormat sdf) {
		try {
			xmlStreamWriter.writeStartElement(Constants.PROPERTY_END);
			xmlStreamWriter.writeCharacters(sdf.format(element.getEnd()));
			xmlStreamWriter.writeEndElement();
		} catch (XMLStreamException e) {
			logger.writeException(Constants.LOG_FAILED_WRITE_TO_FILE);
			e.printStackTrace();
		}
	}

	/**
	 * Writes in @param xmlString into the storage file
	 *
	 * @param xmlString
	 */
	private void writeXMLStringToFile(String xmlString) {
		try {
			Source xmlInput = new StreamSource(new StringReader(xmlString));
			StringWriter outputStringWriter = new StringWriter();
			StreamResult xmlOutput = new StreamResult(outputStringWriter);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute(Constants.ATTRIBUTE_INDENT_NUMBER, Constants.ATTRIBUTE_INDENT_NUMBER_VALUE);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, Constants.PROPERTY_YES);
			transformer.transform(xmlInput, xmlOutput);
			File outputFile;
			if (filePath.equals(Constants.NULL)) {
				outputFile = new File(fileName);
			} else {
				outputFile = new File(filePath, fileName);
			}
			FileWriter fw = new FileWriter(outputFile);
			fw.write(xmlOutput.getWriter().toString());
			fw.close();
		} catch (IOException e) {
			logger.writeException(Constants.LOG_FAILED_WRITE_TO_FILE);
			e.printStackTrace();
		} catch (TransformerException e) {
			logger.writeException(Constants.LOG_FAILED_TRANSFORM_XMLSTRING);
			e.printStackTrace();
		}
	}
}