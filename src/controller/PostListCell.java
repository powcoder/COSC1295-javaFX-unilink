https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package controller;

import javafx.scene.control.ListCell;
import model.Post;

public final class PostListCell extends ListCell<Post> {
	
	private final PostViewController pvc = new PostViewController();
	
	@Override
	protected void updateItem(Post item, boolean empty) {
		// increase ListView performance with ScrollPane
		super.updateItem(item, empty);
		if(empty) {
			setGraphic(null);
		}else {
			pvc.setupPostView(item);
			setGraphic(pvc.postHBox);
		}
	}

}
