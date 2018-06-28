package loaniq.analysis.dummy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;

import loaniq.analysis.DBConnect;
import loaniq.analysis.IAnalyzer;
import loaniq.analysis.RidAnalyzer;
import loaniq.utils.InjectLogger;

public class RidAnalyzerDummy extends RidAnalyzer implements IAnalyzer {
	
	@InjectLogger Logger log;

	@Inject
	public RidAnalyzerDummy(Provider<DBConnect> dbConnection) {
		super(dbConnection);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void prepareAnalyzer(){
		populate_tables();
		populate_columns();
	}

	@Override
	public void analyze(String rid) {
		// TODO Auto-generated method stub
		Vector<String>  sql = build_sql();

	}
	
	public void populate_tables(){
		tables.add("VLS_DEAL");
		tables.add("VLS_FACILITY");
		tables.add("VLS_OUTSTANDING");
	}
	
	private void populate_columns(){
		
		log.debug("Populate_columns()");
		String name = null;
	
		columns.put("VLS_DEAL", "DEA_PID_DEAL");
		columns.put("VLS_DEAL", "DEA_PID_FACILITY");
		columns.put("VLS_DEAL", "DEA_NME_DEAL");
		columns.put("VLS_FACILITY", "FAC_NME_FACILITY");
		columns.put("VLS_FACILITY", "FAC_PID_FACILITY");
		columns.put("VLS_FACILITY", "FAC_RID_OUTSTANDNG");
		columns.put("VLS_OUTSTANDING", "OST_RID_OUTSTANDNG");
		columns.put("VLS_OUTSTANDING", "OST_NME_ALIAS");
		columns.put("VLS_OUTSTANDING", "OST_PID_FACILITY");
	}

}
