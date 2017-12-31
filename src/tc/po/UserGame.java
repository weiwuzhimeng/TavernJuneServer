package tc.po;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import tc.po.GameTable;
import tc.service.impl.GameServiceImpl;
import tc.util.JsonData;

public class UserGame extends UserConnect {

	private static final Logger logger = Logger.getLogger(UserGame.class);
	
	ObjectInputStream ois; // 此app的输入
	ObjectOutputStream oos; // 此app的输出
	Socket socket;

	ArrayList<UserGame> guList;
	ArrayList<String> usernames;
	Map<String, String> userHeroMap;
	
	ArrayList<String> playerHeros; 
	ArrayList<String> publicHeros;
	ArrayList<String> sortedHeros;
	GameTable gameTable;

	// 构造方法必须得写出，即使其父类有此构造方法
	public UserGame(Socket socket) throws IOException {
		ois = new ObjectInputStream(socket.getInputStream());
		oos = new ObjectOutputStream(socket.getOutputStream());
		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
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

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ArrayList<UserGame> getGuList() {
		return guList;
	}

	public void setGuList(ArrayList<UserGame> guList) {
		this.guList = guList;
	}
	
	public ArrayList<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(ArrayList<String> usernames) {
		this.usernames = usernames;
	}
	
	public Map<String, String> getUserHeroMap() {
		return userHeroMap;
	}

	public void setUserHeroMap(Map<String, String> userHeroMap) {
		this.userHeroMap = userHeroMap;
	}

	public ArrayList<String> getPlayerHeros() {
		return playerHeros;
	}

	public void setPlayerHeros(ArrayList<String> playerHeros) {
		this.playerHeros = playerHeros;
	}

	public ArrayList<String> getPublicHeros() {
		return publicHeros;
	}

	public void setPublicHeros(ArrayList<String> publicHeros) {
		this.publicHeros = publicHeros;
	}
	
	public ArrayList<String> getSortedHeros() {
		return sortedHeros;
	}

	public void setSortedHeros(ArrayList<String> sortedHeros) {
		this.sortedHeros = sortedHeros;
	}

	public GameTable getGameTable() {
		return gameTable;
	}

	public void setGameTable(GameTable gameTable) {
		this.gameTable = gameTable;
	}

	//广播消息，发送的消息是相同的
	public void sendMsgToOther(JSONObject jsonObject) {
		try {
			for (int i = 0; i < guList.size(); i++) {
				UserGame ug = guList.get(i);
				ObjectOutputStream oos1 = ug.getOos();
				oos1.writeObject(jsonObject.toString());
				logger.info("广播的内容是(消息相同):"+jsonObject+"使用的oos输入日志测试:"+oos1.hashCode());
				System.out.println("当前对象的sortedHeros："+sortedHeros);
			}
		} catch (IOException e) {
			guList.remove(this);
			e.printStackTrace();
			logger.info("对方退出了，我把它移除了");
		}
	}
	
	//给每个玩家发送的消息不同(给自己发消息)
	public void sendMsgToSingle(JSONObject jsonObject){
		try {
			oos.writeObject(jsonObject.toString());
			logger.info("广播的内容是(消息不同):"+jsonObject);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//广播消息(除了自己)
	public void sendMsgExceptMe(JSONObject jsonObject){
		try {
			for (int i = 0; i < guList.size(); i++) {
				int j = guList.indexOf(this);
				if(i != j){
					UserGame ug = guList.get(i);
					ObjectOutputStream oos1 = ug.getOos();
					oos1.writeObject(jsonObject.toString());
					logger.info("广播的内容是(消息相同):"+jsonObject+"使用的oos输入日志测试:"+oos1.hashCode());
				}
			}
		} catch (IOException e) {
//			guList.remove(this);
			e.printStackTrace();
//			logger.info("对方退出了，我把它移除了");
		}
	}
	
	//关闭资源，设为同步函数的原因：避免在点击取消匹配时，还没释放完毕，其他玩家进到集合中，集合达到正确值，然后进入游戏
	public synchronized boolean closeSource() { 
		//释放guList资源
		if(guList.contains(this)){
			guList.remove(this); //将此客户端从socket连接集合移除
			logger.info("guList移除："+this+"成功，当前guList为："+guList);
			if(guList.size()==0){
				new GameServiceImpl().cleanSource();
			}
		}else{
			logger.info(socket.getInetAddress().getHostAddress()+":guList移除连接失败，因为guList中没有这个连接");
		}
		//释放usernames资源
		if(usernames.contains(this.getUsername())){
			usernames.remove(this.getUsername());
			logger.info("usernames移除"+this.getUsername()+"成功，当前usernames为："+usernames);
		}else{
			logger.info(socket.getInetAddress().getHostAddress()+":usernames移除用户名失败，因为usernames中没有这个用户名");
		}
		//释放socket
		try {
			if (ois != null)
				ois.close();
			if (oos != null)
				oos.close();
			if (socket != null)
				socket.close();
			logger.info("已经关闭了此客户端的资源：关闭输入输出流、关闭socket:"+socket.getInetAddress().getHostAddress());
			return true;
		} catch (IOException e1) {
			e1.printStackTrace();
			logger.info("关闭资源失败");
			return false;
		}
	}
}
