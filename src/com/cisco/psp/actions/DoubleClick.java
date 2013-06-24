package com.cisco.psp.actions;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.cisco.psp.commons.AJAXWaiter;
import com.cisco.psp.commons.ConfigSetting;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.objects.Action;
import com.cisco.psp.testrunner.InvalidTestCaseException;

public class DoubleClick {
	boolean status = true;
	String waiting = "";
	String type = "";
	String value = null;
	String xpath = null;
	String label = null;
	String site = "";
	int timeout = 1800;

	public DoubleClick(Action action) {
		parseAction(action);
	}

	public void startTest(final WebDriver driver, final boolean schedule) {
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<String> task = new Callable<String>() {
			public String call() {
				try {
					Logger.log(Logger.INFO, "Executing the Action.");
					execute(driver, schedule);
					return "Finished";
				} catch (InvalidTestCaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "Abort";
				}
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

	public void execute(WebDriver driver, boolean schedule) throws InvalidTestCaseException {		
		// Click the link
		if (type.toLowerCase().equals("link")) {
			WebElement ele = null;
			if (xpath != null) {
				waiting = AJAXWaiter.waitForXpath(driver, xpath, 5);
				if (waiting.equals("Done")) {
					ele = driver.findElement(By.xpath(xpath));
					Actions action = new Actions(driver);
					action.doubleClick(ele).build().perform();					
					Logger.log(Logger.INFO, "Double Clicked on: " + xpath);
				} else {
					Logger.log(Logger.ERROR, "Cannot find element with xpath: " + xpath);
					ele = null;
					status = false;
				}
			} else {
				missingXpath();
			}
		} else if (type.toLowerCase().equals("row")) {
			WebElement ele = null;
			String rowValue = "";
			if (xpath != null && !xpath.equalsIgnoreCase("")) {
				List<WebElement> rowList = driver.findElements(By.xpath(xpath));
				for (int i = 0; i < rowList.size(); i++) {
					rowValue = rowList.get(i).getText();
					rowValue = rowValue.replace('\n', ' ');
					if (rowValue.contains(value)) {
						ele = rowList.get(i);
						break;
					} else
						ele = null;
				}
			} else if (site == null || site.equalsIgnoreCase("normal") || site.equalsIgnoreCase("regular")
					|| site.equalsIgnoreCase("")) {
				List<WebElement> rowList = driver.findElements(By.xpath(ConfigSetting.getRegularSiteRowsXPath()));
				for (int i = 0; i < rowList.size(); i++) {
					rowValue = rowList.get(i).getText();
					rowValue = rowValue.replace('\n', ' ');
					if (rowValue.contains(value)) {
						ele = rowList.get(i);
						break;
					} else
						ele = null;
				}
			} else if (site != null && site.equalsIgnoreCase("mobile")) {
				List<WebElement> rowList = driver.findElements(By.xpath(ConfigSetting.getMobileSiteRowsXPath()));
				for (int i = 0; i < rowList.size(); i++) {
					rowValue = rowList.get(i).getText();
					rowValue = rowValue.replace('\n', ' ');
					if (rowValue.contains(value)) {
						ele = rowList.get(i);
						break;
					} else
						ele = null;
				}
			}
			if (ele == null) {
				Logger.log(Logger.ERROR, "Cannot find row with value: " + value + " the xpath is: " + xpath);
				Logger.log(Logger.ERROR, "Page source: \n" + driver.getPageSource());
				status = false;
			} else {
				Actions action = new Actions(driver);
				action.doubleClick(ele).build().perform();
				Logger.log(Logger.INFO, "Double Clicked on row: " + ((value == null) ? xpath : rowValue));
			}

		}
	}

	private void missingXpath() throws InvalidTestCaseException {
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
		site = action.getParam("site");
	}
}
