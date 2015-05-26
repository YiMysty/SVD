package tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import reader.ConfigReader;
import entity.Record;
import entity.RecordContainer;

public class FoldsTools {
	ArrayList<RecordContainer> Train;
	ArrayList<RecordContainer> Test;
	public FoldsTools(RecordContainer All){
		ConfigReader reader = new ConfigReader();
		long seed = System.nanoTime();
		Train = new ArrayList<RecordContainer>();
		Test  = new ArrayList<RecordContainer>();
		Collections.shuffle(All.getUserContainer(), new Random(seed));
		List<List<Record>> TrainBuff = new ArrayList<List<Record>>();
		List<List<Record>> TestBuff  = new ArrayList<List<Record>>();
		for(int i=0;i<reader.getConfigurationReader().getFoldsNum();i++){
			TrainBuff.add(new ArrayList<Record>());
			TestBuff.add(new ArrayList<Record>());
		}
		System.out.println("Initiliaze the data for cross validation!");
		int counter = 0;
		for(Record r:All.getUserContainer()){
			int TestNo  = counter%reader.getConfigurationReader().getFoldsNum();
			TestBuff.get(TestNo).add(r);
			for(int i=1;i<reader.getConfigurationReader().getFoldsNum();i++){
				TrainBuff.get((i+TestNo)%reader.getConfigurationReader().getFoldsNum()).add(r);
			}
			counter+=1;
		}
		for(int i=0;i<reader.getConfigurationReader().getFoldsNum();i++){
			Train.add(new RecordContainer(TrainBuff.get(i),All.getUserNum(),All.getMovieNum()));
			Test.add(new RecordContainer(TestBuff.get(i),All.getUserNum(),All.getMovieNum()));
		}
		System.out.println("finished data preparing..");
		reader.close();
	}
	public RecordContainer getTrainDataByIndex(int i){
		return Train.get(i);
	}
	public RecordContainer getTestDataByIndex(int i){
		return Test.get(i);
	}
	
}
