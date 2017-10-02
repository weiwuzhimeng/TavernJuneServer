package tc.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tc.mapper.ForumMapper;
import tc.po.Forum;
import tc.po.ForumData;
import tc.service.ForumService;

//玩家论坛业务处理
@Service
public class ForumServiceImpl implements ForumService{

	@Autowired
	private ForumMapper forumMapper;
	
	private ArrayList<Forum> forumList = new ArrayList<Forum>();
	
	@Override
	public void dealUpload(Forum forum) throws Exception {
		forumMapper.insertForum(forum);
	}

	@Override
	public ForumData dealPush() throws Exception {
		Forum forum = forumMapper.selectForumById(1);
		forumList.add(forum);
		ForumData forumData = new ForumData();
		forumData.setMessage("这里是消息头");
		forumData.setForumList(forumList);
		return forumData;
	}

	
}
