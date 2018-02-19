package tc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import tc.po.User;

//用户持久层
@Repository
public interface UserMapper {

	//通过用户名查询密码
	public String selectPasByName(String username) throws Exception;
	
	//将用户插入数据库
	public void insertUser(User user) throws Exception;
	
	//修改用户头像通过用户名
	//*当有两个或以上参数时，必须使用@Param参数
	public void alertPicByName(@Param("picture") String picture, @Param("username") String username) throws Exception;
	
	//根据id查询用户名
	public String selectNameById(int id) throws Exception;
	
	//根据用户名查询id
	public int selectIdByName(String username) throws Exception;
	
	//根据id查询用户好友
	public List<Integer> selectRelateById(int localUserId) throws Exception;
	
	//请求添加好友
	public void insertRelate(@Param("id1") int id1, @Param("id2") int id2);
	
	//搜索好友[模糊查询]
	public List<String> selectUserByName(String username);
	
}
