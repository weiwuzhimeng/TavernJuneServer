package tc.controller;

import java.io.EOFException;
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
	Socket socket;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	//Map<String, UserConnect> usersMap = null;
	UserConnect uc;
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
				String adress = socket.getInetAddress().getHostAddress(); //用户ip地址
				
				switch(msgType){
					//这里根据请求类型返回消息
					case "#进#入#游#戏":
						logger.info(adress+"已经进入游戏");
						break;
					case "#退#出#游#戏":
						logger.info(adress+"已经退出游戏");
						break;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("客户端断开了连接，随后会释放该客户端的资源**************************************");
			uc.closeSource(); 
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
}
