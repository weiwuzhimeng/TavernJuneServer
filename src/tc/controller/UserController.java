package tc.controller;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;
import tc.po.InfoData;
import tc.po.MessageData;
import tc.service.impl.UserServiceImpl;
import tc.util.JsonData;

//用户的基本操作控制器
@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UserServiceImpl userServiceImpl;

	// 临时测试
	@RequestMapping(value = "/postTest", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String postTest(String username,String password) throws Exception {
		logger.info("@@@测试用户名：" + username+password);
		return "测试成功";
	}

	// 处理登录
	@RequestMapping(value = "/login", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String loginUser(String username, String password) throws Exception {
		logger.info("@@@登陆者用户名：" + username);
		String str = userServiceImpl.dealLogin(username, password);
		return str;
	}

	// 处理注册
	@RequestMapping(value = "/register", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String registerUser(String username, String password) throws Exception {
		String str = userServiceImpl.dealRegister(username, password);
		return str;
	}

	// 处理请求好友列表
	@RequestMapping(value = "/friendsList", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody JSONObject getFriendsList(String username) throws Exception {
		logger.info("@@@请求好友列表的用户名：" + username);
		JSONObject data = userServiceImpl.dealFriendsList(username);
		return data;
	}

	// 处理搜索用户
	@RequestMapping(value = "/searchUser", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody JSONObject searchUser(String username) throws Exception {
		logger.info("@@@请求搜索好友的用户名：" + username);
		JSONObject jsonData = userServiceImpl.dealSearchUser(username);
		return jsonData;
	}

	// 处理请求添加好友
	@RequestMapping(value = "/addFriend", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String addFriend(String adder, String accepter) throws Exception {
		logger.info("@@@请求添加好友的用户名：" + accepter);
		String msg = userServiceImpl.dealAddFriend(adder, accepter);
		return msg;
	}

	// 处理图片上传
	@RequestMapping(value = "/picUpload", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String uploadPic(MultipartFile picFile) throws Exception {
		String username = "zjj"; // 此处的用户名应该是动态的，即是从客户端传过来的
		String str = userServiceImpl.dealPicUpload(picFile, username);
		return str;
	}

//	// 处理询问通知消息
//	@RequestMapping(value = "/askMsg", method = { RequestMethod.POST,
//			RequestMethod.GET }, produces = "application/json;charset=utf-8")
//	public @ResponseBody JSONObject askMsg(String username) throws Exception {
//		logger.info("@@@请求查询消息的用户名：" + username);
//		JSONObject j = userServiceImpl.dealAskMsg(username);
//		return j;
//	}

	// 处理询问用户聊天消息，用户：asker询问他的好友friend发来的消息
	@RequestMapping(value = "/askChat", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody JSONObject askChat(String asker,String friend) throws Exception {
		//asker(accepter)：是询问消息者    friend(informer)：是给asker发消息这
		logger.info("@@@请求聊天消息的用户名：" + asker);
		JSONObject j = userServiceImpl.dealAskChat(asker,friend);
		return j;
	}

	// 主动发送聊天内容
	@RequestMapping(value = "/chat", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody boolean chat(String str){
		logger.info("@@@聊天消息：" + str);
		return userServiceImpl.dealChat(str);
	}

	// 同意添加好友
	
}
