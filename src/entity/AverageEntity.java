package entity;

public class AverageEntity {
	float totalSum;
	int totalNum;
	float average;
	public AverageEntity(){
		totalSum = 0;
		totalNum = 0;
	}
	public float getTotalSum() {
		return totalSum;
	}
	public void setTotalSum(float totalSum) {
		this.totalSum = totalSum;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public void addNum(float score){
		this.totalSum+=score;
		this.totalNum+=1;
		average = totalSum/(float)totalNum; 
	}
	public float getAverage(){
		return average;
	}
	public void setAverage(float average){
		this.average = average;
	}
}
