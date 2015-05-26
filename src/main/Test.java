package main;

import java.io.IOException;

import train.Train;

public class Test {
	public static void main(String args[]) throws IOException{
		Train train = new Train();
		train.doCrossValidation();
	}
}
