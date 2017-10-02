package tc.po;

import java.util.Date;

//官方推送的每个条目的实体类
public class News {
	
	private int id;
	private String title;
	private String description;
	private String pic;
	private Date date;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "Item [id=" + id + ", title=" + title + ", description=" + description + ", pic=" + pic + ", date="
				+ date + "]";
	}

	
}
