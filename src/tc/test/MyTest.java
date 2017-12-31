package tc.test;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import tc.configure.Configure;
import tc.util.JsonData;

public class MyTest {

	public static void main(String[] args) {
		new MyTest().shuffle();
	}

	public void shuffle() {
		Random random = new Random();
		int delay = random.nextInt(5)+5;
		System.out.println(delay);
	}				
}
