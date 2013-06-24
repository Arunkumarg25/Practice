/**
 * AddDeliveryContact - Add a delivery contact, based on the information given
 * Updated - 02/13/12 (Not CSV compatible)
 */
package com.cisco.psp.canned;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cisco.psp.commons.AJAXWaiter;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.commons.Misc;
import com.cisco.psp.objects.Action;


public class AddDeliveryContact{
	boolean status = true;
	String brand = "";
	String brandLogin = "";
	String brandPassword = "";
	String waiting = "";
	String errorMessage = "";
	boolean loggedIn = false;
	
	String customer, email, emailDomain, altEmail, altEmailDomain, SMSEmail, SMSEmailDomain,
		     altSMSEmail, altSMSEmailDomain, firstName, lastName;

	
	public AddDeliveryContact(Action action){
		parseAction(action);
	}
	
	
	public void startTest(WebDriver driver, boolean schedule){
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("var s=document.createElement('script');s.src='http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js';");
		js.executeScript("$(document).ready(function(){" +
				"$('cufontext:contains(\"Notifications\")').parent().parent().siblings('ul.drop_down').show();" +
			  "});");
		AJAXWaiter.waitForXpath(driver, "//div[@id='dashboard']/ul/li[2]/ul/li[2]/a", 10);
		driver.findElement(By.xpath("//div[@id='dashboard']/ul/li[2]/ul/li[2]/a")).click();
		AJAXWaiter.waitForId(driver, "add_contact", 10);
		driver.findElement(By.id("add_contact")).click();
		Logger.log(Logger.INFO, "Clicked + Add Delivery Contact");
		waiting = AJAXWaiter.waitForXpath(driver, "//div[@id='customer_name']/select", 5);
		if(waiting.equals("Fail")){
			status = false;
			Logger.log(Logger.ERROR, "Add Delivery Contact modal did not show up in 5 seconds");
			return;
		}
		driver.findElement(By.id("firstname")).sendKeys(firstName);
		Logger.log(Logger.INFO, "First Name: "+firstName);
		driver.findElement(By.id("lastname")).sendKeys(lastName);
		Logger.log(Logger.INFO, "Last Name: "+lastName);
		driver.findElement(By.id("workemail")).sendKeys(email+emailDomain);
		Logger.log(Logger.INFO, "Email: "+email+emailDomain);
		driver.findElement(By.id("sms")).sendKeys(SMSEmail+SMSEmailDomain);
		Logger.log(Logger.INFO, "SMS Email: "+SMSEmail+SMSEmailDomain);
		driver.findElement(By.id("altsms")).sendKeys(altSMSEmail+altSMSEmailDomain);
		Logger.log(Logger.INFO, "Alternate SMS Email: "+altSMSEmail+altSMSEmailDomain);
		driver.findElement(By.id("altemail")).sendKeys(altEmail+altEmailDomain);
		Logger.log(Logger.INFO, "Alternate Email: "+altEmail+altEmailDomain);
		waiting = AJAXWaiter.waitForXpath(driver, "//option[text()='"+customer+"']", 10);
		if(waiting.equals("Fail")){
			status = false;
			Logger.log(Logger.ERROR, "Cannot find customer ("+customer+") in select");
			driver.findElement(By.xpath("//div[@class='ui-dialog-buttonset']/button")).click();
			return;
		}
		Misc.selectByIdOptionByText(driver, "customername_select", customer);
		driver.findElement(By.xpath("//button[@type='button']")).click();
		Logger.log(Logger.INFO, "Saved Delivery Contact: "+customer + " with email: "+email+emailDomain);
		waiting = AJAXWaiter.waitForXpath(driver, 
				"//table[@id='data_table']/tbody/tr[td[contains(text(),'"+customer+"')]]", 5);
		if(waiting.equals("Fail")){
			status = false;
			Logger.log(Logger.ERROR, "Page did not load after adding contact for 5 seconds");
			return;
		}
		AJAXWaiter.forceSleep(700);
		
		
		//*********************
		//verify added contacts
		//*********************
		AJAXWaiter.waitForXpath(driver, "//table[@id='data_table']/tbody/tr[td[contains(text(), '"+email+emailDomain+"')]]", 10);
		boolean found = false;
		do{
			List<WebElement> rows = driver.findElement(By.xpath("//table[@id='data_table']/tbody")).findElements(By.tagName("tr"));
			for(int k = 0; k < rows.size(); k++){
				AJAXWaiter.waitForElement(driver, rows.get(k).findElements(By.tagName("td")).get(3), 5);
				String currentFirstName = rows.get(k).findElements(By.tagName("td")).get(0).getText();
				String currentLastName = rows.get(k).findElements(By.tagName("td")).get(1).getText();
				//String currentPreferredLanguage = rows.get(k).findElements(By.tagName("td")).get(2).getText();
				String currentEmail = rows.get(k).findElements(By.tagName("td")).get(2).getText();
				String currentCustomer = rows.get(k).findElements(By.tagName("td")).get(3).getText();
				if(currentFirstName.equals(firstName)){
					if(currentLastName.equals(lastName)){
						if(currentEmail.equals(email+emailDomain)){
							if(currentCustomer.equals(customer)){
								rows.get(k).click();
								AJAXWaiter.waitForElementVisibleByXpath(driver, "//span[@id='edit_button']/a", 5);
								String currentSMSEmail = driver.findElement(By.xpath("//span[@id='v_sms']/span")).getAttribute("title");
								String currentAltEmail = driver.findElement(By.xpath("//span[@id='v_altemail']/span")).getAttribute("title");
								String currentAltSMSEmail = driver.findElement(By.xpath("//span[@id='v_altsms']/span")).getAttribute("title");
								if(currentSMSEmail.equals(SMSEmail+SMSEmailDomain) &&
								   currentAltEmail.equals(altEmail+altEmailDomain) &&
								   currentAltSMSEmail.equals(altSMSEmail+altSMSEmailDomain)){
									Logger.log(Logger.INFO, "Verify: Found contact that was just created at row: "+(k+1));
									found = true;
									break;
								}else{
									continue;
								}
							}
						}
					}
				}
			}
		}while(!Misc.isLastPage(driver) && !found);
		if(!found){
			Logger.log(Logger.ERROR, "Did not find the contact that was created for\n"+
						    "Customer: "+customer+"\n"+
						    "Email: "+email+emailDomain);
			status = false;
		}else{
			//close drawer
			driver.findElement(By.id("drawer_handle")).click();
		}
		
		waiting = AJAXWaiter.waitForXpath(driver, "//div[@id='dashboard']/ul/li/a/cufon/canvas", 10);
		if(waiting.equals("Done")){
			driver.findElement(By.xpath("//div[@id='dashboard']/ul/li/a/cufon/canvas")).click();
		}else{
			Logger.log(Logger.ERROR, "Could not find the home button after adding a delivery contact");
			status = false;
		}
	}
	
