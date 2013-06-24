/**
 * Login - Logs in with the given user name and password, also accepts terms and condition if it exists
 * Updated - 02/01/12 (Not CSV compatible)
 * @author jearnold
 */
package com.cisco.psp.canned;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cisco.psp.commons.AJAXWaiter;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.objects.Action;
import com.cisco.psp.testrunner.InvalidTestCaseException;

public class Login {
	boolean status = true;
	String waiting, errorMessage = "";

	String brand, agentId, agentPassword, businessName = "";

	public Login(Action action) {
		parseAction(action);
	}

	public void startTest(WebDriver driver, boolean schedule) throws InvalidTestCaseException {
		driver.get(brand);
		Logger.log(Logger.INFO, "Logging into: " + brand);
		AJAXWaiter.waitForXpath(driver, "//input[@id='accountid']", 5);
		driver.findElement(By.xpath("//input[@id='accountid']")).sendKeys(agentId);
		Logger.log(Logger.INFO, "Username: " + agentId);
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys(agentPassword);
		Logger.log(Logger.INFO, "Password: " + agentPassword);
		driver.findElement(By.xpath("//button[@id='loginbutton']")).click();
		Logger.log(Logger.INFO, "Clicked on Log In button");
		AJAXWaiter.waitForURLChange(driver, 5);
		AJAXWaiter.waitForTagName(driver, "h2", 5);
		if (!driver.findElement(By.xpath("//h2")).getText().toLowerCase().contains("log in")) {
			waiting = AJAXWaiter.waitForXpath(driver,
					"//div[@role='dialog']/div[3]/div/span/button", 3);
			if (waiting.equals("Fail")) {
				Logger.log(Logger.INFO, "Terms and Conditions did not show up");
			} else {
				WebElement acceptButton = driver.findElement(By
						.xpath("//div[@role='dialog']/div[3]/div/span/button"));
				acceptButton.click();
				AJAXWaiter.waitForElementGone(driver, acceptButton, 10);
				Logger.log(Logger.INFO, "Accepted Terms and Conditions dialog");
			}
			if (driver.findElement(By.xpath("//*[@id='header_welcome']")).getText() != null)
				Logger.log(Logger.INFO, "Login succesful");			
		} else {
			String errorMessage = driver.findElement(By.xpath("//div[@class='warnings']"))
					.getText();
			Logger.log(Logger.ERROR, "Login Failed\n" + errorMessage);
			status = false;
		}
	}

	public boolean getStatus() {
		return status;
	}

	private void parseAction(Action act) {
		brand = act.getParam("url");
		agentId = act.getParam("username");
		agentPassword = act.getParam("password");
	}
}
