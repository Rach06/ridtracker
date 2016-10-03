package loaniq.analysis;
/**
 * @author lodonnell
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;

import loaniq.utils.InjectLogger;

public class ridanalyzer implements IAnalyzer {
	
	private final Provider<DBConnect> db_connProvider;
	
	@InjectLogger Logger log;
	
	@Inject	Params param;

	private Vector<String> tables = null;
	private MultiValuedMap columns=null;
	private Vector<String> sql = null;
	MultiValuedMap<String, String> map = new ArrayListValuedHashMap<String, String>();
	/**
	 * 
	 */
	@Inject
	public ridanalyzer(Provider<DBConnect> dbConnection) {
		this.db_connProvider = dbConnection;
		tables = new Vector<String>();
		columns = new ArrayListValuedHashMap<String,String>();
		sql = new Vector<String>();
	}

	public void prepareAnalyzer(){
		//param.db_conn = new PostgresSQLConn(); //test only refactor to DI.
		param.db_conn = db_connProvider.get();
		populate_tables();
		populate_columns();
	}
	
	public void logTest(){
		log.debug("ridanalyser test");
	}
	
	/* (non-Javadoc)
	 * @see loaniq.analysis.IAnalyzer#analyze(java.lang.String)
	 */
	@Override
	public void analyze(String rid){
		if (rid.compareTo("")==0){
			return;
		}

		if (param.db_conn == null){
			log.error("Sorry. The db connection is null. I cant analyse the database");
			return;
		}
		
		print_tables();
		String table;
		String colname;
		Iterator<String> tab_itr = tables.iterator();
		String str_sql = new String("");
		boolean not_first_pass=false;

		while (tab_itr.hasNext()){
			table = (String)tab_itr.next();
			str_sql = "SELECT ";
			//get the associated cols
			//log.debug("columns.get("+table+")");
			ArrayList<String> alcol = (ArrayList<String>) (columns.get(table));
			if (alcol == null){
				//log.debug("Skipping table "+table+" :no cols found");
				continue;
			}
			//log.debug("table-->"+alcol.toString());
			Iterator<String> col_itr = alcol.iterator();
			while (col_itr != null && col_itr.hasNext()){
				colname = (String)col_itr.next();
				if (not_first_pass){
					str_sql+=",";
				}
				not_first_pass=true;
				str_sql += colname;
			}
			str_sql += " FROM "+table;
			log.debug(str_sql);

			try {
				ResultSet rs = param.db_conn.getStatement().executeQuery(str_sql);
				while (rs.next()){
					col_itr = alcol.iterator();
					while (col_itr.hasNext()){
						colname = (String)col_itr.next();
						String dbrid = rs.getString(colname);
						if (dbrid != null){
							if (rid.compareTo(dbrid)==0){
								log.debug("Rid "+rid+" exists in "+table+"."+colname);
							}
						}
					}
				}
			}
			catch (SQLException ex){
				log.error(str_sql);
				log.error(ex.getMessage());
			}
			not_first_pass=false;
		}
	}
	
	
	/* (non-Javadoc)
	 * @see loaniq.analysis.IAnalyzer#build_sql()
	 */
	@Override
	public void build_sql(){
		//Params param = Params.getParams();
		String table;
		String str_sql = new String("");
		boolean not_first_pass=false;
		String colname;
		
		Iterator<String> tab_itr = tables.iterator();

		while (tab_itr.hasNext()){
			table = (String)tab_itr.next();
			str_sql = "SELECT ";
			//get the associated cols
			//log.debug("columns.get("+table+")");
			ArrayList<String> alcol = (ArrayList<String>) (columns.get(table));
			//log.debug("table-->"+alcol.toString());
			Iterator<String> col_itr = alcol.iterator();
			while (col_itr.hasNext()){
				colname = (String)col_itr.next();
				if (not_first_pass){
					str_sql+=",";
				}
				not_first_pass=true;
				str_sql += colname;
			}
			str_sql += " FROM "+table;
			//log.debug(str_sql);
			sql.add(str_sql);
			not_first_pass=false;
		}
	}
	
	/**
	 * Function returns a count of all entities to examine.
	 * @return
	 */
	protected int entity_count(){
		//Params param = Params.getParams();
		String count = null;;
		String sql = new String("select count(VIEW_NAME) as cnt " +
				"from all_views where owner = 'LS2USER'");
		try {
		ResultSet rs = param.db_conn.getStatement().executeQuery(sql);
		while (rs.next()){
			count = rs.getString("cnt");
		}
		return (new Integer(count)).intValue();
		}
		catch (SQLException e){
			log.error(e.getMessage());
			return 0;
		}
	}
	
	protected int col_count(){
		//Params param = Params.getParams();
		String count = null;
		String sql = new String("select count(COLUMN_NAME) as cnt " +
				"from ALL_TAB_COLUMNS where OWNER='LS2USER'");
		try {
		ResultSet rs = param.db_conn.getStatement().executeQuery(sql);
		while (rs.next()){
			count = rs.getString("cnt");
		}
		return (new Integer(count)).intValue();
		}
		catch (SQLException e){
			log.error(e.getMessage());
			return 0;
		}		
	}
	
	/*sample sql: 
	 * select COLUMN_NAME from ALL_TAB_COLUMNS where table_name = 'TLS_DEAL'
	 *  and COLUMN_NAME='DEA_PID_DEAL' and OWNER='LS2USER'
	 */
	
	/**
	 * populate columns
	 */
	private void populate_columns(){
		//Params param = Params.getParams();
		log.debug("Populate_columns()");
		String name = null;
		/*String sql = new String("select TABLE_NAME,COLUMN_NAME from ALL_TAB_COLUMNS " +
				"where OWNER='LS2USER' and TABLE_NAME='TLS_DEAL'");*///debug 1 table
		
		String sql = new String(
				"select COL_TXT_NAME as COLUMN_NAME, COL_TXT_TBNAME as TABLE_NAME " +
				"from tls_syscolumns where col_txt_tbname like 'VLS_%'" 
				//+"where col_txt_tbname IN ('VLS_DEAL','VLS_FACILITY')"
				);
		
		try {
		ResultSet rs = param.db_conn.getStatement().executeQuery(sql);
			while (rs.next()){
				name = rs.getString("COLUMN_NAME");
				
				if (name.contains("ID_")||name.contains("ID_")){
					//if (name.compareTo("GLE_PID_DEAL")==0){
						//log.debug("Found GLE_ENTRY "+name);
					//	log.debug("columns.put("+rs.getString("TABLE_NAME")+","+name+")");
					//}
					
					columns.put(rs.getString("TABLE_NAME").trim(), name);
				}
			}
			//log.debug("columns.size()="+columns.values().size());
		}
		catch (SQLException e){
			log.error(e.getMessage());
		}
	}
	
	public void populate_tables(){
		//tables = new String[entity_count];
		/*
		 * In Oracle you get the tables as
		 * from all_tables or from all_views
           where owner='LS2USER' 
		 * 
		 * in DB2 use SYSIBM.SYSTABLES
		 * 
		 * 
		 */
		//Params param = Params.getParams();
		log.debug("populate_table()");
		String name = null;
		String sql = new String(
				/*"select VIEW_NAME from all_views where OWNER = '"+param.get_schema()+"'" */
				"select table_name from INFORMATION_SCHEMA.views where table_catalog = '"+param.get_schema()+"'" 
				);
		log.debug(sql);
		try {
		ResultSet rs = param.db_conn.getStatement().executeQuery(sql);
			while (rs.next()){
				name = rs.getString("table_name");
				tables.add(name);
			}
		}
		catch (SQLException e){
			log.error(sql);
			log.error(e.getMessage());
		}		
	}
	
	/**
	 * debug function print all views
	 */
	public void print_tables(){
		//IParameters param = Params.getParams();
		Iterator<String> itr = tables.iterator();
			log.debug("All_Views:");
			while (itr.hasNext()){
				log.debug(itr.next());
			}
			log.debug(tables.size()+" Tables in system");
	}
	
	public void print_cols(){
		//IParameters param = Params.getParams();
		Iterator<String> itr = columns.values().iterator();
		log.debug("All_Columns:");
		while(itr.hasNext()){
			log.debug(itr.next());
		}
		log.debug("Detected "+columns.values().size()+" Columns in system");
	}
	
	public void print_views(){
		//print_tables();
		print_cols();
	}
	
	public void print_sql(){
		//IParameters param = Params.getParams();
		Iterator<String> itr = sql.iterator();
		log.debug("All_SQL:");
		while(itr.hasNext()){
			log.debug(itr.next());
		}
		log.debug("There are "+sql.size()+" queries to scan for a rid");		
	}

}
