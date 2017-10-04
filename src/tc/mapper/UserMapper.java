package tc.mapper;

import org.springframework.stereotype.Repository;

import tc.po.User;

//用户持久层
@Repository
public interface UserMapper {

	//通过用户名查询密码
	public String selectPasByName(String username) throws Exception;
	
	//将用户插入数据库
	public void insertUser(User user) throws Exception;
	
}
