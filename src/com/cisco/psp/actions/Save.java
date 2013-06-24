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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.cisco.psp.commons.AJAXWaiter;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.objects.Action;
import com.cisco.psp.testrunner.InvalidTestCaseException;

public class Save {
	boolean status = true;
	String waiting = "";
	String savevar, savemeta, savefile, xpath, activationID = null;
	int timeout = 1800;

	public Save(Action action) {
		parseAction(action);
	}

	public void startTest(final WebDriver driver, final ViewResult<BaseDocument> doc, final boolean schedule) {
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
			if (!result.equalsIgnoreCase("Finished")) {
				Logger.log(Logger.ERROR, "Error occured at calling ece method.");
				status = false;
			}
		} catch (TimeoutException ex) {
			Logger.log(Logger.ERROR, "Time is over " + timeout + " Seconds. Action stopped\n" + ex.getMessage());
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
	 * User wants to save variable to Couch
	 */
	public void execute(WebDriver driver, ViewResult<BaseDocument> doc, boolean schedule) {
		if (savevar != null && savevar != "" && xpath != null && xpath != "") {
			String value = "";
			waiting = AJAXWaiter.waitForXpath(driver, xpath, 5);
			if (activationID.equalsIgnoreCase("true")) {
				if (waiting.equals("Done")) {
					value = driver.findElement(By.xpath(xpath)).getText();
					// hard coded for grab the activation id
					value = value.substring(15, 79);
					if (doc.getProperty("variables") == null) {
						doc.setProperty("variables", new HashMap<String, String>());
					}
					@SuppressWarnings("unchecked")
					HashMap<String, String> variables = (HashMap<String, String>) doc.getProperty("variables");
					variables.put(savevar, value);
					doc.setProperty("variables", variables);
					Logger.log(Logger.INFO, "Saved variable: " + savevar + " with value: " + value);
				} else {
					Logger.log(Logger.ERROR, "Cannot find element: " + xpath);
					status = false;
				}
			} else if (waiting.equals("Done")) {
				value = driver.findElement(By.xpath(xpath)).getText();
				if (doc.getProperty("variables") == null) {
					doc.setProperty("variables", new HashMap<String, String>());
				}
				@SuppressWarnings("unchecked")
				HashMap<String, String> variables = (HashMap<String, String>) doc.getProperty("variables");
				variables.put(savevar, value);
				doc.setProperty("variables", variables);
				Logger.log(Logger.INFO, "Saved variable: " + savevar + " with value: " + value);
			} else {
				Logger.log(Logger.ERROR, "Cannot find element: " + xpath);
				status = false;
			}
		} else if (savemeta != null && savemeta != "" && xpath != null && xpath != "") {
			String value = "";
			waiting = AJAXWaiter.waitForXpath(driver, xpath, 5);
			if (waiting.equals("Done")) {
				value = driver.findElement(By.xpath(xpath)).getText();
				if (doc.getProperty("meta") == null) {
					doc.setProperty("meta", new HashMap<String, String>());
				}
				@SuppressWarnings("unchecked")
				HashMap<String, String> variables = (HashMap<String, String>) doc.getProperty("meta");
				variables.put(savemeta, value);
				doc.setProperty("meta", variables);
				Logger.log(Logger.INFO, "Saved meta: " + savemeta + " with value: " + value);
			} else {
				Logger.log(Logger.ERROR, "Cannot find element: " + xpath);
				status = false;
			}
		} else {
			status = false;
			Logger.log(Logger.ERROR,
					"The value given by \"savevar\" or \"xpath\" is empty. Please check the test case.");
		}
	}

	public boolean getStatus() {
		return status;
	}

	public void parseAction(Action action) {
		savevar = action.getParam("savevar");
		savefile = action.getParam("savefile");
		xpath = action.getParam("xpath");
		timeout = action.getTimeout();
		savemeta = action.getParam("savemeta");
		activationID = action.getParam("activationID");
	}
}
