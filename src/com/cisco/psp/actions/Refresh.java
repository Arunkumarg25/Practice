package com.cisco.psp.actions;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.document.ViewResult;
import org.openqa.selenium.WebDriver;

import com.cisco.psp.commons.Logger;
import com.cisco.psp.objects.Action;

public class Refresh {
	boolean status = true;	
	int timeout = 1800;

	public Refresh(Action action) {
		parseAction(action);
	}

	public void startTest( final WebDriver driver, final ViewResult<BaseDocument> doc, final boolean schedule) {
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<String> task = new Callable<String>() {
			public String call() {
				Logger.log(Logger.INFO, "Executing the Action.");
				execute(driver, doc, schedule);
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
			Logger.log(Logger.ERROR, "Time is over "+timeout+" Seconds. Action stopped\n"+ex.getMessage());
			status = false;
			future.cancel(true);
		} catch (InterruptedException e) {
			Logger.log(Logger.ERROR, "InterruptedException is captured: \n" + e.getMessage());	
			status = false;
			future.cancel(true);
		} catch (ExecutionException e) {
			Logger.log(Logger.ERROR, "ExecutionException is captured: \n" + e.getMessage());
			status = false;
			future.cancel(true);
		} 
	}

	public void execute(WebDriver driver, ViewResult<BaseDocument> doc, boolean schedule) {
		if (timeout == 0)
			timeout = 30;
		Logger.log(Logger.INFO, "Start to refresh current page.");		driver.get(driver.getCurrentUrl());
		
	}

	public boolean getStatus() {
		return status;
	}

	private void parseAction(Action act) {		
		timeout = act.getTimeout();
	}
}
