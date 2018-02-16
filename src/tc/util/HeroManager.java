package tc.util;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.log4j.Logger;

import tc.configure.Configure;

//英雄操作类
public class HeroManager {

	private static final Logger logger = Logger.getLogger(HeroManager.class);

//	// 最多20人玩
//	int[] herosId = new int[Configure.PLAYER_COUNT + 3]; // 玩家数+共有牌数
	ArrayList<Integer> herosId = new ArrayList<Integer>();

	// 狼人问题待解决
	public ArrayList<String> heroSort(ArrayList<String> publicHeros) {
		heroToNum(publicHeros);
		sort(herosId);
		ArrayList<String> sortedHeros = numToHero(herosId);
		// 初次排序后，开始安排狼群的位置
		if (sortedHeros.contains("幽灵")) {
			sortedHeros.add(1, "狼群"); // 如果有幽灵，则狼群一定在第二个位置
		} else {
			sortedHeros.add(0, "狼群"); // 如果没有幽灵，则狼群在第一个位置
		}
		logger.info("排序之后的英雄集合sortedHeros：" + sortedHeros);
		return sortedHeros;
	}

	public void heroToNum(ArrayList<String> publicHeros) {

		for (int i = 0; i < publicHeros.size(); i++) {
			String heroname = publicHeros.get(i);
			switch (heroname) {
			case "幽灵":
				herosId.add(i, 1);
				logger.info("幽灵，1");
				break;
			case "狼Alpha":
				herosId.add(i, 2);
				break;
			case "预言家":
				herosId.add(i, 3);
				break;
			case "狼先知":
				herosId.add(i, 4);
				break;
			case "强盗":
				herosId.add(i, 5);
				logger.info("强盗，5");
				break;
			case "捣蛋鬼":
				herosId.add(i, 6);
				break;
			case "女巫":
				herosId.add(i, 7);
				break;
			case "失眠者":
				herosId.add(i, 8);
				break;
			default:
				logger.info("没有这个英雄");
				break;
			}
		}
		logger.info("映射的集合为(排序前)：" + herosId);
	}

	// 冒泡排序
	public ArrayList<Integer> sort(ArrayList<Integer> herosId) {
		Collections.sort(herosId);
		logger.info("开始输出");
		// 循环输出
		for (int s = 0; s < herosId.size(); s++) {
			logger.info("映射的数组为(排序后)：" + herosId.get(s));
		}
		return herosId;
	}

	public ArrayList<String> numToHero(ArrayList<Integer> herosId) {
		ArrayList<String> sortedHeros = new ArrayList<String>();
		for (int i = 0; i < herosId.size(); i++) {
			int index = herosId.get(i);
			switch (index) {
			case 1:
				sortedHeros.add(i, "幽灵");
				break;
			case 2:
				sortedHeros.add(i, "狼Alpha");
				break;
			case 3:
				sortedHeros.add(i, "预言家");
				break;
			case 4:
				sortedHeros.add(i, "狼先知");
				break;
			case 5:
				sortedHeros.add(i, "强盗");
				break;
			case 6:
				sortedHeros.add(i, "捣蛋鬼");
				break;
			case 7:
				sortedHeros.add(i, "女巫");
				break;
			case 8:
				sortedHeros.add(i, "失眠者");
				break;
			default:
				logger.info("数字错误，导致没有对应的英雄");
			}
		}
		logger.info("映射完的英雄集合是：" + sortedHeros);
		return sortedHeros;
	}
	
	//清理资源
	public void closeSource(){
		herosId.clear();
		logger.info("释放后的herosId："+herosId);
	}

	public static void main(String[] args) {
		ArrayList<String> playerHeros = new ArrayList<String>();
		playerHeros.add("失眠者");
		playerHeros.add("预言家");
		new HeroManager().heroSort(playerHeros);
	}

	// //不确定是否将该方法独立出来
	// public void numToHero(){
	//
	// }
}
