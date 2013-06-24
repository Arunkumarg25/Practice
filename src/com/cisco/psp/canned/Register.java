/**
 * Register - Registers an agent based on given information
 * Updated - 01/30/12 (Not CSV compatible)
 * @author jearnold
 */

package com.cisco.psp.canned;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.cisco.psp.commons.AJAXWaiter;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.commons.Misc;
import com.cisco.psp.objects.Action;


public class Register {
	boolean status = true;
	String waiting = "";
	String agentId, agentPassword, firstName, lastName, company, jobRole, jobLevel, timezone, timezoneCity, address1, address2, 
			 email, emailDomain, altEmail, altEmailDomain, smsEmail, smsEmailDomain, altSMSEmail, altSMSEmailDomain, city, state, zip, phone, privacyStatement = "";
	
	
	public Register(Action act){
		parseAction(act);
	}
	
	public void startFirefox(){
		Logger.log(Logger.INFO, "Starting Firefox...");
		FirefoxProfile profile = new FirefoxProfile();
		profile.setAcceptUntrustedCertificates(true);
		WebDriver driver = new FirefoxDriver(profile);
		Misc.setWindowSize(driver, "max");
		startTest(driver, false);
	}
	
	public void startTest(WebDriver driver, boolean schedule){
		AJAXWaiter.waitForId(driver, "user_id", 10);
		clearFields(driver);
		Logger.log(Logger.INFO, "Adding Agent: "+agentId+"...");
		Logger.log(Logger.DEBUG, "Agent Password: "+agentPassword);
		Logger.log(Logger.DEBUG, "First Name: "+firstName);
		Logger.log(Logger.DEBUG, "Last Name: "+lastName);
		Logger.log(Logger.DEBUG, "Company: " +company);
		Logger.log(Logger.DEBUG, "Job Role: "+jobRole);
		Logger.log(Logger.DEBUG, "Job Level: "+jobLevel);
		Logger.log(Logger.DEBUG, "Timezone: "+timezone);
		Logger.log(Logger.DEBUG, "Timezone city: "+timezoneCity);
		Logger.log(Logger.DEBUG, "Street Address 1: "+address1);
		Logger.log(Logger.DEBUG, "Street Address 2: "+address2);
		Logger.log(Logger.DEBUG, "City: "+city);
		Logger.log(Logger.DEBUG, "State: "+state);
		Logger.log(Logger.DEBUG, "Zip: "+zip);
		Logger.log(Logger.DEBUG, "Phone: "+phone);
		Logger.log(Logger.DEBUG, "Email: "+email+emailDomain);
		Logger.log(Logger.DEBUG, "Alternate Email: "+altEmail+altEmailDomain);
		Logger.log(Logger.DEBUG, "Alternate SMS Email: "+altSMSEmail+altSMSEmailDomain);
		driver.findElement(By.name("user_id")).sendKeys(agentId);
		Logger.log(Logger.INFO, "Entered: "+agentId+" in Cisco.com User ID field");
		driver.findElement(By.name("password")).sendKeys(agentPassword);
		Logger.log(Logger.INFO, "Entered: "+agentPassword+" in Cisco.com Password field");
		driver.findElement(By.name("firstname")).sendKeys(firstName);
		Logger.log(Logger.INFO, "Entered: "+firstName+" in First Name field");
		driver.findElement(By.name("lastname")).sendKeys(lastName);
		Logger.log(Logger.INFO, "Entered: "+lastName+" in Last Name field");
		driver.findElement(By.name("company")).sendKeys(company);
		Logger.log(Logger.INFO, "Entered: "+company+" in Company field");
		if(Misc.selectByNameOptionByText(driver, "job_role", jobRole))
			Logger.log(Logger.INFO, "Selected: "+jobRole+" as the Job Role");
		else
			status = false;
		if(Misc.selectByNameOptionByText(driver, "job_level", jobLevel))
			Logger.log(Logger.INFO, "Selected: "+jobLevel+" as the Job Level");
		else
			status = false;
		if(Misc.selectByNameOptionByText(driver, "region", timezone))
			Logger.log(Logger.INFO, "Selected: "+timezone+" as the timezone region");
		else
			status = false;
		if(Misc.selectByNameOptionByText(driver, "timezone", timezoneCity))
			Logger.log(Logger.INFO, "Selected: "+timezoneCity+" as the timezone city");
		else 
			status = false;
		driver.findElement(By.name("streetaddress1")).sendKeys(address1);
		Logger.log(Logger.INFO, "Entered: "+address1+" in Address Line 1 field");
		driver.findElement(By.name("streetaddress2")).sendKeys(address2);
		Logger.log(Logger.INFO, "Entered: "+(address2.equals("")?"<blank>":address2)+" in Address Line 2 field");
		driver.findElement(By.name("city")).sendKeys(city);
		Logger.log(Logger.INFO, "Entered: "+(city.equals("")?"<blank>":city)+" in City field");
		driver.findElement(By.name("state")).sendKeys(state);
		Logger.log(Logger.INFO, "Entered: "+(state.equals("")?"<blank>":state)+" in State/Province field");
		driver.findElement(By.name("postalcode")).sendKeys(zip);
		Logger.log(Logger.INFO, "Entered: "+(zip.equals("")?"<blank>":zip)+" in Zip/Postal Code field");
		driver.findElement(By.name("phone_number")).sendKeys(phone);
		Logger.log(Logger.INFO, "Entered: "+(phone.equals("")?"<blank>":phone)+" in Phone Number field");
		driver.findElement(By.name("email")).sendKeys(email+emailDomain);
		Logger.log(Logger.INFO, "Entered: "+((email+emailDomain).equals("")?"<blank>":email+emailDomain)+" in Email Address field");
		driver.findElement(By.name("alternate_email")).sendKeys(altEmail+altEmailDomain);
		Logger.log(Logger.INFO, "Entered: "+((altEmail+altEmailDomain).equals("")?"<blank>":altEmail+altEmailDomain)+" in Alternate Email Address field");
		driver.findElement(By.name("sms_email")).sendKeys(smsEmail+smsEmailDomain);
		Logger.log(Logger.INFO, "Entered: "+((smsEmail+smsEmailDomain).equals("")?"<blank>":smsEmail+smsEmailDomain)+" in SMS Email Address field");
		driver.findElement(By.name("sms_emailalternate")).sendKeys(altSMSEmail+altSMSEmailDomain);
		Logger.log(Logger.INFO, "Entered: "+((altSMSEmail+altSMSEmailDomain).equals("")?"<blank>":altSMSEmail+altSMSEmailDomain)+" in Alternate SMS Email field");
		if(privacyStatement.equals("yes")){
			driver.findElement(By.name("privacy")).click();
			Logger.log(Logger.DEBUG, "Clicked on the privacy check box");
		}
		driver.findElement(By.name("submit")).click();
		Logger.log(Logger.DEBUG, "Clicked submit");
		waiting = AJAXWaiter.waitForURLContains(driver, "register_complete", 5);
		if(waiting.equals("Done")){
			String h2 = driver.findElement(By.tagName("h2")).getText();
			if(h2.equals("Registration Complete")){
				Logger.log(Logger.INFO, "Registartion Complete for: "+agentId);
			}else{
				Logger.log(Logger.ERROR, "Unknow Error, page source: \n"+driver.getPageSource());
			}
		}else{
			List<WebElement> errors = driver.findElements(By.className("one_error"));
			if(errors.size()!=0){
				for(int k=0; k<errors.size(); k++){
					try{
						String errorType = errors.get(k).findElement(By.className("one_error_type")).getText();
						String errorDetail = errors.get(k).findElement(By.className("one_error_detail")).getText();
						Logger.log(Logger.ERROR, (errorType==null?"":errorType)+errorDetail);
					}catch(Exception e){
						String error = errors.get(k).getText();
						Logger.log(Logger.ERROR, error);
					}
				}
				status = false;
			}
		}
	}
	
