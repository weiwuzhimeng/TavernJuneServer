package tc.service;

import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;

//用户业务类
public interface UserService {

	//处理用户登录
	public String dealLogin(String username,String password) throws Exception;
	
	//处理用户注册
	public String dealRegister(String username,String password) throws Exception;
	
	//处理请求好友列表
	public JSONObject dealFriendsList(String username) throws Exception;
	
	//处理添加好友
	public String dealAddFriend(String adder,String accepter) throws Exception;
	
	//处理搜索用户
	public JSONObject dealSearchUser(String username) throws Exception;
	
	//处理用户文件上传
	public String dealPicUpload(MultipartFile picFile,String username) throws Exception;
	
}
