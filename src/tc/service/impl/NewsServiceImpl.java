package tc.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tc.mapper.NewsMapper;
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
	public NewsData dealPush() throws Exception {
		News news = newsMapper.selectNewsById(1);
		NewsData newsData = new NewsData();
		newsList.add(news);
		newsData.setMessage("这里是传输json的一些通知");
		newsData.setNewsList(newsList);
		return newsData;
	}

}
