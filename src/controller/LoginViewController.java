https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;


public class LoginViewController {


    @FXML
    private TextField input_username;


    @FXML
    void initialize() {
        
    }
    
    @FXML
    void handleBtnLoginClickEvent() {
    	// check user_name invalid
    	String username = input_username.getText();
    	if(username == null || username.equals("")) {
    		Alert alert = new Alert(AlertType.ERROR, "Username is required", ButtonType.OK);
    		alert.showAndWait();
    		return ;
    	}
    	// first char is not 's'
    	if(username.charAt(0) != 's') {
    		Alert alert = new Alert(AlertType.ERROR, "Invalid Username", ButtonType.OK);
    		alert.showAndWait();
    		return ;
    	}
    	// the substring from 1 is not number
    	try {
    		Integer.parseInt(username.substring(1));
    	}catch(NumberFormatException ex) {
    		Alert alert = new Alert(AlertType.ERROR, "Invalid Username", ButtonType.OK);
    		alert.showAndWait();
    		return ;
    	}
    	
    	UniLinkSession.username = username;
    	UniLinkSession.switchWindow("/view/main_view.fxml", "Main");
    }
    

}

