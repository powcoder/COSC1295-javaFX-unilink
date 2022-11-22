https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.DBTool;
import model.Event;
import javafx.scene.control.Alert.AlertType;


public class PostAddEventViewController {

    @FXML
    private TextField capacity;

    @FXML
    private TextField date;

    @FXML
    private TextField description;

    @FXML
    private TextField title;

    @FXML
    private TextField venue;
    
    private String image = "No_image_available.jpg";
    
    private Stage stage;
    public void setStage(Stage stage) {
		this.stage = stage;
	}

    @FXML
    void handleAddEvent(ActionEvent event) {
    	String t_title, t_description, t_venue, t_date;
    	int t_capacity;
    	try {
    		t_title = title.getText();
        	t_description = description.getText();
        	
        	t_venue = venue.getText();
        	t_date = date.getText();
        	t_capacity = Integer.parseInt(capacity.getText());
        	if(t_title.length() == 0 || t_description.length() == 0 || t_venue.length() == 0 || t_date.length() == 0) {
        		Alert alert = new Alert(AlertType.ERROR, "Error Input", ButtonType.OK);
        		alert.showAndWait();
        		return ;
        	}
    	}catch(Exception e) {
    		Alert alert = new Alert(AlertType.ERROR, "Error Input", ButtonType.OK);
    		alert.showAndWait();
    		return ;
    	}
    	if(!t_date.matches("[0-3][0-9]/[0-1][0-9]/[0-2][0-9][0-9][0-9]")){
    		Alert alert = new Alert(AlertType.ERROR, "Invalid date", ButtonType.OK);
    		alert.showAndWait();
    		return ;
    	}
    	Event mevent = new Event(Event.getEventUniqueId(), t_title, t_description,UniLinkSession.username, t_venue, t_date, t_capacity);
    	mevent.setImage(image);
    	try {
			DBTool.addEvent(mevent);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Alert alert = new Alert(AlertType.CONFIRMATION, "Post Added Successfully", ButtonType.OK);
		alert.showAndWait();
    	stage.close();
    }

    @FXML
    void handleSelectImageEvent(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	FileChooser.ExtensionFilter fex = new FileChooser.ExtensionFilter("JPEG Image(*.jpg)", "*.jpg");
    	fileChooser.getExtensionFilters().add(fex);
    	fex = new FileChooser.ExtensionFilter("PNG Image(*.png)", "*.png");
    	fileChooser.getExtensionFilters().add(fex);
    	File file = fileChooser.showOpenDialog(this.stage);
    	if(file != null) {
    		// move file into image folder
    		String ex = file.getName().substring(file.getName().length() - 3);
    		String filename = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + "." + ex;
    		try {
				Files.copy(file.toPath(), Paths.get("images/" + filename));
				Alert alert = new Alert(AlertType.CONFIRMATION, "Image Selected Successfully", ButtonType.OK);
				alert.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
    		this.image = filename;
    	}
    }


    @FXML
    void initialize() {

    }

}
