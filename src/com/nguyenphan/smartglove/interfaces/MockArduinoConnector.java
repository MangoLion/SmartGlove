package com.nguyenphan.smartglove.interfaces;

import java.util.LinkedList;

import com.nguyenphan.smartglove.main.Sensor;

public class MockArduinoConnector implements ArduinoAPI{
	public static MockArduinoConnector instance;
	public LinkedList<Sensor> sensors = new LinkedList<>();
	
	public MockArduinoConnector() {
		instance = this;
		
		sensors.add(new Sensor("Flex Sensor 1", 0));
		sensors.add(new Sensor("Flex Sensor 2", 0));
		sensors.add(new Sensor("Flex Sensor 3", 0));
		sensors.add(new Sensor("Flex Sensor 4", 0));
		sensors.add(new Sensor("Flex Sensor 5", 0));
	}
	
	@Override
	public LinkedList<Sensor> getSensors() {
		// TODO Auto-generated method stub
		return sensors;
	}

}
