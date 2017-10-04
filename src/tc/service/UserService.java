package tc.service;

//用户业务类
public interface UserService {

	//处理用户登录
	public String dealLogin(String username,String password) throws Exception;
	
	//处理用户注册
	public String dealRegister(String username,String password) throws Exception;
	
}
