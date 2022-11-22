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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.DBTool;
import model.Job;


public class PostAddJobViewController {
	
    @FXML
    private TextField description;

    @FXML
    private TextField proposedPrice;

    @FXML
    private TextField title;
    
    private String image = "No_image_available.jpg";
    
    private Stage stage;
    public void setStage(Stage stage) {
		this.stage = stage;
	}

    @FXML
    void handleAddEvent(ActionEvent event) {
    	String t_description, t_title;
    	double t_proposedPrice;
    	try {
    		t_description = description.getText();
    		t_title = title.getText();
    		t_proposedPrice = Double.parseDouble(proposedPrice.getText());
    		if(t_description.length() == 0 || t_title.length() == 0) {
    			Alert alert = new Alert(AlertType.ERROR, "Error Input", ButtonType.OK);
        		alert.showAndWait();
        		return ;
    		}
    	}catch(Exception e) {
    		Alert alert = new Alert(AlertType.ERROR, "Error Input", ButtonType.OK);
    		alert.showAndWait();
    		return ;
    	}
    	Job job = new Job(Job.getEventUniqueId(), t_title, t_description, UniLinkSession.username, t_proposedPrice);
    	job.setImage(image);;
		try {
			DBTool.addJob(job);
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
