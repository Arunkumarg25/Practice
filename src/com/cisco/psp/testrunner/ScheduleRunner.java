package com.cisco.psp.testrunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import org.jcouchdb.document.Attachment;
import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.document.ViewResult;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.cisco.psp.actions.Click;
import com.cisco.psp.actions.DoubleClick;
import com.cisco.psp.actions.GetHostIP;
import com.cisco.psp.actions.Goto;
import com.cisco.psp.actions.Hover;
import com.cisco.psp.actions.Input;
import com.cisco.psp.actions.ManageFile;
import com.cisco.psp.actions.ManageWindow;
import com.cisco.psp.actions.Navigate;
import com.cisco.psp.actions.Refresh;
import com.cisco.psp.actions.Save;
import com.cisco.psp.actions.Verify;
import com.cisco.psp.actions.Wait;
import com.cisco.psp.canned.AddCustomer;
import com.cisco.psp.canned.AddDeliveryContact;
import com.cisco.psp.canned.EditAccount;
import com.cisco.psp.canned.EditCustomer;
import com.cisco.psp.canned.Login;
import com.cisco.psp.canned.Register;
import com.cisco.psp.canned.RemoveCustomer;
import com.cisco.psp.commons.AJAXWaiter;
import com.cisco.psp.commons.DateOperations;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.commons.StopWatch;
import com.cisco.psp.commons.XMLParser;
import com.cisco.psp.db.CouchDAO;
import com.cisco.psp.objects.Action;
import com.cisco.psp.objects.Workflow;

public class ScheduleRunner {
	private static final Exception InvalidTestCaseException = null;
	static String couchVersion = "";
	static boolean result = true;
	static boolean resultForLoop = true;
	static boolean statusOfThisAction = true;
	static Workflow wf = null;
	static String Error_Code = "";

