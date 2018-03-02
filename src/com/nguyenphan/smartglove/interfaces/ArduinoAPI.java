package com.nguyenphan.smartglove.interfaces;

import java.util.LinkedList;

import com.nguyenphan.smartglove.main.Sensor;

public interface ArduinoAPI {
	public LinkedList<Sensor> getSensors();
}