	public boolean getStatus(){
		return status;
	}
	
	public void parseAction(Action act){		
		customer = act.getParam("customerName*");
		firstName = act.getParam("firstName*");
		lastName = act.getParam("lastName*");
		email = act.getParam("emailAddress*").split("@")[0];
		emailDomain = "@"+act.getParam("emailAddress*").split("@")[1];
		if(checkParam(act,"altEmailAddress")){
			altEmail = act.getParam("altEmailAddress").split("@")[0];
			altEmailDomain = "@"+act.getParam("altEmailAddress").split("@")[1];
		}else{
			altEmail = ""; 
			altEmailDomain = "";
		}
		if(checkParam(act, "smsEmailAddress")){
			SMSEmail = act.getParam("smsEmailAddress").split("@")[0];
			SMSEmailDomain = "@"+act.getParam("smsEmailAddress").split("@")[1];
		}else{
			SMSEmail = "";
			SMSEmailDomain = "";
		}
		if(checkParam(act, "altSmsEmailAddress")){
			altSMSEmail = act.getParam("altSmsEmailAddress").split("@")[0];
			altSMSEmailDomain = "@"+act.getParam("altSmsEmailAddress").split("@")[1];
		}else{
			altSMSEmail = "";
			altSMSEmailDomain = "";
		}
	}
	
	private boolean checkParam(Action act, String paramType){
		if(act.getParam(paramType).equals("") || act.getParam(paramType) == null){
			return false;
		}else
			return true;
	}
}
