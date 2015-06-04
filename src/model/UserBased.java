package model;

import entity.Record;
import entity.RecordContainer;


public class UserBased {
	//
	
	float threshold = 0.5f;
	int userNum = 0;
	int movieNum = 0;
	float[][] scoreMatrix;
	float[][] similarityMatrix;
	
	public void loadData(RecordContainer train) {
		RecordContainer data = train;
		userNum = data.getMovieNum();
		movieNum = data.getMovieNum();
		scoreMatrix = new float[userNum][movieNum];
		
		//Initialize the scoreMatrix
		for (int i = 0; i < scoreMatrix.length; i++) {
			for (int j = 0; j < scoreMatrix[0].length; j++) {
				scoreMatrix[i][j] = 0.0f;
			}
		}
		Record u = null;
		while ((u = data.getNext()) != null) {
			scoreMatrix[u.getId()][u.getMovieId()] = u.getScore();
		}
		System.out.println("Finished initializing the score matrix");
	}
	
	public void similarityMatrix() {
		//Build the similarity matrix
		float[] vec1;
		float[] vec2;
		float[] tempVec1;
		float[] tempVec2;
		float tempSimilarity = 0.0f;
		int index;
		vec1 = new float[movieNum];//movieNum?
		vec2 = new float[movieNum];
		//Initialize the vec1 and vec2
		for (int i = 0; i < vec1.length; i++) {
			vec1[i] = 0.0f;
			vec2[i] = 0.0f;
		}
		//Start to build similarity matirx
		for (int i = 0; i < scoreMatrix.length; i++) {//scoreMatrix.length or scoreMatrix[0].length?
			for (int j = 0; j < scoreMatrix.length; j++) {
				tempVec1 = getRow(i);
				tempVec2 = getRow(j);
				index = 0;
				//Remove the element 0 either in tempVec1 or tempVec2
				for (int k = 0; k < tempVec1.length; k++) {
					if (tempVec1[k] != 0.0f && tempVec2[k] != 0.0f) {
						vec1[index] = tempVec1[k];
						vec2[index] = tempVec2[k];
						index++;
					}
				}
				tempSimilarity = cosineSimilarity(vec1, vec2);
				similarityMatrix[i][j] = tempSimilarity;
			}
		}
	}
	
	public float[] getRow(int x) {
		//Get the xth row from the scoreMatrix
		float[] targetRow;
		targetRow = new float[movieNum];
		
		for (int i = 0; i < targetRow.length; i++) {
			targetRow[i] = 0.0f;
		}
		for (int i = 0; i < scoreMatrix[0].length; i++) {//scoreMatrix[0].length or scoreMatrix.length?
			targetRow[i] = scoreMatrix[x][i];
		}
		return targetRow;
	}
	
	public float cosineSimilarity(float x[], float y[]) {
		//Compute the distance of two user by using cosineSimilarty
		double dotProduct = 0.0;
		double magnitude1 = 0.0;
		double magnitude2 = 0.0;
		double cosineSimilarity = 0.0;
		for (int i = 0; i < x.length; i++) {
			dotProduct += (x[i] * y[i]);
			magnitude1 += Math.pow(x[i], 2);
			magnitude2 += Math.pow(y[i], 2);
		}
		magnitude1 = Math.sqrt(magnitude1);
		magnitude2 = Math.sqrt(magnitude2);
		cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
		return (float)cosineSimilarity;
	}
	
	public void predict() {
		//Predict the missing score in scoreMatrix
		float weightedScoreSum = 0.0f;
		float counter = 0.0f;
		
		for (int i = 0; i < scoreMatrix.length; i++) {
			for (int j = 0; j < scoreMatrix[0].length; j++) {
				if (scoreMatrix[i][j] == 0.0f) {
					for (int k = 0; k < similarityMatrix[0].length; k++) {//or similarityMatrix.length?
						if ((similarityMatrix[j][k] > threshold) && (scoreMatrix[k][i] != 0.0f)) {//Why it is not equal to 0?
							weightedScoreSum += similarityMatrix[j][k] * scoreMatrix[i][k]; //Similarity() * known score
							counter += similarityMatrix[j][k];
						}
					}
					scoreMatrix[i][j] = weightedScoreSum / counter;
					weightedScoreSum = 0.0f;
					counter = 0.0f;
				}
			}
		}
	}
}


/*
def UserSimilarity(train):  
    #建立倒排表  
    item_users = dict()  
    for u,items in train.items():  
        for i in item.keys():  
            if i not in item_users:  
                items_users[i] = set()  
            item_users[i].add(u)  
    #item_users即为物品到用户的倒排表  
    #计算用户之间的相关度  
    C = dict()#任意用户之间的相关度  
    N = dict()#用户正反馈物品的数目  
    for i ,users in item_users:  
        for u in users:  
            N[u] += 1  
            for v in users:  
                if u == v:  
                    continue:  
                C[u][v] += 1  
    #最后计算结果矩阵  
    W = dict()  
    for u ,related_users in C.items():  
        for v,cuv in related_users:  
            W[u][v] = cuv / math.sqrt(N[u]* N[v]*1.0)  
return W  
*/

/*

package model;

import entity.Record;
import entity.RecordContainer;

public class ItemBased{
	float[][] scoreMatrix;
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
		float weightedScoreSum=0.0f;
		float counter=0.0f;
		for(int i=0;i<scoreMatrix.length;i++){
			for(int j=0;j<scoreMatrix[0].length;j++){
				if(scoreMatrix[i][j]==0.0f){
					for(int k=0;k<similarityMatrix[0].length;k++){ //find the similar item according to threshold;
						if(similarityMatrix[j][k]>threshold && scoreMatrix[i][k]!=0.0f ){
							weightedScoreSum+=similarityMatrix[j][k]*scoreMatrix[i][k]; //similarity(weight) * known score  
							counter++;
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
		
		for(int i=0;i<scoreMatrix[0].length;i++){
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

*/