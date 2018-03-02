package com.nguyenphan.smartglove.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
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

public class FrameMonitor extends JFrame {
	Monitor monitor;
	
	public FrameMonitor(Monitor monitor) {
		initUI();
		this.monitor = monitor;
		
		for (Sensor sensor: monitor.sensors)
			addSensor(sensor);
		
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
	
	boolean simulating = false;

	private JPanel paneContent;
	JPanel paneSensors;
	Component glueVertical = Box.createVerticalGlue();
	Random rand = new Random();
	
	public void initUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1460, 820);
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
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		JMenuItem mLoadCSV = new JMenuItem("Load CSV");
		JMenuItem mSaveCSV = new JMenuItem("Save CSV");
		JMenuItem mSim = new JMenuItem("Start/Stop Simulation");
		JMenuItem mNewGesture = new JMenuItem("New Gesture");
		JMenuItem mEditGesture = new JMenuItem("Edit Gesture");
		
		setJMenuBar(menuBar);
		menuBar.add(menuFile);
		menuFile.add(mLoadCSV);
		menuFile.add(mSaveCSV);
		menuFile.add(mSim);
		menuFile.add(mNewGesture);
		menuFile.add(mEditGesture);
		
		mLoadCSV.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")+ File.separator + "data"));
				int returnVal = fc.showOpenDialog(null);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            monitor.loadCSV(file);
		        }
			}
		});
		
		mSaveCSV.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Date date = Calendar.getInstance().getTime();  
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
				String timeStamp = dateFormat.format(date) + ".csv";
				
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")+ File.separator + "data"));
				fc.setSelectedFile(new File(timeStamp));
				int returnVal = fc.showSaveDialog(null);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            monitor.exportCSV(file);
		        }
			}
		});
		
		mNewGesture.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new FrameGesture(null);
			}
		});
		
		mEditGesture.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")+ File.separator + "data" + File.separator + "gestures"));
				int returnVal = fc.showOpenDialog(null);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            new FrameGesture(file);
		        }
			}
		});
		
		mSim.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!simulating) {
					simulating = true;
					
					timer.scheduleAtFixedRate(new TimerTask() {
						
						@Override
						public void run() {
							if (monitor.sensors.getFirst() != MockArduinoConnector.instance.sensors.getFirst()) {
								int result = JOptionPane.showConfirmDialog(null, "Mock Arduino API sensors are not linked. Link them now?");
								if (result == JOptionPane.OK_OPTION) {
									MockArduinoConnector.instance.sensors = monitor.sensors;
								}
							}
							
							Date date = Calendar.getInstance().getTime();  
							DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");  
							String timeStamp = dateFormat.format(date);
							
							for (Sensor sensor: MockArduinoConnector.instance.sensors) {
								sensor.setStatus("online");
								int val = 0;
								
								if (sensor.getLog().values.size() > 0)
									val = Integer.valueOf(sensor.getLog().values.getLast().value);
								else
									val = rand.nextInt(100);
								
								val += rand.nextInt(8)-rand.nextInt(8);
								if (val > 100)
									val = 100;
								if (val < 0)
									val = 0;
								sensor.getLog().values.add(new LogValue(String.valueOf(val), timeStamp));
							}
							refresh();
							
						}
					}, 100,100);
				}
				else {
					timer.cancel();
					timer = new Timer();
					simulating = false;
					for (Sensor sensor: MockArduinoConnector.instance.sensors)
						sensor.setStatus("offline");
					refresh();
				}
			}
		});
		
		setVisible(true);

	}
	
	Timer timer = new Timer();
	
}
