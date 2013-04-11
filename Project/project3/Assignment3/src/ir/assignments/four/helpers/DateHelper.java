package ir.assignments.four.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

	public static String getExpirationDate(int secondsFromNow) {
		SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");		
		Calendar cal = Calendar.getInstance();		
		cal.add(Calendar.SECOND, secondsFromNow);
		
		Date expirationDate = cal.getTime(); 
		return format.format(expirationDate);
	}

}
