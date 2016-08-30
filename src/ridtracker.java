import java.time.Duration;
import java.time.LocalDateTime;
import loaniq.analysis.Params;
import loaniq.analysis.ridanalyzer;

//updated in Mint Aug 30 test for Git
public class ridtracker {

	/**
	 * @param args
	 * @author lodonnell
	 */
	public static void main(String[] args) {
		Params params = Params.getParams();

		if (args.length  != 2 ){
			params.log("Usage: ridtracker <rid> <schema>");	
			return;
		}
		if ( args[0].length() != 8){
			params.log("you did not enter an 8 character loan iq rid (ex. E84OCBHJ");
		}
		else {
			String rid = args[0];
			params.set_schema(args[1]);
			LocalDateTime startPoint = LocalDateTime.now();   // The current date and time			
			ridanalyzer ra = new ridanalyzer();
			ra.analyze(rid);//API_01
			LocalDateTime endPoint = LocalDateTime.now();     // The current date and time
			Duration timeToExecute = Duration.between(endPoint, startPoint);
			params.log("Execution took "+timeToExecute.getSeconds()+ " seconds");
			params.log("finished...");
		}

	}

}
