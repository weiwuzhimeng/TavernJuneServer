package tc.po;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

public class UserConnect extends User {

	private static final Logger logger = Logger.getLogger(UserConnect.class);
	
	Socket socket; // 此app的连接
	ObjectInputStream ois; // 此app的输入
	ObjectOutputStream oos; // 此app的输出
	// Map<String, UserConnect> usersMap = null; // 用户集

	public UserConnect() {

	}

	public UserConnect(Socket socket) throws IOException {
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
	}

	// public UserConnect(Map<String, UserConnect> usersMap,Socket socket)
	// throws IOException {
	// ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
	// ObjectOutputStream oos = new
	// ObjectOutputStream(socket.getOutputStream());
	// this.usersMap = usersMap;
	// this.socket = socket;
	// this.ois = ois;
	// this.oos = oos;
	// }

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

	// public Map<String, UserConnect> getUsersMap() {
	// return usersMap;
	// }
	//
	// public void setUsersMap(Map<String, UserConnect> usersMap) {
	// this.usersMap = usersMap;
	// }

	// 关闭资源
	public boolean closeSource() {
		try {
			if (ois != null)
				ois.close();// 客户端关闭时，服务端相应的关闭(未初始化不用关闭)
			if (oos != null)
				oos.close();
			if (socket != null)
				socket.close();
			logger.info("已经关闭了此客户端的资源：关闭输入输出流、关闭socket："+socket.getInetAddress().getHostAddress());
			return true;
		} catch (IOException e1) {
			e1.printStackTrace();
			logger.info("关闭资源失败");
			return false;
		}
	}
}
