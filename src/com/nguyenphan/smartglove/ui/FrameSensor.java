package com.nguyenphan.smartglove.ui;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;

public class FrameSensor extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameSensor frame = new FrameSensor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrameSensor() {
		setBounds(100, 100, 450, 300);

	}

}
