package com.cisco.psp.actions;

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
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cisco.psp.commons.AJAXWaiter;
import com.cisco.psp.commons.ConfigSetting;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.objects.Action;
import com.cisco.psp.testrunner.InvalidTestCaseException;

public class Click {
	boolean status = true;
	String waiting = "";
	String type = "";
	String value = null;
	String xpath = null;
	String label = null;
	String site = "";
	int timeout = 1800;

	public Click(Action action) {
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
		String method = "";
		// Click the link
		if (type.toLowerCase().equals("link")) {
			WebElement ele = null;
			if (xpath != null) {
				waiting = AJAXWaiter.waitForXpath(driver, xpath, 5);
				if (waiting.equals("Done")) {
					ele = driver.findElement(By.xpath(xpath));
					ele.click();
					Logger.log(Logger.INFO, "Clicked on: " + xpath);
				} else {
					Logger.log(Logger.ERROR, "Cannot find element with xpath: " + xpath);
					Logger.log(Logger.ERROR, "Page source: \n" + driver.getPageSource());
					ele = null;
					status = false;
				}
			} else {
				missingXpath();
			}
		}
		// Click the button
		else if (type.toLowerCase().equals("button")) {
			WebElement ele = null;
			if (xpath == null || xpath == "") {
				try {
					// this is used to click on buttons in an alert/prompt box
					// currently only support ok and cancel buttons
					driver.switchTo().alert();
					if (value.toLowerCase().trim().equals("ok")) {
						driver.switchTo().alert().accept();
						method = "alert prompt";
						Logger.log(Logger.INFO, "Clicked on button: " + value + " button, by " + method + " method");
					} else if (value.toLowerCase().trim().equals("cancel")) {
						driver.switchTo().alert().dismiss();
						method = "alert prompt";
						Logger.log(Logger.INFO, "Clicked on button: " + value + " button, by " + method + " method");
					}
				} catch (NoAlertPresentException e) {
					Logger.log(Logger.ERROR, "NoAlertPresentException is found: " + e.getMessage());
				}
			} else if (xpath != null) {
				waiting = AJAXWaiter.waitForXpath(driver, xpath, 5);
				if (waiting.equals("Done")) {
					ele = driver.findElement(By.xpath(xpath));
					ele.click();
					Logger.log(Logger.INFO, "Clicked on: " + xpath);
				} else {
					Logger.log(Logger.ERROR, "Cannot find element with xpath: " + xpath);
					Logger.log(Logger.ERROR, "Page source: \n" + driver.getPageSource());
					ele = null;
					status = false;
				}
			} else {
				missingXpath();
			}
		}
		// Click the checkbox
		else if (type.toLowerCase().equals("checkbox")) {
			WebElement ele = null;
			if (xpath != null) {
				waiting = AJAXWaiter.waitForXpath(driver, xpath, 5);
				if (waiting.equals("Done")) {
					ele = driver.findElement(By.xpath(xpath));
					method = "given xpath";
				} else {
					Logger.log(Logger.ERROR, "Cannot find element with xpath: " + xpath);
					Logger.log(Logger.ERROR, "Page source: \n" + driver.getPageSource());
					ele = null;
					status = false;
				}
			} else {
				missingXpath();
			}
			if (value.toLowerCase().equals("check") || value.toLowerCase().equals("yes")
					|| value.toLowerCase().equals("true")) {
				if (!ele.isSelected())
					ele.click();
			} else {
				if (ele.isSelected())
					ele.click();
			}
			Logger.log(Logger.INFO, value + "-ed checkbox " + xpath);
		}

		/**
		 * CLICK TYPE ROW. If site is set to normal or regular, it would search
		 * on normal site; if site is set to mobile, it would search on mobile
		 * site; if xpath is given and site is not given, it would use the xpath
		 * to search.
		 */
		else if (type.toLowerCase().equals("row")) {
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
			}
			if (ele == null) {
				Logger.log(Logger.ERROR, "Cannot find row with value: " + value + " the xpath is: " + xpath);
				Logger.log(Logger.ERROR, "Page source: \n" + driver.getPageSource());
				status = false;
			} else {
				ele.click();
				Logger.log(Logger.INFO, "Clicked on row: " + ((value == null) ? xpath : rowValue));
			}

		}
		// Click the menu
		else if (type.toLowerCase().equals("menu")) {
			WebElement ele = null;
			if (xpath != null) {
				waiting = AJAXWaiter.waitForXpath(driver, xpath, 5);
				if (waiting.equals("Done")) {
					ele = driver.findElement(By.xpath(xpath));
					method = "given xpath";
				} else {
					Logger.log(Logger.ERROR, "Cannot find element with given xpath: " + xpath);
					ele = null;
				}
			}
			if (ele == null) {
				waiting = AJAXWaiter
						.waitForXpath(driver, "//a[cufon[cufontext[contains(text(), '" + value + "')]]]", 5);
				if (waiting.equals("Done")) {
					ele = driver.findElement(By.xpath("//a[cufon[cufontext[contains(text(), '" + value + "')]]]"));
					method = "menu value";
				} else {
					ele = null;
				}
			}
			if (ele == null) {
				Logger.log(Logger.ERROR, "Cannot find menu item with value: " + value);
				status = false;
			} else {
				ele.click();
				Logger.log(Logger.INFO, "Clicked on menu: " + ((value == null) ? xpath : value) + ", by " + method
						+ " method");
			}
		}
		// Click submenu
		else if (type.toLowerCase().equals("submenu")) {
			WebElement ele = null;
			if (xpath != null) {
				waiting = AJAXWaiter.waitForXpath(driver, xpath, 5);
				if (waiting.equals("Done")) {
					ele = driver.findElement(By.xpath(xpath));
					method = "given xpath";
				} else {
					Logger.log(Logger.ERROR, "Cannot find element with given xpath: " + xpath);
					ele = null;
				}
			}
			if (ele == null) {
				waiting = AJAXWaiter.waitForXpath(driver, "//ul[@class='drop_down']/li[a[contains(text(), '" + value
						+ "')]]/a", 5);
				if (waiting.equals("Done")) {
					ele = driver.findElement(By.xpath("//ul[@class='drop_down']/li[a[contains(text(), '" + value
							+ "')]]/a"));
					method = "submenu value";
				} else {
					ele = null;
				}
			}
			if (ele == null) {
				Logger.log(Logger.ERROR, "Cannot find submenu item with value: " + value);
				status = false;
			} else {
				ele.click();
				Logger.log(Logger.INFO, "Clicked on submenu: " + ((value == null) ? xpath : value) + ", by " + method
						+ " method");
			}
		}
		// *********************************************************************
		// CLICK TYPE DRAWER HANDLE
		// *********************************************************************
		else if (type.toLowerCase().equals("drawer handle") || type.toLowerCase().equals("handle")) {
			WebElement ele = null;
			if (xpath != null) {
				waiting = AJAXWaiter.waitForXpath(driver, xpath, 5);
				if (waiting.equals("Done")) {
					ele = driver.findElement(By.xpath(xpath));
					method = "given xpath";
				} else {
					Logger.log(Logger.ERROR, "Cannot find element with given xpath: " + xpath);
					ele = null;
				}
			}
			if (ele == null) {
				waiting = AJAXWaiter.waitForXpath(driver, "//div[@id='drawer_handle']", 5);
				if (waiting.equals("Done")) {
					ele = driver.findElement(By.xpath("//div[@id='drawer_handle']"));
					method = "drawer handle";
					ele.click();
					Logger.log(Logger.INFO, "Clicked on drawer handle by " + method + " method");
				} else {
					ele = null;
					Logger.log(Logger.ERROR, "Cannot find drawer handle, make sure you hover over the row first");
					status = false;
				}
			}
		}

