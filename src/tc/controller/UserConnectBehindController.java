package tc.controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import tc.po.UserConnect;

public class UserConnectBehindController implements Runnable{
	
	Socket socket = null;
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	Map<String, UserConnect> usersMap = null;
	UserConnect uc = null;
	
	public UserConnectBehindController(){
		
	}
	
	public UserConnectBehindController(UserConnect uc){
		this.uc = uc;
		this.socket = uc.getSocket();
		this.ois = uc.getOis();
		this.oos = uc.getOos();
		this.usersMap = uc.getUsersMap();
	}

	@Override
	public void run() {
		//在此处接收客户端传输过来的请求，然后根据请求类型，判断进行进一步的处理
	}
	
}
