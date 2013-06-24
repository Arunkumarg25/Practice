/**
 * EditAccount - Edits an existing agent's information
 * Updated - 01/30/12 (Not CSV compatible)
 * @author jearnold
 */
package com.cisco.psp.canned;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cisco.psp.commons.Logger;
import com.cisco.psp.commons.Misc;
import com.cisco.psp.objects.Action;

public class EditAccount {
	boolean status = true;
	String waiting = "";
	String errorMessage = "";
	
	String firstName, lastName, company, jobRole, jobLevel, timezone, timezoneCity, address1, address2, city, state, zip, phone,
		   email, altEmail, smsEmail, altSMSEmail, lightTheme;
	
	public EditAccount(Action action){
		parseAction(action);
	}
	
	public void startTest(WebDriver driver, boolean schedule){
		enterValue(driver.findElement(By.id("firstname")), "First Name", firstName);
		enterValue(driver.findElement(By.id("lastname")), "Last Name", lastName);
		enterValue(driver.findElement(By.id("company")), "Company", company);
		if(jobRole.equals("==="))
			Logger.log(Logger.INFO, "No change in Job Role select");
		else{
			if(Misc.selectByNameOptionByText(driver, "job_role", jobRole))
				Logger.log(Logger.INFO, "Selected: "+jobRole+" as the Job Role");
			else{
				Logger.log(Logger.ERROR, "Could not find option: "+jobRole);
				status = false;
			}
		}
		if(jobLevel.equals("==="))
			Logger.log(Logger.INFO, "No change in Job Level select");
		else{
			if(Misc.selectByNameOptionByText(driver, "job_level", jobLevel))
				Logger.log(Logger.INFO, "Selected: "+jobLevel+" as the Job Level");
			else{
				Logger.log(Logger.ERROR, "Could not find option: "+jobLevel);
				status = false;
			}
		}
		if(timezone.equals("==="))
			Logger.log(Logger.INFO, "No change in Timezone select");
		else{
			if(Misc.selectByNameOptionByText(driver, "region", timezone))
				Logger.log(Logger.INFO, "Selected: "+timezone+" as the Timezone");
			else{
				Logger.log(Logger.ERROR, "Could not find option: "+timezone); 
				status = false;
			}
		}
		if(timezoneCity.equals("==="))
			Logger.log(Logger.INFO, "No change in Timezone City select");
		else{
			if(Misc.selectByNameOptionByText(driver, "timezone", timezoneCity))
				Logger.log(Logger.INFO, "Selected: "+timezoneCity+" as the Timezone City");
			else{
				Logger.log(Logger.ERROR, "Could not find option: "+timezoneCity);
				status = false;
			}
		}
		enterValue(driver.findElement(By.id("streetaddress1")), "Street Address 1", address1);
		enterValue(driver.findElement(By.id("streetaddress2")), "Street Address 2", address2);
		enterValue(driver.findElement(By.id("city")), "City", city);
		enterValue(driver.findElement(By.id("state")), "State/Province", state);
		enterValue(driver.findElement(By.id("postalcode")), "Zip/Postal Code", zip);
		enterValue(driver.findElement(By.id("phone_number")), "Phone Number", phone);
		enterValue(driver.findElement(By.id("email")), "Email Address", email);
		enterValue(driver.findElement(By.id("alternate_email")), "Alternate Email Address", altEmail);
		enterValue(driver.findElement(By.id("sms_email")), "SMS Email Address",  smsEmail); 
		enterValue(driver.findElement(By.id("sms_emailalternate")), "Alternate SMS Email", altSMSEmail); 
		WebElement lightThemeCheckbox = driver.findElement(By.id("theme"));
		if(lightTheme.equals("yes")){
			if(!lightThemeCheckbox.isSelected())
				lightThemeCheckbox.click();
		}else if(lightTheme.equals("no")){
			if(lightThemeCheckbox.isSelected())
				lightThemeCheckbox.click();
		}
		driver.findElement(By.name("submit")).click();
	}
	
	public boolean getStatus(){
		return status;
	}
	
	public void enterValue(WebElement field, String fieldName, String value){
		if(!value.equals("===")){
			field.clear();
			field.sendKeys(value);
			Logger.log(Logger.INFO, "Entered: "+(value.equals("")?"<Blank>":value)+" in "+fieldName+" field");
		}else{
			Logger.log(Logger.INFO, "No change in " + fieldName + " field");
		}
	}
	
	public void parseAction(Action act){
		firstName = act.getParam("firstName*");
		lastName = act.getParam("lastName*");
		company = act.getParam("company*");
		jobRole = act.getParam("jobRole");
		jobLevel = act.getParam("jobLevel");
		timezone = act.getParam("timezone");
		timezoneCity = act.getParam("timezoneCity");
		address1 = act.getParam("address1*");
		address2 = act.getParam("address2");
		city = act.getParam("city*");
		state = act.getParam("state");
		zip = act.getParam("zip*");
		phone = act.getParam("phone*");
		email = act.getParam("email*");
		altEmail = act.getParam("altEmail");
		smsEmail = act.getParam("smsEmail");
		altSMSEmail = act.getParam("altSMSEmail");
		lightTheme = act.getParam("lightTheme");
	}
}
