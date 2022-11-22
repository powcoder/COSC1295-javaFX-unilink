https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package model;

import model.exception.InvalidPostIdException;
import model.exception.NoCapacityException;
import model.exception.RepeatReplyException;

public class Event extends Post {
	private String venue;
	private String date;
	private int capacity;
	private int attendeeCount;
	
//	 name, description, venue, date, capacity.
	public Event() { }
	
	public Event(String id, String title, String description, String creatorId,
			String venue, String date, int capacity) {
		super(id, title, description, creatorId);
		this.venue = venue;
		this.date = date;
		this.capacity = capacity;
		this.attendeeCount = 0;
	}
	
	static private int uniqueId;
	static public String getEventUniqueId() {
		return String.format("EVE%03d", uniqueId ++);
	}
	static public void setPostUniqueId(int uniqueId) {
		Event.uniqueId = uniqueId;
	}
	
	public String getPostDetails() {
		String common = super.getPostDetails();
		String additional = String.format("%-15s%s\n%-15s%s\n%-15s%d\n%-15s%d\n", 
	              "Venue: ",venue,"Date: ",date,"Capacity: ",capacity,"Attendees: ",attendeeCount);
		return common+additional;
	}
	
	public String getReplyPromot() {
		
		String promot = String.format("Name: %s\nVenue: %s\nDate: %s\n",title,venue,date);
		return promot;
	}
	
	

	@Override
	public void handleReply(Reply reply) throws Exception {
		if(!reply.getPostId().equals(id))
			throw new InvalidPostIdException();
		if(attendeeCount>=capacity)
			throw new NoCapacityException();
		String responderId = reply.getResponderId();
		for(Reply r: replies) {
			if(r.getResponderId().equals(responderId))
				throw new RepeatReplyException();
		}
		replies.add(reply);
		attendeeCount++;
		if(attendeeCount==capacity)
			status = CLOSED;
		// update db
		try {
			DBTool.addReply(reply);
			DBTool.updateEvent(this);
		}catch(Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public String getReplyDetails() {
		if(replies.size()==0)
			return "Attendee list: Empty";
		String s = "Attendee list: ";
		s=s+replies.get(0).getResponderId();
		for(int i = 1;i<replies.size();i++)
			s=s+","+replies.get(i).getResponderId();
		return s;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getAttendeeCount() {
		return attendeeCount;
	}

	public void setAttendeeCount(int attendeeCount) {
		this.attendeeCount = attendeeCount;
	}
	
	

}
