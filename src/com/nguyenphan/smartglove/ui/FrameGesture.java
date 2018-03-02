package com.nguyenphan.smartglove.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.nguyenphan.smartglove.interfaces.MockArduinoConnector;
import com.nguyenphan.smartglove.main.LogValue;
import com.nguyenphan.smartglove.main.Monitor;
import com.nguyenphan.smartglove.main.Sensor;

public class FrameGesture extends JFrame {
	Monitor monitor;
	File file;
	
	public FrameGesture(File file) {
		initUI();
		this.file = file;
		monitor = new Monitor();
		if (file == null) {
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(System.getProperty("user.dir")+ File.separator + "data" + File.separator + "gestures"));
			fc.setSelectedFile(new File("gesture.csv"));
			int returnVal = fc.showSaveDialog(null);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	this.file = fc.getSelectedFile();
	        	
	    		//Create separate sensor objects to store only the gesture
	    		for (Sensor sensor: MockArduinoConnector.instance.sensors) {
	    			Sensor recordSensor = new Sensor(sensor.getName(), sensor.getIndex());
	    			monitor.addSensor(recordSensor);
	    			addSensor(recordSensor);
	    		}
	        }else {
	        	dispose();
	        	return;
	        }
		}else {
			monitor.loadCSV(this.file);
			for (Sensor sensor: monitor.sensors) {
    			addSensor(sensor);
    		}
		}
		
		setTitle("Gesture: " + this.file.getName());

		refresh();
	}
	
	public void refresh() {
		for (Component component: paneSensors.getComponents())
			if (component instanceof PaneSensor) {
				PaneSensor paneSensor = (PaneSensor) component;
				paneSensor.refresh();
			}
	}
	
	public void addSensor(Sensor sensor) {
		paneSensors.remove(glueVertical);
		
		paneSensors.add(new PaneSensor(sensor));
		paneSensors.revalidate();
		
		paneSensors.add(glueVertical);
		paneSensors.revalidate();
		revalidate();
	}


	private JPanel paneContent;
	JPanel paneSensors;
	Component glueVertical = Box.createVerticalGlue();
	Random rand = new Random();
	boolean recording = false;
	
	public void initUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 820);
		paneContent = new JPanel();
		paneContent.setBorder(new EmptyBorder(5, 5, 5, 5));
		paneContent.setLayout(new BorderLayout());
		setContentPane(paneContent);
		
		paneSensors = new JPanel();
		BoxLayout boxLayout = new BoxLayout(paneSensors, BoxLayout.PAGE_AXIS);
		paneSensors.setLayout(boxLayout);
		
		paneSensors.add(glueVertical);
		
		JScrollPane scrollPane = new JScrollPane(paneSensors);
		paneContent.add(scrollPane);
		
		JPanel paneSouth = new JPanel(new FlowLayout());
		JButton btRecord = new JButton("Record");
		JButton btSave = new JButton("Save");
		
		add(paneSouth, BorderLayout.SOUTH);
		paneSouth.add(btRecord);
		paneSouth.add(btSave);
		
		btSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				monitor.exportCSV(file);
			}
		});
		
		btRecord.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!recording) {
					for (Sensor sensor: monitor.sensors)
						sensor.getLog().values.clear();
					
					recording = true;
					timer.scheduleAtFixedRate(new TimerTask() {
						
						@Override
						public void run() {
							for (int s = 0; s < monitor.sensors.size(); s ++){
								Sensor sensor = monitor.sensors.get(s);
								Sensor connectedSensor = MockArduinoConnector.instance.sensors.get(s);
								
								sensor.setStatus(connectedSensor.getStatus());
								if (connectedSensor.getLog().values.size() > 0 
										&&(sensor.getLog().values.size() == 0 || sensor.getLog().values.getLast() != connectedSensor.getLog().values.getLast()))
									sensor.getLog().values.add(connectedSensor.getLog().values.getLast());
								
							}
							
							refresh();
						}
					}, 100,100);
				} else {
					recording = false;
					timer.cancel();
					timer = new Timer();
				}
			}
		});
		
		btSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				monitor.exportCSV(file);
			}
		});
		
		setVisible(true);

	}
	
	Timer timer = new Timer();
	
}
