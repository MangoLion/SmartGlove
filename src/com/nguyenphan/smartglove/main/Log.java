package com.nguyenphan.smartglove.main;

import java.util.LinkedList;

public class Log {
	public LinkedList<LogValue> values = new LinkedList<>();
	public LogValue[] getInterval(int start, int end) {
		LogValue result[] = new LogValue[end - start];
		
		int c = 0;
		for (int i = start; i < end; i ++) {
			//if (i < result.length)
				result[c] = values.get(i);
			c++;
		}
		return result;
	}
	
	public String getString() {
		String result = "";
		for (LogValue value: values)
			result += value.timeStamp + ", " + value.value + "\n";
		
		return result;
	}
}