	public static void runWorkflow(String hash, String ip, String port, String db, String xmlPath) {
		XMLParser xmlParser = null;
		CouchDAO couch = new CouchDAO(hash, ip, Integer.parseInt(port), db);
		// if the xml is given, run the test case by the given xml, else get the
		// tc.xml from couch
		if (xmlPath.equals("")) {
			xmlParser = new XMLParser(couch.getXML(hash, ip, port, db));
		} else {
			xmlParser = new XMLParser(xmlPath);
		}
		try {
			wf = xmlParser.getWrokflow();
		} catch (InvalidTestCaseException e) {
			return;
		}
		String profileName = wf.getProfile();
		String browser = wf.getBrowser();
		Logger.logInCouchDB = true;

		StopWatch timer = new StopWatch();
		timer.start();

		// Make the driver (either firefox or ie)
		// Firefox is the default if browser attribute is not defined
		WebDriver driver = null;
		if (browser == null || browser.equals("firefox")) {
			if (profileName.equals("")) {
				Logger.log(Logger.INFO, "Using Firefox with new profile...");
				FirefoxProfile profile = new FirefoxProfile();
				profile.setAcceptUntrustedCertificates(true);
				driver = new FirefoxDriver(profile);
			} else {
				try {
					Logger.log(Logger.INFO, "Using Firefox with profile: " + profileName + "...");
					ProfilesIni allProfiles = new ProfilesIni();
					FirefoxProfile profile = allProfiles.getProfile(profileName);
					profile.setAcceptUntrustedCertificates(true);
					driver = new FirefoxDriver(profile);
				} catch (NullPointerException e) {
					Logger.log(Logger.ERROR, "Cannot find profile given: " + profileName
							+ ", make sure you have this firefox profile.");
					result = false;
				}
			}
		} else if (browser.equals("ie") || browser.equals("internet explorer")) {
			Logger.log(Logger.INFO, "Using Internet Explorer...");
			driver = new InternetExplorerDriver();
		} else {
			Logger.log(Logger.ERROR,
					"Could not understand your browser attribute, make sure you specify browser used to run this test.");
			result = false;
		}

		ViewResult<BaseDocument> doc = couch.getDocument(ip, Integer.parseInt(port), db, hash);
		try {
			Logger.log(Logger.INFO, "=====Workflow " + wf.getWfId() + " start=====");
			// run each individual action in the workflow
			int sumFailureLoop = 0;
			for (int i = 0; i < wf.getActions().size(); i++) {
				Action actionToRun = wf.getActions().get(i);
				if (actionToRun.getName().equalsIgnoreCase("submitTCResult")) {
					// Single action to run
				} else if (actionToRun.getLoop() == null || actionToRun.getLoop().equalsIgnoreCase("")) {
					if (actionToRun.getName().equals("save")) {
						if (actionToRun.isInLoop())
							Logger.loopLog = true;
						else
							Logger.loopLog = false;
						Logger.log(Logger.INFO,
								"--- " + actionToRun.getAcID() + " Action Start: " + actionToRun.getName());
						runAction(actionToRun, driver, doc);
						if (!statusOfThisAction) {
							result = false;
						}
						couch.updateDocument(ip, Integer.parseInt(port), db, doc);
					} else {
						if (actionToRun.isInLoop())
							Logger.loopLog = true;
						else
							Logger.loopLog = false;
						Logger.log(Logger.INFO,
								"--- " + actionToRun.getAcID() + " Action Start: " + actionToRun.getName());
						runAction(actionToRun, driver, doc);
						if (!statusOfThisAction) {
							result = false;
						}
					}
					// Find a Loop start
				} else if (actionToRun.getLoop() != null && !actionToRun.getLoop().equalsIgnoreCase("")
						&& actionToRun.getIterations() != null && !actionToRun.getIterations().equalsIgnoreCase("")) {
					if (actionToRun.getInterupt() != null && !actionToRun.getInterupt().equalsIgnoreCase("")
							&& actionToRun.getSleep() != null && actionToRun.getStopType() != null
							&& !actionToRun.getStopType().equalsIgnoreCase("")) {
						ArrayList<Action> loopArr = new ArrayList<Action>();
						for (int j = i; j < wf.getActions().size(); j++) {
							Action actionInLoop = wf.getActions().get(j);
							if (actionInLoop.getLoop().equalsIgnoreCase(actionToRun.getLoop())) {
								loopArr.add(actionInLoop);
							}
						}
						// If loop contains more than 1 action, it will load
						// them all and run them sequentially
						Logger.loopLog = true;
						resultForLoop = true;
						if (loopArr.size() > 1) {
							Logger.log(Logger.INFO, "[LOOP: " + actionToRun.getLoop() + " START, ITERATIONS: "
									+ actionToRun.getIterations() + ", BREAK: " + actionToRun.getInterupt()
									+ ", SLEEP: " + actionToRun.getSleep() + "]");
							runLoopArr(loopArr, driver, doc, actionToRun.getIterations(), actionToRun.getSleep(),
									actionToRun.getInterupt());
							Logger.log(Logger.INFO, "[LOOP: " + actionToRun.getLoop() + " END, RESULT: "
									+ (resultForLoop ? "PASS" : "FAIL") + "]");
							if (!resultForLoop) {
								result = false;
							}

							// If loop contains only 1 action, it will load it
							// and run it.
						} else if (loopArr.size() == 1) {
							Logger.log(Logger.INFO, "[LOOP: " + actionToRun.getLoop() + " START, ITERATIONS: "
									+ actionToRun.getIterations() + ", BREAK: " + actionToRun.getInterupt()
									+ ", SLEEP: " + actionToRun.getSleep() + "]");
							for (int s = 0; s < Integer.parseInt(actionToRun.getIterations()); s++) {
								Logger.log(Logger.INFO, "--- " + actionToRun.getAcID() + " Action Start: "
										+ actionToRun.getName());
								runAction(actionToRun, driver, doc);
								if (actionToRun.getInterupt().equalsIgnoreCase("y") && statusOfThisAction)
									break;
								else if (!statusOfThisAction) {
									sumFailureLoop++;
									AJAXWaiter.forceSleep(Integer.parseInt(actionToRun.getSleep()) * 1000);
								}
							}
							if (sumFailureLoop == Integer.parseInt(actionToRun.getIterations())) {
								resultForLoop = false;
							}
							if (!resultForLoop) {
								result = false;
							}
							sumFailureLoop = 0;
							Logger.log(Logger.INFO, "[LOOP: " + actionToRun.getLoop() + " END, RESULT: "
									+ (resultForLoop ? "PASS" : "FAIL") + "]");
							Logger.loopLog = false;
						}
					} else {
						throw InvalidTestCaseException;
					}
				}
			}

			if (result == false || resultForLoop == false) {
				Logger.log(Logger.INFO, "=====Workflow " + wf.getWfId() + " end=====");
				Logger.log(Logger.INFO, "Result: Fail");
				Logger.log(Logger.INFO, "Time: " + timer.stop());
				// attach the log file to couch db
				Attachment summaryLogAttachment = new Attachment("text/plain", Logger.summaryLog.getBytes());
				doc.addAttachment(DateOperations.getDateString() + "-" + wf.getWfId() + ".summary",
						summaryLogAttachment);
				// set the location of the log file to TC_Summary_Location
				// Variable in couch
				if (Error_Code != null && Error_Code != "") {
					doc.setProperty("wf_result_" + wf.getWfId(), Error_Code);
				} else {
					doc.setProperty("wf_result_" + wf.getWfId(), "fail");
				}
				couch.updateDocument(ip, Integer.parseInt(port), db, doc);
			} else {
				Logger.log(Logger.INFO, "=====Workflow " + wf.getWfId() + " end=====");
				Logger.log(Logger.INFO, "Result: Pass");
				Logger.log(Logger.INFO, "Time: " + timer.stop());
				// attach the log file to couch db
				Attachment summaryLogAttachment = new Attachment("text/plain", Logger.summaryLog.getBytes());
				doc.addAttachment(DateOperations.getDateString() + "-" + wf.getWfId() + ".summary",
						summaryLogAttachment);
				// set the location of the log file to TC_Summary_Location
				// Variable in couch
				doc.setProperty("wf_result_" + wf.getWfId(), "pass");
				couch.updateDocument(ip, Integer.parseInt(port), db, doc);
			}
		} catch (AbortException e) {
			Logger.log(Logger.INFO, "Result: Fail");
			Logger.log(Logger.INFO, "Time: " + timer.stop());
			// attach the log file to couch db
			Attachment summaryLogAttachment = new Attachment("text/plain", Logger.summaryLog.getBytes());
			doc.addAttachment(DateOperations.getDateString() + "-" + wf.getWfId() + ".summary", summaryLogAttachment);
			// set the location of the log file to TC_Summary_Location Variable
			// in couch
			doc.setProperty("wf_result_" + wf.getWfId(), "fail");
			couch.updateDocument(ip, Integer.parseInt(port), db, doc);
		} catch (ErrorCodeException e) {
			Logger.log(Logger.INFO, "Result: Fail");
			Logger.log(Logger.INFO, "Time: " + timer.stop());
			// attach the log file to couch db
			Attachment summaryLogAttachment = new Attachment("text/plain", Logger.summaryLog.getBytes());
			doc.addAttachment(DateOperations.getDateString() + "-" + wf.getWfId() + ".summary", summaryLogAttachment);
			// set the location of the log file to TC_Summary_Location Variable
			// in couch
			doc.setProperty("wf_result_" + wf.getWfId(), Error_Code);
			couch.updateDocument(ip, Integer.parseInt(port), db, doc);
		} catch (InvalidTestCaseException e) {
			Logger.log(Logger.ERROR, "Something wrong with the test case, please check your testcase XML file");
			Attachment summaryLogAttachment = new Attachment("text/plain", Logger.summaryLog.getBytes());
			doc.addAttachment(DateOperations.getDateString() + "-" + wf.getWfId() + ".summary", summaryLogAttachment);
			// set the location of the log file to TC_Summary_Location Variable
			// in couch
			doc.setProperty("wf_result_" + wf.getWfId(), "fail");
			couch.updateDocument(ip, Integer.parseInt(port), db, doc);
		} catch (Exception e) {
			String error = e.toString() + "\n";
			for (int i = 0; i < e.getStackTrace().length; i++) {
				error += "    " + e.getStackTrace()[i].toString() + "\n";
			}
			Logger.log(Logger.ERROR, error);
			Logger.log(Logger.INFO, "=====Workflow " + wf.getWfId() + " end=====");
			Logger.log(Logger.INFO, "Result: Incomplete");
			Logger.log(Logger.INFO, "Time: " + timer.stop());
			// attach the log file to couch db
			Attachment summaryLogAttachment = new Attachment("text/plain", Logger.summaryLog.getBytes());
			doc.addAttachment(DateOperations.getDateString() + "-" + wf.getWfId() + ".summary", summaryLogAttachment);
			// set the location of the log file to TC_Summary_Location Variable
			// in couch
			doc.setProperty("Exceptions_" + wf.getWfId(), e.toString());
			doc.setProperty("wf_result_" + wf.getWfId(), "incomplete");
			couch.updateDocument(ip, Integer.parseInt(port), db, doc);
		} finally {
			driver.quit();
		}
	}

