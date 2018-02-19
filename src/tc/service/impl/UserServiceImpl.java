package tc.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tc.mapper.InfoMapper;
import tc.mapper.UserMapper;
import tc.po.Info;
import tc.po.User;
import tc.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	//日志
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private InfoMapper infoMapper;
	
	// 返回信息
	private String str = "";

	// 处理用户登录
	@Override
	public String dealLogin(String username, String password) throws Exception {
		// 判断用户名或密码是否正确(需要前端app帮助校验：用户名和密码不能为空)
		String databasePas = userMapper.selectPasByName(username);
		if (password.equals(databasePas)) {
			str = "登录成功";
		} else {
			str = "用户名或密码错误";
		}
		logger.info(str+"，稍后将此str返回给客户端**********************************************************");
		return str;
	}

	// 处理用户注册
	@Override
	public String dealRegister(String username, String password) throws Exception {
		String databasePas = userMapper.selectPasByName(username);

		if (databasePas == null) { // 注：查询结果是null，而不是""(因为是没有密码，而不是密码是'')
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			userMapper.insertUser(user);
			str = "注册成功";
			logger.info(str + username);
		} else {
			str = "该用户已被注册";
			logger.info(str + username);
		}
		return str;
	}
	
	//处理上传图片
	@Override
	public String dealPicUpload(MultipartFile picFile,String username) throws Exception {
		//原始名称
		String originalFilename = picFile.getOriginalFilename();
		//上传图片
		if (picFile != null && originalFilename != null && originalFilename.length() > 0) {
			// 存储图片的物理路径,需要转义(注：服务器没有F盘，应该改为C盘)
			String pic_path = "F:\\EclipseWeb\\picLinshi\\";
			// 新的图片名称
			String picture = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
			// 新图片
			File newFile = new File(pic_path + picture);
			// 将内存中的数据写入磁盘
			picFile.transferTo(newFile);
			// 修改数据库中相应的图片数据
			userMapper.alertPicByName(picture,username);
			
			//注：此处应该校验，即向数据库中查询该用户的图片名称(修改后)是否是newFileName
			str = "上传图片成功";
			logger.info(str);
			return str;
		}else{
			str = "图片名称不合法或图片不存在";
			logger.info(str);
			return str;
		}
	}

	//请求好友列表
	@Override
	public ArrayList<String> dealFriendsList(String username) throws Exception {
		int id = userMapper.selectIdByName(username);
		List<Integer> friendsList = userMapper.selectRelateById(id);
		ArrayList<String> namesList = new ArrayList<String>();
		if(friendsList.size()==0){
			logger.info("该用户无好友");
		}else{
			for (Integer friendId: friendsList) {
				String name = userMapper.selectNameById(friendId);
				namesList.add(name);
			}
			logger.info("它的好友集合是："+namesList);
		}
		return namesList;
	}


	//添加好友
	@Override
	public String dealAddFriend(String adder, String accepter) throws Exception {
		String str = null; //返回信息
		int id1 = userMapper.selectIdByName(adder);
		int id2 = userMapper.selectIdByName(accepter);
		List<Integer> adderIds = userMapper.selectRelateById(id1);
		if(adderIds==null || adderIds.size()==0){
			userMapper.insertRelate(id1, id2);
			userMapper.insertRelate(id2, id1);
			str="添加成功";
			Info info = new Info();
			info.setUserId(id2); 
			info.setAccepter(accepter);
			info.setInformer(adder);
			info.setTitle("添加好友");
			info.setMessage("["+adder+"]"+"请求添加,"+"["+accepter+"]"+"为好友");
			infoMapper.insertInfo(info);
		}else{
			if(adderIds.contains(id2)){
				str="添加失败，二者已经是好友";
			}else{
				userMapper.insertRelate(id1, id2);
				userMapper.insertRelate(id2, id1);
				str="添加成功";
				Info info = new Info();
				info.setUserId(id2); 
				info.setAccepter(accepter);
				info.setInformer(adder);
				info.setTitle("添加好友");
				info.setMessage("["+adder+"]"+" 请求添加 "+"["+accepter+"]"+" 为好友");
				infoMapper.insertInfo(info);
			}
		}
		logger.info(str);
		return str;
	}

	@Override
	public ArrayList<String> dealSearchUser(String username) throws Exception {
		List<String> usersList = userMapper.selectUserByName(username);
		return (ArrayList<String>) usersList;
	}
	
}
