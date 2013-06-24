package com.cisco.psp.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.WebDriver;

import com.cisco.psp.commons.Logger;
import com.cisco.psp.objects.Action;
import com.cisco.psp.testrunner.InvalidTestCaseException;

public class Goto {
	boolean status = true;
	String waiting = "";
	String errorMessage = "";
	int timeout=1800;
	String type, value = "";
	Map<String, String> urlMap = new HashMap<String, String>();
	
	
	public Goto(Action action){
		parseAction(action);
		urlMap.put("overview", "index.html");
		urlMap.put("delivery rules", "notifications.html");
		urlMap.put("delivery contacts", "contacts.html");
		urlMap.put("events", "events.html");
		urlMap.put("report listing", "reports.html");
		urlMap.put("report schedule", "report_schedule.html");
		urlMap.put("agent overview", "agent_overview.html");
		urlMap.put("invite agent", "agent_invite.html");
		urlMap.put("pending agents", "pending_agents.html");
		urlMap.put("approved agents", "agents.html");
		urlMap.put("rejected agents","rejected_agents.html");
		urlMap.put("contact information", "contacts.html");
		urlMap.put("warranty information", "warranty.html");
		urlMap.put("hardware end of life", "hardware.html");
		urlMap.put("software end of life", "software.html");
		urlMap.put("product security advisories", "psirt.html");
		urlMap.put("field notices", "field_notice.html");
		urlMap.put("firmware", "firmware.html");
		urlMap.put("account", "account.html");
	}
	
	public void startTest( final WebDriver driver, final boolean schedule) {
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<String> task = new Callable<String>() {
			public String call() {
				execute(driver, schedule);
				return "Finished";
			}
		};
		Future<String> future = executor.submit(task);
		try {
			String result = future.get(timeout, TimeUnit.SECONDS);
			if(!result.equalsIgnoreCase("Finished")){				
				Logger.log(Logger.ERROR, "Error occured at calling ece method.");
				status = false;
			}
		} catch (TimeoutException ex) {
			Logger.log(Logger.ERROR, "Time is over "+timeout+" Seconds. Action stopped");
			status = false;
			future.cancel(true);
		} catch (InterruptedException e) {
			status = false;
			future.cancel(true);
		} catch (ExecutionException e) {
			status = false;
			future.cancel(true);
		} 
	}
	
	public void execute(WebDriver driver, boolean schedule){
		String url = driver.getCurrentUrl();
		if(type.toLowerCase().equals("page")){
			if(urlMap.containsKey(value.toLowerCase())){
				String goToUrl = urlMap.get(value.toLowerCase());
				url = url.replaceAll("\\w+\\.html", goToUrl);
				driver.get(url);
				Logger.log(Logger.INFO, "Went to page: "+value);
			}else{
				Logger.log(Logger.ERROR, "Cannot find page: "+value);
				status = false;
			}
		}
	}
	
	public boolean getStatus(){
		return status;
	}
	
	private void parseAction(Action act){
		type = act.getParam("type");
		value = act.getParam("value");
		timeout = act.getTimeout();
	}
}