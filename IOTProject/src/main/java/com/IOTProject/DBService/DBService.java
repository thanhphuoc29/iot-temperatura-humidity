package com.IOTProject.DBService;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBService {
	public static DBService instance;
	
	public static DBService gI() {
		if(instance == null) {
			instance = new DBService();
		}
		return instance;
	}
	
	public Connection getConnection() {
		Connection conn = null;
		try {
			String url = "jdbc:mysql://localhost:3306/sensor_data?useUnicode=true&characterEncoding=utf-8";
			String user ="root";
			String password = "29012002";
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}