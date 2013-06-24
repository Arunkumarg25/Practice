/**
 * EditCustomer - Changes a customers information, use === to indicate no change to a certain field
 * Updated - 01/30/12 (Not CSV compatible)
 * @author jearnold
 */
package com.cisco.psp.canned;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import com.cisco.psp.commons.AJAXWaiter;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.commons.Misc;
import com.cisco.psp.objects.Action;

public class EditCustomer {
	boolean status = true;
	String waiting = "";
	String errorMessage = "";
	boolean loggedIn = false;

	File moddedFile;

	String businessName, editBusinessName, editCustomerImage, editIndustry, editCity, editZip,
			editCountry, editTimezone, editTimezoneCity, editStreetAddress, editState,
			editDevicePassword;

	public EditCustomer(Action act) {
		parseAction(act);
	}

	public void startTest(WebDriver driver, boolean schedule) {
		AJAXWaiter.waitForXpath(driver, "//table[@id='data_table']//th[@id='created']", 3);
		driver.findElement(By.xpath("//table[@id='data_table']//th[@id='created']")).click();

		AJAXWaiter.waitDataTableLoad(driver, 30);
		Logger.log(Logger.INFO, "Editing customer: " + businessName + "...");
		int cNameLength = businessName.length();
		if (cNameLength > 25)
			cNameLength = 25;
		waiting = AJAXWaiter.waitForXpath(
				driver,
				"//table[@id='data_table']//tr[td[contains( text(), '"
						+ businessName.substring(0, cNameLength) + "')]]//td[3]", 5);
		if (waiting.equals("Fail")) {
			status = false;
			Logger.log(Logger.ERROR, "Cannot find customer: " + businessName);
			return;
		}
		driver.findElement(
				By.xpath("//table[@id='data_table']//tr[td[contains( text(), '"
						+ businessName.substring(0, cNameLength) + "')]]//td[3]")).click();
		AJAXWaiter.waitForXpath(driver, "//div[@id='dashboard']/ul/li[6]/a/cufon/canvas", 5);
		String url = driver.getCurrentUrl();
		driver.get(url + "&page=profile");
		if (!editBusinessName.equals("===")) {
			driver.findElement(By.name("ae_businessname")).clear();
			driver.findElement(By.name("ae_businessname")).sendKeys(editBusinessName);
			Logger.log(Logger.INFO, "Changed Customer Name to: " + editBusinessName);

		} else {
			Logger.log(Logger.INFO, "No change to customer name");
		}
		if (!editIndustry.equals("===")) {
			Misc.selectByNameOptionByTextContains(driver, "ae_industry", editIndustry);
			Logger.log(Logger.INFO, "Changed industry to: " + editIndustry);

		} else {
			Logger.log(Logger.INFO, "No change to industry");
		}
		if (!editCustomerImage.equals("===") && !editCustomerImage.equals("")) {
			File imageFile = new File(editCustomerImage);
			if (!imageFile.exists()) {
				Logger.log(Logger.ERROR, "Path of image: " + imageFile.getAbsolutePath());
				Logger.log(Logger.ERROR, "Cannot find image file: " + editCustomerImage);
				status = false;
				driver.findElement(By.xpath("//div[@id='dashboard']/ul/li/a/cufon/canvas")).click();
				return;
			}
			driver.findElement(By.name("business_image")).sendKeys(imageFile.getAbsolutePath());
			Logger.log(Logger.INFO, "Changed customer image to: " + editCustomerImage);

		} else {
			Logger.log(Logger.INFO, "No change to customer image");
		}
		if (!editTimezone.equals("===")) {
			Misc.selectByNameOptionByTextContains(driver, "ae_region", editTimezone);
			Logger.log(Logger.INFO, "Changed timezone to: " + editTimezone);

		} else {
			Logger.log(Logger.INFO, "No change to timezone");
		}
		if (!editTimezoneCity.equals("===")) {
			Misc.selectByNameOptionByTextContains(driver, "ae_timezone", editTimezoneCity);
			Logger.log(Logger.INFO, "Changed timezone city to: " + editTimezoneCity);

		} else {
			Logger.log(Logger.INFO, "No change to timezone city");
		}
		if (!editDevicePassword.equals("===")) {
			driver.findElement(By.name("ae_loginpassword")).clear();
			driver.findElement(By.name("ae_loginpassword")).sendKeys(editDevicePassword);
			Logger.log(Logger.INFO, "Changed device password: " + editDevicePassword);
		} else {
			Logger.log(Logger.INFO, "No change to device password");
		}
		driver.findElement(By.name("save")).click();

		AJAXWaiter.waitForXpath(driver, "//div[@id='dashboard']/ul/li/a/cufon/canvas", 5);
		driver.findElement(By.xpath("//div[@id='dashboard']/ul/li/a/cufon/canvas")).click();

		if (!editBusinessName.equals("===")) {
			cNameLength = editBusinessName.length();
			if (cNameLength > 25)
				cNameLength = 25;
			waiting = AJAXWaiter.waitForXpath(
					driver,
					"//table[@id='data_table']//tr[td[contains( text(), '"
							+ editBusinessName.substring(0, cNameLength) + "')]]//td[3]", 5);
			if (waiting.equals("Fail")) {
				status = false;
				Logger.log(Logger.ERROR, "Cannot find customer: " + editBusinessName
						+ " after change");
				return;
			}
			driver.findElement(
					By.xpath("//table[@id='data_table']//tr[td[contains( text(), '"
							+ editBusinessName.substring(0, cNameLength) + "')]]//td[3]")).click();
		} else {
			cNameLength = businessName.length();
			if (cNameLength > 25)
				cNameLength = 25;
			waiting = AJAXWaiter.waitForXpath(
					driver,
					"//table[@id='data_table']//tr[td[contains( text(), '"
							+ businessName.substring(0, cNameLength) + "')]]//td[3]", 5);
			if (waiting.equals("Fail")) {
				status = false;
				Logger.log(Logger.ERROR, "Cannot find customer: " + businessName + " after change");
				return;
			}
			driver.findElement(
					By.xpath("//table[@id='data_table']//tr[td[contains( text(), '"
							+ businessName.substring(0, cNameLength) + "')]]//td[3]")).click();
		}
		driver.get(url + "&page=profile");
		// *****************************************
		// verify customer profile changed correctly
		// *****************************************
		String createdCustomerName = "";
		String createdIndustry = "";
		String createdStreetAddress = "";
		String createdCity = "";
		String createdState = "";
		String createdZip = "";
		String createdCountry = "";
		AJAXWaiter.waitForName(driver, "ae_businessname", 5);
		createdCustomerName = driver.findElement(By.name("ae_businessname")).getAttribute("value");
		if (createdCustomerName.equals(editBusinessName)) {
			Logger.log(Logger.INFO, "Verify: customer name correct");
		} else {
			if (!editBusinessName.equals("===")) {
				Logger.log(Logger.ERROR, "Customer name does not match after customer creation");
				status = false;
			}
		}
		Select industrySelect = new Select(driver.findElement(By.name("ae_industry")));
		createdIndustry = industrySelect.getFirstSelectedOption().getText();
		if (createdIndustry.equals(editIndustry)) {
			Logger.log(Logger.INFO, "Verify: customer industry correct");
		} else {
			if (!editIndustry.equals("===")) {
				Logger.log(Logger.ERROR, "Customer industry does not match after customer creation");
				status = false;
			}
		}

		driver.get(url + "&page=address");

		if (!editStreetAddress.equals("===")) {
			driver.findElement(By.name("ae_streetaddress")).clear();
			driver.findElement(By.name("ae_streetaddress")).sendKeys(editStreetAddress);
			Logger.log(Logger.INFO, "Changed street address to: " + editStreetAddress);

		} else {
			Logger.log(Logger.INFO, "No change to street address");
		}
		if (!editCity.equals("===")) {
			driver.findElement(By.name("ae_city")).clear();
			driver.findElement(By.name("ae_city")).sendKeys(editCity);
			Logger.log(Logger.INFO, "Changed city to: " + editCity);

		} else {
			Logger.log(Logger.INFO, "No change to city");
		}
		if (!editState.equals("===")) {
			driver.findElement(By.name("ae_state")).clear();
			driver.findElement(By.name("ae_state")).sendKeys(editState);
			Logger.log(Logger.INFO, "Changed state to: " + editState);

		} else {
			Logger.log(Logger.INFO, "No change to state");
		}
		if (!editZip.equals("===")) {
			driver.findElement(By.name("ae_postal")).clear();
			driver.findElement(By.name("ae_postal")).sendKeys(editZip);
			Logger.log(Logger.INFO, "Changed postal code to: " + editZip);

		} else {
			Logger.log(Logger.INFO, "No change to postal code");
		}
		if (!editCountry.equals("===")) {
			Misc.selectByNameOptionByText(driver, "ae_country", editCountry);
			Logger.log(Logger.INFO, "Changed country to: " + editCountry);

		} else {
			Logger.log(Logger.INFO, "No change to country");
		}
		driver.findElement(By.name("save")).click();

		AJAXWaiter.waitForXpath(driver, "//div[@id='dashboard']/ul/li/a/cufon/canvas", 5);
		driver.findElement(By.xpath("//div[@id='dashboard']/ul/li/a/cufon/canvas")).click();

		if (!editBusinessName.equals("===")) {
			cNameLength = editBusinessName.length();
			if (cNameLength > 25)
				cNameLength = 25;
			waiting = AJAXWaiter.waitForXpath(
					driver,
					"//table[@id='data_table']//tr[td[contains( text(), '"
							+ editBusinessName.substring(0, cNameLength) + "')]]//td[3]", 5);
			if (waiting.equals("Fail")) {
				status = false;
				Logger.log(Logger.ERROR, "Cannot find customer: " + editBusinessName
						+ " after change");
				return;
			}
			driver.findElement(
					By.xpath("//table[@id='data_table']//tr[td[contains( text(), '"
							+ editBusinessName.substring(0, cNameLength) + "')]]//td[3]")).click();
		} else {
			cNameLength = businessName.length();
			if (cNameLength > 25)
				cNameLength = 25;
			waiting = AJAXWaiter.waitForXpath(
					driver,
					"//table[@id='data_table']//tr[td[contains( text(), '"
							+ businessName.substring(0, cNameLength) + "')]]//td[3]", 5);
			if (waiting.equals("Fail")) {
				status = false;
				Logger.log(Logger.ERROR, "Cannot find customer: " + businessName + " after change");
				return;
			}
			driver.findElement(
					By.xpath("//table[@id='data_table']//tr[td[contains( text(), '"
							+ businessName.substring(0, cNameLength) + "')]]//td[3]")).click();
		}
		driver.get(url + "&page=address");

		// *************************************
		// Verify address page changed correctly
		// *************************************
		createdStreetAddress = driver.findElement(By.name("ae_streetaddress"))
				.getAttribute("value");
		if (createdStreetAddress.equals(editStreetAddress)) {
			Logger.log(Logger.INFO, "Verify: Street address correct");
		} else {
			if (!editStreetAddress.equals("===")) {
				Logger.log(Logger.ERROR, "Street address does not match after customer creation");
				status = false;
			}
		}
		createdCity = driver.findElement(By.name("ae_city")).getAttribute("value");
		if (createdCity.equals(editCity)) {
			Logger.log(Logger.INFO, "Verify: City correct");
		} else {
			if (!editCity.equals("===")) {
				Logger.log(Logger.ERROR, "City does not match after customer creation");
				status = false;
			}
		}
		createdState = driver.findElement(By.name("ae_state")).getAttribute("value");
		if (createdState.equals(editState)) {
			Logger.log(Logger.INFO, "Verify: State correct");
		} else {
			if (!editState.equals("===")) {
				Logger.log(Logger.ERROR, "State does not match after customer creation");
				status = false;
			}
		}
		createdZip = driver.findElement(By.name("ae_postal")).getAttribute("value");
		if (createdZip.equals(editZip)) {
			Logger.log(Logger.INFO, "Verify: Zip correct");
		} else {
			if (!editZip.equals("===")) {
				Logger.log(Logger.ERROR, "Zip does not match after customer creation");
				status = false;
			}
		}
		Select countrySelect = new Select(driver.findElement(By.name("ae_country")));
		createdCountry = countrySelect.getFirstSelectedOption().getText();
		if (createdCountry.equals(editCountry)) {
			Logger.log(Logger.INFO, "Verify: Country correct");
		} else {
			if (!editCountry.equals("===")) {
				Logger.log(Logger.ERROR, "Country does not match after customer creation");
				status = false;
			}
		}
		AJAXWaiter.waitForXpath(driver, "//div[@id='dashboard']/ul/li/a/cufon/canvas", 5);
		driver.findElement(By.xpath("//div[@id='dashboard']/ul/li/a/cufon/canvas")).click();

	}

