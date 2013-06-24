package com.cisco.psp.db;

import org.jcouchdb.db.Database;

public class CouchDB {

	public Database createDB(String IP, int port, String DBname){
		return new Database(IP, port, DBname);
	}
}
