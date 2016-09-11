package loaniq.analysis;
/**
 * @author lodonnell
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

public class ridanalyzer {
	

	private Vector<String> tables = null;
	private MultiValuedMap columns=null;
	private Vector<String> sql = null;
	MultiValuedMap<String, String> map = new ArrayListValuedHashMap<String, String>();
	/**
	 * 
	 */
	public ridanalyzer() {
		tables = new Vector<String>();
		columns = new ArrayListValuedHashMap<String,String>();
		sql = new Vector<String>();
		populate_tables();
		populate_columns();
	}
	
	/**
	 * brute force scan of the database tables for the rid
	 * there are time implications to running this
	 */
	public void analyze(String rid){
		if (rid.compareTo("")==0){
			return;
		}
		Params param = Params.getParams();
		print_tables();
		String table;
		String colname;
		Iterator<String> tab_itr = tables.iterator();
		String str_sql = new String("");
		boolean not_first_pass=false;

		while (tab_itr.hasNext()){
			table = (String)tab_itr.next();
			/*if ("VLS_GL_ENTRY".compareTo(table)==0){
				param.log("2Found "+table);
			}*/
			str_sql = "SELECT ";
			//get the associated cols
			//param.log("columns.get("+table+")");
			ArrayList<String> alcol = (ArrayList<String>) (columns.get(table));
			if (alcol == null){
				//param.log("Skipping table "+table+" :no cols found");
				continue;
			}
			//param.log("table-->"+alcol.toString());
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
			param.log(str_sql);

			try {
				ResultSet rs = param.db_conn.stmt.executeQuery(str_sql);
				while (rs.next()){
					col_itr = alcol.iterator();
					while (col_itr.hasNext()){
						colname = (String)col_itr.next();
						String dbrid = rs.getString(colname);
						if (dbrid != null){
							if (rid.compareTo(dbrid)==0){
								param.log("Rid "+rid+" exists in "+table+"."+colname);
							}
						}
					}
				}
			}
			catch (SQLException ex){
				param.log(str_sql);
				param.log(ex.getMessage());
			}
			not_first_pass=false;
		}
	}
	
	
	/**
	 * Function builds sql to scan a database for a rid
	 * @param rid
	 */
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
			//param.log("columns.get("+table+")");
			ArrayList<String> alcol = (ArrayList<String>) (columns.get(table));
			//param.log("table-->"+alcol.toString());
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
			//param.log(str_sql);
			sql.add(str_sql);
			not_first_pass=false;
		}
	}
	
	/**
	 * Function returns a count of all entities to examine.
	 * @return
	 */
	protected int entity_count(){
		Params param = Params.getParams();
		String count = null;;
		String sql = new String("select count(VIEW_NAME) as cnt " +
				"from all_views where owner = 'LS2USER'");
		try {
		ResultSet rs = param.db_conn.stmt.executeQuery(sql);
		while (rs.next()){
			count = rs.getString("cnt");
		}
		return (new Integer(count)).intValue();
		}
		catch (SQLException e){
			param.log(e.getMessage());
			return 0;
		}
	}
	
	protected int col_count(){
		Params param = Params.getParams();
		String count = null;
		String sql = new String("select count(COLUMN_NAME) as cnt " +
				"from ALL_TAB_COLUMNS where OWNER='LS2USER'");
		try {
		ResultSet rs = param.db_conn.stmt.executeQuery(sql);
		while (rs.next()){
			count = rs.getString("cnt");
		}
		return (new Integer(count)).intValue();
		}
		catch (SQLException e){
			param.log(e.getMessage());
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
		Params param = Params.getParams();
		param.log("Populate_columns()");
		String name = null;
		/*String sql = new String("select TABLE_NAME,COLUMN_NAME from ALL_TAB_COLUMNS " +
				"where OWNER='LS2USER' and TABLE_NAME='TLS_DEAL'");*///debug 1 table
		
		String sql = new String(
				"select COL_TXT_NAME as COLUMN_NAME, COL_TXT_TBNAME as TABLE_NAME " +
				"from tls_syscolumns where col_txt_tbname like 'VLS_%'" 
				//+"where col_txt_tbname IN ('VLS_DEAL','VLS_FACILITY')"
				);
		
		try {
		ResultSet rs = param.db_conn.stmt.executeQuery(sql);
			while (rs.next()){
				name = rs.getString("COLUMN_NAME");
				
				if (name.contains("ID_")||name.contains("ID_")){
					//if (name.compareTo("GLE_PID_DEAL")==0){
						//param.log("Found GLE_ENTRY "+name);
					//	param.log("columns.put("+rs.getString("TABLE_NAME")+","+name+")");
					//}
					
					columns.put(rs.getString("TABLE_NAME").trim(), name);
				}
			}
			//param.log("columns.size()="+columns.values().size());
		}
		catch (SQLException e){
			param.log(e.getMessage());
		}
	}
	
	/**
	 * function populates an array of tables to scan
	 */
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
		Params param = Params.getParams();
		param.log("populate_table()");
		String name = null;
		String sql = new String("select VIEW_NAME from all_views where OWNER = '"+
				param.get_schema()+"'" 
				//+" and view_name IN ('VLS_DEAL','VLS_FACILITY')"
				);
		param.log(sql);
		try {
		ResultSet rs = param.db_conn.stmt.executeQuery(sql);
			while (rs.next()){
				name = rs.getString("VIEW_NAME");
				tables.add(name);
			}
		}
		catch (SQLException e){
			param.log(sql);
			param.log(e.getMessage());
		}		
	}
	
	/**
	 * debug function print all views
	 */
	public void print_tables(){
		Params param = Params.getParams();
		Iterator<String> itr = tables.iterator();
			param.log("All_Views:");
			while (itr.hasNext()){
				param.log(itr.next());
			}
			param.log(tables.size()+" Tables in system");
	}
	
	public void print_cols(){
		Params param = Params.getParams();
		Iterator<String> itr = columns.values().iterator();
		param.log("All_Columns:");
		while(itr.hasNext()){
			param.log(itr.next());
		}
		param.log("Detected "+columns.values().size()+" Columns in system");
	}
	
	public void print_views(){
		//print_tables();
		print_cols();
	}
	
	public void print_sql(){
		Params param = Params.getParams();
		Iterator<String> itr = sql.iterator();
		param.log("All_SQL:");
		while(itr.hasNext()){
			param.log(itr.next());
		}
		param.log("There are "+sql.size()+" queries to scan for a rid");		
	}

}
