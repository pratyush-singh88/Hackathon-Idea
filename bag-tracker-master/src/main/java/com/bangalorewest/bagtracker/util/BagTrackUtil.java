package com.bangalorewest.bagtracker.util;

import com.bangalorewest.bagtracker.constants.MessageBuilderConstants;

public class BagTrackUtil {
	
	public static String airportCode(String flight) {
		String[] splitSegments = flight.split(MessageBuilderConstants.HASH.getMessage());
		String[] firstSplitSegment = splitSegments[0].split("-");
		return firstSplitSegment[2];
	}
	
	public static String airportCodeFromMessage(String message) {
		String[] splitSegments = message.split(MessageBuilderConstants.DOT_V.getMessage());
		String airport = splitSegments[1].substring(2, 5);
		return airport;
	}
	
	public static String monthInNumber(String month) {
		
		if (month.equalsIgnoreCase("Jan")) {
			return "01";
		}
		else if (month.equalsIgnoreCase("Feb")) {
			return "02";
		}
		else if (month.equalsIgnoreCase("Mar")) {
			return "03";
		}
		else if (month.equalsIgnoreCase("Apr")) {
			return "04";
		}
		else if (month.equalsIgnoreCase("May")) {
			return "05";
		}
		else if (month.equalsIgnoreCase("Jun")) {
			return "06";
		}
		else if (month.equalsIgnoreCase("Jul")) {
			return "07";
		}
		else if (month.equalsIgnoreCase("Aug")) {
			return "08";
		}
		else if (month.equalsIgnoreCase("Sep")) {
			return "09";
		}
		else if (month.equalsIgnoreCase("Oct")) {
			return "10";
		}
		else if (month.equalsIgnoreCase("Nov")) {
			return "11";
		}
		else if (month.equalsIgnoreCase("Dec")) {
			return "12";
		}
		
		return month;
	}

}
