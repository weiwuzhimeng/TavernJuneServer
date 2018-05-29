package tc.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tc.mapper.NewsMapper;
import tc.po.Info;
import tc.po.News;
import tc.po.NewsData;
import tc.service.NewsService;

//官方推送业务处理
@Service
public class NewsServiceImpl implements NewsService{
	
	@Autowired
	private NewsMapper newsMapper;
	
	private ArrayList<News> newsList = new ArrayList<News>();

	@Override
	public JSONObject dealPush() throws Exception {
		//---------------------------------------------------------
		ArrayList<News> list = (ArrayList<News>) newsMapper.selectNews();
		
		//总json
		JSONObject j = new JSONObject();
		j.put("title", "新闻资讯"); //添加总json的属性1
		
		JSONArray newsList = new JSONArray();
		
		for(int i=0;i<list.size();i++){
			News news = list.get(i);
			JSONObject in = new JSONObject();
			in.put("id", news.getId());
			in.put("title", news.getTitle());
			in.put("description", news.getDescription());
			in.put("pic", news.getPic());
			in.put("link", news.getLink());
			newsList.add(i, in);
		}
		
		j.put("newsList", newsList); //添加总json的属性2
		
		return j;
	}

}
