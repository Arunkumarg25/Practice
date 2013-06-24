package com.cisco.psp.commons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;

public class DownloadFile {

	public DownloadFile() {
		super.getClass();
	}

	/**
	 * Verify the file name and return the result.
	 * 
	 * @param expectedFileName
	 * @param destFolder
	 * @return boolean If the file name is the same with the expected one, it
	 *         returns true. Otherwise it returns false.
	 */
	public static boolean verifyFileName(String expectedFileName,
			String destFolder) {
		File generatedFile = new File(destFolder + File.separator
				+ expectedFileName);
		if (generatedFile.exists()
				&& generatedFile.getName().equalsIgnoreCase(expectedFileName)) {
			Logger.log(Logger.INFO, "The file name: " + generatedFile.getName()
					+ " is the same as expected.");
			return true;
		} else {
			Logger.log(Logger.ERROR,
					"The file name: " + generatedFile.getName()
							+ " is not expected or the file is not existed.");
			return false;
		}
	}

	/**
	 * Verify the file size and return result.
	 * 
	 * @param file
	 * @return boolean If the file size is not 0, then it returns true.
	 *         Otherwise it returns false.
	 */
	public static boolean verifyFileSize(File file) {
		if (file.isFile()) {
			if (file.length() != 0) {
				Logger.log(Logger.INFO,
						"The size of the file " + file.getName() + " is "
								+ Long.toString(file.length()));
				return true;
			} else {
				Logger.log(Logger.ERROR, "The file: " + file.getName()
						+ " is an empty file. Please check it.");
				return false;
			}
		} else {
			Logger.log(Logger.ERROR,
					"The given destination: " + file.getAbsolutePath()
							+ " is not a file. Please check it.");
			return false;
		}
	}

	/**
	 * Verify the file size and return result.
	 * 
	 * @param expectedFileName
	 * @param destFolder
	 * @return
	 */
	public static boolean verifyFileSize(String expectedFileName,
			String destFolder) {
		File file = new File(destFolder + File.separator + expectedFileName);
		if (file.isFile()) {
			if (file.length() != 0) {
				Logger.log(Logger.INFO,
						"The size of the file " + file.getName() + " is "
								+ Long.toString(file.length()));
				return true;
			} else {
				Logger.log(Logger.ERROR, "The file: " + file.getName()
						+ " is an empty file. Please check it.");
				return false;
			}
		} else {
			Logger.log(Logger.ERROR,
					"The given destination: " + file.getAbsolutePath()
							+ " is not a file. Please check it.");
			return false;
		}
	}
	
	public static boolean verifyCSV(String expectedFileName, String destFolder, String searchString){
		boolean b = false;		
		File file = new File(destFolder + File.separator + expectedFileName);
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br=new BufferedReader(fr);
            String strLine="";
            while((strLine=br.readLine())!=null){
            	if(strLine.contains(searchString)){
            		b = true; 
            		break;
            	}           	
            }
            fr.close();
            br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return b;	
	}
	

	/**
	 * Delete file or Directory
	 * 
	 * @param expectedFileName
	 * @param destFolder
	 * @return
	 */
	public static boolean deleteFile(String expectedFileName, String destFolder) {
		File file = new File(destFolder + File.separator + expectedFileName);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					if (!files[i].delete()) {
						deleteFile(files[i].getName(), files[i].getParent());
					}
				} else if (files[i].isFile()) {
					files[i].delete();
				}
			}
		}
		return file.delete();
	}

	/**
	 * Unzip the file that was specifed in test case
	 * 
	 * @param zipFileName
	 * @param destFolder
	 * @return boolean
	 * 
	 */
	public static boolean unzipFile(String zipFileName, String destFolder) {
		File file;
		ZipFile zipFile;
		try {
			file = new File(destFolder + File.separator + zipFileName);
			zipFile = new ZipFile(file);			
			InputStream is = null;

			for (Enumeration<ZipArchiveEntry> enumEntries = zipFile
					.getEntries(); enumEntries.hasMoreElements();) {
				ZipArchiveEntry entry = enumEntries.nextElement();
				is = zipFile.getInputStream(entry);
				if (entry.isDirectory()) {
					File tempDir = new File(destFolder + File.separator
							+ entry.getName());
					tempDir.mkdirs();
				} else {
					File tempFile = new File(destFolder + File.separator
							+ entry.getName());
					if (tempFile.exists()) {
						FileOutputStream fos = new FileOutputStream(tempFile);
						IOUtils.copy(is, fos);
						fos.flush();
						fos.close();
					} else {
						if (!tempFile.getParentFile().exists()) {
							tempFile.getParentFile().mkdirs();
						}
						tempFile.createNewFile();
						FileOutputStream fos = new FileOutputStream(tempFile);
						IOUtils.copy(is, fos);
						fos.flush();
						fos.close();
					}
				}
			}
			is.close();
			zipFile.close();
			return true;
		} catch (Exception e) {
			Logger.log(Logger.ERROR, "Error Info: " + e.getLocalizedMessage());
			return false;
		}
	}
}
