package tc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DIVDate {

	public static String divDate(Date date){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String str=df.format(date);
		return str;
	}
}
