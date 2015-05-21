package reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import model.User;

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
		HashMap<String,List<User>> map = new HashMap<String,List<User>>();
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
				User u = new User(uid,mid,score);
				if(movieMap.containsKey(mid)){
					u.setMovieId(movieMap.get(mid));
				}else{
					u.setMovieId(movieId);
					movieMap.put(mid, movieId);
					movieId+=1;
				}
				if(map.containsKey(uid)){
					map.get(uid).add(u);
				}else{
					map.put(uid, new ArrayList<User>());
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
			for(User u:map.get(uid)){
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
}
