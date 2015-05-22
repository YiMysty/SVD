package entity;

import java.util.Iterator;
import java.util.List;

public class UserData {
	int UserNum;
	int movieNum;
	float maxPreference;
	float minPreference;
	List<User> userContainer;
	Iterator<User> iter;
	public UserData(int userNum, int movieNum, float maxPreference,
			float minPreference, List<User> userContainer) {
		super();
		UserNum = userNum;
		this.movieNum = movieNum;
		this.maxPreference = maxPreference;
		this.minPreference = minPreference;
		this.userContainer = userContainer;
		iter = userContainer.iterator();
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
	public List<User> getUserContainer() {
		return userContainer;
	}
	public void setUserContainer(List<User> userContainer) {
		this.userContainer = userContainer;
	}
	public Iterator<User> getIter() {
		return iter;
	}
	public void setIter(Iterator<User> iter) {
		this.iter = iter;
	}
	public User getNext(){
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
