package com.nguyenphan.smartglove.main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Monitor {

	public LinkedList<Sensor> sensors = new LinkedList<>();
	
	public LogValue[] gatherAllLogs() {
		LogValue result[] = new LogValue[sensors.size()];
		
		return result;
	}
	
	public void addSensor(Sensor sensor) {
		sensors.add(sensor);
	}
	
	public void loadCSV(File file) {
		for (Sensor sensor: sensors)
			sensor.getLog().values.clear();
		
		ArrayList<String> lines = new ArrayList<>();
		try {
			lines.addAll(Files.readAllLines(Paths.get(file.getAbsolutePath())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Read Failed");
			return;
		}
		//Read sensor names
		String names[] = lines.get(0).split(",");
		for (int s = 0; s < names.length; s ++)
			addSensor(new Sensor(names[s], s));
		
		lines.remove(0);
		
		//Read values
		for (String line: lines) {
			String value[] = line.split(",");
			String timeStamp = value[value.length-1];
			for (int l = 0; l < value.length-1; l++) {
				sensors.get(l).log.values.add(new LogValue(value[0], timeStamp));
			}
		}
		
		JOptionPane.showMessageDialog(null,"CSV loaded, sensors: " + names.length + ", entries: " + lines.size());
		
	}
	
	public void exportCSV(File file) {
		if (sensors.size() == 0)
			return;
		
		String export = "";
		for (int i = 0; i < sensors.size(); i++) {
			export += sensors.get(i).getName();
			if (i != sensors.size()-1)
				export +=  ",";
			else
				export += "\n";
		}
		
		int lineNum = sensors.getFirst().getLog().values.size();
		for (int l = 0; l < lineNum; l++) {
			for (int s = 0; s < sensors.size(); s ++) {
				Log log = sensors.get(s).getLog();
				export += log.values.get(l).value;
				if (s != sensors.size()-1)
					export += ",";
				else
					export += "," + log.values.get(l).timeStamp + "\n";
			}
		}
		
		if (file == null) {
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(System.getProperty("user.dir")+ File.separator + "data"));
			fc.setSelectedFile(new File("data.csv"));
			int returnVal = fc.showSaveDialog(null);
	
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	file = fc.getSelectedFile();    
	        }else
	        	return;
		}
        
        
        try {
			Files.write(Paths.get(file.getAbsolutePath()), export.getBytes(Charset.forName("UTF-8")));
			
			JOptionPane.showMessageDialog(null, "CSV file exported successfully");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Export Failed");
		}
	}

}
