/**
 * RemoveCustomer - Deletes a customer, clicks the customer name, go to profile then click on delete
 * Updated - 02/06/12 (Not CSV compatible)
 * @author jearnold
 */
package com.cisco.psp.canned;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import com.cisco.psp.commons.AJAXWaiter;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.objects.Action;


public class RemoveCustomer{
	boolean status = true;
	String waiting = "";
	String errorMessage = "";
	
	String customerName;
	
	
	public RemoveCustomer(Action action){
		parseAction(action);
	}
	
	
	public void startTest(WebDriver driver, boolean schedule){
		AJAXWaiter.waitForXpath(driver, "//table[@id='data_table']//th[@id='created']", 3);
		driver.findElement(By.xpath("//table[@id='data_table']//th[@id='created']")).click();
		Logger.log(Logger.INFO, "Deleting customer: "+customerName+"...");
		AJAXWaiter.waitDataTableLoad(driver, 20);
		int cNameLength = customerName.length();
		if(cNameLength > 25) 
			cNameLength = 25;
		waiting = AJAXWaiter.waitForXpath(driver, "//table[@id='data_table']//tr[td[contains( text(), '"
				+ customerName.substring(0, cNameLength) + "')]]//td[3]", 5);
		if(waiting.equals("Fail")){
			status = false;
			Logger.log(Logger.ERROR, "Cannot find customer: "+customerName);
			return;
		}
		driver.findElement(By.xpath("//table[@id='data_table']//tr[td[contains( text(), '"
				+ customerName.substring(0, cNameLength) + "')]]//td[3]")).click();
		AJAXWaiter.waitForXpath(driver, "//div[@id='dashboard']/ul/li[6]/a/cufon/canvas", 5);
		String url = driver.getCurrentUrl();
		driver.get(url+"&page=profile");
		AJAXWaiter.waitForId(driver, "delete", 10);
		driver.findElement(By.id("delete")).click();
		AJAXWaiter.waitForXpath(driver, "//button[@type='button']", 3);
		driver.findElement(By.xpath("//button[@type='button']")).click();
		try{
			errorMessage = driver.findElement(By.className("one_error")).getText();
			if(!errorMessage.equals("")){
				Logger.log(Logger.ERROR, errorMessage);
				AJAXWaiter.waitForXpath(driver, "//div[@id='dashboard']/ul/li/a/cufon/canvas", 3);
				driver.findElement(By.xpath("//div[@id='dashboard']/ul/li/a/cufon/canvas")).click();
				status = false;
			}
		}catch(NoSuchElementException e){
			Logger.log(Logger.INFO, "Deleted customer: "+customerName);
		}
	}
	
	public boolean getStatus(){
		return status;
	}
	
	
	public void parseAction(Action act){
		customerName = act.getParam("customerName*");
	}
}
