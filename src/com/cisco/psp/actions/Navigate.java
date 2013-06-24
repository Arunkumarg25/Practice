package com.cisco.psp.actions;

import java.util.HashMap;
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

import com.cisco.psp.commons.AJAXWaiter;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.objects.Action;

public class Navigate {
	boolean status = true;
	String waiting = "";
	String url, loadvar, verify = null;
	boolean verifyurl = true;
	int timeout = 1800;

	public Navigate(Action action) {
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
		if (verify == null || verify.equals("true") || verify.equals("yes"))
			verifyurl = true;
		else if (verify.equals("fast") || verify.equals("no"))
			verifyurl = false;
		if (loadvar != null) {
			// load the url from couch variable
			@SuppressWarnings("unchecked")
			HashMap<String, String> variables = (HashMap<String, String>) doc.getProperty("variables");
			String value = variables.get(loadvar);
			if (value.equals("")) {
				Logger.log(Logger.ERROR, "Cannot find variable with name: " + loadvar);
				status = false;
				return;
			} else {
				driver.get(value);
				if (verifyurl) {
					waiting = AJAXWaiter.waitForURLContains(driver, value, timeout);
					if (waiting.equals("Fail")) {
						Logger.log(Logger.ERROR, "Failed to load url: " + value + " after " + timeout + " seconds");
						status = false;
					} else {
						Logger.log(Logger.INFO, "Navigated to: " + value);
					}
				} else {
					Logger.log(Logger.INFO, "Verify is false, sent request to navigate to url: " + url);
				}
			}
		} else {
			driver.get(url);
			if (verifyurl) {
				waiting = AJAXWaiter.waitForURLContains(driver, url, timeout);
				if (waiting.equals("Fail")) {
					Logger.log(Logger.ERROR, "Failed to load url: " + url + " after " + timeout + " seconds");
					status = false;
				} else {
					Logger.log(Logger.INFO, "Navigated to: " + url);
				}
			} else {
				Logger.log(Logger.INFO, "Verify is false, sent request to navigate to url: " + url);
			}
		}
	}

	public boolean getStatus() {
		return status;
	}

	private void parseAction(Action act) {
		url = act.getParam("url");
		loadvar = act.getParam("loadvar");
		verify = act.getParam("verify");
		timeout = act.getTimeout();
	}
}
