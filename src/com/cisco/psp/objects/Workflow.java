package com.cisco.psp.objects;

import java.util.ArrayList;

public class Workflow {
	public static final int CONTINUE = 0;
	public static final int ABORT = 0;
	private String wfId = "";
	private int stoptype;
	private String profile = "";
	private String browser = null;
	private ArrayList<Action> actions = new ArrayList<Action>();
	
	public Workflow(){
		wfId = "";
		actions = new ArrayList<Action>();
	}
	
	public Workflow(String wfId, ArrayList<Action> actions){
		this.wfId = wfId;
		this.actions = actions;
	}
	
	public Workflow(String wfId, ArrayList<Action> actions, int stoptype, String browser, String profile){
		this.wfId = wfId;
		this.actions = actions;
		this.stoptype = stoptype;
		this.browser = browser;
		this.profile = profile;
	}

	public String getWfId() {
		return wfId;
	}

	public void setWfId(String tcId) {
		this.wfId = tcId;
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}
	
	public void addAction(Action action){
		actions.add(action);
	}
	
	public int getStoptype(){
		return stoptype;
	}
	
	public void setStoptype(int stoptype){
		this.stoptype = stoptype;
	}
	
	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getProfile(){
		return profile;
	}
	
	public void setProfile(String profile){
		this.profile = profile;
	}
	
	public String toString(){
		String workflow = "";
		workflow += "wfid: "+wfId+" stoptype: "+stoptype+" profile: "+profile;
		for(int i =0; i < actions.size(); i++){
			workflow += actions.get(i).toString();
		}
		return workflow;
	}
}
