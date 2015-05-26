package model;

import java.io.IOException;

import reader.ConfigReader;
import reader.DataProcessor;
import entity.Record;
import entity.RecordContainer;

public class PlainSVD {
	float[][] userMatrix;
	float[][] movieMatrix;
	float[][] scoreMatrix;
	float[][] userdiff;
	float[][] moviediff;
	float maxScore = 0.0f;
	float minScore = 0.0f;
	static float small = 0.0000001f;
	public void loadData(RecordContainer train){
		System.out.println("initialize the score matrix.....");
		ConfigReader reader = new ConfigReader();
		DataProcessor processor = new DataProcessor();
		RecordContainer data = train;
		if(data==null){
			try {
				data = processor.DataLoader();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		userMatrix = new float[data.getUserNum()][reader.getConfigurationReader().getDimension()];
		userdiff   = new float[data.getUserNum()][reader.getConfigurationReader().getDimension()];
		movieMatrix = new float[data.getMovieNum()][reader.getConfigurationReader().getDimension()];
		moviediff = new float[data.getMovieNum()][reader.getConfigurationReader().getDimension()];
		scoreMatrix = new float[data.getUserNum()][data.getMovieNum()];
		maxScore = data.getMaxPreference();
		minScore = data.getMinPreference();
		//initialize the scoreMatrix
		for(int i=0;i<scoreMatrix.length;i++)
			for(int j=0;j<scoreMatrix[0].length;j++){
				scoreMatrix[i][j] = 0.0f;
			}
		Record u = null;
		while((u=data.getNext())!=null){
			scoreMatrix[u.getId()][u.getMovieId()] = u.getScore();
		}
		System.out.println("finished intialize the score matrix...");
		System.out.println("initialize the userMatrix and movieMatrix");
		for(int i=0;i<userMatrix.length;i++)
			for(int j=0;j<userMatrix[0].length;j++){
				userMatrix[i][j] = (float) (Math.random()*Math.sqrt((data.getMaxPreference()-data.getMinPreference())/reader.getConfigurationReader().getDimension()));
			}
		for(int i=0;i<movieMatrix.length;i++)
			for(int j=0;j<movieMatrix[0].length;j++){
				movieMatrix[i][j] = (float) (Math.random()*Math.sqrt((data.getMaxPreference()-data.getMinPreference())/reader.getConfigurationReader().getDimension()));
				
			}
		System.out.println("finished initialize the userMatrix and movieMatrix");
		reader.close();
	}
	private void resetDiff(){
		for(int i=0;i<userdiff.length;i++)
			for(int j=0;j<userdiff[0].length;j++)
				userdiff[i][j] = 0.0f;
		
		for(int i=0;i<moviediff.length;i++)
			for(int j=0;j<moviediff[0].length;j++)
				moviediff[i][j] = 0.0f;
	}
	public float doUpdate(){
		resetDiff();
		ConfigReader reader = new ConfigReader();
		int dimension = reader.getConfigurationReader().getDimension();
		float regular = reader.getConfigurationReader().getk();
		float learningrate = reader.getConfigurationReader().getLearningRate();
		reader.close();
		float totalerror = 0;
		int count = 0;
		for(int i=0;i<userMatrix.length;i++)
			for(int j=0;j<movieMatrix.length;j++){
				if(Math.abs(scoreMatrix[i][j]-0.0f)>small){
					float predictValue = doPrediction(i,j);
					
					float error = scoreMatrix[i][j]-predictValue;
					count+=1;
					totalerror+=error*error;
					for(int k=0;k<dimension;k++){
						userdiff[i][k]+=error*movieMatrix[j][k]-regular*userMatrix[i][k];
						moviediff[j][k]+=error*userMatrix[i][k]-regular*movieMatrix[j][k];
					}
				}
			}
		for(int i=0;i<userMatrix.length;i++)
			for(int j=0;j<dimension;j++)
				userMatrix[i][j] +=learningrate*userdiff[i][j];
		for(int i=0;i<movieMatrix.length;i++)
			for(int j=0;j<dimension;j++)
				movieMatrix[i][j]+=learningrate*moviediff[i][j];
		return (float) (Math.sqrt(totalerror/(float)count));
	}
	public float doPrediction(int u,int m){
		ConfigReader reader = new ConfigReader();
		float score = 0.0f;
		for(int i=0;i<reader.getConfigurationReader().getDimension();i++){
			score +=userMatrix[u][i]*movieMatrix[m][i];
		}
		if(score>maxScore-minScore){
			score =maxScore;
		}else if(score<0){
			score = minScore;
		}else{
			score = minScore+score;
		}
		return score;
	}
}
