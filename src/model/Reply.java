https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package model;

public class Reply {
	private String postId;
	private double value;
	private String responderId;
	
	public Reply() { }
	
	public Reply(String postId, double value, String responderId) {
		super();
		this.postId = postId;
		this.value = value;
		this.responderId = responderId;
	}

	/**
	 * @return the postId
	 */
	public String getPostId() {
		return postId;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @return the responderId
	 */
	public String getResponderId() {
		return responderId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void setResponderId(String responderId) {
		this.responderId = responderId;
	}

}
