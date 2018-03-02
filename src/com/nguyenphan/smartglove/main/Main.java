package com.nguyenphan.smartglove.main;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.CeruleanSkin;
import org.pushingpixels.substance.api.skin.OfficeBlue2007Skin;

import com.nguyenphan.smartglove.interfaces.MockArduinoConnector;
import com.nguyenphan.smartglove.ui.FrameMonitor;

public class Main {
	public static void main(String[] args) {
		Monitor monitor = new Monitor();
		for (Sensor sensor: new MockArduinoConnector().getSensors())
			monitor.addSensor(sensor);
		
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame.setDefaultLookAndFeelDecorated(true);
				JDialog.setDefaultLookAndFeelDecorated(true);
				SubstanceLookAndFeel.setSkin(new OfficeBlue2007Skin());
				
				new FrameMonitor(monitor);
			}
		});
		
	}
}
