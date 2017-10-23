package tc.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import tc.controller.UserConnectFrontController;

public class ConfigureInit implements ServletContextListener {
	
	private static final Logger logger = Logger.getLogger(ConfigureInit.class);
	
	Thread userConnectController = null;
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.error("初始化啦。。。");
		userConnectController = new UserConnectFrontController();
		userConnectController.start();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if(userConnectController!=null && !userConnectController.isInterrupted()){
			userConnectController.interrupt();
			logger.info("已经关闭服务端的连接线程(主链接)。。。。");
		}
	}

}
