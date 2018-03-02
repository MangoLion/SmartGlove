package com.nguyenphan.smartglove.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.ImageProducer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.nguyenphan.smartglove.main.LogValue;
import com.nguyenphan.smartglove.main.Sensor;

public class PaneGraph extends JPanel{

	Sensor sensor;
	
	public PaneGraph(Sensor sensor) {
		//setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1000, 100));
		this.sensor = sensor;

	}
	
	public void refresh() {
		
	}
	
	@Override
	protected void paintComponent(Graphics arg0) {
		// TODO Auto-generated method stub
		super.paintComponent(arg0);
		
		ArrayList<Integer> list = new ArrayList<>();
		
		int width = getWidth();
		int height = getHeight();
		
		LogValue[] logs;
		LinkedList<LogValue> values = sensor.getLog().values;
		if (values.size() < 100)
			logs = sensor.getLog().getInterval(0, values.size());
		else
			logs = sensor.getLog().getInterval(values.size()-100, values.size());

		int lastVal = 0;
		for (int i = 0; i < logs.length; i ++) {
			LogValue value = logs[i];
			if (value == null)
				continue;
			int val = Integer.valueOf(value.value);
			if (i == 0)
				lastVal = val;
			arg0.drawLine(width/100*i, height - height/100*lastVal,width/100*i+width/100, height -  height/100*val);
			lastVal = val;
		}
	}

}
