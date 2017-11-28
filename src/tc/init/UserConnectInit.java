package tc.init;

import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import tc.controller.UserConnectFrontController;

//初始化tomcat的一些配置，这里初始化的是用户tcp连接程序
public class UserConnectInit implements ServletContextListener {
	
	private static final Logger logger = Logger.getLogger(UserConnectInit.class);
	
	Thread userConnectController = null;
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		if(userConnectController == null){
			userConnectController=new UserConnectFrontController();
			userConnectController.start();
			logger.info("总tcp连接线程已开起");
		}
		
		//测试程序是否还在运行
		new Thread(){
			public void run(){
				logger.info("******【监控服务端是否在运行的程序已经开启】******");
				try {
					while(true){
						Thread.sleep(1000*60*60); //每小时输出一次
						logger.info("【当前时间是：】"+new Date());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if(userConnectController!=null && !userConnectController.isInterrupted()){
			userConnectController.interrupt();
			logger.info("总tcp连接线程已经关闭");
		}
	}

}
