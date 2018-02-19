package tc.controller;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import tc.service.impl.UserConnectImpl;
import tc.service.impl.UserServiceImpl;

//用户的基本操作控制器
@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = Logger.getLogger(UserController.class);
	
	@Autowired
	private UserServiceImpl userServiceImpl;

	// 处理登录
	@RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET },produces = "application/json;charset=utf-8")
	public @ResponseBody String loginUser(String username, String password) throws Exception {
		logger.info("@@@登陆者用户名："+username);
		String str = userServiceImpl.dealLogin(username, password);
		return str;
	}

	// 处理注册
	@RequestMapping(value = "/register", method = { RequestMethod.POST, RequestMethod.GET },produces = "application/json;charset=utf-8")
	public @ResponseBody String registerUser(String username, String password) throws Exception {
		String str = userServiceImpl.dealRegister(username, password);
		return str;
	}
	
	//处理请求好友列表
	@RequestMapping(value = "/friendsList",method = {RequestMethod.POST, RequestMethod.GET},produces = "application/json;charset=utf-8")
	public @ResponseBody ArrayList<String> getFriendsList(String username) throws Exception{
		logger.info("@@@请求好友列表的用户名："+username);
		ArrayList<String> friendsList = userServiceImpl.dealFriendsList(username);
		return friendsList;
	}
	
	//处理搜索用户[模糊查询]
	@RequestMapping(value = "/searchUser",method = {RequestMethod.POST, RequestMethod.GET},produces = "application/json;charset=utf-8")
	public @ResponseBody ArrayList<String> searchUser(String username) throws Exception{
		logger.info("@@@请求搜索好友的用户名："+username);
		ArrayList<String> usersList = userServiceImpl.dealSearchUser(username);
		return usersList;
	}
		
	// 处理图片上传
	@RequestMapping(value = "/picUpload", method = { RequestMethod.POST, RequestMethod.GET },produces = "application/json;charset=utf-8")
	public @ResponseBody String uploadPic(MultipartFile picFile) throws Exception {
		String username = "zjj"; // 此处的用户名应该是动态的，即是从客户端传过来的
		String str = userServiceImpl.dealPicUpload(picFile, username);
		return str;
	}
}
