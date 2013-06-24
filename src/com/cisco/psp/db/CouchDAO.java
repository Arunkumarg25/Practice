package com.cisco.psp.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.jcouchdb.db.Database;
import org.jcouchdb.document.Attachment;
import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.document.ViewResult;

import com.cisco.psp.commons.ConfigSetting;
import com.cisco.psp.commons.Logger;

public class CouchDAO {
	private String couchHash = "";
	private String couchIP = "";
	private int couchPort;
	private String DBname = "";
	private Database db = null;

	public CouchDAO() {
		super.getClass();
	}

	public CouchDAO(String couchHash, String couchIP, int couchPort,
			String DBname) {
		this.couchHash = couchHash;
		this.couchIP = couchIP;
		this.couchPort = couchPort;
		this.DBname = DBname;
	}

	/**
	 * Upload certain file into couch
	 * 
	 * @param couchIP
	 * @param couchPort
	 * @param DBname
	 * @param doc
	 * @param fileName
	 * @return String, the id of that document
	 */
	public String uploadAttachment(String couchIP, int couchPort,
			String DBname, ViewResult<BaseDocument> doc, String fileName) {
		CouchDB couchDB = new CouchDB();
		db = couchDB.createDB(couchIP, couchPort, DBname);
		Attachment attachment;
		try {
			File f = new File(ConfigSetting.getDownloadDest() + File.separator
					+ fileName);
			attachment = new Attachment("text/plain",
					FileUtils.readFileToByteArray(f));
			doc.addAttachment(fileName, attachment);
			doc.setProperty("attachment", "true");
			if (doc.getProperty("variables") == null) {
				doc.setProperty("variables", new HashMap<String, String>());
			}
			@SuppressWarnings("unchecked")
			HashMap<String, String> variables = (HashMap<String, String>) doc
					.getProperty("variables");
			variables.put("attachmentlink",
					"http://" + ConfigSetting.getCouchIP() + ":"
							+ ConfigSetting.getCouchPort() + "/" + DBname + "/"
							+ doc.getId() + "/" + fileName);
			variables.put("attachmentfile", fileName);
			doc.setProperty("variables", variables);
			db.createOrUpdateDocument(doc);
			return doc.getId();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Load the file from the couch
	 * 
	 * @param couchIP
	 * @param couchPort
	 * @param DBname
	 * @param doc
	 * @param fileName
	 * @return boolean
	 */
	public boolean loadAttachment(String couchIP, int couchPort, String DBname,
			ViewResult<BaseDocument> doc, String fileName) {
		try {
			URL attachmentURL = new URL("http://" + couchIP + ":" + couchPort
					+ "/" + DBname + "/" + doc.getId() + "/" + fileName);
			Logger.log(Logger.INFO, "Opening connection to couch DB: "
					+ "http://" + couchIP + ":" + couchPort + "/" + DBname
					+ "/" + doc.getId() + "/" + fileName);
			URLConnection attachmentConnection = attachmentURL.openConnection();
			InputStream is = attachmentConnection.getInputStream();
			File file = new File(ConfigSetting.getDownloadDest()
					+ File.separator + fileName);
			FileOutputStream fos = new FileOutputStream(file);
			IOUtils.copy(is, fos);
			is.close();
			fos.close();
			Logger.log(Logger.INFO,
					fileName + " has been downloaded. Please check it in "
							+ file.getAbsolutePath());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Get a document from couch db based on the doc hash
	 * 
	 * @param couchIP
	 * @param couchPort
	 * @param DBname
	 * @param docID
	 * @return
	 */
	public ViewResult<BaseDocument> getDocument(String couchIP, int couchPort,
			String DBname, String docID) {
		CouchDB couch = new CouchDB();
		db = couch.createDB(couchIP, couchPort, DBname);
		ViewResult<BaseDocument> doc = db.query(docID, BaseDocument.class,
				null, null, null);
		return doc;
	}

	/**
	 * Update a document with the given doc
	 * 
	 * @param couchIP
	 * @param couchPort
	 * @param DBname
	 * @param doc
	 */
	public void updateDocument(String couchIP, int couchPort, String DBname,
			ViewResult<BaseDocument> doc) {
		CouchDB couch = new CouchDB();
		db = couch.createDB(couchIP, couchPort, DBname);
		db.updateDocument(doc);
	}

	/**
	 * Get the tc.xml from couch db, and return it as InputStream to the xml
	 * parser
	 * 
	 * @param hash
	 *            - the hash for the couch db document
	 * @param ip
	 *            - the ip of the couch db
	 * @param port
	 *            - the port of the couch db
	 * @param db
	 *            - the db name of the couch db
	 * @return InputStream: the xml file as InputStream to xml parser
	 */
	public InputStream getXML(String hash, String ip, String port, String db) {
		try {
			URL tc = new URL("http://" + ip + ":" + port + "/" + db + "/"
					+ hash + "/tc.xml");
			Logger.log(Logger.DEBUG, "Opening connection to couch DB: " + ip
					+ ":" + port);
			URLConnection yc = tc.openConnection();
			return yc.getInputStream();
		} catch (Exception e) {
			return null;
		}
	}

	public String getCouchHash() {
		return couchHash;
	}

	public void setCouchHash(String couchHash) {
		this.couchHash = couchHash;
	}

	public String getCouchIP() {
		return couchIP;
	}

	public void setCouchIP(String couchIP) {
		this.couchIP = couchIP;
	}

	public int getCouchPort() {
		return couchPort;
	}

	public void setCouchPort(int couchPort) {
		this.couchPort = couchPort;
	}

	public String getDBname() {
		return DBname;
	}

	public void setDBname(String dBname) {
		this.DBname = dBname;
	}

	public Database getDb() {
		return db;
	}

	public void setDb(Database db) {
		this.db = db;
	}

}
