/**
 * Miscellaneous methods used by test cases that do not really have a commons class to belong to
 * 
 * @author jearnold
 * Last Modified: 01/25/2012
 */
package com.cisco.psp.commons;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Misc {
	
	/**
	 * Add www in front of brand, used to bypass certificate issues
	 * @param brand - the brand to login to
	 * @return brand with www added
	 */
	public static String addWWW(String brand){
		String brandWithWWW = brand;
		if(brandWithWWW.contains("www.")){
			
		}else{
			String[] tempString = brandWithWWW.split("https://");
			brandWithWWW="https://www."+tempString[1];
		}
		return brandWithWWW;
	}
	
	/**
	 * Select an option in a HTML select object using webdriver
	 * @param driver - the webdriver to use
	 * @param selectName - the name of the select element
	 * @param optionText - the text of the option to select
	 */
	public static boolean selectByNameOptionByText(WebDriver driver, String selectName, String optionText){
		WebElement select = driver.findElement(By.name(selectName));
		List<WebElement> options = select.findElements(By.tagName("option"));
		for(int i=0; i<options.size(); i++){
			WebElement option = options.get(i);
			driver.switchTo().defaultContent();
			if(option.getText().trim().toLowerCase().equals(optionText.toLowerCase())){
				option.click();
				break;
			}
			//if could not find the option after the last iteration
			if(i == (options.size()-1)){
				Logger.log(Logger.ERROR, "Could not find option: "+optionText);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Select an option in a HTML select object using webdriver
	 * @param driver - the webdriver to use
	 * @param selectName - the name of the select element
	 * @param optionText - the text of the option that contains to select
	 */
	public static boolean selectByNameOptionByTextContains(WebDriver driver, String selectName, String optionText){
		WebElement select = driver.findElement(By.name(selectName));
		List<WebElement> options = select.findElements(By.tagName("option"));
		for(int i=0; i<options.size(); i++){
			WebElement option = options.get(i);
			if(option.getText().trim().toLowerCase().contains(optionText.toLowerCase())){
				option.click();
				break;
			}
			//if could not find the option after the last iteration
			if(i == (options.size()-1)){
				Logger.log(Logger.ERROR, "Could not find option that contains: "+optionText);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Select an option in a HTML select object using webdriver
	 * @param driver - the webdriver to use
	 * @param selectId - the id of the select element
	 * @param optionText - the text of the option to select
	 */
	public static boolean selectByIdOptionByText(WebDriver driver, String selectId, String optionText){
		WebElement select = driver.findElement(By.id(selectId));
		List<WebElement> options = select.findElements(By.tagName("option"));
		for(int i=0; i<options.size(); i++){
			WebElement option = options.get(i);
			if(option.getText().trim().toLowerCase().equals(optionText.toLowerCase())){
				option.click();
				break;
			}
			//if could not find the option after the last iteration
			if(i == (options.size()-1)){
				Logger.log(Logger.ERROR, "Could not find option: "+optionText);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Select an option in a HTML select object using webdriver
	 * @param driver - the webdriver to use
	 * @param selectId - the id of the select element
	 * @param optionText - the text of the option that contains to select
	 */
	public static boolean selectByIdOptionByTextContains(WebDriver driver, String selectId, String optionText){
		WebElement select = driver.findElement(By.id(selectId));
		List<WebElement> options = select.findElements(By.tagName("option"));
		for(int i=0; i<options.size(); i++){
			WebElement option = options.get(i);
			if(option.getText().trim().toLowerCase().contains(optionText.toLowerCase())){
				option.click();
				break;
			}
			//if could not find the option after the last iteration
			if(i == (options.size()-1)){
				Logger.log(Logger.ERROR, "Could not find option that contains: "+optionText);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Select an option in a HTML select object using webdriver
	 * @param driver - the webdriver to use
	 * @param selectId - the id of the selected element
	 * @param optionText - the text of the option that starts with to select
	 */
	public static boolean selectByIdOptionByTextStartsWith(WebDriver driver, String selectId, String optionText){
		WebElement select = driver.findElement(By.id(selectId));
		List<WebElement> options = select.findElements(By.tagName("option"));
		for(int i=0; i<options.size(); i++){
			WebElement option = options.get(i);
			if(option.getText().trim().toLowerCase().startsWith(optionText.toLowerCase())){
				option.click();
				break;
			}
			//if could not find the option after the last iteration
			if(i == (options.size()-1)){
				Logger.log(Logger.ERROR, "Could not find option that starts with: "+optionText);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Select an option in a HTML select object using webdriver
	 * @param driver - the webdriver to use
	 * @param selectId - the id of the select element
	 * @param optionText - the value of the option to select
	 */
	public static boolean selectByIdOptionByValue(WebDriver driver, String selectId, String optionValue){
		WebElement select = driver.findElement(By.id(selectId));
		List<WebElement> options = select.findElements(By.tagName("option"));
		for(int i=0; i<options.size(); i++){
			WebElement option = options.get(i);
			if(option.getAttribute("value").trim().toLowerCase().equals(optionValue.toLowerCase())){
				option.click();
				break;
			}
			//if could not find the option after the last iteration
			if(i == (options.size()-1)){
				Logger.log(Logger.ERROR, "Could not find option with value: "+optionValue);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Select a check box using webdriver
	 * @param driver - the webdriver
	 * @param checkBoxId - the id of the check box to select
	 */
	public static void select(WebDriver driver, String checkBoxId){
		WebElement checkBox = driver.findElement(By.id(checkBoxId));
		if(!checkBox.isSelected())
			checkBox.click();
	}
	
	/**
	 * Unselect a check box if it is already selected
	 * @param driver - the webdriver to use
	 * @param checkBoxId - the id of the check box
	 */
	public static void unselect(WebDriver driver, String checkBoxId){
		WebElement checkBox = driver.findElement(By.id(checkBoxId));
		if(checkBox.isSelected())
			checkBox.click();
	}
	
	
	/**
	 * Move the browser window to 0,0 and resize it to whatever size
	 * @param driver - the web driver
	 * @param size - the size of the browser window to resize to
	 */
	public static void setWindowSize(WebDriver driver, String size){
		((JavascriptExecutor)driver).executeScript("window.moveTo(0, 0);");
		if(size.equals("max")){
			((JavascriptExecutor)driver).executeScript("window.resizeTo(window.screen.availWidth, window.screen.availHeight);");
		}else{
			((JavascriptExecutor)driver).executeScript("window.resizeTo("+size+");");
		}
	}
	
	/**
	 * Find out which page is the overview table on
	 * @param driver - the web driver
	 * @return
	 */
	public static int getCurrentPage(WebDriver driver){
		int currentPage = 1;
		String currentPageText = driver.findElement(By.xpath("//div[@id='data_table_paginate']/span[3]/span[@class='paginate_active']")).getText();
		currentPage = Integer.parseInt(currentPageText);
		return currentPage;
	}
	
	/**
	 * Make the data table go to the next page
	 * @param driver - the web driver
	 */
	public static void nextPage(WebDriver driver){
		AJAXWaiter.waitForId(driver, "data_table_next", 10);
		driver.findElement(By.id("data_table_next")).click();
	}
	
	/**
	 * Find out if the "user" is on the last page of the overview table
	 * @param driver - the web driver
	 * @return
	 */
	public static boolean isLastPage(WebDriver driver){
		int currentPage = 1;
		AJAXWaiter.waitDataTableLoad(driver, 60);
		AJAXWaiter.waitForXpath(driver, "//div[@id='data_table_paginate']/span[3]", 10);
		List<WebElement> pages = driver.findElement(By.xpath("//div[@id='data_table_paginate']/span[3]")).findElements(By.tagName("span"));
		currentPage = Misc.getCurrentPage(driver);
		if(currentPage == Integer.parseInt(pages.get(pages.size()-1).getText()))
			return true;
		else
			return false;
	}
	
	/**
	 * Convert all the strings in an array to lower case
	 * @param array - the array to convert
	 * @return the array in lower case
	 */
	public static String[] toLowerCase(String[] array){
		for(int i = 0; i < array.length; i++){
			array[i] = array[i].toLowerCase();
		}
		return array;
	}

}
