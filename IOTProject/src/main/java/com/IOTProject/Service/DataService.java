package com.IOTProject.Service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.IOTProject.DBService.DBService;
import com.IOTProject.model.ActionLog;
import com.IOTProject.model.Data;
import com.IOTProject.model.Sensor;

@Service
public class DataService {
	
	
	public List<Data> getListDataByFilter(String start,String end,int limit,int offset) {
//		System.err.println("day: "+day);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isFilter = false;
		String sql = "SELECT * FROM `data` WHERE DATE(time) >= ? AND DATE(time) <= ? ORDER BY time DESC LIMIT ? OFFSET ?";
		if(start.equals("") || end.equals("")) {
			sql = "SELECT * FROM `data` ORDER BY time DESC LIMIT ? OFFSET ?";
			
		} else {
			sql = "SELECT * FROM `data` WHERE DATE(time) >= ? AND DATE(time) <= ? ORDER BY time DESC LIMIT ? OFFSET ?";
			isFilter = true;
		}
		List<Data> lstData = new ArrayList<>();
		try {
			conn = DBService.gI().getConnection();
			ps = conn.prepareStatement(sql);
			if(isFilter) {
				ps.setString(1, start);
				ps.setString(2, end);
				ps.setInt(3, limit);
				ps.setInt(4, offset);
			} else {
				ps.setInt(1, limit);
				ps.setInt(2, offset);
			}
			
			rs = ps.executeQuery();
			while (rs.next()) {
				Data temp = new Data(rs.getFloat("temperature"),
						rs.getFloat("humidity"),
						rs.getFloat("light"),
						rs.getTimestamp("time"));
				temp.setDobui(rs.getFloat("dobui"));
				lstData.add(temp);
//				System.out.println("time: "+rs.getTimestamp("time"));
			}
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return lstData;
	}
	
	public List<Data> getListDataNow() {
//		System.err.println("day: "+day);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM `data` WHERE DATE(time) = ?";
		Date now = new Date(System.currentTimeMillis());
		List<Data> lstData = new ArrayList<>();
		try {
			conn = DBService.gI().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setDate(1, now);
			rs = ps.executeQuery();
			while (rs.next()) {
				Data tempData = new Data(rs.getFloat("temperature"),
						rs.getFloat("humidity"),
						rs.getFloat("light"),
						rs.getTimestamp("time"));
				tempData.setDobui(rs.getFloat("dobui"));
				lstData.add(tempData);
//				System.out.println("time: "+rs.getTimestamp("time"));
			}
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return lstData;
	}
	
	public int getRowsData(String start, String end) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) as count FROM `data`";
		boolean isFilter = false;
		if(start.equals("") || end.equals("")) {
			sql = "SELECT COUNT(*) as count FROM `data`";
		} else {
			sql = "SELECT COUNT(*) as count FROM `data` WHERE DATE(time) >= ? AND DATE(time) <= ?";
			isFilter = true;
		}
		int rows = 0;
		try {
			conn = DBService.gI().getConnection();
			ps = conn.prepareStatement(sql);
			if(isFilter) {
				ps.setString(1, start);
				ps.setString(2, end);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				rows = rs.getInt("count");
				System.out.println("count: "+rows);
			}
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return rows;
	}
	
	public int getRowsActionLog(String start, String end) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) as count FROM `history_action`";
		boolean isFilter = false;
		if(start.equals("") || end.equals("")) {
			sql = "SELECT COUNT(*) as count FROM `history_action`";
		} else {
			sql = "SELECT COUNT(*) as count FROM `history_action` WHERE DATE(time) >= ? AND DATE(time) <= ?";
			isFilter = true;
		}
		int rows = 0;
		try {
			conn = DBService.gI().getConnection();
			ps = conn.prepareStatement(sql);
			if(isFilter) {
				ps.setString(1, start);
				ps.setString(2, end);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				rows = rs.getInt("count");
				System.out.println("num rows action: "+rows);
			}
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return rows;
	}
	
