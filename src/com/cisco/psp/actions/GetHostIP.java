package com.cisco.psp.actions;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
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

import com.cisco.psp.commons.Logger;
import com.cisco.psp.objects.Action;

public class GetHostIP {
	boolean status = true;
	String waiting = "";
	String savemeta = "";
	int timeout = 1800;
	String type, value = "";

	public GetHostIP(Action action) {
		parseAction(action);
	}

	public void startTest(final WebDriver driver, final ViewResult<BaseDocument> doc, final boolean schedule) {
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<String> task = new Callable<String>() {
			public String call() {
				execute(driver, doc, schedule);
				return "Finished";
			}
		};
		Future<String> future = executor.submit(task);
		try {
			String result = future.get(timeout, TimeUnit.SECONDS);
			if (!result.equalsIgnoreCase("Finished")) {
				Logger.log(Logger.ERROR, "Error occured at calling ece method.");
				status = false;
			}
		} catch (TimeoutException ex) {
			Logger.log(Logger.ERROR, "Time is over " + timeout + " Seconds. Action stopped");
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

	public void execute(WebDriver driver, ViewResult<BaseDocument> doc, boolean schedule) {
		List<InetAddress> addrList = new ArrayList<InetAddress>();
		Enumeration<NetworkInterface> enu;
		try {
			enu = NetworkInterface.getNetworkInterfaces();
			while (enu.hasMoreElements()) {
				NetworkInterface ifc = (NetworkInterface) enu.nextElement();
				if (ifc.isUp()) {
					Enumeration<InetAddress> enuIP = ifc.getInetAddresses();
					while (enuIP.hasMoreElements()) {
						addrList.add(enuIP.nextElement());
					}
				}
			}
			if (savemeta.equalsIgnoreCase("true")) {
				if (addrList.size() != 0) {
					String ip = "ip";
					if (doc.getProperty("meta") == null) {
						doc.setProperty("meta", new HashMap<String, String>());
					}
					@SuppressWarnings("unchecked")
					HashMap<String, String> meta = (HashMap<String, String>) doc.getProperty("meta");
					for (int i = 0; i < addrList.size(); i++) {
						meta.put(ip + String.valueOf(i), addrList.get(i).toString());
						Logger.log(Logger.INFO, "Save IP: " + addrList.get(i).toString());
					}
					doc.setProperty("meta", meta);
					Logger.log(Logger.INFO, "Save the IPs to Couch DB.");
				}
			}
		} catch (Exception e) {
			status = false;
			Logger.log(Logger.ERROR, "Save the IPs Failed: "+ e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public boolean getStatus() {
		return status;
	}

	private void parseAction(Action act) {
		type = act.getParam("type");
		value = act.getParam("value");
		savemeta = act.getParam("savemeta");
		timeout = act.getTimeout();
	}
}