package tc.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import tc.po.GameTable;
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
		gameServiceImpl = new GameServiceImpl(ug); //初始化业务层
		boolean clientIsRead = true;
		logger.info(socket.getInetAddress().getHostAddress()+"已经开启数据读取");
		try {
			while (clientIsRead) {
				String str = (String) ois.readObject();
				logger.info("[这里是游戏服务端]"+socket.getInetAddress().getHostAddress()+"发送的消息是："+str+"*****************");
				//JSONObject jsonObject = new JSONObject(str);
				JSONObject jsonObject = JSONObject.fromObject(str);
				String msgType = (String) jsonObject.get("msgType");
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
						gameServiceImpl.dealRoundOver(jsonObject);
						break;
					case "#发#言#结#束":
						gameServiceImpl.dealSpeakOver();
						break;
					case "#投#票#结#束":
						String choosedName = (String) jsonObject.get("msg1"); //choosedName表示被他人投票的用户名
						gameServiceImpl.dealVoteOver(choosedName); 
						break;
					case "#本#局#结#束":
						new GameTable().closeSource(); //正常退出
						break;
					default:
						logger.info("msgType有误");
						break;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
			logger.info("[游戏连接]:"+ug.getUsername()+"断开了连接，随后会进行游戏方面的资源释放(全部释放)");
			new GameTable(guList).closeSource(); //异常退出(这里必须要传入guList，否则无法获取guList)
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
