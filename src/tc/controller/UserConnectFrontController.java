package tc.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import tc.configure.Configure;
import tc.po.UserConnect;


//用户Tcp连接的前端控制器(用于接收各个socket连接)
public class UserConnectFrontController extends Thread{
	
	private static final Logger logger = Logger.getLogger(UserConnectFrontController.class);
	boolean serverIsStart = false;
	//Map<String, UserConnect> usersMap = new HashMap<String,UserConnect>();

	@Override
	public void run() {
		serverIsStart = true;
		logger.info("总连接服务端已启动");
		try {
			@SuppressWarnings("resource")
			ServerSocket ss = new ServerSocket(Configure.USER_CONNECT_PORT);
			
			while(serverIsStart){
				Socket socket = ss.accept();
				logger.info(socket.getInetAddress().getHostAddress()+"已连接总tcp");
				UserConnect uc = new UserConnect(socket);
				UserConnectBehindController ucbc = new UserConnectBehindController(uc);
				new Thread(ucbc).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
}
