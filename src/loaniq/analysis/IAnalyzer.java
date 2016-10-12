package loaniq.analysis;

import java.util.Vector;

public interface IAnalyzer {


	/**
	 * brute force scan of the database tables for the rid
	 * there are time implications to running this
	 */
	void analyze(String rid);

	/**
	 * Function builds sql to scan a database for a rid
	 * @param rid
	 */
	Vector<String> build_sql();

	
	public void prepareAnalyzer();

}