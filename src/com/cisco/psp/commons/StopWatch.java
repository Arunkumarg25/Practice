package com.cisco.psp.commons;

public class StopWatch {
	private long time = 0;
	
	public StopWatch(){
		time = System.currentTimeMillis();
	}
	
	public void start(){
		time = System.currentTimeMillis();
	}
	
	public String stop(){
		int seconds = (int)((System.currentTimeMillis() - time)/1000.0);
		int minutes = 0;
		int hours = 0;
		if(seconds > 60){
			minutes = (int) (seconds/60);
			seconds = seconds - (minutes*60);
		}
		if(minutes > 60){
			hours = minutes/60;
			minutes = minutes - (hours*60);
		}
		return hours+" hours, "+minutes+" minutes, "+seconds+" seconds.";
	}
	
	public int secondsElapsed(){
		int seconds = (int)((System.currentTimeMillis() - time)/1000.0);
		return seconds;
	} 	
	
	public void reset(){
		time = System.currentTimeMillis();
	}
}
