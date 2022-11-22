https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package main;

import controller.UniLinkSession;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DBTool;

public class UniLinkGUI extends Application {
	
	public static void main(String[] args) {
		DBTool.initialize();
		Application.launch(UniLinkGUI.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		UniLinkSession.curStage = stage;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login_view.fxml"));
		stage.setTitle("Login");
		stage.setScene(new Scene(loader.load()));
		stage.show();
	}

}
