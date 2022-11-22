https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.UniLinkGUI;

// global static information
public class UniLinkSession {
	public static String username;
	public static Stage curStage;
	
	public static void switchWindow(String fxmlpath, String title) {
		UniLinkSession.curStage.close();
    	UniLinkSession.curStage = new Stage();
		FXMLLoader loader = new FXMLLoader(UniLinkGUI.class.getResource(fxmlpath));
		UniLinkSession.curStage.setTitle(title);
		try {
			UniLinkSession.curStage.setScene(new Scene(loader.load()));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		UniLinkSession.curStage.show();
	}
	
	public static void clear() {
		username = "";
	}
}
