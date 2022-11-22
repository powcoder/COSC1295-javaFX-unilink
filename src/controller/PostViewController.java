https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.UniLinkGUI;
import model.Event;
import model.Job;
import model.Post;
import model.Reply;
import model.Sale;
import model.exception.InvalidUserInputException;


public class PostViewController {

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Label label3;

    @FXML
    private Label label4;

    @FXML
    HBox postHBox;

    @FXML
    private ImageView postImageView;

    @FXML
    private Label statusLabel;

    @FXML
    private Label titleLabel;
    
    @FXML
    private Label descriptionLabel;
    
    @FXML
    private Button btnReply;
    
    @FXML
    private Button btnDetails;
    
    public PostViewController() {
    	FXMLLoader loader = new FXMLLoader(UniLinkGUI.class.getResource("/view/post_view.fxml"));
    	loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
    }
    
    private Post post;
    void setupPostView(Post post) {
    	this.post = post;
    	this.titleLabel.setText("title:" + post.getTitle());
    	String str_status = post.getStatus() ? "Open" : "Closed";
    	this.statusLabel.setText("status:" + str_status);
    	this.descriptionLabel.setText("description:" + post.getDescription());
    	btnReply.setVisible(false);
    	if(post.getStatus() && !UniLinkSession.username.equals(post.getCreatorId())) {
    		btnReply.setVisible(true);
    	}
    	btnDetails.setVisible(false);
    	if(UniLinkSession.username.equals(post.getCreatorId())){
    		btnDetails.setVisible(true);
    	}
    	// set custom label for different POST type
    	String color = "white";
    	this.label1.setText("");
    	this.label2.setText("");
    	this.label3.setText("");
    	this.label4.setText("");
    	if(post instanceof Event) {
    		Event event = (Event) post;
    		this.label1.setText("venue:" + event.getVenue());
    		this.label2.setText("date:" + event.getDate());
    		this.label3.setText("capacity:" + event.getCapacity());
    		this.label4.setText("attendeeCount:" + event.getAttendeeCount());
    		color = "darkcyan";
    	}else if(post instanceof Job) {
    		Job job = (Job) post;
    		this.label1.setText("proposedPrice:$" + job.getProposedPrice());
    		if(Double.compare(job.getLowestOffer(), Double.MAX_VALUE) == 0) {
    			this.label2.setText("lowestOffer: NO OFFER");
    		}else {
    			this.label2.setText("lowestOffer:$" + job.getLowestOffer());
    		}
    		color = "lightblue";
    	}else if(post instanceof Sale) {
    		Sale sale = (Sale) post;
    		this.label1.setText("highestOffer:$" + sale.getHighestOffer());
    		this.label2.setText("minimumRaise:$" + sale.getMinimumRaise());
    		if(post.getCreatorId().equals(UniLinkSession.username)) {
    			this.label3.setText("askingPrice:$" + sale.getAskingPrice());
    		}
    		color = "pink";
    	}
    	this.postHBox.setStyle("-fx-background-color: " + color + ";");
    	File imageFile = new File("images/" + post.getImage());
    	Image image = new Image(imageFile.toURI().toString());
//    	System.out.println(imageFile.getAbsolutePath());
    	this.postImageView.setImage(image);
    }

    @FXML
    void handleMoreDetailsEvent(ActionEvent event) {
    	UniLinkSession.curStage.close();
    	UniLinkSession.curStage = new Stage();
		FXMLLoader loader = new FXMLLoader(UniLinkGUI.class.getResource("/view/post_details_view.fxml"));
		UniLinkSession.curStage.setTitle("Post Details");
		try {
			UniLinkSession.curStage.setScene(new Scene(loader.load()));
			PostDetailsViewController pvc = loader.getController();
			pvc.setUpView(post);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		UniLinkSession.curStage.show();
    }

    @FXML
    void handleReplyEvent(ActionEvent event) {
    	try {
    		if(post instanceof Event) {
        		Event event1 = (Event) post;
        		Reply reply = new Reply(post.getId(), 1.0, UniLinkSession.username);
        		event1.handleReply(reply);
        		this.label4.setText("attendeeCount:" + event1.getAttendeeCount());
        		Alert alert = new Alert(AlertType.CONFIRMATION, "Reply Successfully", ButtonType.OK);
        		alert.showAndWait();
        	}else {
        		TextInputDialog dialog = new TextInputDialog();
        		dialog.setContentText("Input your offer");
        		Optional<String> input = dialog.showAndWait();
        		if(input.isPresent()) {
        			double offer = 0;
        			try {
        				offer = Double.parseDouble(input.get());
        			}catch(Exception e) {
        				throw new InvalidUserInputException();
        			}
        			Reply reply = new Reply(post.getId(), offer, UniLinkSession.username);
        			boolean salePostClose = false;
    	    		if(post instanceof Sale) {
    	    			Sale sale = (Sale) post;
    	    			sale.handleReply(reply);
    	    			this.label1.setText("highestOffer:$" + sale.getHighestOffer());
    	    			if(!post.getStatus()) {
    	    				salePostClose = true;
    	    			}
    	    		}else {
    	    			Job job = (Job) post;
    	    			job.handleReply(reply);
    	    			this.label2.setText("lowestOffer:$" + job.getLowestOffer());
    	    		}
    	    		if(salePostClose) {
    	    			if(!post.getStatus()) {
    	    	    		btnReply.setVisible(false);
    	    	    	}
    	    			Alert alert = new Alert(AlertType.CONFIRMATION, "Congratulation!! You own this product now, please contact " + post.getCreatorId()  + " for more details.", ButtonType.OK);
        	    		alert.showAndWait();
    	    		}else {
    	    			Alert alert = new Alert(AlertType.CONFIRMATION, "Offer Accepted", ButtonType.OK);
        	    		alert.showAndWait();
    	    		}
        		}
        	}
    	}catch(Exception ex) {
    		Alert alert = new Alert(AlertType.ERROR, ex.getMessage(), ButtonType.OK);
    		alert.showAndWait();
    	}
    }

    @FXML
    void initialize() {

    }

}
