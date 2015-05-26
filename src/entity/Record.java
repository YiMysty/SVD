package entity;

public class Record {
	int id;
	int movieId;
	float score;
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
	public void setScore(float score) {
		this.score = score;
	}
	public float getScore() {
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
	public Record(String id, String movieId, String score) {
		super();
		this.id = Integer.parseInt(id);
		this.movieId = Integer.parseInt(movieId);
		this.score = Float.parseFloat(score);
	}
	public Record(int id, int movieId, float score) {
		super();
		this.id = id;
		this.movieId = movieId;
		this.score = score;
	}
	public Record(String s){
		String Split[] = s.split(",");
		if(Split.length>1){
			this.id = Integer.parseInt(Split[0]);
			this.movieId = Integer.parseInt(Split[1]);
			this.score = Float.parseFloat(Split[2]);
		}else{
			System.out.println("initialize error not correct format.----"+s);
		}
	}
	public Record() {
		super();
	}
	public String toString(){
		return this.id+","+this.movieId+","+this.score;
	}
	
}
