package com.cisco.psp.actions;

import java.io.File;
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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.cisco.psp.commons.AJAXWaiter;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.objects.Action;
import com.cisco.psp.testrunner.InvalidTestCaseException;

public class Input {
	boolean status = true;
	String waiting = "";
	String method = "";
	String type = "";
	String label = "";
	String value = null;
	String xpath = null;
	int timeout = 1800;
	String loadvar = "";
	String loadmeta = "";
	
	public Input(Action action) {
		parseAction(action);
	}

	public void startTest( final WebDriver driver, final ViewResult<BaseDocument> doc, final boolean schedule) {
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<String> task = new Callable<String>() {
			public String call() {
				try {
					Logger.log(Logger.INFO, "Executing the Action.");
					execute(driver, doc, schedule);
					return "Finished";
				} catch (InvalidTestCaseException e) {					
					e.printStackTrace();
					return "Abort";
				}				
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
	/**
	 * Support input text, textfield, and select the option by the text 
	 * @param driver
	 * @param schedule
	 * @throws InvalidTestCaseException
	 */
	public void execute(WebDriver driver, ViewResult<BaseDocument> doc,boolean schedule)
			throws InvalidTestCaseException {		
		if (type.toLowerCase().equals("text field")) {
			WebElement ele = null;
			if (xpath != null) {
				waiting = AJAXWaiter.waitForXpath(driver, xpath, 5);
				if (waiting.equals("Done")) {
					ele = driver.findElement(By.xpath(xpath));
				} else {
					Logger.log(Logger.ERROR, "Cannot find element with xpath: "
							+ xpath);					
					status = false;
					ele = null;
				}
			} else {
				missingXpath();
			}
			if (ele != null) {
				if(loadvar != null && loadvar != ""){
					@SuppressWarnings("unchecked")
					HashMap<String, String> variables = (HashMap<String, String>) doc.getProperty("variables");
					String value = variables.get(loadvar);
					ele.clear();
					ele.sendKeys(value);
					Logger.log(Logger.INFO, "Typed: " + value + " from Couch into " + xpath
							+ " text field");
				}else if (loadmeta != null && loadmeta != ""){
					@SuppressWarnings("unchecked")
					HashMap<String, String> variables = (HashMap<String, String>) doc.getProperty("meta");
					String value = variables.get(loadmeta);
					ele.clear();
					ele.sendKeys(value);
					Logger.log(Logger.INFO, "Typed: " + value + " from Couch into " + xpath
							+ " text field");
				}else {
					ele.clear();
					ele.sendKeys(value);
					Logger.log(Logger.INFO, "Typed: " + value + " in " + xpath
							+ " text field");					
				}				
			}
		} else if (type.toLowerCase().equals("select")) {
			Select ele = null;
			if (xpath != null) {
				waiting = AJAXWaiter.waitForXpath(driver, xpath, 5);
				if (waiting.equals("Done")) {
					ele = new Select(driver.findElement(By.xpath(xpath)));
				} else {
					Logger.log(Logger.ERROR, "Cannot find element with xpath: "
							+ xpath);					
					status = false;
					ele = null;
				}
			} else {
				missingXpath();
			}
			if (ele == null) {
				Logger.log(Logger.ERROR, "Cannot find select: " + xpath);
				Logger.log(Logger.ERROR,
						"Page source: \n" + driver.getPageSource());
				status = false;
			} else {
				ele.selectByVisibleText(value);
				if (ele.getFirstSelectedOption().getText().equals(value)) {
					Logger.log(Logger.INFO, "Selected: " + value + " in "
							+ xpath);
				} else {
					Logger.log(Logger.ERROR, "Cannot find the option: " + value
							+ " in select: " + xpath);
					status = false;
				}
			}
		} else if (type.toLowerCase().equals("file")) {
			WebElement ele = null;
			File f = new File(value);
			String absPath = f.getAbsolutePath();
			if (xpath != null) {
				waiting = AJAXWaiter.waitForXpath(driver, xpath, 5);
				if (waiting.equals("Done")) {
					ele = driver.findElement(By.xpath(xpath));
				} else {
					ele = null;
				}
			}else{
				missingXpath();
			}
			if (ele == null) {
				Logger.log(Logger.ERROR, "Cannot find file input: " + xpath);				
				status = false;
			} else {
				ele.sendKeys(absPath);
				Logger.log(Logger.INFO, "Entered: " + absPath + " in " + xpath
						+ " file field");
			}
		} else {
			Logger.log(Logger.ERROR, "Action input with type: " + type
					+ " is not supported!");
			status = false;			
		}
	}

	private void missingXpath() throws InvalidTestCaseException{
		Logger.log(Logger.ERROR, "xpath paramtype cannot be null");
		status = false;
		throw new InvalidTestCaseException();
	}
	
	public boolean getStatus() {
		return status;
	}

	public void parseAction(Action action) {
		type = action.getParam("type");
		label = action.getParam("label");
		value = action.getParam("value");
		xpath = action.getParam("xpath");
		timeout = action.getTimeout();
		loadvar = action.getParam("loadvar");
		loadmeta = action.getParam("meta");
	}
}