	private static void runLoopArr(ArrayList<Action> loopArr, WebDriver driver, ViewResult<BaseDocument> doc,
			String iterations, String sleep, String interupt) throws AbortException, InvalidTestCaseException,
			ErrorCodeException {
		int sumFailure = 0;
		for (int i = 0; i < Integer.parseInt(iterations); i++) {
			Logger.log(Logger.INFO, "[LOOP START ITERATION: " + (i + 1) + "]");
			for (int j = 0; j < loopArr.size(); j++) {
				Logger.log(Logger.INFO, "---- Action " + loopArr.get(j).getAcID() + " Start.");
				runAction(loopArr.get(j), driver, doc);
				if (!statusOfThisAction) {
					Logger.log(Logger.INFO, "[LOOP ITERATION: " + (i + 1) + " ACTION: " + (j + 1) + " FAILED]");
					sumFailure++;
					break;
				}
			}
			if (interupt.equalsIgnoreCase("y") && statusOfThisAction)
				break;
			else {
				AJAXWaiter.forceSleep(Integer.parseInt(sleep) * 1000);
			}
		}
		if (sumFailure == Integer.parseInt(iterations)) {
			resultForLoop = false;
			sumFailure = 0;
		}
	}

	public static InputStream getXML(String xmlPath) {
		InputStream is = null;
		try {
			is = new FileInputStream(xmlPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return is;
	}

	private static void runAction(Action act, WebDriver driver, ViewResult<BaseDocument> doc) throws AbortException,
			InvalidTestCaseException, ErrorCodeException {
		String actionName = act.getName().toLowerCase();
		if (actionName.equals("save")) {
			Save tc = new Save(act);
			tc.startTest(driver, doc, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("gethostip")) {
			GetHostIP tc = new GetHostIP(act);
			tc.startTest(driver, doc, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("navigate")) {
			Navigate tc = new Navigate(act);
			tc.startTest(driver, doc, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("refresh")) {
			Refresh tc = new Refresh(act);
			tc.startTest(driver, doc, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("goto")) {
			Goto tc = new Goto(act);
			tc.startTest(driver, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("input")) {
			Input tc = new Input(act);
			tc.startTest(driver, doc, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("click")) {
			Click tc = new Click(act);
			tc.startTest(driver, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("doubleclick")) {
			DoubleClick tc = new DoubleClick(act);
			tc.startTest(driver, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("hover")) {
			Hover tc = new Hover(act);
			tc.startTest(driver, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("verify")) {
			Verify tc = new Verify(act);
			tc.startTest(driver, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("managefile")) {
			ManageFile tc = new ManageFile(act);
			tc.startTest(driver, doc, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("managewindow")) {
			ManageWindow tc = new ManageWindow(act);
			tc.startTest(driver, doc, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("wait")) {
			Wait tc = new Wait(act);
			tc.startTest(driver, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("login")) {
			Login tc = new Login(act);
			tc.startTest(driver, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("register")) {
			Register tc = new Register(act);
			tc.startTest(driver, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("editaccount")) {
			EditAccount tc = new EditAccount(act);
			tc.startTest(driver, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("addcustomer")) {
			AddCustomer tc = new AddCustomer(act);
			tc.startTest(driver, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("removecustomer")) {
			RemoveCustomer tc = new RemoveCustomer(act);
			tc.startTest(driver, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("editcustomer")) {
			EditCustomer tc = new EditCustomer(act);
			tc.startTest(driver, true);
			endAction(act, tc.getStatus());
		} else if (actionName.equals("addcontact")) {
			AddDeliveryContact tc = new AddDeliveryContact(act);
			tc.startTest(driver, true);
			endAction(act, tc.getStatus());
		} else {
			Logger.log(Logger.ERROR, "The action: '" + actionName + "' is not supported, please check your input");
			result = false;
		}
	}

	private static void endAction(Action act, boolean status) throws AbortException, ErrorCodeException {
		if (act.getStopType().equals("abort") && status == false) {
			Logger.log(Logger.INFO, "=====Workflow " + wf.getWfId() + " end=====");
			Logger.log(Logger.INFO, "Action " + act.getAcID() + ": " + act.getName() + " aborted");
			throw new AbortException();
		} else if (act.getStopType().equals("continue") && status == false) {
		} else if (act.getStopType().equalsIgnoreCase("ignored")) {
			status = true;
		} else if (!act.getStopType().equals("abort") && !act.getStopType().equals("continue")
				&& !act.getStopType().equalsIgnoreCase("ignored") && status == false) {
			Logger.log(Logger.INFO, "=====Workflow " + wf.getWfId() + " end=====");
			Logger.log(Logger.INFO, "Action " + act.getAcID() + ": " + act.getName() + " aborted");
			Logger.log(Logger.INFO, "Error Code is " + act.getStopType());
			Error_Code = act.getStopType();
			status = false;
			throw new ErrorCodeException();
		}
		statusOfThisAction = status;
	}
}
