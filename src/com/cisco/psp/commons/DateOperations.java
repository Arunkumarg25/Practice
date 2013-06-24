/**
 * DateOperations - this class is used for formatting date strings, getting the date time now and
 * returning it as a string
 * 
 * @author jearnold
 * Last Modified: 4/13/2011
 */
package com.cisco.psp.commons;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateOperations {
	
	/**
	 * Gets the date now in the format MM-dd-yyyy_HH-mm-ss.
	 * Since this is used in file name, so there can only be dashes
	 * @return the formatted date time string
	 */
	public static String getDateTimeString(){
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	/**
	 * Gets the date now in the format MM-dd-yyyy, this does not include time
	 * Since this is used in the file name, so there can only be dashes
	 * @return the formatted date string
	 */
	public static String getDateString(){
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}
	/**
	 * Gets the date now in the format passed in by user
	 * @param format - the format of the date string to be returned
	 * @return the formatted date string
	 */
	public static String getDateString(String format){
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	/**
	 * Add the 0 in front of the month and day if they are only one digit
	 * @param date - the date string that needs to be formatted
	 * @return the date string that is formatted to mm/dd/yyyy
	 */
	public static String fixDateString(String date){
		String month, day, year;
		if(date.length() < 10){
			String[] dateArray = date.split("/");
			if(dateArray[0].length()<2)
				month = "0"+dateArray[0];
			else
				month = dateArray[0];
			
			if(dateArray[1].length()<2)
				day = "0"+dateArray[1];
			else
				day = dateArray[1];
			
			year = dateArray[2];
			
			return month+"/"+day+"/"+year;
		}else
			return date;
	}
}
