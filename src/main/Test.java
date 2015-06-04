package main;

import java.io.IOException;

import train.Train;

public class Test {
	public static void main(String args[]) throws IOException{
		//FOR SVM
		Train train = new Train("SVM");
		train.doCrossValidation();
		//FOR ITEMBASED
		Train train2 = new Train("ItemBased");
		train2.doCrossValidation();

	}
}
