package com.cisco.psp.objects;

import java.util.HashMap;
import java.util.Iterator;

public class Action {
	private String acID = "";
	private String name = "";
	private String dfid = "";
	private int timeout = 1800;
	private String stoptype = "";
	private String loop = "";
	private String iterations = "";
	private String sleep = "";
	private String interupt = "";	
	private HashMap<String, String> params = new HashMap<String, String>();

	public Action() {
		acID = "";
		name = "";
		dfid = "";
		params = new HashMap<String, String>();
		timeout = 1800;
		stoptype = "";
	}

	public Action(String acID, String name, String dfid, HashMap<String, String> params) {
		this.acID = acID;
		this.name = name;
		this.dfid = dfid;
		this.params = params;
	}

	public Action(String acID, String name, HashMap<String, String> params, int timeout, String stoptype) {
		this.acID = acID;
		this.name = name;
		this.params = params;
		this.timeout = timeout;
		this.stoptype = stoptype;
	}

	public String getAcID() {
		return acID;
	}

	public void setAcID(String acID) {
		this.acID = acID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDfid() {
		return dfid;
	}

	public void setDfid(String dfid) {
		this.dfid = dfid;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

	public String getParam(String key) {
		return params.get(key.toLowerCase());
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setStoptype(String stoptype) {
		this.stoptype = stoptype;
	}

	public String getStopType() {
		return stoptype;
	}

	public String getLoop() {
		return loop;
	}

	public void setLoop(String loop) {
		this.loop = loop;
	}

	public String getIterations() {
		return iterations;
	}

	public void setIterations(String iterations) {
		this.iterations = iterations;
	}

	public String getSleep() {
		return sleep;
	}

	public void setSleep(String sleep) {
		this.sleep = sleep;
	}

	public String getInterupt() {
		return interupt;
	}

	public void setInterupt(String interupt) {
		this.interupt = interupt;
	}

	public boolean isInLoop() {
		if (loop == null || loop.equals("")) {
			return false;
		} else
			return true;
	}

	public String toString() {
		String actionString = "";
		actionString += "Action ID: " + acID + "\n";
		actionString += "Action Name: " + name + "\n";
		// actionString += "Action DFID: "+dfid+"\n";
		actionString += "Action Stoptype: " + stoptype + '\n';
		Iterator<String> paramsIterator = params.keySet().iterator();
		while (paramsIterator.hasNext()) {
			String key = paramsIterator.next();
			String value = params.get(key);
			actionString += key + ": " + value + "\n";
		}
		return actionString;
	}
}
