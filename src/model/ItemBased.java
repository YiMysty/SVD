package model;

import java.io.IOException;

import reader.ConfigReader;
import reader.DataProcessor;
import entity.AverageEntity;
import entity.Record;
import entity.RecordContainer;

public class ItemBased{
	public float[][] scoreMatrix;
	float[][] similarityMatrix;
	float threshold = 0.5f;
	int UserNum=0;
	int MovieNum=0;
	public void loadData(RecordContainer train){
		RecordContainer data = train;
		UserNum=data.getUserNum();
		MovieNum=data.getMovieNum();
		scoreMatrix = new float[UserNum][MovieNum];
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

	}
	public void similarityMatrix(){
		//built the similarity Matrix
		System.out.println(MovieNum);
		similarityMatrix=new float[MovieNum][MovieNum];
		System.out.println(MovieNum);
		float[] vec1;
		float[] vec2;
		float[] tempVec1;
		float[] tempVec2;
		float tempSimilarity=0.0f;
		int index=0;
		vec1=new float[UserNum];
		vec2=new float[UserNum];
		//initialize vec1 and vec2
		for(int i=0;i<vec1.length;i++){			
				vec1[i] = 0.0f;
				vec2[i] = 0.0f;
		}
		//build similarity Matrix
		for(int i=0;i<scoreMatrix[0].length;i++){
			for(int j=0;j<scoreMatrix[0].length;j++){
				tempVec1=getCol(i);
				tempVec2=getCol(j);	
				index=0;
				//remove 0, make length of two array equal
				for(int k=0;k<tempVec1.length;k++){
					if(tempVec1[k]!=0.0f && tempVec2[k]!=0.0f){
						vec1[index]=tempVec1[k];
						vec2[index]=tempVec2[k];
						index++;
					}
				}
				tempSimilarity=cosineSimilarity(vec1,vec2);
				similarityMatrix[i][j]=tempSimilarity;
			}	
		}
	}
	public void doPrediction(){
		//Predict, fill out the scoreMatrix
		float weightedScoreSum=0.0f;
		float counter=0.0f;
		for(int i=0;i<scoreMatrix.length;i++){
			for(int j=0;j<scoreMatrix[0].length;j++){
				if(scoreMatrix[i][j]==0.0f){
					for(int k=0;k<similarityMatrix[0].length;k++){ //find the similar item according to threshold;
						if(similarityMatrix[j][k]>threshold && scoreMatrix[i][k]!=0.0f ){
							weightedScoreSum+=similarityMatrix[j][k]*scoreMatrix[i][k]; //similarity(weight) * known score  
							counter+=similarityMatrix[j][k];
						}
					}
					scoreMatrix[i][j]=weightedScoreSum/counter; //prediction!
					weightedScoreSum=0.0f; //reset
					counter=0.0f;	//reset
				}
			}
		}
		
		
	}
	public float[] getCol(int x){
		//Get xth column from scoreMatrix
		float[] targetCol;
		targetCol=new float[UserNum];
		//initialize targetCol
		for(int i=0;i<targetCol.length;i++){			
			targetCol[i] = 0.0f;			
		}
		
		for(int i=0;i<scoreMatrix.length;i++){
			targetCol[i]=scoreMatrix[i][x];
		}

		return targetCol;
	}
	public float cosineSimilarity(float x[], float y[]){
		//compute cosineSimilarity of two vector
		double dotProduct=0.0;
		double magnitude1=0.0;
		double magnitude2=0.0;
		double cosineSimilarity=0.0;
		for(int i=0;i<x.length;i++){
			dotProduct+=x[i]*y[i];
			magnitude1+=Math.pow(x[i],2);
			magnitude2+=Math.pow(y[i],2);
		}
		magnitude1=Math.sqrt(magnitude1);
		magnitude2=Math.sqrt(magnitude2);
		cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
		return (float)cosineSimilarity;	
	}

}