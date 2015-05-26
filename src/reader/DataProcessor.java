package reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import entity.Record;
import entity.RecordContainer;

public class DataProcessor {
	public void DataTrimAndRestore() throws IOException{
		BufferedReader dataReader = null;
		ConfigReader reader = new ConfigReader();
		String FileName = reader.getConfigurationReader().getInputFileName();
		try {
			 dataReader = new BufferedReader(new FileReader(FileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		HashMap<String,List<Record>> map = new HashMap<String,List<Record>>();
		HashMap<String,Integer>    movieMap = new HashMap<String,Integer>();
		int movieId = 0;
		int count = 0;
		while((line=dataReader.readLine())!=null){
			count+=1;
			if(count%10000==0){
				System.out.println("finished: "+((float)count/(float)10000)+"%");
			}
			String []split = line.split(",");
			if (split.length<=1){
				continue;
			}else{
				String uid =   split[0];
				String mid =   split[1];
				String score = split[2];
				Record u = new Record(uid,mid,score);
				if(map.containsKey(uid)){
					map.get(uid).add(u);
				}else{
					map.put(uid, new ArrayList<Record>());
					map.get(uid).add(u);
				}
			}
		}
		System.out.println("======finished mapping job=================");
		dataReader.close();
		String writeContent = "";
		for(int i=0;i<reader.getConfigurationReader().getTotalUser();i++){
			System.out.println("finished "+i+" user");
			String uid = String.valueOf(i);
			for(Record u:map.get(uid)){
				String mid = u.getMovieId()+"";
				if(movieMap.containsKey(mid)){
					u.setMovieId(movieMap.get(mid));
				}else{
					u.setMovieId(movieId);
					movieMap.put(mid, movieId);
					movieId+=1;
				}
				writeContent+=u.toString()+"\n";
			}
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(reader.getConfigurationReader().getProcessedFileName()));
		bw.write(writeContent);
		bw.close();
		System.out.println("finished processed fileName start to write map file");
		writeContent = "";
		count = 0;
		Iterator<Entry<String, Integer>> it = movieMap.entrySet().iterator();
		while(it.hasNext()){
			count +=1;
			if(count%10000==0){
				System.out.println(count);
			}
			Entry<String, Integer> e = it.next();
			writeContent = writeContent+e.getKey()+","+e.getValue()+"\n";
		
		}
		bw = new BufferedWriter(new FileWriter(reader.getConfigurationReader().getProcessedMovieMapFileName()));
		bw.write(writeContent);
		bw.close();
		System.out.println("finished writing...");
		reader.close();
	}
	//load the data from the processed file..
	public RecordContainer DataLoader() throws IOException{
		ConfigReader reader = new ConfigReader();
		BufferedReader br = new BufferedReader(new FileReader(reader.getConfigurationReader().getProcessedFileName()));
		String line = "";
		HashSet<Integer> userSet = new HashSet<Integer>();
		HashSet<Integer> movieSet = new HashSet<Integer>();
		float maxScore = 1-Float.MAX_VALUE;
		float minScore = Float.MAX_VALUE;
		float averageScore = 0;
		int totalPeople = 0;
		List<Record> user = new ArrayList<Record>();
		System.out.println("start reading data");
		while((line=br.readLine())!=null){
			Record u = new Record(line);
			if(u.getScore()>maxScore){
				maxScore = u.getScore();
			}if(u.getScore()<minScore){
				minScore = u.getScore();
			}
			userSet.add(u.getId());
			movieSet.add(u.getMovieId());
			user.add(u);
			averageScore+=u.getScore();
			totalPeople+=1;
		}
		RecordContainer userdata = new RecordContainer(userSet.size(), movieSet.size(), maxScore, minScore, user,averageScore/(float)totalPeople);
		br.close();
		reader.close();
		return userdata;
	}
}
