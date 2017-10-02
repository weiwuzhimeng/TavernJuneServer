package tc.mapper;

import org.springframework.stereotype.Repository;

import tc.po.Forum;

//论坛持久层
@Repository
public interface ForumMapper {
	
	//将用户论坛插入数据库
	public void insertForum(Forum forum) throws Exception;
	
	//查找论坛并返回给用户
	public Forum selectForumById (int id) throws Exception;
	
}
