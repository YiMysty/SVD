package model;

import entity.RecordContainer;

public abstract class Model {
	public void loadData(RecordContainer trainingData){};
	public float doUpdate(){
		return 0;
	}
	public float doPrediction(int u,int m){
		return 0;
	}
}
