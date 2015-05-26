package model;

import java.io.IOException;

import reader.ConfigReader;
import reader.DataProcessor;
import entity.AverageEntity;
import entity.Record;
import entity.RecordContainer;

public class BiasSVD {
	float[][] userMatrix;
	float[][] movieMatrix;
	float[][] scoreMatrix;
	float[][] userdiff;
	float[][] moviediff;
	AverageEntity[]   userAverage;
	AverageEntity[]   movieAverage;
	float[]userAverageDiff;
	float[]movieAverageDiff;
	float globalAverage = 0;
	float maxScore = 0.0f;
	float minScore = 0.0f;
	static float small = 0.0000001f;
	
	public void loadData(RecordContainer trainingData){
		System.out.println("initialize the score matrix.....");
		ConfigReader reader = new ConfigReader();
		DataProcessor processor = new DataProcessor();
		RecordContainer data = trainingData;
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
		userAverage = new AverageEntity[data.getUserNum()];
		userAverageDiff = new float[data.getUserNum()];
		movieAverage= new AverageEntity[data.getMovieNum()];
		movieAverageDiff = new float[data.getMovieNum()];
		for(int i=0;i<data.getUserNum();i++)
			userAverage[i] = new AverageEntity();
		for(int i=0;i<data.getMovieNum();i++)
			movieAverage[i] = new AverageEntity();
		globalAverage = data.getAverageScore();
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
			userAverage[u.getId()].addNum(u.getScore());
			movieAverage[u.getMovieId()].addNum(u.getScore());
		}
		for(AverageEntity e:userAverage){
			e.setAverage(e.getAverage()-data.getAverageScore());
		}
		for(AverageEntity e:movieAverage){
			e.setAverage(e.getAverage()-data.getAverageScore());
		}
		System.out.println("finished intialize the score matrix...");
		System.out.println("initialize the userMatrix and movieMatrix");
		for(int i=0;i<userMatrix.length;i++)
			for(int j=0;j<userMatrix[0].length;j++){
				//userMatrix[i][j] = (float) (Math.random()*Math.sqrt((data.getMaxPreference()-data.getMinPreference())/reader.getConfigurationReader().getDimension()));
				userMatrix[i][j] = (float) ((float)Math.random()*Math.sqrt(2));
			}
		for(int i=0;i<movieMatrix.length;i++)
			for(int j=0;j<movieMatrix[0].length;j++){
				//movieMatrix[i][j] = (float) (Math.random()*Math.sqrt((data.getMaxPreference()-data.getMinPreference())/reader.getConfigurationReader().getDimension()));
				movieMatrix[i][j] = (float) ((float)Math.random()*Math.sqrt(2));
			}
		System.out.println("finished initialize the userMatrix and movieMatrix");
		reader.close();
	}
	private void resetDiff(){
		for(int i=0;i<userAverageDiff.length;i++)
			userAverageDiff[i] = 0;
		for(int i=0;i<movieAverageDiff.length;i++)
			movieAverageDiff[i] = 0;
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
		float movieError[] = new float[movieMatrix.length];
		for(int  i=0;i<movieError.length;i++)
			movieError[i] = 0;
		reader.close();
		float totalerror = 0;
		int count = 0;
		for(int i=0;i<userMatrix.length;i++){
			int userError = 0;
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
					userError +=error;
					movieError[j]+=error;
				}
			}
			userAverageDiff[i] = (userError-regular)*userAverage[i].getAverage();
		}
		//Calculate the movieAverageDiff
		
		for(int i=0;i<userMatrix.length;i++)
			for(int j=0;j<dimension;j++)
				userMatrix[i][j] +=learningrate*userdiff[i][j];
		for(int i=0;i<userAverageDiff.length;i++){
//			userAverage[i].setAverage(userAverage[i].getAverage()+learningrate*userAverageDiff[i]);
		}
		for(int i=0;i<movieError.length;i++){
//			float diff = (movieError[i]-regular)*movieAverage[i].getAverage();
//			movieAverage[i].setAverage(movieAverage[i].getAverage()+learningrate*diff);
		}
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
		score+=globalAverage+userAverage[u].getAverage()+movieAverage[m].getAverage();
		if(score>maxScore){
			score =maxScore;
		}else if(score<minScore){
			score = minScore;
		}
		return score;
	}
}
