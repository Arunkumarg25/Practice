package com.cisco.psp.actions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.document.ViewResult;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cisco.psp.commons.Logger;
import com.cisco.psp.objects.Action;
import com.cisco.psp.testrunner.InvalidTestCaseException;

public class ManageWindow {
	boolean status = true;
	String waiting = "";
	String type, title, position = null;
	int timeout = 1800;

	public ManageWindow(Action action) {
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
		if (type.equals("switch")) {
			HashSet<String> windows = (HashSet<String>) driver.getWindowHandles();
			String currentWindow = driver.getWindowHandle();
			Iterator<String> it = windows.iterator();
			while (it.hasNext()) {
				driver.switchTo().window(it.next());
				String window_title = driver.getTitle();
				if (window_title.contains(title)) {
					Logger.log(Logger.INFO, "Switched to window with title: " + window_title);
				}
			}
			driver.switchTo().window(currentWindow);
			Logger.log(Logger.ERROR, "Cannot find open window with title: " + title);
			status = false;
			
		} else if (type.equalsIgnoreCase("switchtoframe")) {
			if (position == null || position == "") {
				driver.switchTo().defaultContent();
				List<WebElement> webeles = driver.findElements(By.tagName("Frame"));
				for (WebElement webele : webeles) {
					if (webele.getAttribute("name").equalsIgnoreCase(title)) {
						driver = driver.switchTo().frame(webele);
						Logger.log(Logger.INFO, "Switched to frame with name: " + title);
						break;
					}
				}
				
			} else {
				driver.switchTo().defaultContent();
				driver.switchTo().frame(Integer.parseInt(position));
				Logger.log(Logger.INFO, "Switched to frame with position: " + position);
			}

		} else if (type.equals("close")) {
			HashSet<String> windows = (HashSet<String>) driver.getWindowHandles();
			String currentWindow = driver.getWindowHandle();
			Iterator<String> it = windows.iterator();
			while (it.hasNext()) {
				driver.switchTo().window(it.next());
				String window_title = driver.getTitle();
				if (window_title.contains(title)) {
					driver.close();
					driver.switchTo().window(currentWindow);
					Logger.log(Logger.INFO, "Closed window with title: " + window_title);
				}
			}
			Logger.log(Logger.ERROR, "Cannot find open window with title: " + title);
			status = false;
		}		
	}

	public boolean getStatus() {
		return status;
	}

	private void parseAction(Action act) {
		type = act.getParam("type");
		title = act.getParam("title");
		timeout = act.getTimeout();
		position = act.getParam("position");
	}
}
