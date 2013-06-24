/**
 * AJAXWaiter - (Selenium) waits for ajax (asynchronous) operations and specific elements,
 * texts, text fields, etc. to be visible
 * 
 * @author jearnold
 * Last Modified: 1/11/2012
 */
package com.cisco.psp.commons;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AJAXWaiter {
	
	
	/**
	 * Waits for an element to appear after AJAX call by using the WebDriver method
	 * this uses the element that is passed in
	 * @param driver - the web driver
	 * @param ele - the element to wait for
	 * @param seconds - the number of seconds to wait
	 * @return
	 */
	public static String waitForElement(WebDriver driver, WebElement ele, int seconds){
		for(int i = 0; i<seconds*2; i++){
			try{
				driver.switchTo().defaultContent();
				if(ele.isDisplayed()){
					return "Done";
				}
			}catch(NoSuchElementException e){
				AJAXWaiter.forceSleep(500);
			}catch(StaleElementReferenceException e){
				AJAXWaiter.forceSleep(500);
			}
		}
		return "Fail";
	}
	
	/**
	 * Waits for an element to contain text given in value, this does not only wait for the element
	 * to show up, but also makes sure that the element contains "value" in its text
	 * @param driver - the web driver
	 * @param xpath - the xpath of the element 
	 * @param value - the text of the element
	 * @param seconds - the number of seconds to wait
	 * @return
	 */
	public static String waitForElementTextContainsByXpath(WebDriver driver, String xpath, String value, int seconds){
		for(int i = 0; i<seconds*2; i++){
			try{
				WebElement element = driver.findElement(By.xpath(xpath));
				if(element.isDisplayed()){
					if(element.getText().contains(value))
						return "Done";
					else{
						AJAXWaiter.forceSleep(500);
					}
				}else{
					AJAXWaiter.forceSleep(500);
				}
			}catch(NoSuchElementException e){
				AJAXWaiter.forceSleep(500);
			}catch(StaleElementReferenceException e){
				AJAXWaiter.forceSleep(500);
			}	
		}
		return "Fail";
	}
	
	/**
	 * Waits for an element to appear after AJAX call by using the WebDriver method
	 * this uses xpath to locate an element to wait for
	 * @param driver - the web driver
	 * @param xpath - the xpath of the element to wait for to show up
	 * @param seconds - the number of seconds to wait
	 * @return Done if element appears, Fail otherwise
	 */
	public static String waitForXpath(WebDriver driver, String xpath, int seconds){
		for(int i = 0; i<seconds*2; i++){
			try{				
				//driver.switchTo().defaultContent();
				WebElement element = driver.findElement(By.xpath(xpath));				
				if(element.isDisplayed()){
					Logger.log(Logger.INFO, "The element "+ element.getTagName() + "is displayed.");
					return "Done";
				}
			}catch(NoSuchElementException e){
				AJAXWaiter.forceSleep(500);
			}catch(StaleElementReferenceException e){
				AJAXWaiter.forceSleep(500);
			}
		}
		return "Fail";
	}
	
	/**
	 * Waits for an element to appear after AJAX call by using the WebDriver method
	 * this uses the id of the element to locate an element to wait for
	 * @param driver - the web driver
	 * @param id - the if of the element to wait for to show up
	 * @param seconds - the number of seconds to wait
	 * @return Done if element appears, Fail otherwise
	 */
	public static String waitForId(WebDriver driver, String id, int seconds){
		for(int i = 0; i<seconds*2; i++){
			try{
				WebElement element = driver.findElement(By.id(id));
				if(element.isDisplayed()){
					return "Done";
				}
			}catch(NoSuchElementException e){
				AJAXWaiter.forceSleep(500);
			}catch(StaleElementReferenceException e){
				AJAXWaiter.forceSleep(500);
			}
		}
		return "Fail";
	}
	
	/**
	 * Waits for an element to appear after AJAX call by using the WebDriver method
	 * this uses the linkText of the element to locate an element to wait for
	 * @param driver - the web driver
	 * @param linkText - the linkText of the element to wait for to show up
	 * @param seconds - the number of seconds to wait
	 * @return Done if element appears, Fail otherwise
	 */
	public static String waitForLinkText(WebDriver driver, String linkText, int seconds){
		for(int i = 0; i<seconds*2; i++){
			try{
				WebElement element = driver.findElement(By.linkText(linkText));
				if(element.isDisplayed()){
					return "Done";
				}
			}catch(NoSuchElementException e){
				AJAXWaiter.forceSleep(500);
			}catch(StaleElementReferenceException e){
				AJAXWaiter.forceSleep(500);
			}
		}
		return "Fail";
	}
	
	/**
	 * Waits for an element to appear after AJAX call by using the WebDriver method
	 * this uses the name of the element to locate an element to wait for
	 * @param driver - the web driver
	 * @param name - the name of the element to wait for to show up
	 * @param seconds - the number of seconds to wait
	 * @return Done if element appears, Fail otherwise
	 */
	public static String waitForName(WebDriver driver, String name, int seconds){
		for(int i = 0; i<seconds*2; i++){
			try{
				WebElement element = driver.findElement(By.name(name));
				if(element.isDisplayed()){
					return "Done";
				}
			}catch(NoSuchElementException e){
				AJAXWaiter.forceSleep(500);
			}catch(StaleElementReferenceException e){
				AJAXWaiter.forceSleep(500);
			}
		}
		return "Fail";
	}
	
	/**
	 * Waits for an element to appear after AJAX call by using the WebDriver method
	 * this uses the name of the element to locate an element to wait for
	 * @param driver - the web driver
	 * @param name - the name of the element to wait for to show up
	 * @param seconds - the number of seconds to wait
	 * @return Done if element appears, Fail otherwise
	 */
	public static String waitForTagName(WebDriver driver, String tagName, int seconds){
		for(int i = 0; i<seconds*2; i++){
			try{
				WebElement element = driver.findElement(By.tagName(tagName));
				if(element.isDisplayed()){
					return "Done";
				}
			}catch(NoSuchElementException e){
				AJAXWaiter.forceSleep(500);
			}catch(StaleElementReferenceException e){
				AJAXWaiter.forceSleep(500);
			}
		}
		return "Fail";
	}
	
	/**
	 * Waits for an element to go away after ajax call
	 * 
	 * @param driver - the web driver to use
	 * @param ele - the element to wait for to be gone
	 * @param seconds - the number of seconds to wait
	 * @return
	 */
	public static String waitForElementGone(WebDriver driver, WebElement ele, int seconds){
		for (int i = 0; i < seconds*2; i++){
			try{
				if(ele.isDisplayed()){
					AJAXWaiter.forceSleep(500);
				}
			}catch(NoSuchElementException e){
				return "Done";
			}catch(StaleElementReferenceException e){
				return "Done";
			}
		}
		return "Fail";
	}
	
	/**
	 * Waits for an element to go away after ajax call
	 * This methods uses the linkText to locate an element
	 * @param driver - the web driver to use
	 * @param linkText - the linkText of the element 
	 * @param seconds - the number of seconds to wait
	 * @return
	 */
	public static String webDriverWaitLinkTextGone(WebDriver driver, String linkText, int seconds){
		for(int i = 0; i<seconds*2; i++){
			try{
				WebElement element = driver.findElement(By.linkText(linkText));
				if(element.isDisplayed()){
					AJAXWaiter.forceSleep(500);
				}
			}catch(NoSuchElementException e){
				return "Done";
			}catch(StaleElementReferenceException e){
				return "Done";
			}
		}
		return "Fail";
	}
	
	/**
	 * Waits for an element to become visible after animation call by using the WebDriver method
	 * this uses the xpath of the element to locate an element to wait for
	 * @param driver - the web driver
	 * @param xpath - the xpath of the element to wait for to show up
	 * @param seconds - the number of seconds to wait
	 * @return Done if element appears, Fail otherwise
	 */
	public static String waitForElementVisibleByXpath(WebDriver driver, String xpath, int seconds){
		for(int i = 0; i<seconds*2; i++){
			try{
				WebElement element = driver.findElement(By.xpath(xpath));
				if(element.isDisplayed()){
					return "Done";
				}else{
					AJAXWaiter.forceSleep(500);
				}
			}catch(ElementNotVisibleException e){
				AJAXWaiter.forceSleep(500);
			}
		}
		return "Fail";
	}
	
	/**
	 * Waits for an element to become visible after some kind of animation call
	 * this uses the id of the element to locate an element to wait for
	 * @param driver - the web driver
	 * @param id - the id of the element to wait for to show up
	 * @param seconds - the number of seconds to wait
	 * @return Done if element appears, Fail other wise
	 */
	public static String waitForElementVisibleById(WebDriver driver, String id, int seconds){
		for(int i = 0; i<seconds*2; i++){
			try{
				WebElement element = driver.findElement(By.id(id));
				if(element.isDisplayed()){
					return "Done";
				}else{
					AJAXWaiter.forceSleep(500);
				}
			}catch(ElementNotVisibleException e){
				AJAXWaiter.forceSleep(500);
			}
		}
		return "Fail";
	}
	
	/**
	 * Waits for the URL to change, this happens when the user navigates to a different page
	 * @param driver - the web driver
	 * @param seconds - the number of seconds to wait
	 * @return
	 */
	public static String waitForURLChange(WebDriver driver, int seconds){
		Logger.log(Logger.INFO, "timeout: "+ seconds);
		String oldURL = driver.getCurrentUrl();
		for(int i = 0; i<seconds*2; i++){
			String newURL = driver.getCurrentUrl();
			Logger.log(Logger.INFO, "oldURL: "+ oldURL);
			Logger.log(Logger.INFO, "newURL: "+ newURL);
			if(oldURL.equals(newURL)){
				Logger.log(Logger.DEBUG, "Waiting for URL change");
			}
			else{
				return "Done";
			}
			AJAXWaiter.forceSleep(500);
		}
		return "Fail";
	}
	
	/**
	 * Waits for the URL to contain a specific String
	 * @param driver - the web driver
	 * @param contains - the String that the URL should contain before moving on
	 * @param seconds - the number of seconds to wait
	 * @return
	 */
	public static String waitForURLContains(WebDriver driver, String contains, int seconds){
		String url = "";
		for(int i = 0; i<seconds*2; i++){
			url = driver.getCurrentUrl();
			if(url.contains(contains))
				return "Done";
			else
				AJAXWaiter.forceSleep(500);
		}
		return "Fail";
	}
	
	/**
	 * Wait for the data table finish loading - the processing spinning widget stop spinning 
	 * @param driver - the web driver
	 * @param seconds - the number of seconds to wait
	 * @return
	 */
	public static String waitDataTableLoad(WebDriver driver, int seconds){
		String processing = "";
		String waiting = AJAXWaiter.waitForId(driver, "data_table_processing", 10);
		if(waiting.equals("Fail")){
			return "Fail";
		}
		for(int i = 0; i<seconds*2; i++){
			processing = driver.findElement(By.id("data_table_processing")).getAttribute("style");
			if(processing.contains("visible"))
				AJAXWaiter.forceSleep(500);
			else
				return "Done";
		}
		return "Fail";
	}
	
	/**
	 * Makes the tread sleep specified amount of time
	 * @param miliseconds - the amount of time to sleep
	 */
	public static void forceSleep(int miliseconds){
		try{
			Thread.sleep(miliseconds);
		}catch(InterruptedException ex){}
	}
}
