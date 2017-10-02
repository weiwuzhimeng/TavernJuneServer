package tc.po;

import java.util.ArrayList;

//玩家论坛数据源
public class ForumData {

	private String message;
	private ArrayList<Forum> forumList;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ArrayList<Forum> getForumList() {
		return forumList;
	}
	public void setForumList(ArrayList<Forum> forumList) {
		this.forumList = forumList;
	}
	
	@Override
	public String toString() {
		return "ForumData [message=" + message + ", forumList=" + forumList + "]";
	}
	
}
