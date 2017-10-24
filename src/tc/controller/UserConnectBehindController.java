package tc.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import tc.po.UserConnect;
import tc.util.JsonData;

//用户Tcp连接的后端控制器(用于将用户的请求分配给相应的业务层)
public class UserConnectBehindController implements Runnable{
	
	private static final Logger logger = Logger.getLogger(UserConnectBehindController.class);
	Socket socket = null;
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	//Map<String, UserConnect> usersMap = null;
	UserConnect uc = null;
	boolean clientIsRead = false;
	
	public UserConnectBehindController(){
		
	}
	
	public UserConnectBehindController(UserConnect uc){
		this.uc = uc;
		this.socket = uc.getSocket();
		this.ois = uc.getOis();
		this.oos = uc.getOos();
		//this.usersMap = uc.getUsersMap();
	}

	@Override
	public void run() {
		clientIsRead = true;
		logger.info("客户端在服务端的映射已经开始读取消息");
		try {
			while(clientIsRead){
				String str = (String) ois.readObject();
				logger.info("客户端发送的消息是："+str);
				JSONObject jsonObject = new JSONObject(str);
				String msgType = (String) jsonObject.get("msgType");
				
				switch(msgType){
					//这里根据请求类型返回消息
					case "#进#入#游#戏":
						JSONObject jsonObject2 = JsonData.createJsonObject("main", "#允#许#游#戏");
						oos.writeObject(jsonObject2.toString());
						break;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
}
