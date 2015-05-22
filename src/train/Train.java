package train;

import reader.ConfigReader;
import model.SVD;

public class Train {
	SVD svdmodel; 
	public Train(){
		svdmodel = new SVD();
		svdmodel.loadData();
	}
	public void doTrain(){
		System.out.println("Start do training.....");
		ConfigReader reader = new ConfigReader();
		for(int i=0;i<reader.getConfigurationReader().getIterationTimes();i++){
			System.out.println("round "+i+" RMSE:"+svdmodel.doUpdate());
		}
		System.out.println("iteration finished...");
	}
}
