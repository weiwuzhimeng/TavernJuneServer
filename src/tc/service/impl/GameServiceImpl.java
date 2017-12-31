package tc.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import tc.configure.Configure;
import tc.po.GameTable;
import tc.po.UserGame;
import tc.util.HeroManager;
import tc.util.JsonData;

public class GameServiceImpl {

	private static final Logger logger = Logger.getLogger(GameServiceImpl.class);

	UserGame ug;
	ArrayList<UserGame> guList; //socket集合 
	ArrayList<String> usernames; //用户名集合
	Map<String, String> userHeroMap; //公共牌+玩家私有
	ArrayList<String> playerHeros; //1.身份为狼的玩家的用户名集合
	ArrayList<String> publicHeros; //1.所有英雄的集合公共+私有(用于帮助生成sortedHeros)  2.公共英雄集合
	ArrayList<String> sortedHeros; //1.用于生成map集合  2.所有英雄排序后的集合

	GameTable gameTable;

	public GameServiceImpl() {
	}

	public GameServiceImpl(UserGame ug) {
		this.ug = ug;
		this.guList = ug.getGuList();
		this.usernames = ug.getUsernames();
		this.userHeroMap = ug.getUserHeroMap();
		this.playerHeros = ug.getPlayerHeros();
		this.publicHeros = ug.getPublicHeros();
		this.sortedHeros = ug.getSortedHeros();
		this.gameTable = ug.getGameTable();
	}

	// 处理玩家准备就绪，广播第一个选择英雄的用户名
	public void dealReady(String username) {
		logger.info("当前用户名集合1：" + usernames);
		usernames.add(username);
		logger.info("当前用户名集合2：" + usernames);
		ug.setUsername(username);
		logger.info("已经准备好选择英雄的玩家数量：" + usernames.size());
		if (usernames.size() == Configure.PLAYER_COUNT) {
			logger.info("准备就绪的玩家数量已经达到正确值，下面开始广播玩家集合");
			JSONObject jsonObject = JsonData.createJsonObject4("usernames", null, usernames);
			ug.sendMsgToOther(jsonObject);
			logger.info("玩家用户名集合发送完毕，集合是：" + usernames);
		}
	}

	// 当客户端安排玩家未知完毕时，发送第一个选择英雄的玩家名
	public void dealArrangeOver(String username) {
		logger.info("[这里是安排完毕]");
		synchronized (gameTable) {
			gameTable.arrangeOver++;
		}
		logger.info(username + "正在进行arrangeOver++" + "此玩家所持有的gameTable的哈希码是：" + gameTable.hashCode());
		logger.info("arrangeOver的值是：" + gameTable.arrangeOver);
		if (gameTable.arrangeOver == Configure.PLAYER_COUNT) {
			logger.info("arrangeOver已经达到正确值，下面广播第一个选择的用户名");
			JSONObject jsonObject = JsonData.createJsonObject6("chooser", usernames.get(gameTable.usernamesId),
					"heroname", gameTable.usernamesId, gameTable.chooseType);
			ug.sendMsgToOther(jsonObject);
			logger.info("第一个选择英雄的玩家广播完毕，消息类型[chooser][第一个用户名][无效][已选人数][选择类型]：" + jsonObject);

			// 清理资源
			gameTable.arrangeOver = 0;
			logger.info("[安排完毕：]gameTable.arrangeOver资源清理完毕");
		}
	}

