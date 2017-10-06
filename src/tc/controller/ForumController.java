package tc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tc.po.Forum;
import tc.po.ForumData;
import tc.service.impl.ForumServiceImpl;

//论坛控制类
@Controller
@RequestMapping("/forum")
public class ForumController {
	
	@Autowired
	private ForumServiceImpl forumServiceImpl;
	
	//处理上传论坛请求
	@RequestMapping(value="upload",method={RequestMethod.POST,RequestMethod.GET})
	public void uploadForum(@RequestBody Forum forum) throws Exception{
		forumServiceImpl.dealUpload(forum);
	}
	
	//处理论坛下载请求
	@RequestMapping(value="push",method={RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ForumData pushForum() throws Exception{
		ForumData forumData = forumServiceImpl.dealPush();
		return forumData;
	}
}