		// CLICK TYPE AREA, TOPOLOGY ITEM ON MOBILE WEB
		else if (type.toLowerCase().equals("area")) {
			WebElement ele = null;
			if (xpath != null && value == null) {
				waiting = AJAXWaiter.waitForXpath(driver, xpath, 5);
				if (waiting.equals("Done")) {
					ele = driver.findElement(By.xpath(xpath));
					method = "given xpath";
				} else {
					Logger.log(Logger.ERROR, "Cannot find element with xpath: " + xpath);
					ele = null;
				}
			} else if (xpath != null && value != null) {
				waiting = AJAXWaiter.waitForXpath(driver, xpath, 5);
				if (waiting.equals("Done")) {
					List<WebElement> eles = driver.findElements(By.xpath(xpath));
					for (int i = 0; i < eles.size(); i++) {
						waiting = AJAXWaiter.waitForElement(driver, eles.get(i), 5);
						if (waiting.equals("Done")) {
							if (value.equals(eles.get(i).getAttribute("title"))) {
								ele = eles.get(i);
								break;
							}
						} else {
							Logger.log(Logger.INFO, "Time out.");
							ele = null;
						}
					}
					method = "given value and xpath";
				} else {
					Logger.log(Logger.ERROR, "Cannot find these elements with given xpath: " + xpath);
					ele = null;
					status = false;
					return;
				}
			}
			String link = ele.getAttribute("href");
			ele.click();
			Logger.log(Logger.INFO, "Navigated to: " + link);
		}

		// CLICK TYPE IS NULL, NO TYPE GIVEN, FIND ELEMENT BY XPATH AND CLICK
		else if (type == null) {
			WebElement ele = null;
			if (xpath != null) {
				waiting = AJAXWaiter.waitForXpath(driver, xpath, 5);
				if (waiting.equals("Done")) {
					ele = driver.findElement(By.xpath(xpath));
					ele.click();
					Logger.log(Logger.INFO, "Clicked on: " + xpath);
				} else {
					Logger.log(Logger.ERROR, "Cannot find element with xpath: " + xpath);
					Logger.log(Logger.ERROR, "Page source: \n" + driver.getPageSource());
					ele = null;
					status = false;
				}
			} else {
				missingXpath();
			}
		}

		// UNKNOWN TYPE
		else {
			Logger.log(Logger.ERROR, "Action click with type: " + type + " is not supported!");
			status = false;
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
