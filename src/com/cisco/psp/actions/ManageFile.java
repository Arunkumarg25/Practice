package com.cisco.psp.actions;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.document.ViewResult;
import org.openqa.selenium.WebDriver;

import com.cisco.psp.commons.ConfigSetting;
import com.cisco.psp.commons.DownloadFile;
import com.cisco.psp.commons.Logger;
import com.cisco.psp.db.CouchDAO;
import com.cisco.psp.objects.Action;
import com.cisco.psp.testrunner.InvalidTestCaseException;

public class ManageFile {
	boolean status = true;
	String type, value = null;
	int timeout=1800;

	public ManageFile(Action act) {
		parseAction(act);
	}

	public boolean getStatus() {
		return status;
	}

	private void parseAction(Action act) {
		type = act.getParam("type");
		value = act.getParam("value");
		timeout=act.getTimeout();
	}

	public void startTest( final WebDriver driver, final ViewResult<BaseDocument> doc, final boolean schedule) {
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<String> task = new Callable<String>() {
			public String call() {
				try {
					Logger.log(Logger.INFO, "Executing the Action.");
					execute(driver, doc, schedule);
					return "Finished";
				} catch (InvalidTestCaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "Abort";
				}				
			}
		};
		Future<String> future = executor.submit(task);
		try {
			String result = future.get(timeout, TimeUnit.SECONDS);
			if(!result.equalsIgnoreCase("Finished")){				
				Logger.log(Logger.ERROR, "Error occured at calling ece method.");
				status = false;
			}
		} catch (TimeoutException ex) {
			Logger.log(Logger.ERROR, "Time is over "+timeout+" Seconds. Action stopped\n"+ex.getMessage());
			status = false;
			future.cancel(true);
		} catch (InterruptedException e) {
			Logger.log(Logger.ERROR, "InterruptedException is captured: \n" + e.getMessage());	
			status = false;
			future.cancel(true);
		} catch (ExecutionException e) {
			Logger.log(Logger.ERROR, "ExecutionException is captured: \n" + e.getMessage());
			status = false;
			future.cancel(true);
		} 
	}
	
	public void execute(WebDriver driver, ViewResult<BaseDocument> doc, boolean schedule)
			throws InvalidTestCaseException {
		if (type.equalsIgnoreCase("delete")) {
			String destFolder = ConfigSetting.getDownloadDest();
			boolean result = DownloadFile.deleteFile(value, destFolder);
			if (result)
				Logger.log(Logger.INFO, "File: " + value + " has been deleted successfully.");
			else {
				Logger.log(Logger.ERROR, "File: " + value + " deleted failed");
				status = false;
			}
		} else if (type.equalsIgnoreCase("unzip")) {
			String destFolder = ConfigSetting.getDownloadDest();
			boolean result = DownloadFile.unzipFile(value, destFolder);
			if (result) {
				Logger.log(Logger.INFO, " Zip File: " + value + " has been unzipped successfully.");
			} else {
				Logger.log(Logger.ERROR, " Unzip " + value + " failed. Please check it.");
				status = false;
			}
		} else if (type.equalsIgnoreCase("savefile")) {
			CouchDAO couch = new CouchDAO();
			String docid = couch.uploadAttachment(ConfigSetting.getCouchIP(), ConfigSetting.getCouchPort(),
					ConfigSetting.getCouchDBName(), doc, value);
			if (docid != null) {
				Logger.log(
						Logger.INFO,
						" Upload the file: " + value + " to the configured CouchDB" + "\n\t"
								+ "Please visit the follow link to check the result. \n\t" + "http://"
								+ ConfigSetting.getCouchIP() + ":" + ConfigSetting.getCouchPort()
								+ "/_utils/document.html?" + ConfigSetting.getCouchDBName() + "/" + doc.getId());
			} else {
				Logger.log(Logger.ERROR, "Upload " + value + " failed. Please check The followed link. \n " + "http://"
						+ ConfigSetting.getCouchIP() + ":" + ConfigSetting.getCouchPort() + "/_utils/document.html?"
						+ ConfigSetting.getCouchDBName() + "/" + doc.getId());
				status = false;
			}
		} else if (type.equalsIgnoreCase("loadfile")) {
			CouchDAO couch = new CouchDAO();
			boolean result = couch.loadAttachment(ConfigSetting.getCouchIP(), ConfigSetting.getCouchPort(),
					ConfigSetting.getCouchDBName(), doc, value);
			if (result) {
				Logger.log(Logger.INFO, " Load File: " + value + "success.");
			} else {
				Logger.log(Logger.ERROR, " Load File " + value + " failed. Please check it.");
				status = false;
			}
		}
	}
}
