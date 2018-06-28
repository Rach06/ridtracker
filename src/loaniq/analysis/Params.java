package loaniq.analysis;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import loaniq.utils.InjectLogger;

/**
 * 
 * @author lodonnell
 * Singleton class to manage parameters globally
 *
 */
@Singleton
public class Params {
	@InjectLogger Logger log;
	

	@Inject 
	public Params(){
		
	}

	public String schema = null;
	
	
	public DBConnect db_conn;
	
	public String filename = "data-analysis.txt";
	
	private File file;

	
	public void set_schema(String schema){
		this.schema = schema;
	}
	
	public String get_schema(){
		return this.schema;
	}
	
	public String getFileName(){
		return file.getName();
	}

	public void setFile(String file) {
		//TODO: back up any existing file
		this.file = new File(file);
	}
	
	public void writeToFile(List<String> lines){
	
		//Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
		//List<String> names = Lists.newArrayList("John", "Jane", "Adam", "Tom");
	    //File file = new File("test.txt");
	    CharSink sink = Files.asCharSink(file, Charsets.UTF_8, FileWriteMode.APPEND);
	    //Files.asCharSink(File, Charset, FileWriteMode...)
	    try {
			sink.writeLines(lines, " ");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
