package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private String fileAdd;
	//private Writer output;
	final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public Logger(String fileAdd) throws IOException{
		
		this.fileAdd = fileAdd;
		
		
	}
	public void writeException(String content) throws IOException{
		Date date = new Date();
		String temp = dateFormat.format(date) + " exception occurs: " + content+"\n";
		writeline(temp);
	}
	public void writeLogs(String content) throws IOException{
		Date date = new Date();
		String temp = dateFormat.format(date) + " Log: " + content+"\n";
		writeline(temp);
	}
	
	private void writeline(String content) throws IOException{
		Writer output = new BufferedWriter(new FileWriter(fileAdd, true));
		output.append(content);
		output.close();
	}
}