	public boolean getStatus(){
		return status;
	}
	
	private void parseAction(Action act){	
		agentId= act.getParam("agentID*");
		agentPassword = act.getParam("agentPassword*");
		firstName=act.getParam("firstName*");
		lastName=act.getParam("lastName*");
		company=act.getParam("company*");
		jobRole=act.getParam("jobRole");
		jobLevel=act.getParam("jobLevel");
		timezone=act.getParam("timezone");
		timezoneCity=act.getParam("timezoneCity");
		address1=act.getParam("address1*");
		address2=act.getParam("address2");
		if(address2 == null){
			address2 = "";
		}
		city=act.getParam("city*");
		state=act.getParam("state");
		if(state == null)
			state = "";
		zip=act.getParam("zip*");
		phone=act.getParam("phone*");
		email=act.getParam("email*").split("@")[0];
		emailDomain = "@"+act.getParam("email*").split("@")[1];
		if(checkParam(act,"altEmail")){
			altEmail = act.getParam("altEmail").split("@")[0];
			altEmailDomain = "@"+act.getParam("altEmail").split("@")[1];
		}else{
			altEmail = ""; 
			altEmailDomain = "";
		}
		if(checkParam(act, "smsEmail")){
			smsEmail = act.getParam("smsEmail").split("@")[0];
			smsEmailDomain = "@"+act.getParam("smsEmail").split("@")[1];
		}else{
			smsEmail = "";
			smsEmailDomain = "";
		}
		if(checkParam(act, "altSMSEmail")){
			altSMSEmail = act.getParam("altSMSEmail").split("@")[0];
			altSMSEmailDomain = "@"+act.getParam("altSMSEmail").split("@")[1];
		}else{
			altSMSEmail = "";
			altSMSEmailDomain = "";
		}
		privacyStatement = act.getParam("privacyStatement*");
	}
	
	private void clearFields(WebDriver driver){
		Logger.log(Logger.DEBUG, "Clearing all fields"); 
		driver.findElement(By.name("user_id")).clear();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("firstname")).clear();
		driver.findElement(By.name("lastname")).clear();
		driver.findElement(By.name("company")).clear();
		driver.findElement(By.name("streetaddress1")).clear();
		driver.findElement(By.name("streetaddress2")).clear();
		driver.findElement(By.name("city")).clear();
		driver.findElement(By.name("state")).clear();
		driver.findElement(By.name("postalcode")).clear();
		driver.findElement(By.name("phone_number")).clear();
		driver.findElement(By.name("email")).clear();
	}
	
	private boolean checkParam(Action act, String paramType){
		if(act.getParam(paramType) == null || act.getParam(paramType).equals("")){
			return false;
		}else
			return true;
	}
}
