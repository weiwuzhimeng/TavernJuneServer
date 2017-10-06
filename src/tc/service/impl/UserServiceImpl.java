package tc.service.impl;

import java.io.File;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tc.mapper.UserMapper;
import tc.po.User;
import tc.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	//日志
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private UserMapper userMapper;
	// 返回信息
	private String str = "";

	// 处理用户登录
	@Override
	public String dealLogin(String username, String password) throws Exception {
		// 判断用户名或密码是否正确(需要前端app帮助校验：用户名和密码不能为空)
		String databasePas = userMapper.selectPasByName(username);
		if (password.equals(databasePas)) {
			str = "登陆成功";
		} else {
			str = "用户名或密码错误";
		}
		logger.info(str);
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
			// 存储图片的物理路径,需要转义
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

}
