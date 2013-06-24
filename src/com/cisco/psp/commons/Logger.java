/**
 * Logger class used to log any operations and errors
 * 
 * @author jearnold
 * Last Modified: 10/13/11
 */
package com.cisco.psp.commons;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	// The file name is in the log directory with the date of logging
	private static String logFileDir = "../log/selenium/";
	public static String logFileName = logFileDir + DateOperations.getDateString() + ".log";
	public final static int DEBUG = 0;
	public final static int INFO = 1;
	public final static int ERROR = 2;
	public static boolean debug = false;
	public static boolean info = true;
	public static boolean error = true;
	public static boolean logInCouchDB = false;
	public static String actionLog = "";
	public static String summaryLog = "";
	public static boolean loopLog = false;
	public static String loopSpace = "    ";

	/**
	 * Logs a regular message in the log file, also prints it out to the console
	 * 
	 * @param message
	 *            - the message to log
	 */
	public static void log(String message) {
		System.out.println("[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss") + "]"
				+ message);
		File file = new File(logFileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(logFileName, true));
			out.write("[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss") + "]" + message);
			out.newLine();
			out.close();
		} catch (IOException e) {
			if (isDirThere())
				System.err.println(e);
		}
	}

	/**
	 * Log based on the level and if that level is turned on or not
	 * 
	 * @param level
	 *            - the logging level (DEBUG, INFO, ERROR)
	 * @param message
	 *            - the message to log
	 */
	public static void log(int level, String message) {
		if (level == 0 && debug) {
			String[] messageArray = message.split("\n");
			for (int i = 0; i < messageArray.length; i++) {
				String logMessage = "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
						+ " DEBUG]" + messageArray[i];
				if (loopLog) {
					logMessage = "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
							+ " DEBUG]" + loopSpace + messageArray[i];
				}
				System.out.println(logMessage);
				File file = new File(logFileName);
				try {
					if (!file.exists()) {
						file.createNewFile();
					}
					BufferedWriter out = new BufferedWriter(new FileWriter(logFileName, true));
					out.write(logMessage);
					out.newLine();
					out.close();
				} catch (IOException e) {
					if (isDirThere())
						System.err.println(e);
				}
				if (logInCouchDB) {
					actionLog += logMessage + "\n";
					summaryLog += logMessage + "\n";
				}
			}
		} else if (level == 1 && info) {
			String[] messageArray = message.split("\n");
			for (int i = 0; i < messageArray.length; i++) {
				String logMessage = "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
						+ "  INFO]" + messageArray[i];
				if (loopLog) {
					logMessage = "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
							+ "  INFO]" + loopSpace + messageArray[i];
				}
				System.out.println(logMessage);
				File file = new File(logFileName);
				try {
					if (!file.exists()) {
						file.createNewFile();
					}
					BufferedWriter out = new BufferedWriter(new FileWriter(logFileName, true));
					out.write(logMessage);
					out.newLine();
					out.close();
				} catch (IOException e) {
					if (isDirThere())
						System.err.println(e);
				}
				if (logInCouchDB) {
					actionLog += logMessage + "\n";
					summaryLog += logMessage + "\n";
				}
			}
		} else if (level == 2 && error) {
			String[] messageArray = message.split("\n");
			if (messageArray.length == 1) {
				String logMessage1 = "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
						+ " ERROR]************************************************************";
				String logMessage2 = "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
						+ " ERROR]" + message;
				String logMessage3 = "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
						+ " ERROR]************************************************************";
				if (loopLog) {
					logMessage1 = "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
							+ " ERROR]" + loopSpace
							+ "************************************************************";
					logMessage2 = "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
							+ " ERROR]" + loopSpace + message;
					logMessage3 = "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
							+ " ERROR]" + loopSpace
							+ "************************************************************";
				}
				System.out.println(logMessage1);
				System.out.println(logMessage2);
				System.out.println(logMessage3);
				File file = new File(logFileName);
				try {
					if (!file.exists()) {
						file.createNewFile();
					}
					BufferedWriter out = new BufferedWriter(new FileWriter(logFileName, true));
					out.write(logMessage1);
					out.newLine();
					out.write(logMessage2);
					out.newLine();
					out.write(logMessage3);
					out.newLine();
					out.close();
				} catch (IOException e) {
					if (isDirThere())
						System.err.println(e);
				}
				if (logInCouchDB) {
					actionLog += logMessage1 + "\n" + logMessage2 + "\n" + logMessage3 + "\n";
					summaryLog += logMessage1 + "\n" + logMessage2 + "\n" + logMessage3 + "\n";
				}
			} else {
				String logMessage1 = "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
						+ " ERROR]************************************************************";
				if (loopLog) {
					logMessage1 = "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
							+ " ERROR]" + loopSpace
							+ "************************************************************";
				}
				System.out.println(logMessage1);
				String logMessage2 = "";
				for (int i = 0; i < messageArray.length; i++) {
					if (i != messageArray.length - 1) {
						if (loopLog) {
							logMessage2 += "["
									+ DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
									+ " ERROR]" + loopSpace + messageArray[i] + "\n";
						}else{
							logMessage2 += "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
									+ " ERROR]" + messageArray[i] + "\n";
						}
					} else {
						if (loopLog) {
							logMessage2 += "["
									+ DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
									+ " ERROR]" + loopSpace + messageArray[i];
						}else{
							logMessage2 += "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
									+ " ERROR]" + messageArray[i];
						}
					}
				}
				System.out.println(logMessage2);
				String logMessage3 = "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
						+ " ERROR]************************************************************";
				if (loopLog) {
					logMessage3 = "[" + DateOperations.getDateString("MM/dd/yyyy HH:mm:ss")
							+ " ERROR]" + loopSpace
							+ "************************************************************";
				}
				System.out.println(logMessage3);
				File file = new File(logFileName);
				try {
					if (!file.exists()) {
						file.createNewFile();
					}
					BufferedWriter out = new BufferedWriter(new FileWriter(logFileName, true));
					out.write(logMessage1);
					out.newLine();
					String[] message2Lines = logMessage2.split("\n");
					for (int i = 0; i < message2Lines.length; i++) {
						out.write(message2Lines[i]);
						out.newLine();
					}
					out.write(logMessage3);
					out.newLine();
					out.close();
				} catch (IOException e) {
					if (isDirThere())
						System.err.println(e);
				}
				if (logInCouchDB) {
					actionLog += logMessage1 + "\n" + logMessage2 + "\n" + logMessage3 + "\n";
					summaryLog += logMessage1 + "\n" + logMessage2 + "\n" + logMessage3 + "\n";
				}
			}
		}
	}

	private static boolean isDirThere() {
		File logFile = new File(logFileDir);
		if (logFile.exists())
			return true;
		else
			return false;
	}
}
