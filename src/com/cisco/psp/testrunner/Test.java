package com.cisco.psp.testrunner;

import org.apache.commons.configuration.ConfigurationException;

import com.cisco.psp.commons.ConfigSetting;


public class Test {
	public static void main(String[] arags) throws ConfigurationException{
		System.out.println(ConfigSetting.getDownloadDest());
	}
}