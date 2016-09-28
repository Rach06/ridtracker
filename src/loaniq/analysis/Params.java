package loaniq.analysis;

import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import loaniq.utils.InjectLogger;

/**
 * 
 * @author lodonnell
 * Singleton class to manage globals
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

	
	public void set_schema(String schema){
		this.schema = schema;
	}
	
	public String get_schema(){
		return this.schema;
	}
	

}
