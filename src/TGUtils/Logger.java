//@@author A0126833E
package TGUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private String fileAdd;
	final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public Logger(String fileAdd) {

		this.fileAdd = fileAdd;
	}
	//log exception
	public void writeException(String content) {
		Date date = new Date();
		String temp = dateFormat.format(date) + " exception occurs: " + content + "\n";
		writeline(temp);

	}
	//log adding of an event
	public void writeAddEventLog(String name) {
		writeLog(Constants.LOG_ADD_TASK + name);
	}
	//log deleting of an event
	public void writeDeleteEventLog(String name) {
		writeLog(Constants.LOG_DELETE_TASK + name);
	}
	//write a log with timestamp (content) is string
	public void writeLog(String content) {
		Date date = new Date(); //current time
		String temp = dateFormat.format(date) + " Log: " + content + "\n";
		writeline(temp);
	}

	private void writeline(String content) {
		try {
			Writer output = new BufferedWriter(new FileWriter(fileAdd, true));
			output.append(content);
			output.close();
		} catch (IOException e) {
			System.out.println("error writing log");
			e.printStackTrace();
		}

	}
}
