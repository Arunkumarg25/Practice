package com.cisco.psp.actions;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.cisco.psp.commons.AJAXWaiter;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.objects.Action;

public class Hover {
	boolean status = true;
	String waiting = "";
	String type = "";
	String value = "";
	int timeout = 1800;
	String xpath = "";

	public Hover(Action action) {
		parseAction(action);
	}

	public void startTest(final WebDriver driver, final boolean schedule) {
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<String> task = new Callable<String>() {
			public String call() {
				Logger.log(Logger.INFO, "Executing the Action.");
				execute(driver, schedule);
				return "Finished";
			}
		};
		Future<String> future = executor.submit(task);
		try {
			String result = future.get(timeout, TimeUnit.SECONDS);
			if (!result.equalsIgnoreCase("Finished")) {
				Logger.log(Logger.ERROR, "Error occured at calling ece method.");
				status = false;
			}
		} catch (TimeoutException ex) {
			Logger.log(Logger.ERROR, "Time is over " + timeout + " Seconds. Action stopped:\n" + ex.getMessage());
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

	public void execute(WebDriver driver, boolean schedule) {
		// int pageId = PageMappings.getCurrentPage(driver);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("var s=document.createElement('script');s.src='http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js';");
		// String method = "";
		if (type.toLowerCase().equals("englishmenu") || type.toLowerCase().equals("menu")) {
			AJAXWaiter.waitForXpath(driver, "//cufontext[contains ( text(), '" + value + "')]", 3);
			js.executeScript("$(document).ready(function(){" + "$('cufontext:contains(\"" + value
					+ "\")').parents('li').children('ul.drop_down').show();" + "});");
			AJAXWaiter.forceSleep(700);
			Logger.log(Logger.INFO, "Displayed sub-menu of: " + value);
		} else if (type.toLowerCase().equals("germanymenu") || type.toLowerCase().equals("frenchmenu")
				|| type.toLowerCase().equals("italianmenu") || type.toLowerCase().equals("spanishmenu")) {
			AJAXWaiter.waitForXpath(driver, "//span[contains ( text(), '" + value + "')]", 3);
			js.executeScript("$(document).ready(function(){" + "$('a:contains(\"" + value
					+ "\")').parents('li').children('ul.drop_down').show();" + "});");
			AJAXWaiter.forceSleep(700);
			Logger.log(Logger.INFO, "Displayed sub-menu of: " + value);
		} else if (type.toLowerCase().equals("row")) {
			js.executeScript("$(document).ready(function(){" + "$('td:contains(\"" + value
					+ "\")').parent().mouseover();" + "});");
			Logger.log(Logger.INFO, "Hovered over table row: " + value);
		} else if (type.toLowerCase().equals("link")) {
			if (value.toLowerCase().equals("customize")) {
				js.executeScript("$(document).ready(function(){" + "$('#panel').show();" + "});");
				AJAXWaiter.forceSleep(700);
				Logger.log(Logger.INFO, "Displayed customize options panel");
			}
		} else {
			Logger.log(Logger.ERROR, "Action hover with type: " + type + " is not supported!");
			status = false;
			return;
		}
	}

	public boolean getStatus() {
		return status;
	}

	public void parseAction(Action action) {
		type = action.getParam("type");
		value = action.getParam("value");
		timeout = action.getTimeout();
		xpath = action.getParam("xpath");
	}
}
