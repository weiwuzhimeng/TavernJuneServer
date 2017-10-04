package tc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tc.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;

	@RequestMapping(value="/login",method={RequestMethod.POST,RequestMethod.GET})
	public String loginUser(String username,String password) throws Exception{
		String str = userServiceImpl.dealLogin(username, password);
		return str;
	}
	
	@RequestMapping(value="/register",method={RequestMethod.POST,RequestMethod.GET})
	public String registerUser(String username,String password) throws Exception{
		String str = userServiceImpl.dealRegister(username, password);
		return str;
	}
}
