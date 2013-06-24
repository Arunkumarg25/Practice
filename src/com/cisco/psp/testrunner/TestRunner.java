package com.cisco.psp.testrunner;
import com.cisco.psp.commons.Logger;

public class TestRunner {
	public static void main (String[] args){
		int argNum = args.length;
		String logLevel = "";
		String scheduleHash = "";
		String couchIp = "";
		String couchPort = "";
		String couchDB = "";
		String xmlPath = "";
		
		for(int i = 0; i < argNum; i+=2){
			String arg = args[i].trim();
			if(arg.equals("-l"))
				logLevel = args[i+1].trim();
			else if(arg.equals("-s"))
				scheduleHash = args[i+1].trim();
			else if(arg.equals("-i"))
				couchIp = args[i+1].trim();
			else if(arg.equals("-p"))
				couchPort = args[i+1].trim();
			else if(arg.equals("-db"))
				couchDB = args[i+1].trim();
			else if(arg.equals("-xml"))
				xmlPath = args[i+1].trim();
			else{
				System.out.println("The argument "+arg+" is not supported, please use -h for help.");
				break;
			}
		}
		
		if(!logLevel.equals("")){
			Logger.debug = false;
			Logger.info = false;
			Logger.error = false;
			if(logLevel.contains("debug"))
				Logger.debug = true;
			if(logLevel.contains("info"))
				Logger.info = true;
			if(logLevel.contains("error"))
				Logger.error = true;
			if(!logLevel.contains("debug") && !logLevel.contains("info") && !logLevel.contains("error")){
				System.out.println("Logging level: "+logLevel+" not found, check your -l option");
				return;
			}
		}
		
		if(!scheduleHash.equals("")){
			ScheduleRunner.runWorkflow(scheduleHash, couchIp, couchPort, couchDB, xmlPath);
			return;
		}
	}
}
