package model;

public class User {
	int id;
	int movieId;
	double score;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMovieId() {
		return movieId;
	}
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public double getScore() {
		return score;
	}
	public void setId(String id) {
		this.id = Integer.parseInt(id);
	}
	public void setScore(String score) {
		this.score = Float.parseFloat(score);
	}
	public void setMovieId(String movieId) {
		this.movieId = Integer.parseInt(movieId);
	}
	public User(String id, String movieId, String score) {
		super();
		this.id = Integer.parseInt(id);
		this.movieId = Integer.parseInt(movieId);
		this.score = Float.parseFloat(score);
	}
	public User(int id, int movieId, double score) {
		super();
		this.id = id;
		this.movieId = movieId;
		this.score = score;
	}
	public User() {
		super();
	}
	public String toString(){
		return this.id+","+this.movieId+","+this.score;
	}
	
}
