package tc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
import tc.service.impl.NewsServiceImpl;

//推送控制类
@Controller
@RequestMapping("/news")
public class NewsController {

	@Autowired
	private NewsServiceImpl newsServiceImpl;
	
	//推送消息
	@RequestMapping(value="/push",method={RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody JSONObject pushNews() throws Exception{
		JSONObject j = newsServiceImpl.dealPush();
		return j;
	}
	
}
