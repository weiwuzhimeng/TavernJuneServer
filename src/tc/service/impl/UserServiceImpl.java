package tc.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tc.mapper.InfoMapper;
import tc.mapper.MessageMapper;
import tc.mapper.UserMapper;
import tc.po.Info;
import tc.po.InfoData;
import tc.po.Message;
import tc.po.MessageData;
import tc.po.User;
import tc.service.UserService;
import tc.util.EmojiUtil;
import tc.util.JsonData;

@Service
public class UserServiceImpl implements UserService {
	
	// 日志
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private InfoMapper infoMapper;
	
	@Autowired
	private MessageMapper messageMapper;

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
		logger.info(str + "，稍后将此str返回给客户端**********************************************************");
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

	// 处理上传图片
	@Override
	public String dealPicUpload(MultipartFile picFile, String username) throws Exception {
		// 原始名称
		String originalFilename = picFile.getOriginalFilename();
		// 上传图片
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
			userMapper.alertPicByName(picture, username);

			// 注：此处应该校验，即向数据库中查询该用户的图片名称(修改后)是否是newFileName
			str = "上传图片成功";
			logger.info(str);
			return str;
		} else {
			str = "图片名称不合法或图片不存在";
			logger.info(str);
			return str;
		}
	}

	// 请求好友列表
	@Override
	public JSONObject dealFriendsList(String username) throws Exception {
		int id = userMapper.selectIdByName(username);
		List<Integer> friendsList = userMapper.selectRelateById(id);
		ArrayList<String> namesList = new ArrayList<String>();
		if (friendsList.size() == 0) {
			logger.info("该用户无好友");
		} else {
			for (Integer friendId : friendsList) {
				String name = userMapper.selectNameById(friendId);
				namesList.add(name);
			}
			logger.info("它的好友集合是：" + namesList);
		}
		JSONObject data = JsonData.createJsonObject4("好友列表", "", namesList);
		return data;
	}

	// 添加好友
	@Override
	public String dealAddFriend(String adder, String accepter) throws Exception {
		String str = null; // 返回信息
		int id1 = userMapper.selectIdByName(adder);
		int id2 = userMapper.selectIdByName(accepter);
		List<Integer> adderIds = userMapper.selectRelateById(id1);
		if (adderIds == null || adderIds.size() == 0) {
			userMapper.insertRelate(id1, id2);
			userMapper.insertRelate(id2, id1);
			str = "添加成功";
			Info info = new Info();
			info.setUserId(id2);
			info.setAccepter(accepter);
			info.setInformer(adder);
			info.setTitle("添加好友");
			info.setMessage("[" + adder + "]" + "请求添加," + "[" + accepter + "]" + "为好友");
			infoMapper.insertInfo(info);
		} else {
			if (adderIds.contains(id2)) {
				str = "添加失败，二者已经是好友";
			} else {
				userMapper.insertRelate(id1, id2);
				userMapper.insertRelate(id2, id1);
				str = "添加成功";
				Info info = new Info();
				info.setUserId(id2);
				info.setAccepter(accepter);
				info.setInformer(adder);
				info.setTitle("添加好友");
				info.setMessage("[" + adder + "]" + " 请求添加 " + "[" + accepter + "]" + " 为好友");
				infoMapper.insertInfo(info);
			}
		}
		logger.info(str);
		return str;
	}

	//处理搜索用户
	@Override
	public JSONObject dealSearchUser(String username) throws Exception {
		ArrayList<String> list = (ArrayList<String>) userMapper.selectUserByName(username);
		logger.info("搜索到的用户集合是："+list);
		JSONObject jsonData = JsonData.createJsonObject4("用户集合","",list);
		return jsonData;
	}

	// 处理询问通知消息
	public JSONObject dealAskMsg(String username) throws Exception {
		int userId = userMapper.selectIdByName(username);
		List<Info> infoList = infoMapper.selectInfoById(userId);
		ArrayList<Info> infos = (ArrayList<Info>) infoList;
		
		//总json
		JSONObject j = new JSONObject();
		j.put("title", "用户列表"); //添加总json的属性1
		
		JSONArray jsonInfos = new JSONArray();
		
		for(int i=0;i<infos.size();i++){
			Info info = infos.get(i);
			JSONObject in = new JSONObject();
			in.put("userId", info.getId());
			in.put("accepter", info.getAccepter());
			in.put("informer", info.getInformer());
			in.put("title", info.getTitle());
			in.put("message", info.getMessage());
			jsonInfos.add(i, in);
		}
		
		j.put("infos", jsonInfos); //添加总json的属性2
		
		return j;
	}
	
	//处理询问聊天消息
	public JSONObject dealAskChat(String asker,String friend) throws Exception {
		ArrayList<Message> msgList = null;
		if(friend == null || friend.equals("")){
			//查询该用户asker的所有消息
			msgList = messageMapper.selectMessageByName(asker);
		}else{
			msgList = messageMapper.selectFriendMessageByName(asker, friend);
			logger.info("***********************测试message"+msgList);
		}
		//总json
		JSONObject j = null;
		if(msgList.size() == 0){
			j = new JSONObject();
			j.put("title", "没有聊天"); //添加总json的属性1
		}else{
			j = new JSONObject();
			j.put("title", "聊天列表"); //添加总json的属性1
			
			JSONArray jsonInfos = new JSONArray();
			
			for(int i=0;i<msgList.size();i++){
				Message m = msgList.get(i);
				JSONObject in = new JSONObject();
				in.put("accepter", m.getAccepter());
				in.put("informer", m.getInformer());
				in.put("title", m.getTitle());
				
//				String code = m.getMessage();
//				//将code 转为 带有表情的字符
//				String emoji = EmojiUtil.emojiConverterUnicodeStr(code);
				
				in.put("message", m.getMessage());
				jsonInfos.add(i, in);
				messageMapper.deleteMessage(m.getId());
			}
			j.put("infos", jsonInfos); //添加总json的属性2
		}
		logger.info("当前总jsonObject："+j.toString());
		return j;
	}
	
	//主动聊天
	public boolean dealChat(String str){
		JSONObject j = JSONObject.fromObject(str);
		String accepter = j.getString("accepter");
		String informer = j.getString("informer");
		String title = j.getString("title");
		String message = j.getString("message");
		
//		//带有表情的字符串message 转换为 编码
//		String code = EmojiUtil.emojiConverterToAlias(message);
//		logger.info("发送的聊天消息是："+j.toString());
		
		
		Message m = new Message();
		m.setAccepter(accepter);
		m.setInformer(informer);
		m.setTitle(title);
		m.setMessage(message);
		logger.info("得到的message对象是："+m.toString());
		try {
			messageMapper.insertMessage(m);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
