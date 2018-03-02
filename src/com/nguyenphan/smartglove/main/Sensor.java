package com.nguyenphan.smartglove.main;

import com.nguyenphan.smartglove.interfaces.Loggable;

public class Sensor implements Loggable{
	String name,status = "offline";
	int index;
	Log log = new Log();
	
	public Sensor(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
}
