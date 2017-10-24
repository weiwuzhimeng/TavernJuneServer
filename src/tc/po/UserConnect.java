package tc.po;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;


public class UserConnect extends User{

	Socket socket; // 此app的连接
	ObjectInputStream ois = null; // 此app的输入
	ObjectOutputStream oos = null; // 此app的输出
	//Map<String, UserConnect> usersMap = null; // 用户集
	
	public UserConnect(){
		
	}
	
	public UserConnect(Socket socket) throws IOException {
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
	}
	
//	public UserConnect(Map<String, UserConnect> usersMap,Socket socket) throws IOException {
//		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//		this.usersMap = usersMap;
//		this.socket = socket;
//		this.ois = ois;
//		this.oos = oos;
//	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}

//	public Map<String, UserConnect> getUsersMap() {
//		return usersMap;
//	}
//
//	public void setUsersMap(Map<String, UserConnect> usersMap) {
//		this.usersMap = usersMap;
//	}

}
