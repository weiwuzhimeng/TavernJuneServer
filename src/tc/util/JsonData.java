package tc.util;

import java.util.ArrayList;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;


public class JsonData {

	public static JSONObject createJsonObject(String msgType, int msg1) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("msgType", msgType);
			jsonObject.put("msg1", msg1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	// 格式：消息类型+下个用户名+上个英雄名
	public static JSONObject createJsonObject2(String msgType, String msg1, String msg2, int msg3) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("msgType", msgType);
			jsonObject.put("msg1", msg1);
			jsonObject.put("msg2", msg2);
			jsonObject.put("msg3", msg3);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	// 用于通知下一个选择英雄的玩家是谁 格式:消息类型+下一个选择英雄的玩家名字+已经选择英雄的玩家数
	public static JSONObject createJsonObject3(String msgType, String msg1, int msg2) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("msgType", msgType);
			jsonObject.put("msg1", msg1);
			jsonObject.put("msg2", msg2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	// 将每个玩家的英雄和中立英雄广播出去
	public static JSONObject createJsonObject4(String msgType, String msg1, ArrayList<String> list) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("msgType", msgType);
			jsonObject.put("msg1", msg1);
			//
			JSONArray jsonArray = JSONArray.fromObject(list);
			jsonObject.put("msg2", jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public static JSONObject createJsonObject5(String msgType, String msg1) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("msgType", msgType);
			jsonObject.put("msg1", msg1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public static JSONObject createJsonObject6(String msgType, String msg1, String msg2, int msg3, int msg4) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("msgType", msgType);
			jsonObject.put("msg1", msg1);
			jsonObject.put("msg2", msg2);
			jsonObject.put("msg3", msg3);
			jsonObject.put("msg4", msg4);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public static JSONObject createJsonObject7(String msgType,String str,Map<String, String> userHeroMap) {
		JSONObject jsonObject = new JSONObject();
		try {
			//数据类型
			jsonObject.put("msgType", msgType);
			//msg1
			jsonObject.put("msg1", str);
			//msg2
			JSONObject jsonObject2 = JSONObject.fromObject(userHeroMap);
			jsonObject.put("msg2", jsonObject2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	public static JSONObject createJsonObject8(String msgType,String str,Map<String, String> userHeroMap,ArrayList<String> list) {
		JSONObject jsonObject = new JSONObject();
		try {
			//数据类型
			jsonObject.put("msgType", msgType);
			//msg1
			jsonObject.put("msg1", str);
			//msg2
			JSONObject jsonObject2 = JSONObject.fromObject(userHeroMap);
			jsonObject.put("msg2", jsonObject2);
			//msg3
			JSONArray jsonArray = JSONArray.fromObject(list);
			jsonObject.put("msg3", jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	public static JSONObject createJsonObject8(String msgType,short[] buffer){
		JSONObject jsonObject = new JSONObject();
		try {
			//数据类型
			jsonObject.put("msgType", msgType);
			//msg1
			jsonObject.put("msg1", buffer);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	public static JSONObject createJsonObject9(String msgType,String msg1,String msg2,String msg3){
		JSONObject jsonObject = new JSONObject();
		try {
			//数据类型
			jsonObject.put("msgType", msgType);
			//msg1
			jsonObject.put("msg1", msg1);
			jsonObject.put("msg2", msg2);
			jsonObject.put("msg3", msg3);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
}
