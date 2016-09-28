package loaniq.utils;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Named;

import loaniq.analysis.DBConnect;
import loaniq.analysis.IAnalyzer;
import loaniq.analysis.PostgresSQLConn;
import loaniq.analysis.ridanalyzer;


public class RidTrackerModule extends AbstractModule  {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		bindListener(Matchers.any(), new Log4JTypeListener());
		bind(IAnalyzer.class).to(ridanalyzer.class);
	}
	
	@Provides
	  DBConnect provideDBConnect() {
	    DBConnect db_conn = new PostgresSQLConn();
	    db_conn.setJdbcUrl("jdbc:postgresql:loaniq");
	    db_conn.setUsername("ls2user");
	    db_conn.setPassword("password");
	    db_conn.initializeConnection();
	    return db_conn;
	  }

}
