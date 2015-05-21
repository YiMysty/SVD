package main;

import java.io.IOException;

import reader.DataProcessor;

public class Test {
	public static void main(String args[]){
		DataProcessor processor = new DataProcessor();
		try {
			processor.DataTrimAndRestore();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
