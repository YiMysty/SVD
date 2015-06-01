package model;

import java.io.IOException;

import reader.ConfigReader;
import reader.DataProcessor;
import entity.User;
import entity.UserData;

public class ItemBased{
	float[][] scoreMatrix;
	float[][] similarityMatrix;
	float threshold=0.5;
	public void loadData(){
		scoreMatrix = new float[data.getUserNum()][data.getMovieNum()];
		//initialize the scoreMatrix
		for(int i=0;i<scoreMatrix.length;i++)
			for(int j=0;j<scoreMatrix[0].length;j++){
				scoreMatrix[i][j] = 0.0f;
			}
		User u = null;
		while((u=data.getNext())!=null){
			scoreMatrix[u.getId()][u.getMovieId()] = u.getScore();
		}
		System.out.println("finished intialize the score matrix...");

	}
	public void similarityMatrix(){
		//built the similarity Matrix
		float [] vec1;
		float [] vec2;
		float [] tempVec1;
		float [] tempvec2;
		float tempSimilarity=0.0;
		int index=0;

		for(int i=0;i<scoreMatrix[0].length;i++){
			for(int j=0;j<scoreMatrix[0].length;j++){
				tempVec1=getCol(i);
				tempVec2=getCol(j);	
				index=0;
				//remove 0, make length of two array equal
				for(int k=0;j<tempVec1.length;k++){
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
		float weightedScoreSum=0.0;
		float  counter=0.0;
		for(int i=0;i<scoreMatrix.length;i++){
			for(int j=0;j<scoreMatrix[0].length;j++){
				if(scoreMatrix[i][j]==0.0f]){
					for(int k=0;k<similarityMatrix[0].length;k++){ //find the similar item according to threshold;
						if(similarityMatrix[j][k]>threshold && scoreMatrix[i][k]!=0.0f ){
							weightedScoreSum+=similarityMatrix[j][k]*scoreMatrix[i][k] //similarity(weight) * known score  
							counter++;
						}
					}
					scoreMatrix[i][j]=weightedScoreSum/counter; //prediction!
					weightedScoreSum=0.0; //reset
					counter=0.0;	//reset
				}
			}
		}
	}
	public float[] getCol(int x){
		//Get xth column from scoreMatrix
		float[] targetCol;
		for(int i=0;i<scoreMatrix[0].length;i++){
			targetCol[i]=scoreMatrix[i][x]
		}

		return targetCol;
	}
	public float cosineSimilarity(float x[], float y[]){
		//compute cosineSimilarity of two vecotr
		double dot=0.0;
		double magnitude1=0.0;
		double magnitude=0.0;
		double cosineSimilarity;
		for(int i=0;i<x.length;i++){
			dot+=x[i]*y[i];
			magnitude1+=Math.pow(x[i],2);
			magnitude2+=Math.pow(y[i],2);
		}
		magnitude1=Math.sqrt(magnitude1);
		magnitude2=Math.sqrt(magnitude2);
		cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
		return cosineSimilarity;	
	}

}