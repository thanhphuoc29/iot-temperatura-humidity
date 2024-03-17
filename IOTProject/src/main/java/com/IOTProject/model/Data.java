package com.IOTProject.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Data {
	@JsonProperty("temperature")
	private float temperature;
	@JsonProperty("humidity")
	private float humidity;
	@JsonProperty("light")
	private float light;
	@JsonProperty("dobui")
	private float dobui;
	
	@JsonProperty("time")
	private Timestamp timeStamp;
	
	public Data() {
		// TODO Auto-generated constructor stub
	}

	public Data(float temperature, float humidity, float light, Timestamp timeStamp) {
		super();
		this.temperature = temperature;
		this.humidity = humidity;
		this.light = light;
		this.timeStamp = timeStamp;
	}

	public float getTemperature() {
		return temperature;
	}
	
	public float getDobui() {
		return dobui;
	}

	public void setDobui(float dobui) {
		this.dobui = dobui;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public float getLight() {
		return light;
	}

	public void setLight(float light) {
		this.light = light;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public boolean isValid() {
        return temperature != 0.0 && humidity != 0.0;
    }
	
}
