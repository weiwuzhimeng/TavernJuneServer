package tc.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import tc.po.UserConnect;
import tc.service.impl.GameServiceImpl;
import tc.service.impl.UserConnectImpl;

//用户Tcp连接的后端控制器(用于将用户的请求分配给相应的业务层)
public class UserConnectBehindController implements Runnable{
	
	private static final Logger logger = Logger.getLogger(UserConnectBehindController.class);
	
	UserConnectImpl userConnectImpl;
	
	Socket socket;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	Map<String, UserConnect> usersMap;
	UserConnect uc;
	boolean clientIsRead = false;
	
	public UserConnectBehindController(){
		
	}
	
	public UserConnectBehindController(UserConnect uc){
		this.uc = uc;
		this.socket = uc.getSocket();
		this.ois = uc.getOis();
		this.oos = uc.getOos();
		this.usersMap = uc.getUcMap();
	}

	@Override
	public void run() {
		userConnectImpl = new UserConnectImpl(uc);
		clientIsRead = true;
		logger.info("客户端在服务端的映射已经开始读取消息");
		try {
			while(clientIsRead){
				String str = (String) ois.readObject();
				logger.info("客户端发送的消息是："+str);
				//JSONObject jsonObject = new JSONObject(str);
				JSONObject jsonObject = JSONObject.fromObject(str);
				String msgType = (String) jsonObject.get("msgType");
				String adress = socket.getInetAddress().getHostAddress(); //用户ip地址
				
				switch(msgType){
					//这里根据请求类型返回消息    问题：发送的usernames中的name在usersMap中查不到怎么办？？？
					case "#发#送#用#户":
						String username = (String) jsonObject.get("msg1");
						uc.setUsername(username);
						usersMap.put(username, uc);
						logger.info(username+"连接上了总tcp，并加入map中，当前map："+usersMap);
						break;
					case "#进#入#游#戏":
						logger.info(adress+"已经进入游戏");
						break;
					case "#退#出#游#戏":
						logger.info(adress+"已经退出游戏");
						break;
					case "#请#求#语#音":
						userConnectImpl.dealSpeech(jsonObject);
						break;
					case "#分#配#房#间":
						userConnectImpl.dealRoom(jsonObject);
						break;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
			usersMap.remove(uc.getUsername());
			logger.info("[总连接]:"+uc.getUsername()+"断开了连接，随后会进行总连接方面的资源释放");
			uc.closeSource(); 
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
