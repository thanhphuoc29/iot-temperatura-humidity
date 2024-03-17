package com.IOTProject.model;

public class Sensor {
	public int id;
	public String name;
	public boolean isOn = false;
	public String nameUser;
	
	
	public int getId() {
		return id;
	}
	
	public Sensor() {
		// TODO Auto-generated constructor stub
	}
	public Sensor(int id, String name, boolean isOn) {
		super();
		this.id = id;
		this.name = name;
		this.isOn = isOn;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}
	
	
}
