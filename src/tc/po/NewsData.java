package tc.po;

import java.util.ArrayList;

//官方推送json数据源
public class NewsData {
	
	private String message;
	private ArrayList<News> newsList;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ArrayList<News> getNewsList() {
		return newsList;
	}
	public void setNewsList(ArrayList<News> newsList) {
		this.newsList = newsList;
	}
	
	@Override
	public String toString() {
		return "NewsData [message=" + message + ", newsList=" + newsList + "]";
	}
	
}
