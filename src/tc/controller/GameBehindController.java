package tc.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import tc.po.UserGame;
import tc.service.impl.GameServiceImpl;

public class GameBehindController implements Runnable {

	private static final Logger logger = Logger.getLogger(GameBehindController.class);
	
	private GameServiceImpl gameServiceImpl;
	
	private UserGame ug;
	private Socket socket;
	private ObjectInputStream ois;
	boolean clientIsRead = false;
	ArrayList<UserGame> guList;
	ArrayList<String> sortedHeros; //新添

	public GameBehindController() {
	}

	public GameBehindController(UserGame ug) {
		this.ug = ug;
		this.socket = ug.getSocket();
		this.ois = ug.getOis();
		this.guList = ug.getGuList();
		this.sortedHeros = ug.getSortedHeros(); //新添
	}

	@Override
	public void run() {
		logger.info("当前sortedHeros16："+sortedHeros+"，"+sortedHeros.hashCode());
		gameServiceImpl = new GameServiceImpl(ug); //初始化业务层
		logger.info("当前sortedHeros17："+sortedHeros+"，"+sortedHeros.hashCode());
		boolean clientIsRead = true;
		logger.info("当前sortedHeros18："+sortedHeros+"，"+sortedHeros.hashCode());
		logger.info(socket.getInetAddress().getHostAddress()+"已经开启数据读取");
		try {
			logger.info("当前sortedHeros19："+sortedHeros+"，"+sortedHeros.hashCode());
			while (clientIsRead) {
				logger.info("当前sortedHeros20："+sortedHeros+"，"+sortedHeros.hashCode());
				String str = (String) ois.readObject();
				logger.info("当前sortedHeros21："+sortedHeros+"，"+sortedHeros.hashCode());
				logger.info("[这里是游戏服务端]"+socket.getInetAddress().getHostAddress()+"发送的消息是："+str+"*****************");
				//JSONObject jsonObject = new JSONObject(str);
				JSONObject jsonObject = JSONObject.fromObject(str);
				logger.info("当前sortedHeros22："+sortedHeros+"，"+sortedHeros.hashCode());
				String msgType = (String) jsonObject.get("msgType");
				logger.info("当前sortedHeros23："+sortedHeros+"，"+sortedHeros.hashCode());
				logger.info(socket.getInetAddress().getHostAddress()+"请求的消息类型是："+msgType);
				
				switch(msgType){
					case "#取#消#匹#配":     //case和ug.closeSource()这两行会出现同步问题吗？
						ug.closeSource();
						break;
					case "#准#备#就#绪":
						String username = (String) jsonObject.get("msg1");
						logger.info("username是："+username);
						gameServiceImpl.dealReady(username);
						logger.info("gameServiceImpl:"+gameServiceImpl.hashCode());
						break;
					case "#安#排#完#毕":
						String username1 = (String) jsonObject.get("msg1");
						gameServiceImpl.dealArrangeOver(username1);
						break;
					case "#选#择#完#毕":
						gameServiceImpl.dealChooseOver(jsonObject);
						break;
					case "#文#字#聊#天":
						gameServiceImpl.dealChat(jsonObject);
						break;
					case "#开#始#行#动":
						gameServiceImpl.dealHeroAction();
						break;
					case "#回#合#结#束":
						logger.info("当前sortedHeros24："+sortedHeros+"，"+sortedHeros.hashCode());
						gameServiceImpl.dealRoundOver(jsonObject);
						logger.info("当前sortedHeros25："+sortedHeros+"，"+sortedHeros.hashCode());
						break;
					case "#退#出#游#戏":
						ug.closeSource(); //资源释放，待解决
						break;
					default:
						logger.info("msgType有误");
						break;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			logger.info(socket.getInetAddress().getHostAddress()+":断开了连接，随后会释放该客户端的资源********************");
			ug.closeSource();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
