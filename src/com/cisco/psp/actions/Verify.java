package com.cisco.psp.actions;

import java.util.ArrayList;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.cisco.psp.commons.AJAXWaiter;
import com.cisco.psp.commons.ConfigSetting;
import com.cisco.psp.commons.DownloadFile;
import com.cisco.psp.commons.HTMLSourceParser;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.objects.Action;
import com.cisco.psp.testrunner.InvalidTestCaseException;

public class Verify {
	boolean status = true;
	String waiting = "";
	String errorMessage = "";

	String type, value, label, xpath, noValue, searchstring = null;
	int timeout = 1800;
	Action action = null;

	public Verify(Action act) {
		parseAction(act);
		action = act;
	}

	public void startTest( final WebDriver driver, final boolean schedule) {
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<String> task = new Callable<String>() {
			public String call() {
				try {
					Logger.log(Logger.INFO, "Executing the Action.");
					execute(driver, schedule);					
					return "Finished";
				} catch (InvalidTestCaseException e) {					
					Logger.log(Logger.ERROR, "InvalidTestCaseException is catched:\n"+e.getLocalizedMessage());
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
		} catch (TimeoutException e) {
			Logger.log(Logger.ERROR, "Time is over "+timeout+" Seconds. Action stopped:\n"+ e.getMessage());
			status = false;
			future.cancel(true);
		} catch (InterruptedException e) {
			Logger.log(Logger.ERROR, "InterruptedException is captured: \n" + e.getMessage());
			status = false;
			future.cancel(true);
		} catch (ExecutionException e) {
			Logger.log(Logger.ERROR, "InterruptedException is captured: \n" + e.getMessage());			
			status = false;
			future.cancel(true);
		} 
	}
	
	public void execute(WebDriver driver, boolean schedule) throws InvalidTestCaseException {
		// If the type is filename, then verify the downloaded file's name
		try {
			if (type.equalsIgnoreCase("filename")) {
				String destFolder = ConfigSetting.getDownloadDest();
				if (DownloadFile.verifyFileName(value, destFolder)) {
					Logger.log(Logger.INFO, "File Name: " + value + " has been verified.");
				} else {
					Logger.log(Logger.ERROR, "File Name: " + value + " is incorrect.");
					status = false;					
				}
			}

			// if the type is filesize, then verify the downloaded file is not
			// empty
			else if (type.equalsIgnoreCase("filesize")) {
				String destFolder = ConfigSetting.getDownloadDest();
				if (DownloadFile.verifyFileSize(value, destFolder)) {
					Logger.log(Logger.INFO, "File size is not zero.");
				} else {
					Logger.log(Logger.ERROR, "File size is zero. Please check the file.");
					status = false;					
				}
			}

			else if (type.equalsIgnoreCase("verifyCSV")) {
				String destFolder = ConfigSetting.getDownloadDest();
				if (DownloadFile.verifyCSV(value, destFolder, searchstring)) {
					Logger.log(Logger.INFO, "This CSV file contains: " + searchstring);
				} else {
					Logger.log(Logger.ERROR, "This CSV file does not contain: " + searchstring);
					status = false;
				}
			}

			// else if the verify type is a text field, then verify the text in
			// the
			// text field is correct
			else if (type.toLowerCase().equals("text field") || type.toLowerCase().equals("textfield")) {
				if (xpath != null) {
					String contentFromPage = driver.findElement(By.xpath(xpath)).getAttribute("value");
					if (contentFromPage.equals(value)) {
						Logger.log(Logger.INFO, "Verify: field " + xpath + " is " + contentFromPage + " (correct)");
					} else {
						Logger.log(Logger.ERROR, "Verify: field " + xpath + " is: " + contentFromPage
								+ "\ninstead of: " + value);
						status = false;
					}
				} else {
					missingXpath();
				}
			}
			
			// verifying page could be either an new open page or the current
			// page
			else if (type.toLowerCase().equals("page") || type.toLowerCase().equals("url")) {								
				if (driver.getCurrentUrl().contains(value)) {
					Logger.log(Logger.INFO, "Current open window is: " + driver.getCurrentUrl());					
				} else {
					Logger.log(Logger.INFO, "currentURL does not have the value.");
					HashSet<String> windows = (HashSet<String>) driver.getWindowHandles();
					String currentWindow = driver.getWindowHandle();
					Iterator<String> it = windows.iterator();
					while (it.hasNext()) {
						Logger.log(Logger.INFO, "Switching window." + driver.getCurrentUrl());
						driver.switchTo().window(it.next());
						String url = driver.getCurrentUrl();
						if (url.contains(value)) {
							Logger.log(Logger.INFO, "Found open window: " + driver.getCurrentUrl());
							driver.switchTo().window(currentWindow);
							return;
						} else {
							Logger.log(Logger.INFO,
									"The given URL is not found. Found another open window: " + driver.getCurrentUrl());
						}
					}
					status = false;
					Logger.log(Logger.ERROR,
							"Did not find open window: " + value + "\n" + "Current URL is: " + driver.getCurrentUrl());
				}
			
			//
			} else if (type.toLowerCase().equals("tabletext") || type.toLowerCase().equals("table text")) {				
				waiting = AJAXWaiter
						.waitForXpath(driver, "//tr[td[contains(text(), '" + label + "')]]//td[2]", 3);
				if (waiting.equals("Fail")) {
					Logger.log(Logger.ERROR, "Could not find the label: " + label);
					status = false;
					return;
				}
				WebElement textElement = driver.findElement(By.xpath("//tr[td[contains(text(), '" + label
						+ "')]]//td[2]"));
				String text = textElement.getText();
				Logger.log(Logger.INFO, "Verifying table text for label: " + label);
				verifyRegexText(text, value);
			
			//
			} else if (type.toLowerCase().equals("text")) {
				if (xpath == null) {
					String source = driver.getPageSource();
					source = source.split("<body")[1];
					source = HTMLSourceParser.takeOutElements(source, "script");
					source = HTMLSourceParser.takeOutElements(source, "style");
					String text = "";
					boolean start = false;
					for (int i = 0; i < source.length(); i++) {
						if (source.charAt(i) == '>') {
							start = true;
							continue;
						}
						if (start && source.charAt(i) == '<') {
							start = false;
							continue;
						}
						if (start) {
							if (source.charAt(i) == '\n' || source.charAt(i) == '\t')
								continue;
							text += source.charAt(i);
						}
					}
					verifyRegexText(text, value);
				} else {
					waiting = AJAXWaiter.waitForXpath(driver, xpath, 10);
					if (waiting.equals("Fail")) {
						Logger.log(Logger.ERROR, "Could not find the element with given xpath: " + xpath);
						status = false;
						return;
					}
					WebElement textElement = driver.findElement(By.xpath(xpath));
					String text = textElement.getText();
					verifyRegexText(text, value);
				}
			} else if (type.toLowerCase().equals("link")) {
				ArrayList<WebElement> links = (ArrayList<WebElement>) driver.findElements(By.tagName("a"));
				for (int i = 0; i < links.size(); i++) {
					if (verifyLink(links.get(i).getText(), value)) {
						Logger.log(Logger.INFO, "Found link with text: " + value);
						return;
					}
				}
				Logger.log(Logger.ERROR, "Could not find link with text: " + value);
				status = false;
			
			//
			} else if (type.toLowerCase().equals("select")) {
				if (xpath != null) {
					AJAXWaiter.waitForXpath(driver, xpath, 3);
					Select select = new Select(driver.findElement(By.xpath(xpath)));
					String selectedValue = select.getFirstSelectedOption().getText();
					if (selectedValue.equals(value)) {
						Logger.log(Logger.INFO, "Verify: select " + xpath + " is " + selectedValue + " (correct)");
					} else {
						Logger.log(Logger.ERROR, "Verify: select " + xpath + " is: " + selectedValue + "\ninstead of: "
								+ value);
						status = false;
					}
				} else {
					missingXpath();
				}

			} else if (type.toLowerCase().equals("row")) {
				if (noValue != null) {
					if (xpath != null) {
						List<WebElement> rowList = driver.findElements(By.xpath(xpath));
						for (int i = 0; i < rowList.size(); i++) {
							AJAXWaiter.waitForElement(driver, rowList.get(i), 3);
							String rowValue = rowList.get(i).getText();
							rowValue = rowValue.replace('\n', ' ');
							if (rowValue.contains(noValue)) {
								Logger.log(Logger.ERROR, "Found value: " + noValue + " at row: " + (i + 1));
								status = false;
								return;
							}
						}
						Logger.log(Logger.INFO, "Did not find: " + noValue + " in any rows on this page");
					} else {
						missingXpath();
					}
				} else if (value != null) {
					if (xpath != null) {
						List<WebElement> rowList = driver.findElements(By.xpath(xpath));
						String rowValue = "";
						for (int i = 0; i < rowList.size(); i++) {
							AJAXWaiter.waitForElement(driver, rowList.get(i), 3);
							rowValue = rowValue + rowList.get(i).getText();
							rowValue = rowValue.replace('\n', ' ');
							if (rowValue.contains(value)) {
								Logger.log(Logger.INFO, "Found value: " + value + " at row: " + (i + 1));
								return;
							}
						}
						Logger.log(Logger.ERROR, "Did not find: " + value + " in any rows on this page");
						status = false;						
					} else {
						missingXpath();
					}
				}
			//
			} else if (type.toLowerCase().equals("element")) {
				if (xpath != null) {
					waiting = AJAXWaiter.waitForXpath(driver, xpath, 3);
					if (waiting.equals("Done")) {
						Logger.log(Logger.INFO, "Found element with xpath: " + xpath);
					} else {
						Logger.log(Logger.ERROR, "Could not find element with xpath: " + xpath);
						status = false;
					}
				} else {
					missingXpath();
				}
			} 
			
			//
			else {
				Logger.log(Logger.ERROR, "Action verify with type: " + type + " is not supported!");
				status = false;
			}
		} catch (StaleElementReferenceException e) {
			if (!action.isInLoop()) {
				Logger.log(Logger.ERROR, "StaleElementReferenceException: " + "Element not found in the cache - "
						+ "perhaps the page has changed since it was looked up");
				Logger.log(Logger.ERROR, "Use a loop on this verify action to ensure stability");
				status = false;
			} else {
				Logger.log(Logger.ERROR, "StaleElementReferenceException: " + "Element not found in the cache - "
						+ "perhaps the page has changed since it was looked up");
				status = false;
			}
		} catch (NoSuchElementException e) {
			if (!action.isInLoop()) {
				Logger.log(Logger.ERROR, "NoSuchElementException");
				Logger.log(Logger.ERROR, "Use a loop on this verify action to ensure stability");
				status = false;
			} else {
				Logger.log(Logger.ERROR, "NoSuchElementException");
				status = false;
			}
		}
	}

	public boolean getStatus() {
		return status;
	}

	private void parseAction(Action act) {
		type = act.getParam("type");
		value = act.getParam("value");
		label = act.getParam("label");
		xpath = act.getParam("xpath");
		noValue = act.getParam("novalue");
		timeout = act.getTimeout();
		searchstring = act.getParam("searchstring");
	}

	private void missingXpath() throws InvalidTestCaseException {
		Logger.log(Logger.ERROR, "xpath paramtype cannot be null");
		status = false;
		throw new InvalidTestCaseException();
	}

	private void verifyRegexText(String text, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		boolean found = false;
		if (matcher.find()) {
			found = true;
		}
		if (found) {
			Logger.log(Logger.INFO, "Found text: " + text + "\nRegex passed");
		} else {
			Logger.log(Logger.ERROR, "Could not match regex: " + regex + " in text: " + text);
			status = false;
		}
	}

	private boolean verifyLink(String linkText, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(linkText);
		boolean found = false;
		if (matcher.find()) {
			found = true;
		}
		if (found) {
			return true;
		} else {
			return false;
		}
	}
}
