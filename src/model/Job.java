https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package model;
import java.util.Comparator;

import model.exception.InvalidOfferPriceException;
import model.exception.InvalidPostIdException;
//import model.exception.RepeatReplyException;

public class Job extends Post {
	private double proposedPrice;
	private double lowestOffer;
	
	static private int uniqueId = 3;
	static public String getEventUniqueId() {
		return String.format("JOB%03d", uniqueId ++);
	}
	static public void setPostUniqueId(int uniqueId) {
		Job.uniqueId = uniqueId;
	}
	
	public Job() { }
	
	public Job(String id, String title, String description, String creatorId,double proposedPrice) {
		super(id, title, description, creatorId);
		this.proposedPrice = proposedPrice;
		this.lowestOffer = Double.MAX_VALUE;
	}
	
	public String getPostDetails() {
		String common = super.getPostDetails();
		String s = String.format("Proposed price: $%.2f\n", proposedPrice);
		if(Double.compare(lowestOffer, Double.MAX_VALUE)==0)
			s=s+"Lowest offer:  NO OFFER\n";
		else
			s=s+String.format("Lowest offer:  $%.2f\n", lowestOffer);
		return common+s;
	}
	
	@Override
	public void handleReply(Reply reply) throws Exception {
		if(!reply.getPostId().equals(id))
			throw new InvalidPostIdException();
//		String responderId = reply.getResponderId();
//		for(Reply r: replies) {
//			if(r.getResponderId().equals(responderId))
//				throw new RepeatReplyException();
//		}
		double offer = reply.getValue();
		if(Double.compare(offer, lowestOffer)>=0)
			throw new InvalidOfferPriceException();
		replies.add(reply);
		lowestOffer = offer;
		// update db
		try {
			DBTool.addReply(reply);
			DBTool.updateJob(this);
		}catch(Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
	}

	public String getReplyPromot() {
		String promot = String.format("Name: %s\nProposed price: $%.2f\nLowest offer: $%.2f\n",title,proposedPrice,lowestOffer);
		return promot;
	}
	
	@Override
	public String getReplyDetails() {
		String s = "-- Offer History --\n";
		if(replies.size()==0)
			s=s+"NO OFFER\n";
		else {
			replies.sort(new Comparator<Reply>() {
				@Override
				public int compare(Reply o1, Reply o2) {
					return Double.compare(o1.getValue(),o2.getValue());
				}
				
			});
			for(Reply r:replies)
				s=s+String.format("%s: $%.2f\n",r.getResponderId(),r.getValue());
		}
		return s;
	}
	
	public double getProposedPrice() {
		return proposedPrice;
	}
	
	public void setProposedPrice(double proposedPrice) {
		this.proposedPrice = proposedPrice;
	}
	
	public double getLowestOffer() {
		return lowestOffer;
	}
	
	public void setLowestOffer(double lowestOffer) {
		this.lowestOffer = lowestOffer;
	}
	
}
