package com.nguyenphan.smartglove.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.nguyenphan.smartglove.main.Sensor;

public class PaneSensor extends JPanel {
	Sensor sensor;
	
	public PaneSensor(Sensor sensor) {
		super(new BorderLayout());
		this.sensor = sensor;
		
		initUI();
		refresh();
	}
	
	Border borderGreen = BorderFactory.createLineBorder(Color.green);
	Border borderRed = BorderFactory.createLineBorder(Color.red);
	public void refresh() {
		lbName.setText(sensor.getName());
		lbStatus.setText("Status: " + sensor.getStatus());
		if (sensor.getStatus().equalsIgnoreCase("online"))
			lbStatus.setBorder(borderGreen);
		else
			lbStatus.setBorder(borderRed);
		//paneGraph.refresh(sensor);
		paneGraph.repaint();
		repaint();
		revalidate();
	}
	
	JLabel lbName, lbStatus;
	JButton btMenu;
	PaneGraph paneGraph;
	public void initUI() {
		setBorder(BorderFactory.createLineBorder(Color.black));
		setPreferredSize(new Dimension(700, 150));
		lbName = new JLabel("Sensor Name:");
		lbStatus = new JLabel("Status: offline");
		btMenu = new JButton("...");
		paneGraph = new PaneGraph(sensor);
		paneGraph.setBackground(Color.WHITE);
		
		
		JPanel paneNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
		paneNorth.add(lbName);
		paneNorth.add(lbStatus);
		paneNorth.add(btMenu);
		
		add(paneNorth, BorderLayout.NORTH);
		add(paneGraph);
		
		btMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new FrameLog(sensor);
			}
		});
		
	}

}
