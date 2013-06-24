package com.cisco.psp.actions;

import org.openqa.selenium.WebDriver;

import com.cisco.psp.commons.AJAXWaiter;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.objects.Action;

public class Wait {
	boolean status = true;
	
	String type, value = "";
	
	public Wait(Action action){
		parseAction(action);
	}
	
	public void startTest(WebDriver driver, boolean schedule){
		Logger.log(Logger.INFO, "Sleeping for: "+value+" "+type);
		if (type.toLowerCase().equals("seconds")){
			AJAXWaiter.forceSleep(Integer.parseInt(value)*1000);
		}else if(type.toLowerCase().equals("minutes")){
			AJAXWaiter.forceSleep(Integer.parseInt(value)*60000);
		}else if(type.toLowerCase().equals("miliseconds")){
			AJAXWaiter.forceSleep(Integer.parseInt(value));
		}else if(type.toLowerCase().equals("hours")){
			AJAXWaiter.forceSleep(Integer.parseInt(value)*60*60*1000);
		}
	}
	
	public boolean getStatus(){
		return status;
	}
	
	private void parseAction(Action act){
		type=act.getParam("type");
		value = act.getParam("value");
	}
}
