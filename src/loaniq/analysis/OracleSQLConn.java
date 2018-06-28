/**
 * Models a DB Connection
 *
 */
package loaniq.analysis;

import java.sql.*;

/**
 * @author lodonnell
 *
 */

public class OracleSQLConn implements DBConnect {
	private Connection connection = null;
	
	private Statement stmt = null;
	private final String driverName = "oracle.jdbc.OracleDriver";
	private String jdbcUrl;
	private String username;
	private String password;
	
	
    /* (non-Javadoc)
	 * @see loaniq.analysis.DBConnect#getConnection()
	 */
    @Override
	public Connection getConnection(){
    	return connection;
    }
    
    /* (non-Javadoc)
	 * @see loaniq.analysis.DBConnect#getStatement()
	 */
    @Override
	public Statement getStatement(){
    	return stmt;
    }
    
    @Override
    public void setUsername(String user){
    	username = user;
    }
    
    @Override
    public void setPassword(String pwd){
    	password = pwd;
    }
    
    @Override
    public void setJdbcUrl(String url){
    	this.jdbcUrl = url;
    }
    
    @Override
    public void initializeConnection(){
		try{
			Class.forName(driverName);
			//String url = "jdbc:oracle:thin:@//LOANIQDUB3:1521/DUB3LIQ1";
			//String url = "jdbc:oracle:thin:@loaniqlod:1521:loaniq";

			connection = DriverManager.getConnection(jdbcUrl, username, password);
			stmt = connection.createStatement(
                   ResultSet.TYPE_SCROLL_INSENSITIVE,
                   ResultSet.CONCUR_UPDATABLE);
		}
		catch (ClassNotFoundException e){
			System.out.println("Error "+driverName+" Driver Class Not Found");
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
    }
	
	public OracleSQLConn(){
		
	}
	
	
	
	

}