	public boolean getStatus() {
		return status;
	}

	private void parseAction(Action act) {
		businessName = act.getParam("customerName*");
		editBusinessName = act.getParam("editCustomerName*");
		editIndustry = act.getParam("editIndustry*");
		editCity = act.getParam("editCity*");
		editZip = act.getParam("editZip*");
		editCountry = act.getParam("editCountry*");
		editTimezone = act.getParam("editTimezone*");
		editTimezoneCity = act.getParam("editTimezoneCity*");
		editStreetAddress = act.getParam("editStreetAddress*");
		editState = act.getParam("editState");
		editDevicePassword = act.getParam("editDevicePassword");
		editCustomerImage = "";
		logChanges();
	}

	private void logChanges() {
		Logger.log(Logger.INFO, "Customer to edit: " + businessName);
		if (!editBusinessName.equals("==="))
			Logger.log(Logger.INFO, "Customer name after: " + editBusinessName);
		else
			Logger.log(Logger.INFO, "Customer name after: No change");
		if (!editCustomerImage.equals("==="))
			Logger.log(Logger.INFO, "Customer Image after: " + editCustomerImage);
		else
			Logger.log(Logger.INFO, "Customer Image after: No change");
		if (!editIndustry.equals("==="))
			Logger.log(Logger.INFO, "Industry after: " + editIndustry);
		else
			Logger.log(Logger.INFO, "Industry after: No change");
		if (!editStreetAddress.equals("==="))
			Logger.log(Logger.INFO, "Street Address after: " + editStreetAddress);
		else
			Logger.log(Logger.INFO, "Street Address after: No change");
		if (!editCity.equals("==="))
			Logger.log(Logger.INFO, "City after: " + editCity);
		else
			Logger.log(Logger.INFO, "City after: No change");
		if (!editState.equals("==="))
			Logger.log(Logger.INFO, "State after: " + editState);
		else
			Logger.log(Logger.INFO, "State after: No change");
		if (!editCountry.equals("==="))
			Logger.log(Logger.INFO, "Country after: " + editCountry);
		else
			Logger.log(Logger.INFO, "Country after: No change");
		if (!editTimezone.equals("==="))
			Logger.log(Logger.INFO, "Timezone after: " + editTimezone);
		else
			Logger.log(Logger.INFO, "Timezone after: No change");
		if (!editTimezoneCity.equals("==="))
			Logger.log(Logger.INFO, "Timezone city after: " + editTimezoneCity);
		else
			Logger.log(Logger.INFO, "Timezone city after: No change");
		if (!editDevicePassword.equals("==="))
			Logger.log(Logger.INFO, "Device password after: " + editDevicePassword);
		else
			Logger.log(Logger.INFO, "Device password after: No change");
	}

}
