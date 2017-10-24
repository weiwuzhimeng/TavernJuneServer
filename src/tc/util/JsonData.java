package tc.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonData {

	public static JSONObject createJsonObject(String msgType,String msg1){
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("msgType", msgType);
			jsonObject.put("msg1", msg1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
}
