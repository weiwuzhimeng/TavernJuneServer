package tc.service;

import tc.po.Forum;
import tc.po.ForumData;

public interface ForumService {

	public void dealUpload(Forum forum) throws Exception;
	
	public ForumData dealPush() throws Exception;
}
