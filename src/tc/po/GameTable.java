package tc.po;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import tc.controller.GameBehindController;
import tc.po.UserGame;
import tc.util.JsonData;

public class GameTable implements Runnable{
	
	private static final Logger logger = Logger.getLogger(GameTable.class);
	
	ArrayList<UserGame> guList; //连接集合
	ArrayList<String> usernames = new ArrayList<String>();//玩家用户名集合
	Map<String, String> userHeroMap = new HashMap<String,String>(); //【新增】玩家用户名和英雄名映射集合
	
	//本桌游戏内的玩家公共资源
	ArrayList<String> playerHeros = new ArrayList<String>(); 
	ArrayList<String> publicHeros = new ArrayList<String>();
	ArrayList<String> sortedHeros = new ArrayList<String>(); //新修改
	public int chooseType; //选择类型
	public int usernamesId; //usernames角标
	public int arrangeOver; //完毕(多用途)的人数

	public GameTable(ArrayList<UserGame> guList){
		this.guList = guList;
	}

	public void init(UserGame ug){
		ug.setGuList(guList);
		ug.setUsernames(usernames);
		ug.setUserHeroMap(userHeroMap);
		ug.setPlayerHeros(playerHeros);
		ug.setPublicHeros(publicHeros);
		ug.setSortedHeros(sortedHeros);
		ug.setGameTable(this);
	}

	@Override
	public void run() {
		
		for(int i=0;i<guList.size();i++){
			UserGame ug = guList.get(i);
			this.init(ug);
			GameBehindController gbc = new GameBehindController(ug);
			Thread thread = new Thread(gbc);
			thread.start();
		}
		//通知集合内的各个客户端已经匹配完毕
		JSONObject jsonObject = JsonData.createJsonObject("mateOver", guList.size());
		logger.info(guList);
		UserGame ug = guList.get(0);
		ug.sendMsgToOther(jsonObject);
	}
	
//	public void 
}
