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

public class DBConn {
	protected Connection connection = null;
	
	public Statement stmt = null;
	
	public DBConn(){
		//create a connection during initialization
		//DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());

		String driverName = "oracle.jdbc.driver.OracleDriver";
		try{
			Class.forName(driverName);
			String url = "jdbc:oracle:thin:@//LOANIQDUB3:1521/DUB3LIQ1";
			//String url = "jdbc:oracle:thin:@loaniqlod:1521:loaniq";
			String username = "LIQ7300";   
			String password = "LIQ7300";
			connection = DriverManager.getConnection(url, username, password);
			stmt = connection.createStatement(
                   ResultSet.TYPE_SCROLL_INSENSITIVE,
                   ResultSet.CONCUR_UPDATABLE);
		}
		catch (ClassNotFoundException e){
			System.out.println("Error Oracle Driver Class Not Found");
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
		
		
		
	}
	
	
	
	

}
