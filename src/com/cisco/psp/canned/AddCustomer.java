/**
 * AddCustomer - Adds a customer, changes the device password and verifies the information added
 * Updated - 02/06/12 (Not CSV compatible)
 */
package com.cisco.psp.canned;
import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.cisco.psp.commons.AJAXWaiter;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.commons.Misc;
import com.cisco.psp.objects.Action;


public class AddCustomer{
	boolean status = true;
	String waiting = "";
	String errorMessage = "";
	boolean loggedIn = false;
	
	File moddedFile;
	
	String businessName, customerImage, industry, streetAddress, city, state, zip, country, devicePassword,
		     addContact, firstName, lastName, email, emailDomain, altEmail, altEmailDomain, 
		     SMSEmail, SMSEmailDomain, altSMSEmail, altSMSEmailDomain;
	
	public AddCustomer(Action action){
		parseAction(action);
	}
	
	public void startTest(WebDriver driver, boolean schedule){
		Logger.log(Logger.INFO, "Adding Customer: " + businessName + " ");
		AJAXWaiter.waitForLinkText(driver, "+ Add Customer", 30);
		driver.findElement(By.linkText("+ Add Customer")).click();
		waiting = AJAXWaiter.waitForId(driver, "ca_business_name", 10);
		if(waiting.equals("Fail")){
			status = false;
			Logger.log(Logger.ERROR, "Add customer modal window did not show up.");
			return;
		}
		
		//******************************
		// pop up window to add customer
		//******************************
		driver.findElement(By.name("business_name")).sendKeys(businessName);
		Logger.log(Logger.INFO, "Business Name: "+businessName);
		try{
			Misc.selectByNameOptionByText(driver, "business_industry", industry);
			Logger.log(Logger.INFO, "Business Industry: "+industry);
		}catch(NoSuchElementException e){
			Logger.log(Logger.INFO, "Did not find "+industry+", use (Other) as default");
		}
		if(customerImage.equals("")){
			Logger.log(Logger.INFO, "No image specified, not going to add an image for this customer");
		}else{
			File imageFile = new File(customerImage);
			if(!imageFile.exists()){
				Logger.log(Logger.ERROR, "Image path: "+imageFile.getAbsolutePath());
				Logger.log(Logger.ERROR, "Cannot find image file: "+customerImage);
				status = false;
				driver.findElement(By.xpath("//div[@class='ui-dialog-buttonset']/button")).click();
				return;
			}
			driver.findElement(By.name("business_image")).sendKeys(imageFile.getAbsolutePath());
			
		}
		driver.findElement(By.name("business_streetaddress")).sendKeys(streetAddress);
		Logger.log(Logger.INFO, "Stree Address: "+streetAddress);
		driver.findElement(By.name("business_city")).sendKeys(city);
		Logger.log(Logger.INFO, "Business City: "+city);
		driver.findElement(By.name("business_state")).sendKeys(state);
		Logger.log(Logger.INFO, "Business State: "+state);
		driver.findElement(By.name("business_postal")).sendKeys(zip);
		Logger.log(Logger.INFO, "Business Postal: "+zip);
		Misc.selectByNameOptionByText(driver, "business_country", country);
		Logger.log(Logger.INFO, "Business Country: "+country);
		driver.findElement(By.xpath("//span[@id='next']/button")).click();
		if(addContact.toLowerCase().equals("yes")){
			Logger.log(Logger.INFO, "Adding Contact...");
			driver.findElement(By.name("fname")).sendKeys(firstName);
			Logger.log(Logger.INFO, "First Name: "+firstName);
			driver.findElement(By.name("lname")).sendKeys(lastName);
			Logger.log(Logger.INFO, "Last Name: "+lastName);
			driver.findElement(By.name("email_work")).sendKeys(email+emailDomain);
			Logger.log(Logger.INFO, "Email: "+email+emailDomain);
			driver.findElement(By.name("email_personal")).sendKeys(altEmail+altEmailDomain);
			Logger.log(Logger.INFO, "Alternate Email: "+altEmail+altEmailDomain);
			driver.findElement(By.name("phone_work")).sendKeys(SMSEmail+SMSEmailDomain);
			Logger.log(Logger.INFO, "SMS Email: "+SMSEmail+SMSEmailDomain);
			driver.findElement(By.name("phone_personal")).sendKeys(altSMSEmail+altSMSEmailDomain);
			Logger.log(Logger.INFO, "Alternate SMS Email: "+altSMSEmail+altSMSEmailDomain); 
		}else{
			if(driver.findElement(By.name("optional_contact_check")).isSelected())
				driver.findElement(By.name("optional_contact_check")).click();
			Logger.log(Logger.INFO, "Not adding a contact for this customer");
		}
		
		driver.findElement(By.xpath("//span[@id='next']/button")).click();
		List<WebElement> error = driver.findElements(By.xpath("//span[@id='cust_error_msg']/span"));
		if(error.size() != 0){
			for(int k = 0; k < error.size(); k++){
				Logger.log(Logger.ERROR, error.get(k).getText());
			}
			status = false;
			driver.findElement(By.xpath("//div[@class='ui-dialog-buttonset']/button")).click();
			return;
		}
		driver.findElement(By.xpath("//span[@id='submit']/button")).click();
		waiting = AJAXWaiter.waitForURLContains(driver, "&page=", 20);
		if(waiting.equals("Fail")){
			status = false;
			String errorMessage = driver.findElement(By.id("cust_update_status")).getText();
			Logger.log(Logger.ERROR, errorMessage);
			driver.findElement(By.xpath("//div[@id='dashboard']/ul/li/a/cufon/canvas")).click();
			return;
		}
		AJAXWaiter.waitForXpath(driver, "//div[@id='dashboard']/ul/li[6]/a", 5);
		//WebElement profileMenu = driver.findElement(By.xpath("//div[@id='dashboard']/ul/li[6]/a"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("var s=document.createElement('script');s.src='http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js';");
		js.executeScript("$(document).ready(function(){" +
							"$('ul.drop_down').show();" +
						  "});");
		AJAXWaiter.waitForXpath(driver, "//ul[@class='drop_down']/li[a[contains(text(), 'Profile')]]/a", 5);
		driver.findElement(By.xpath("//ul[@class='drop_down']/li[a[contains(text(), 'Profile')]]/a")).click();
		//************************
		//Change customer password
		//************************
		AJAXWaiter.waitForName(driver, "ae_loginpassword", 5);
		driver.findElement(By.name("ae_loginpassword")).clear();
		driver.findElement(By.name("ae_loginpassword")).sendKeys(devicePassword);
		AJAXWaiter.forceSleep(1000);
		driver.findElement(By.name("save")).click();
		Logger.log(Logger.INFO, "Changed device password to: "+devicePassword);
		AJAXWaiter.forceSleep(2000);
		
		//*******************************
		//verify customer added correctly
		//*******************************
		String createdDevicePassword = "";
		String createdCustomerName = "";
		String createdIndustry = "";
		String createdStreetAddress = "";
		String createdCity = "";
		String createdState = "";
		String createdZip = "";
		String createdCountry = "";
		waiting = AJAXWaiter.waitForName(driver, "ae_businessname", 10);
		if(waiting.equals("Done")){
			createdCustomerName = driver.findElement(By.name("ae_businessname")).getAttribute("value");
			if(createdCustomerName.equals(businessName)){
				Logger.log(Logger.INFO, "Verify: customer name correct");
			}else{
				Logger.log(Logger.ERROR, "Customer name does not match after customer creation");
				status = false;
			}
			Select industrySelect = new  Select(driver.findElement(By.name("ae_industry")));
			createdIndustry = industrySelect.getFirstSelectedOption().getText();
			if(createdIndustry.equals(industry)){
				Logger.log(Logger.INFO, "Verify: customer industry correct");
			}else{
				Logger.log(Logger.ERROR, "Customer industry does not match after customer creation\n"+
										 "Created industry: "+createdIndustry+" VS User Input: "+industry);
				status = false;
			}
			createdDevicePassword = driver.findElement(By.name("ae_loginpassword")).getAttribute("value");
			if(createdDevicePassword.equals(devicePassword)){
				Logger.log(Logger.INFO, "Verify: device password correct");
			}else{
				Logger.log(Logger.ERROR, "Device password does not match after customer creation");
				status = false;
			}
		}else{
			Logger.log(Logger.ERROR, "On a different page after saving device password (cannot find the bussines name text field)\n"+
									 driver.getPageSource());
			status = false;
			return;
		}
		js.executeScript("$(document).ready(function(){" +
				"$('ul.drop_down').show();" +
			  "});");
		AJAXWaiter.waitForXpath(driver, "//ul[@class='drop_down']/li[a[contains(text(), 'Address')]]/a", 5);
		driver.findElement(By.xpath("//ul[@class='drop_down']/li[a[contains(text(), 'Address')]]/a")).click();
		waiting = AJAXWaiter.waitForName(driver, "ae_streetaddress", 10);
		if(waiting.equals("Done")){
			createdStreetAddress = driver.findElement(By.name("ae_streetaddress")).getAttribute("value");
			if(createdStreetAddress.equals(streetAddress)){
				Logger.log(Logger.INFO, "Verify: Street address correct");
			}else{
				Logger.log(Logger.ERROR, "Street address does not match after customer creation");
				status = false;
			}
			createdCity = driver.findElement(By.name("ae_city")).getAttribute("value");
			if(createdCity.equals(city)){
				Logger.log(Logger.INFO, "Verify: City correct");
			}else{
				Logger.log(Logger.ERROR, "City does not match after customer creation");
				status = false;
			}
			createdState = driver.findElement(By.name("ae_state")).getAttribute("value");
			if(createdState.equals(state)){
				Logger.log(Logger.INFO, "Verify: State correct");
			}else{
				Logger.log(Logger.ERROR, "State does not match after customer creation");
				status = false;
			}
			createdZip = driver.findElement(By.name("ae_postal")).getAttribute("value");
			if(createdZip.equals(zip)){
				Logger.log(Logger.INFO, "Verify: Zip correct");
			}else{
				Logger.log(Logger.ERROR, "Zip does not match after customer creation");
				status = false;
			}
			Select countrySelect = new  Select(driver.findElement(By.name("ae_country")));
			createdCountry = countrySelect.getFirstSelectedOption().getText();
			if(createdCountry.equals(country)){
				Logger.log(Logger.INFO, "Verify: Country correct");
			}else{
				Logger.log(Logger.ERROR, "Country does not match after customer creation");
				status = false;
			}
		}else{
			Logger.log(Logger.ERROR, "On a different page after going to customer's address page (cannot locate street address text field)\n"+
					driver.getPageSource());
			status = false;
		}
			
		waiting = AJAXWaiter.waitForXpath(driver, "//div[@id='dashboard']/ul/li/a/cufon/canvas", 10);
		if(waiting.equals("Done")){
			driver.findElement(By.xpath("//div[@id='dashboard']/ul/li/a/cufon/canvas")).click();
		}else{
			Logger.log(Logger.ERROR, "Could not find the home button after changing device password");
			status = false;
		}

	}
	
	
	public boolean getStatus(){
		return status;
	}
	
	
	private void parseAction(Action act){
		businessName = act.getParam("customerName*");
		industry = act.getParam("industry*");
		customerImage = "";
		streetAddress = act.getParam("streetAddress*");
		city = act.getParam("city*");
		state = act.getParam("state");
		zip = act.getParam("zip*");
		country = act.getParam("country*");
		addContact = act.getParam("addContact*");
		if(addContact.equals("true")){
			addContact="yes";
		}else
			addContact="no";
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
		if(checkParam(act, "altSMSEmailAddress")){
			altSMSEmail = act.getParam("altSMSEmailAddress").split("@")[0];
			altSMSEmailDomain = "@"+act.getParam("altSMSEmailAddress").split("@")[1];
		}else{
			altSMSEmail = "";
			altSMSEmailDomain = "";
		}
		devicePassword = act.getParam("devicePassword*");
	}
	
	private boolean checkParam(Action act, String paramType){
		if(act.getParam(paramType).equals("") || act.getParam(paramType) == null){
			return false;
		}else
			return true;
	}
}
