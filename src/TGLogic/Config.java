package TGLogic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


public class Config {

	private String _fileName = "TangGuo";
	private String _filePath = "";
		
	public Config() {
		readConfig();
	}
	
	public void setFileName(String newName) {
		_fileName = newName;
	}
	
	public void setFilePath(String newPath) {
		_filePath = newPath;
	}
	
	public String getFileName() {
		return _fileName;
	}
	
	public String getFilePath() {
		return _filePath;
	}
	
	private void readConfig() {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("config.properties");
			prop.load(input);

			_filePath = prop.getProperty("filePath");
			_fileName = prop.getProperty("fileName");

		} catch (IOException e) {
			writeConfig();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void writeConfig() {
		Properties prop = new Properties();
		OutputStream output = null;

		try {
			
			prop.setProperty("fileName", _fileName);
			prop.setProperty("filePath", _filePath);
			
			output = new FileOutputStream("config.properties");
			prop.store(output, null);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	public static void main(String[] args) {
		
	}
	
}
