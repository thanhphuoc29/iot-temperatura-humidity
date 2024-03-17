package com.IOTProject.model;

import java.sql.Timestamp;

public class ActionLog {
	public int id;
	public String name;
	public String action;
	public Timestamp time;
	
	public ActionLog(String name, String action, Timestamp time) {
		super();
		this.name = name;
		this.action = action;
		this.time = time;
	}
	
	
}
