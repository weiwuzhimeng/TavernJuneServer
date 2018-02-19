package tc.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import tc.controller.GameFrontController;

public class GameInit implements ServletContextListener{

	private static final Logger logger = Logger.getLogger(GameInit.class);
	
	Thread gameFront = null;
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.error("游戏连接初始化啦。。。");
		if(gameFront == null){
			gameFront=new GameFrontController();
			gameFront.start();
			logger.info("游戏前端控制器线程已经开启");
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if(gameFront!=null && !gameFront.isInterrupted()){
			gameFront.interrupt();
			logger.info("游戏前端控制器线程已关闭");
		}
	}
}
