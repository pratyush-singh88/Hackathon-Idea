/**
 * 
 */
package com.bangalorewest.bagtracker.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author sudhanshu.singh
 *
 */
public class DateTimeUtil {

	public static Date parseStringDate(String date) {
		Date utilDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyy");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			utilDate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return utilDate;
	}

	public static String formatDateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyy");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public static Date parseStringDate(String date, String format) {
		Date utilDate = null;
		//SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyy");
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			utilDate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return utilDate;
	}
	
	public static String formatDateToString(Date date, String format) {
		//SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyy");
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static String getCurrentTime(String format) {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String time = sdf.format(date);
		
		return time;
	}

}
