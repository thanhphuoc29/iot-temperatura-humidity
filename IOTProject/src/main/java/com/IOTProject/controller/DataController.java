package com.IOTProject.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.IOTProject.Service.DataService;
import com.IOTProject.model.Data;
import com.IOTProject.model.Sensor;

@RestController
@CrossOrigin
public class DataController {
	
	@Autowired
	DataService Service;
	
	@GetMapping("/getData")
	public List<Data> getListData() {
		return Service.getListDataNow();
	}
	
//	@PostMapping("/getDataFilter")
//	public List<Data>getListDataFilter(@RequestBody Map<String, String> data) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		List<Data> lstData = new ArrayList<>();
//		try {
//			Date start = new Date(sdf.parse(data.get("startDay")).getTime());
//			Date end = new Date(sdf.parse(data.get("endDay")).getTime());
//			lstData =  Service.getListDataByDay(
//					start,
//					end,
//					Integer.parseInt(data.get("limit")),
//					Integer.parseInt(data.get("offset")));
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return lstData;
//	}
	
//	@PostMapping("/getRows")
//	public ResponseEntity<Map<String, Integer>> getRows(@RequestBody Map<String, String> data) {
//		Map<String, Integer> responseData = new HashMap<>();
//		responseData.put("rows", Service.getRowsData(data.get("start"),data.get("end")));
//		return ResponseEntity.ok(responseData);
//	}

	@PostMapping("/getDataFilter")
	public ResponseEntity<Map<String, Object>> getAllData(@RequestBody Map<String, String> data) {
		Map<String, Object> responeData = new HashMap<>();
		responeData.put("data", Service.getListDataByFilter(data.get("startDay"),data.get("endDay"),Integer.parseInt(data.get("limit")) ,Integer.parseInt(data.get("offset"))));
		responeData.put("rows", Service.getRowsData(data.get("startDay"),data.get("endDay")));
		return ResponseEntity.ok(responeData);
	}
	
	@PostMapping("/getActionLogFilter")
	public ResponseEntity<Map<String, Object>> getActionLogs(@RequestBody Map<String, String> data) {
		Map<String, Object> responeData = new HashMap<>();
		responeData.put("data", Service.getActionLogs(data.get("startDay"),data.get("endDay"),Integer.parseInt(data.get("limit")) ,Integer.parseInt(data.get("offset"))));
		responeData.put("rows", Service.getRowsActionLog(data.get("startDay"),data.get("endDay")));
		return ResponseEntity.ok(responeData);
	}
	
	@GetMapping("/getSensor")
	public List<Sensor> getListSenSor() {
		return Service.getListSensor();
	}
	
	@PostMapping("/addData")
	public boolean updateData(@RequestBody Data data) {
		boolean status = false;
		try {
			status = Service.updateData(data);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return status;
	}
	
//	@PostMapping("/updateSensor")
//	public boolean updateStatusSensor(@RequestBody Sensor sensor) {
//		boolean status = false;
//		try {
//			status = Service.updateStatusSensor(sensor);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return status;
//	}
	
}
