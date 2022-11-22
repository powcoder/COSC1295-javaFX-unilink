https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package model;
import java.util.Comparator;

import model.exception.InvalidOfferPriceException;
import model.exception.InvalidPostIdException;
//import model.exception.RepeatReplyException;

public class Sale extends Post {
	private double askingPrice;
	private double highestOffer;
	private double minimumRaise;
	
	static private int uniqueId = 3;
	static public String getEventUniqueId() {
		return String.format("SAL%03d", uniqueId ++);
	}
	static public void setPostUniqueId(int uniqueId) {
		Sale.uniqueId = uniqueId;
	}
	
	public Sale() { }
	
	//name, description, asking price and minimum raise.
	public Sale(String id, String title, String description, String creatorId,
			double askingPrice, double minimumRaise) {
		super(id, title, description, creatorId);
		this.askingPrice = askingPrice;
		this.highestOffer = 0.0;
		this.minimumRaise = minimumRaise;
	}
	
	public String getPostDetails() {
		String common = super.getPostDetails();
		String s = String.format("Minimum raise: $%.2f\n", minimumRaise);
		if(Double.compare(highestOffer, 0.0)==0)
			s=s+"Highest offer:  NO OFFER\n";
		else
			s=s+String.format("Highest offer: $%.2f\n", highestOffer);
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
		if(Double.compare(offer, highestOffer)<=0)
			throw new InvalidOfferPriceException();
		if(Double.compare(offer-highestOffer, minimumRaise)<=0)
			throw new InvalidOfferPriceException();
		replies.add(reply);
		if(Double.compare(offer, askingPrice)>=0)
			status = CLOSED;
		highestOffer = offer;
		// update DB
		try {
			DBTool.addReply(reply);
			DBTool.updateSale(this);
		}catch(Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
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
					return Double.compare(o2.getValue(),o1.getValue());
				}
				
			});
			for(Reply r:replies)
				s=s+String.format("%s: $%.2f\n",r.getResponderId(),r.getValue());
		}
		return s;
	}
	
	@Override
	public String getReplyPromot() {
		String promot = String.format("Name: %s\nAskig price: $%.2f\nHighest offer: $%.2f\nMinimum raise: $%.2f\n",title,askingPrice,highestOffer,minimumRaise);
		return promot;
	}
	
	public double getAskingPrice() {
		return askingPrice;
	}
	
	public void setAskingPrice(double askingPrice) {
		this.askingPrice = askingPrice;
	}
	
	public double getHighestOffer() {
		return highestOffer;
	}
	
	public void setHighestOffer(double highestOffer) {
		this.highestOffer = highestOffer;
	}
	
	public double getMinimumRaise() {
		return minimumRaise;
	}
	
	public void setMinimumRaise(double minimumRaise) {
		this.minimumRaise = minimumRaise;
	}

}
