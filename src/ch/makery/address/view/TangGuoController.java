package ch.makery.address.view;

import javafx.scene.control.TextField;
import main.Logic;

import javafx.scene.control.TextArea;

import ch.makery.address.MainApp;
import javafx.fxml.FXML;

public class TangGuoController {
	private MainApp mainApp;
	private Logic logic;
	
	@FXML
	private TextField commandLine;
	
	@FXML
	private TextArea display;
	
	public TangGuoController() {
		logic = new Logic("tryUI");
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	private void initialize(){
		commandLine.setOnAction((event)->{
			String userInput = commandLine.getText();
			System.out.println(userInput);
			String output = logic.executeInputs(userInput);
			System.out.println(output);
			display.setText(output);
			commandLine.setText("");
		});
	}
	
}
