package train;

import java.io.IOException;

import entity.Record;
import entity.RecordContainer;
import reader.ConfigReader;
import reader.DataProcessor;
import tools.FoldsTools;
import model.PlainSVD;

public class Train {
	PlainSVD svdmodel; 
	public Train(){
		svdmodel = new PlainSVD();
		svdmodel.loadData(null);
	}
	public void doTrain(){
		System.out.println("Start do training.....");
		ConfigReader reader = new ConfigReader();
		for(int i=0;i<reader.getConfigurationReader().getIterationTimes();i++){
			System.out.println("round "+i+" RMSE:"+svdmodel.doUpdate());
		}
		System.out.println("iteration finished...");
	}
	public void doCrossValidation() throws IOException{
		DataProcessor processor = new DataProcessor();
		FoldsTools folds = new FoldsTools(processor.DataLoader());
		System.out.println("Start do cross validation.....");
		ConfigReader reader = new ConfigReader();
		for(int i =0;i<reader.getConfigurationReader().getFoldsNum();i++){
			RecordContainer train = folds.getTrainDataByIndex(i);
			svdmodel = new PlainSVD();
			svdmodel.loadData(train);
			float result = Float.MAX_VALUE;
			for(int j=0;j<reader.getConfigurationReader().getIterationTimes()&&result>reader.getConfigurationReader().getThreshold()
					;j++){
				System.out.println("round "+j+" RMSE:"+(result=svdmodel.doUpdate()));
			}
			RecordContainer test  = folds.getTestDataByIndex(i);
			float totalerror = 0;
			for(Record r:test.getUserContainer()){
				float error = svdmodel.doPrediction(r.getId(), r.getMovieId())-r.getScore();
				totalerror+=error*error;
			}
			System.out.println("======"+(float) (Math.sqrt(totalerror/(float)test.getUserContainer().size()))+"============");
		}
	}
}
