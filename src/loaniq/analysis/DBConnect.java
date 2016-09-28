package loaniq.analysis;

import java.sql.Connection;
import java.sql.Statement;

public interface DBConnect {

	Connection getConnection();

	Statement getStatement();

	void setJdbcUrl(String url);

	void setUsername(String user);

	void setPassword(String pwd);

	void initializeConnection();

}