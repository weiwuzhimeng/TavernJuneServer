package tc.po;


//玩家论坛的实体类
public class Forum {
	
	private int id;
	private int userId;
	private String picture;
	private String username;
	private String description;
	private String content;
	private String date;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "Forum [id=" + id + ", userId=" + userId + ", picture=" + picture + ", username=" + username
				+ ", description=" + description + ", content=" + content + ", date=" + date + "]";
	}
	
	
	
}
