package tc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tc.mapper.UserMapper;
import tc.po.User;
import tc.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;
	//返回信息
	private String str = "";
	

	//处理用户登录
	@Override
	public String dealLogin(String username, String password) throws Exception {
		//判断用户名或密码是否正确(需要前端app帮助校验：用户名和密码不能为空)
		String databasePas = userMapper.selectPasByName(username);
		if(password.equals(databasePas)){
			str = "登陆成功";
		}else{
			str = "用户名或密码错误";
		}
		System.out.println(str);
		return str;
	}

	//处理用户注册
	@Override
	public String dealRegister(String username, String password) throws Exception {
		String databasePas = userMapper.selectPasByName(username);
		
		if(databasePas==null){ //注：查询结果是null，而不是""(因为是没有密码，而不是密码是'')
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			userMapper.insertUser(user);
			str = "注册成功";
			System.out.println(str+username);
		}else{
			str = "该用户已被注册";
			System.out.println(str+username);
		}
		return str;
	}
	
	
}
