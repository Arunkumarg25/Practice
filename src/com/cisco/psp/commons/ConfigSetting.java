package com.cisco.psp.commons;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigSetting {
	public ConfigSetting() {
		super.getClass();
	}

	public static String getDownloadDest() {
		Configuration config;
		try {
			config = new PropertiesConfiguration(
					"./data/selenium_config/config.properties");
			return config.getString("downloadDest");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get the XPath of the rows on Mobile site
	 * 
	 * @return String
	 */
	public static String getMobileSiteRowsXPath() {
		Configuration config;
		try {
			config = new PropertiesConfiguration(
					"./data/selenium_config/config.properties");
			return config.getString("mobileSiteRowsXPath");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get the XPath of the rows on regular site
	 * 
	 * @return
	 */
	public static String getRegularSiteRowsXPath() {
		Configuration config;
		try {
			config = new PropertiesConfiguration(
					"./data/selenium_config/config.properties");
			return config.getString("regularSiteRowsXPath");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getCouchIP() {
		Configuration config;
		try {
			config = new PropertiesConfiguration(
					"./data/selenium_config/config.properties");
			return config.getString("couchIP");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static int getCouchPort() {
		Configuration config;
		try {
			config = new PropertiesConfiguration(
					"./data/selenium_config/config.properties");
			return config.getInt("couchPort");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String getCouchDBName() {
		Configuration config;
		try {
			config = new PropertiesConfiguration(
					"./data/selenium_config/config.properties");
			return config.getString("couchDBName");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