	// 选择完毕接受英雄名和用户名
	public void dealChooseOver(JSONObject jsonMap) {
		gameTable.arrangeOver++; // 玩家选择完毕计数器

		String heroname = null;
		// String username = null;
		try {
			heroname = (String) jsonMap.get("msg1");
			// username = (String) jsonMap.get("msg2");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		sortedHeros.add(heroname); // sortedHeros用于生成map集合
		logger.info("逐加中的sortedHeros： "+sortedHeros);
		if(!heroname.equals("狼人")){
			if(!heroname.equals("皮匠")){
				publicHeros.add(heroname);
			}
		}
//		if((!heroname.equals("狼人")) &&  (!heroname.equals("皮匠"))){
//			publicHeros.add(heroname); // publicHeros用于记录所有的英雄(私有+公立)
//		}

		if (gameTable.arrangeOver == Configure.PLAYER_COUNT + 3) { // 选到倒数第二张中立牌时
			// 广播最后一个选择的英雄
			JSONObject jsonObject = JsonData.createJsonObject6("chooseOver", "没有下一个用户", heroname, gameTable.arrangeOver,
					gameTable.chooseType);
			ug.sendMsgToOther(jsonObject);
			logger.info("！！！服务端向客户端发送的内容是(广播告诉客户端所有玩家已经选择英雄结束+上一个用户选择的英雄名+已经选择英雄的人数+玩家选择的类型0/1)：" + jsonObject);

			// 清理资源
			gameTable.arrangeOver = 0;
			gameTable.chooseType = 0;
			gameTable.usernamesId = 0;
			logger.info("[选择完毕：]arrangeOver、chooseType、usernamesId全部清理完毕");

			// 广播选择结束
			JSONObject jsonObject3 = JsonData.createJsonObject5("overInfo", "选择结束");
			ug.sendMsgToOther(jsonObject3);

			heroAllot(sortedHeros);

			// 广播json数据，该数据是公共3张牌和该用户的牌
			logger.info("开始广播map集合(私有+共有)");
			JSONObject jsonObject2 = JsonData.createJsonObject7("cards","",userHeroMap);
			ug.sendMsgToOther(jsonObject2);
			logger.info("￥￥￥服务端向客户端发送的内容是(玩家用户名英雄映射+公共区3张牌)" + jsonObject2.toString());
			logger.info("￥￥￥map:" + userHeroMap);
		} else {
			// 同步代码块，防止发生线程问题
			synchronized (this) {
				gameTable.usernamesId++;
				if (gameTable.usernamesId >= Configure.PLAYER_COUNT) {
					gameTable.usernamesId = gameTable.usernamesId % Configure.PLAYER_COUNT;
					gameTable.chooseType = 1;
					logger.info("当前gameTable.chooseType是：" + gameTable.chooseType);
				}
			}
			logger.info("++后的gameTable.usernamesId是：" + gameTable.usernamesId);
			String chooser = usernames.get(gameTable.usernamesId);
			logger.info("下一个选择英雄的用户名是：" + chooser);
			JSONObject jsonObject = JsonData.createJsonObject6("chooseOver", chooser, heroname, gameTable.arrangeOver,
					gameTable.chooseType);
			ug.sendMsgToOther(jsonObject);
			logger.info("+++服务端向客户端发送的内容是(下一个选择英雄的用户名+上一个用户选择的英雄名+已经选择英雄的人数+下一个用户的选择类型0/1)：" + jsonObject);
		}

	}

	// 处理开始行动
	public void dealHeroAction() {
		gameTable.arrangeOver++;
		if(gameTable.arrangeOver == Configure.PLAYER_COUNT){
			gameTable.arrangeOver=0; //清理资源
			logger.info("待排序的publicHeros(传入sortedHeros前)"+publicHeros);
			
			/*
			 * this.sortedHeros = new HeroManager().heroSort(publicHeros);
			 * 错误原因：this.sortedHeros原本指向的是公共资源(线程共享资源)，但是被sortHerosModel赋值后，this.sortedHeros这个引用就指向了一个新对象(sortHerosModel对象)
			 * */
			//修改的代码
			ArrayList<String> sortedHerosModel = new HeroManager().heroSort(publicHeros);
			logger.info("得到sortedHerosModel是："+sortedHerosModel);
			for(int i=0; i<sortedHerosModel.size(); i++){
				this.sortedHeros.add(sortedHerosModel.get(i));
			}
			logger.info("得到我们的sortedHeros："+sortedHeros);
			
			//publicHeros开始装载中央牌
			publicHeros.clear();
			publicHeros.add(userHeroMap.get("centerCard1"));
			publicHeros.add(userHeroMap.get("centerCard2"));
			publicHeros.add(userHeroMap.get("centerCard3"));
			logger.info("******中立牌是publicHeros："+publicHeros);
			logger.info("*******准备执行heroSort()");
			logger.error("*******准备执行heroSort()");
			//英雄行动开始
			heroSort();
		}
	}
	
	

	// 处理回合结束   ？？？？问题map集合没有角标
	public void dealRoundOver(JSONObject jsonObject) {
		logger.info("当前sortedHeros5："+sortedHeros+"，"+sortedHeros.hashCode());
		// 提取json数据
		String heroname = (String) jsonObject.get("msg1");
		logger.info("当前sortedHeros6："+sortedHeros+"，"+sortedHeros.hashCode());
		switch (heroname) {
		case "幽灵":
			//机制复杂，暂不处理
			break;
		case "狼群":
			logger.info("当前sortedHeros7："+sortedHeros+"，"+sortedHeros.hashCode());
			//狼群都回合结束完毕，再进行向下广播进行回合的英雄
			gameTable.usernamesId++;
			logger.info("当前sortedHeros8："+sortedHeros+"，"+sortedHeros.hashCode());
			if(gameTable.usernamesId == playerHeros.size()){ 
				logger.info("当前sortedHeros9："+sortedHeros+"，"+sortedHeros.hashCode());
				gameTable.usernamesId=0;
				logger.info("当前sortedHeros10："+sortedHeros+"，"+sortedHeros.hashCode());
				heroSort();
			}
			break;
		case "狼Alpha":
			//将中央狼牌与一名角色交换
			logger.info("狼Alpha交换前的userHeroMap："+userHeroMap);
			String playerName = (String) jsonObject.get("msg2"); //获得被交换的玩家名称
			String heroName = userHeroMap.get(playerName); //获得这个玩家所对应的英雄名
			String centerHeroName = userHeroMap.get("centerCard1"); //获得中央centerCard1处的英雄名，开始时centerCard1一定是狼
			userHeroMap.put("centerCard1", heroName);
			userHeroMap.put(playerName, centerHeroName);
			logger.info("狼Alpha交换后的userHeroMap："+userHeroMap);
			break;
		case "预言家":
			//服务端不需操作
			break;
		case "狼先知":
			//服务端不需操作
			break;
		case "强盗":
			//强盗会交换自己与一名其他玩家的英雄
			logger.info("强盗交换前的userHeroMap："+userHeroMap);
			String myPlayerName = (String) jsonObject.get("msg2"); //自己的用户名
			String otherPlayerName = (String) jsonObject.get("msg3"); //他人的用户名
			String myHeroName = userHeroMap.get(myPlayerName); //自己的英雄名
			String otherHeroName = userHeroMap.get(otherPlayerName); //他人的英雄名
			userHeroMap.put(myPlayerName, otherHeroName);
			userHeroMap.put(otherPlayerName, myHeroName);
			logger.info("强盗交换后的userHeroMap："+userHeroMap);
			break;
		case "捣蛋鬼":
			//会交换两个玩家的英雄
			logger.info("捣蛋鬼交换前的userHeroMap："+userHeroMap);
			String otherPlayerName1 = (String) jsonObject.get("msg2"); //他人1的名称
			String otherPlayerName2 = (String) jsonObject.get("msg3"); //他人2的名称
			String otherHeroName1 = userHeroMap.get(otherPlayerName1);
			String otherHeroName2 = userHeroMap.get(otherPlayerName2);
			userHeroMap.put(otherPlayerName1, otherHeroName2);
			userHeroMap.put(otherPlayerName2, otherHeroName1);
			logger.info("捣蛋鬼交换后的userHeroMap："+userHeroMap);
			break;
		case "女巫":
			//会交换中央牌和一个玩家牌
			logger.info("女巫交换前的userHeroMap："+userHeroMap);
			String centerCardName = (String) jsonObject.get("msg2"); //中央某英雄的用户名(centerCard1、2、3之一)
			String playerName2 = (String) jsonObject.get("msg3"); //某玩家的用户名
			String centerHeroName2 = userHeroMap.get(centerCardName); //通过中央用户名获得对应的英雄名
			String heroName2 = userHeroMap.get(playerName2); //通过某玩家用户名获得对应的英雄名
			userHeroMap.put(playerName2, centerHeroName2);
			userHeroMap.put(centerCardName, heroName2);
			logger.info("女巫交换后的userHeroMap："+userHeroMap);
			break;
		case "失眠者":
			//服务端不需操作
			break;
		default:
			logger.info("没有这个英雄");
			break;
		}
		if(!heroname.equals("狼群")){
			heroSort();
		}
	}

	// 英雄放置的方法
	public void heroAllot(ArrayList<String> sortedHeros) {
		logger.info("乱序前的sortedHeros：" + sortedHeros);
		Collections.shuffle(sortedHeros);
		logger.info("乱序后的sortedHeros：" + sortedHeros);
		//找到一个狼，放到公共区
		for (int i = 0; i < sortedHeros.size(); i++) {
			String heroname = sortedHeros.get(i);
			if (heroname.charAt(0) == '狼' && userHeroMap.size() == 0) {
				userHeroMap.put("centerCard1", heroname);
				sortedHeros.remove(i);
			} 
		}
		//填充map集合
		for (int i = 0; i < sortedHeros.size(); i++) {
			if (userHeroMap.size() < 3) {
				//放置公共英雄
				String mapKey = null;
				switch (userHeroMap.size()) {
				case 0:
					mapKey = "centerCard1";
					break;
				case 1:
					mapKey = "centerCard2";
					break;
				case 2:
					mapKey = "centerCard3";
					break;
				}
				userHeroMap.put(mapKey, sortedHeros.get(i));
			} else {
				//放置私有英雄
				String heroname = sortedHeros.get(i);
				userHeroMap.put(usernames.get(i-2), heroname); //i-2，因为(大小)：usersnames+(公共3张-1)=sortedHeros。
				if(heroname.charAt(0) == '狼'){
					playerHeros.add(usernames.get(i-2)); //playerHeros暂时保存身份为狼的玩家的用户名
				}
			}
		}
		logger.info("装填完的map："+userHeroMap);
		logger.info("暂存私有狼玩家用户名集合："+playerHeros);
		// 清理资源
		sortedHeros.clear();
		logger.info("英雄放置完毕");
	}

//	// 玩家匹配英雄的方法
//	public void playerMapHero() {
//		for (int i = 0; i < guList.size(); i++) {
//			UserGame ug = guList.get(i);
//			JSONObject jsonObject = JsonData.createJsonObject4("heroAllot", playerHeros.get(i), playerHeros);
//			ug.sendMsgToSingle(jsonObject);
//		}
//	}

	// 实现文字聊天室
	public void dealChat(JSONObject jsonObject) {
		ug.sendMsgExceptMe(jsonObject);
	}

	// 制定英雄出牌的先后顺序      逻辑有问题
	public void heroSort() {
		logger.info("***[heroSort()开始执行，当前gameTable.arrangeOver："+gameTable.arrangeOver);
		logger.error("***[heroSort()开始执行，当前gameTable.arrangeOver："+gameTable.arrangeOver);
		logger.info("当前sortedHeros："+sortedHeros+"，"+sortedHeros.hashCode());
		JSONObject jsonObject = null;
		String heroname = null;

		if(gameTable.arrangeOver < sortedHeros.size()){
			heroname = sortedHeros.get(gameTable.arrangeOver);
			logger.info("***[gameTable.arrangeOver为："+gameTable.arrangeOver+"此时的heroname为："+heroname);
			logger.info("并会在随后进行arrangeOver++");
			gameTable.arrangeOver++;
		}else{
			logger.info("广播英雄出牌顺序结束，随后会结束heroSort()，当前gameTable.arrangeOver："+gameTable.arrangeOver+"，当前sortedHeros.size()："+sortedHeros.size());
			//进度******************
			//释放资源
			//#开#始#发#言      返回#发#言#结#束   发言两轮     顺序按照：usernames用户名集合
			//#开#始#投#票      返回#投#票#结#束   返回票数最多用户名  票相同都返回
			return ;
		}
		
		if (!heroname.equals("狼群")) {
			//下面的if用于处理公共牌，没有玩家响应的问题，这里我们采用递归，三秒之后自动进行下个英雄的行动广播
			if(publicHeros.contains(heroname)){
				jsonObject = JsonData.createJsonObject7("#进#行#回#合", heroname,userHeroMap);
				ug.sendMsgToOther(jsonObject);
				logger.info("["+heroname+"：属于中立牌，服务端随机延迟后自动进行向下广播]");
				try {
					//生成一个5-10秒的随机数，并延迟相应秒数
					Random random = new Random();
					int delay = random.nextInt(5)+5;
					Thread.sleep(delay*1000); 
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				heroSort();
			}else{
				jsonObject = JsonData.createJsonObject7("#进#行#回#合", heroname,userHeroMap);
				ug.sendMsgToOther(jsonObject);
			}
		} else {
			//临时修改(日后需完善)
			if(playerHeros.size() == 0){
				logger.info("当前sortedHeros11："+sortedHeros+"，"+sortedHeros.hashCode());
				jsonObject = JsonData.createJsonObject8("#进#行#回#合", "狼群",userHeroMap, playerHeros);
				ug.sendMsgToOther(jsonObject);
				logger.info("当前sortedHeros12："+sortedHeros+"，"+sortedHeros.hashCode());
				logger.info("[狼群为空，服务端随机延迟后自动进行向下广播]");
				//生成一个5-10秒的随机数，并延迟相应秒数
				Random random = new Random();
				int delay = random.nextInt(5)+5;
				logger.info("当前sortedHeros13："+sortedHeros+"，"+sortedHeros.hashCode());
				try {
					Thread.sleep(delay*1000);
					logger.info("当前sortedHeros14："+sortedHeros+"，"+sortedHeros.hashCode());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
				logger.info("当前sortedHeros15："+sortedHeros+"，"+sortedHeros.hashCode());
				heroSort();
			}else{
				logger.info("当前sortedHeros2："+sortedHeros+"，"+sortedHeros.hashCode());
				jsonObject = JsonData.createJsonObject8("#进#行#回#合", "狼群",userHeroMap, playerHeros);
				logger.info("当前sortedHeros3："+sortedHeros+"，"+sortedHeros.hashCode());
				ug.sendMsgToOther(jsonObject);
				logger.info("当前sortedHeros4："+sortedHeros+"，"+sortedHeros.hashCode());
			}
		}
	}		

	// 释放资源(服务端程序回归为无玩家的原始状态)
	public void cleanSource() {
		gameTable.chooseType = 0;
		gameTable.usernamesId = 0;
		gameTable.arrangeOver = 0;
		playerHeros.clear();
		publicHeros.clear();
		logger.info("由于本桌已经没有玩家(guList大小为0)，游戏端服务器已经回归原始状态");
	}
}
