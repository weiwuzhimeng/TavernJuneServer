package tc.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import tc.configure.Configure;
import tc.po.GameTable;
import tc.po.UserGame;

public class GameFrontController extends Thread {
	
	private static final Logger logger = Logger.getLogger(GameFrontController.class);

	LinkedList<UserGame> ll = new LinkedList<UserGame>();
	boolean serverIsStart = false;
	Object obj = new Object();

	ServerSocket ss;

	public void run() {
		serverIsStart = true;
		logger.info("游戏服务端启动");
		try {
			ss = new ServerSocket(Configure.USER_GAME_PORT);

			while (serverIsStart) {

				Socket socket = ss.accept();
				UserGame ug = new UserGame(socket);
				
				synchronized(obj){
					ll.add(ug);
					
					if(ll.size() >= Configure.PLAYER_COUNT){
						ArrayList<UserGame> guList = new ArrayList<UserGame>();
						for(int i=0;i<Configure.PLAYER_COUNT;i++){
							UserGame ug2 = ll.poll();
							guList.add(ug2);
							logger.info("当前集合是："+guList);
						}
						GameTable gt = new GameTable(guList);
						Thread thread = new Thread(gt);
						thread.start();
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();
				logger.error("这里是fanlly，已经关闭了ss");
			} catch (IOException e1) {
				e1.printStackTrace();
				logger.error("ss关闭失败");
			}
		}
	}
}
