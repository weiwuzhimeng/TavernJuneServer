package tc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import tc.service.impl.UserServiceImpl;

//用户的基本操作控制器
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserServiceImpl userServiceImpl;

	// 处理登录
	@RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET },produces = "application/json;charset=utf-8")
	public @ResponseBody String loginUser(String username, String password) throws Exception {
		String str = userServiceImpl.dealLogin(username, password);
		return str;
	}

	// 处理注册
	@RequestMapping(value = "/register", method = { RequestMethod.POST, RequestMethod.GET },produces = "application/json;charset=utf-8")
	public @ResponseBody String registerUser(String username, String password) throws Exception {
		String str = userServiceImpl.dealRegister(username, password);
		return str;
	}

	// 处理图片上传
	@RequestMapping(value = "/picUpload", method = { RequestMethod.POST, RequestMethod.GET },produces = "application/json;charset=utf-8")
	public @ResponseBody String uploadPic(MultipartFile picFile) throws Exception {
		String username = "zjj"; // 此处的用户名应该是动态的，即是从客户端传过来的
		String str = userServiceImpl.dealPicUpload(picFile, username);
		return str;
	}
}
