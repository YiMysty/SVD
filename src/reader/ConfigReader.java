package reader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
public class ConfigReader {
	static configurationReader reader;
	public configurationReader getConfigurationReader(){
		if(reader==null)
			reader = new configurationReader();
		return reader;
	}
	public void close(){
		reader.close();
	}

public class configurationReader{
	String FileName = "config.properties";
	Properties property;
	FileInputStream fis;
	public configurationReader(){
		property = new Properties();
		try {
			fis = new FileInputStream(FileName);
			property.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close(){
		try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getInputFileName(){
		return property.getProperty("filename");
	}
	
	public int getTotalUser(){
		return Integer.parseInt(property.getProperty("totaluser"));
	}
	public String getProcessedFileName(){
		return property.getProperty("processedFileName");
	}
	public String getProcessedMovieMapFileName(){
		return property.getProperty("processedMapFile");
	}
}
}