	public List<Data> getAllListData(int limit,int offset) {
//		System.err.println("day: "+day);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM `data` ORDER BY time DESC LIMIT ? OFFSET ?";
		List<Data> lstData = new ArrayList<>();
		try {
			conn = DBService.gI().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, limit);
			ps.setInt(2, offset);
			rs = ps.executeQuery();
			while (rs.next()) {
				lstData.add(new Data(rs.getFloat("temperature"),
						rs.getFloat("humidity"),
						rs.getFloat("light"),
						rs.getTimestamp("time")));
//				System.out.println("time: "+rs.getTimestamp("time"));
			}
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return lstData;
	}
	
	public List<Sensor> getListSensor() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM `sensor`";
		List<Sensor> lstData = new ArrayList<>();
		try {
			conn = DBService.gI().getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				lstData.add(new Sensor(rs.getInt("id"),
						rs.getString("name"),
						rs.getInt("isOn") == 1 ? true : false));
				
			}
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return lstData;
	}
	
	public List<ActionLog> getActionLogs(String start,String end,int limit,int offset) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM `history_action`";
		boolean isFilter = false;
//		String sql = "SELECT * FROM `data` WHERE DATE(time) >= ? AND DATE(time) <= ? LIMIT ? OFFSET ?";
		if(start.equals("") || end.equals("")) {
			sql = "SELECT * FROM `history_action` LIMIT ? OFFSET ?";
			
		} else {
			sql = "SELECT * FROM `history_action` WHERE DATE(time) >= ? AND DATE(time) <= ? LIMIT ? OFFSET ?";
			isFilter = true;
		}
		List<ActionLog> lstAction = new ArrayList<>();
		try {
			conn = DBService.gI().getConnection();
			ps = conn.prepareStatement(sql);
			if(isFilter) {
				ps.setString(1, start);
				ps.setString(2, end);
				ps.setInt(3, limit);
				ps.setInt(4, offset);
			} else {
				ps.setInt(1, limit);
				ps.setInt(2, offset);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				lstAction.add(new ActionLog(rs.getString("name"),
						rs.getString("action"),
						rs.getTimestamp("time")));
				
			}
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return lstAction;
	}
	
	public boolean updateData(Data data) {
		boolean status = false;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "INSERT INTO `data` (temperature,humidity,light,dobui,time) VALUES (?,?,?,?,?)";
		List<Data> lstData = new ArrayList<>();
		try {
			conn = DBService.gI().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setFloat(1, data.getTemperature());
			ps.setFloat(2, data.getHumidity());
			ps.setFloat(3, data.getLight());
			ps.setFloat(4, data.getDobui());
			ps.setTimestamp(5, data.getTimeStamp());
			ps.executeUpdate();
			conn.commit();
			conn.close();
			System.out.println("update data done");
			status = true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return status;
	}
	
	public boolean updateStatusSensor(Sensor sensor) {
		boolean status = false;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "UPDATE sensor SET isOn = ? WHERE id = ?";
		String INSERT_ACTION = "INSERT INTO history_action (`name`,action) VALUES (?,?)";
		try {
			conn = DBService.gI().getConnection();
		    
		    // Tắt chế độ tự động commit
		    conn.setAutoCommit(false);
		    
		    ps = conn.prepareStatement(sql);
		    
		    // Danh sách các bản ghi sensor cần cập nhật
		    
		    ps.setInt(1, sensor.isOn ? 1 : 0);
	        ps.setInt(2, sensor.getId());
	        ps.executeUpdate();

		    conn.commit();
		    
		    //insert log action
		    conn.setAutoCommit(false);
		    ps = conn.prepareStatement(INSERT_ACTION);
		    ps.setString(1, sensor.nameUser);
		    String action = (sensor.isOn ? "turn on light " : "turn off light ") + (sensor.getId());
		    ps.setString(2, action);
		    ps.executeUpdate();
		    conn.commit();
		    System.out.println("save status sensor done!");
		    status = true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return status;
	}
}
