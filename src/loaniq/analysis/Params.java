package loaniq.analysis;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * 
 * @author lodonnell
 * Singleton class to manage globals
 *
 */
public class Params {

	private Params() {
		db_conn = new DBConn();
		try {
			logfile = new FileWriter("c:\\temp\\ridtracker\\ridtracker.log");
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	private FileWriter logfile = null;
	public String schema = null;
	
	protected static Params _the_params;
	
	
	public static synchronized Params getParams(){
		if (_the_params == null){
			Params._the_params = new Params();
		}
		return _the_params;
	}
	
	public DBConn db_conn;
	
	public void log(String msg){
		//TODO write to file
		System.out.println(msg);
		
		try {
			final Date now = new Date();
			logfile.write(now.toString()+" "+msg+'\n');
			logfile.flush();
		}
		catch(IOException e){
			System.out.println("Error "+e.getMessage());
		}
		catch(NullPointerException e){
			System.out.println("Error writing to file "+e.getMessage());
		}

	}
	
	public void set_schema(String schema){
		this.schema = schema;
	}
	
	public String get_schema(){
		return this.schema;
	}
	

}
