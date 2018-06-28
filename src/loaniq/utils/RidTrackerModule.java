package loaniq.utils;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;

import loaniq.analysis.DBConnect;
import loaniq.analysis.IAnalyzer;
import loaniq.analysis.OracleSQLConn;
import loaniq.analysis.dummy.RidAnalyzerDummy;


public class RidTrackerModule extends AbstractModule  {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		bindListener(Matchers.any(), new Log4JTypeListener());
		//bind(IAnalyzer.class).to(ridanalyzer.class);
		bind(IAnalyzer.class).to(RidAnalyzerDummy.class);
	}
	
	@Provides
	  DBConnect provideDBConnect() {
	    //DBConnect db_conn = new PostgresSQLConn();
		DBConnect db_conn = new OracleSQLConn();
	    //db_conn.setJdbcUrl("jdbc:postgresql:loaniq");
	    db_conn.setJdbcUrl("jdbc:oracle:thin:@//LOANIQDUB3:1521/DUB3LIQ1");
	    db_conn.setUsername("LIQ7300");
	    db_conn.setPassword("LIQ7300");
	    db_conn.initializeConnection();
	    return db_conn;
	  }

}
