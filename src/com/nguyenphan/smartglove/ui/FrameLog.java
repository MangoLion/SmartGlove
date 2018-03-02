package com.nguyenphan.smartglove.ui;

import java.awt.BorderLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.nguyenphan.smartglove.main.Log;
import com.nguyenphan.smartglove.main.Sensor;

public class FrameLog extends JFrame{
	Timer timer;
	
	public FrameLog(Sensor sensor) {
		setSize(300, 300);
		JPanel paneContent = new JPanel(new BorderLayout());
		setContentPane(paneContent);
		
		JTextPane textPane = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(textPane);
		add(scrollPane);
		
		setTitle(sensor.getName());
		textPane.setText(sensor.getLog().getString());
		
		setVisible(true);
		
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				textPane.setText(sensor.getLog().getString());
			}
		}, 100, 100);
	}
	
	@Override
	public void dispose() {
		timer.cancel();
		super.dispose();
	}
}
