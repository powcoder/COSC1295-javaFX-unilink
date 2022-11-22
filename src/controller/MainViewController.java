https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.Alert.AlertType;
import main.UniLinkGUI;
import model.DBTool;
import model.Event;
import model.Job;
import model.Post;
import model.Sale;

public class MainViewController {
	
	@FXML
    private ComboBox<String> comboBoxCreator;

    @FXML
    private ComboBox<String> comboBoxStatus;

    @FXML
    private ComboBox<String> comboBoxType;
    
    @FXML
    private Label labelUserId;

    @FXML
    void handleComboBoxCreatorChangeEvent(ActionEvent event) {
    	this.loadDBData();
    	this.loadPostDataToListView();
    }

    @FXML
    void handleComboBoxStatusChangeEvent(ActionEvent event) {
    	this.loadDBData();
    	this.loadPostDataToListView();
    }

    @FXML
    void handleComboBoxTypeChangeEvent(ActionEvent event) {
    	this.loadDBData();
    	this.loadPostDataToListView();
    }
    
    private void showPopUpWindow(String fxmlpath, String title) {
    	FXMLLoader loader = new FXMLLoader(UniLinkGUI.class.getResource(fxmlpath));
		try {
			Parent parent = loader.load();
	    	Stage stage = new Stage();
	    	if(title.contains("Event")) {
	    		PostAddEventViewController controller = (PostAddEventViewController) loader.getController();
	    		controller.setStage(stage);
	    	}else if(title.contains("Job")) {
	    		PostAddJobViewController controller = (PostAddJobViewController) loader.getController();
	    		controller.setStage(stage);
	    	}else if(title.contains("Sale")) {
	    		PostAddSaleViewController controller = (PostAddSaleViewController) loader.getController();
	    		controller.setStage(stage);
	    	}
	    	stage.setTitle(title);
	    	stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(parent));
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		this.loadDBData();
		this.loadPostDataToListView();
    }
    
    @FXML
    void handleNewEventPostEvent(ActionEvent event) {
    	this.showPopUpWindow("/view/postAddEvent_view.fxml", "Add Event");
    }

    @FXML
    void handleNewJobPostEvent(ActionEvent event) {
    	this.showPopUpWindow("/view/postAddJob_view.fxml", "Add Job");
    }

    @FXML
    void handleNewSalePostEvent(ActionEvent event) {
    	this.showPopUpWindow("/view/postAddSale_view.fxml", "Add Sale");
    }
    
    @FXML
    void handleDataExportEvent(ActionEvent event) {
    }

    @FXML
    void handleDataImportEvent(ActionEvent event) {
    }

    @FXML
    void handleDeveloperInforShowEvent(ActionEvent event) {
    	Alert alert = new Alert(AlertType.INFORMATION, "DeveloperInfor: xxx(student name),xxx(student number)", ButtonType.OK);
		alert.showAndWait();
    }

    @FXML
    void handleQuitEvent(ActionEvent event) {
    	UniLinkSession.curStage.close();
    }
    
    @FXML
    void handleLogoutEvent(ActionEvent event) {
    	UniLinkSession.clear();
    	UniLinkSession.switchWindow("/view/login_view.fxml", "login");
    }
    
    private void initlaizeComboBox() {
    	this.comboBoxType.getItems().clear();
    	this.comboBoxType.getItems().add("All");
    	this.comboBoxType.getItems().add("Event");
    	this.comboBoxType.getItems().add("Sale");
    	this.comboBoxType.getItems().add("Job");
    	this.comboBoxType.getSelectionModel().selectFirst();
    	this.comboBoxType.setVisibleRowCount(4);
    	
    	this.comboBoxCreator.getItems().clear();
    	this.comboBoxCreator.getItems().add("All");
    	this.comboBoxCreator.getItems().add("My Post");
    	this.comboBoxCreator.getSelectionModel().selectFirst();
    	this.comboBoxCreator.setVisibleRowCount(2);
    	
    	this.comboBoxStatus.getItems().clear();
    	this.comboBoxStatus.getItems().add("All");
    	this.comboBoxStatus.getItems().add("Open");
    	this.comboBoxStatus.getItems().add("Closed");
    	this.comboBoxStatus.getSelectionModel().selectFirst();
    	this.comboBoxStatus.setVisibleRowCount(3);
    }
    
    @FXML
    private ListView<Post> postListView;
    
    private ObservableList<Post> observableList = FXCollections.observableArrayList();
    private void loadPostDataToListView() {
    	observableList.setAll(this.postData);
    	this.postListView.setItems(observableList);
    	this.postListView.setCellFactory(new Callback<ListView<Post>, ListCell<Post>>(){

			@Override
			public ListCell<Post> call(ListView<Post> param) {
				// TODO Auto-generated method stub
				return new PostListCell();
			}
    		
    	});
    }
    
    private List<Post> postData;
    private void loadDBData() {
    	postData = new LinkedList<>();
    	try {
    		// fitered by Creator/Status/Type
    		String type = this.comboBoxType.getSelectionModel().getSelectedItem();
    		String status = this.comboBoxStatus.getSelectionModel().getSelectedItem();
    		String creator = this.comboBoxCreator.getSelectionModel().getSelectedItem();
    		Boolean fstatus = null;
    		String fcreatorId = null;
    		if(status.equals("Open")) {
    			fstatus = true;
    		}else if(status.equals("Closed")) {
    			fstatus = false;
    		}
    		if(!creator.equals("All")) {
    			fcreatorId = UniLinkSession.username;
    		}
//    		System.out.println("Status:"+ fstatus + " creatorId:" + fcreatorId);
    		if(type.equals("All")) {
    			List<Event> events = DBTool.getAllEventFilterd(fstatus, fcreatorId);
    			List<Sale> sales = DBTool.getAllSaleFilterd(fstatus, fcreatorId);
    			List<Job> jobs = DBTool.getAllJobFilterd(fstatus, fcreatorId);
    			this.postData.addAll(events);
    			this.postData.addAll(sales);
    			this.postData.addAll(jobs);
    		}else if(type.equals("Event")) {
    			List<Event> events = DBTool.getAllEventFilterd(fstatus, fcreatorId);
    			this.postData.addAll(events);
    		}else if(type.equals("Sale")) {
    			List<Sale> sales = DBTool.getAllSaleFilterd(fstatus, fcreatorId);
    			this.postData.addAll(sales);
    		}else if(type.equals("Job")) {
    			List<Job> jobs = DBTool.getAllJobFilterd(fstatus, fcreatorId);
    			this.postData.addAll(jobs);
    		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
    }
    
    @FXML
    void initialize() {
    	// Initialize CobomBox Value
    	initlaizeComboBox();
    	// Initialize user label shown
    	this.labelUserId.setText("UserId: " + UniLinkSession.username);
    	// Load DB Data
    	try {
			Event.setPostUniqueId(DBTool.getEntryNum("EVENT") + 1);
			Sale.setPostUniqueId(DBTool.getEntryNum("SALE") + 1);
			Job.setPostUniqueId(DBTool.getEntryNum("JOB") + 1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
    	this.loadDBData();
    	// add post into listview
    	this.loadPostDataToListView();
    }
}