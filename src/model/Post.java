https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package model;
import java.util.List;

import java.util.ArrayList;

public abstract class Post {
	transient public static final boolean OPEN = true; // no need serialized
	transient public static final boolean CLOSED = false; // no need serialized 
	
	protected String id;
	protected String title;
	protected String description;
	protected String creatorId;
	protected boolean status;
	protected String image;
	transient protected List<Reply> replies; // no need serialized, already serialized from db
	
	public Post() { }
	
	public Post(String id, String title, String description, String creatorId) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.creatorId = creatorId;
		this.status = OPEN;
		this.replies = new ArrayList<>();
		this.image = "No_image_available.jpg";
	}
	
//	ID: EVE001
//	Title: Movie on Sunday
//	Description: Join us to see a movie this
//	 Sunday with ticket
//	discounts applied to all
//	 group members!
//	Creator ID: s3456223
//	Status: OPEN
	public String getPostDetails() {
		String statusString = "OPEN";
		if(status==CLOSED)
			statusString = "CLOSED";
		return String.format("%-15s%s\n%-15s%s\n%-15s%s\n%-15s%s\n%-15s%s\n", 
				              "ID: ",id,"Title: ",title,"Description: ",description,"Creator ID: ",creatorId,"Status: ",statusString);
	}
	
	public String getCreatorId() {
		return creatorId;
	}
	
	public String getId() {
		return id;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Reply> getReplies() {
		return replies;
	}

	public void setReplies(List<Reply> replies) {
		this.replies = replies;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public abstract void handleReply(Reply reply) throws Exception;
	
	public abstract String getReplyDetails();
	public abstract String getReplyPromot();

	
	
	
}

	
