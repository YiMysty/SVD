package entity;

import java.util.Iterator;
import java.util.List;

public class RecordContainer {
	int UserNum;
	int movieNum;
	float maxPreference;
	float minPreference;
	List<Record> userContainer;
	Iterator<Record> iter;
	float averageScore = 0;
	public RecordContainer(int userNum, int movieNum, float maxPreference,
			float minPreference, List<Record> userContainer,float averageScore) {
		super();
		this.UserNum = userNum;
		this.movieNum = movieNum;
		this.maxPreference = maxPreference;
		this.minPreference = minPreference;
		this.userContainer = userContainer;
		this.averageScore = averageScore;
		iter = userContainer.iterator();
	}
	public RecordContainer(List<Record>userContainer,int userNum,int movieNum){
		this.maxPreference = 1-Float.MAX_VALUE;
		this.minPreference = Float.MAX_VALUE;
		this.userContainer = userContainer;
		iter = userContainer.iterator();
		float Score = 0;
		for(Record r:userContainer){
			this.minPreference = Math.min(this.minPreference, r.getScore());
			this.maxPreference = Math.max(this.maxPreference, r.getScore());
			Score+=r.getScore();
		}
		this.averageScore = Score/(float)userContainer.size();
		this.UserNum = userNum;
		this.movieNum = movieNum;
	}
	public int getUserNum() {
		return UserNum;
	}
	public void setUserNum(int userNum) {
		UserNum = userNum;
	}
	public int getMovieNum() {
		return movieNum;
	}
	public void setMovieNum(int movieNum) {
		this.movieNum = movieNum;
	}
	public float getMaxPreference() {
		return maxPreference;
	}
	public void setMaxPreference(float maxPreference) {
		this.maxPreference = maxPreference;
	}
	public float getMinPreference() {
		return minPreference;
	}
	public void setMinPreference(float minPreference) {
		this.minPreference = minPreference;
	}
	public List<Record> getUserContainer() {
		return userContainer;
	}
	public void setUserContainer(List<Record> userContainer) {
		this.userContainer = userContainer;
	}
	public Iterator<Record> getIter() {
		return iter;
	}
	public void setIter(Iterator<Record> iter) {
		this.iter = iter;
	}
	public float getAverageScore() {
		return averageScore;
	}
	public void setAverageScore(float averageScore) {
		this.averageScore = averageScore;
	}
	public Record getNext(){
		if(iter.hasNext()){
			return iter.next();
		}
		return null;
	}
	public String toString(){
		return "UserNum:"+UserNum+",MovieNum:"+movieNum+","+"minPreference:"+minPreference+","+
				"maxPreference:"+maxPreference+",DataSize:"+userContainer.size();
	}
}
