package tc.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import tc.configure.Configure;
import tc.po.UserConnect;


//用户Tcp连接的前端控制器(用于接收各个socket连接)
public class UserConnectFrontController extends Thread{
	
	private static final Logger logger = Logger.getLogger(UserConnectFrontController.class);
	boolean serverIsStart = false;
	//Map<String, UserConnect> usersMap = new HashMap<String,UserConnect>();

	@Override
	public void run() {
		logger.error("连接开始啦。。。");
		serverIsStart = true;
		try {
			ServerSocket ss = new ServerSocket(Configure.USER_CONNECT_PORT);
			
			while(serverIsStart){
				Socket socket = ss.accept();
				UserConnect uc = new UserConnect(socket);
				UserConnectBehindController ucbc = new UserConnectBehindController(uc);
				new Thread(ucbc).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
}
