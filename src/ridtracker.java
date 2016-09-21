import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.logging.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import loaniq.analysis.IAnalyzer;
import loaniq.analysis.Params;
import loaniq.analysis.ridanalyzer;
import loaniq.utils.InjectLogger;
import loaniq.utils.RidTrackerModule;

//updated in Mint Aug 30 test for Git
public class ridtracker {

	/**
	 * @param args
	 * @author lodonnell
	 */
	
	@InjectLogger Logger log;
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new RidTrackerModule());
		//Logger log = injector.getInstance(Logger.class);
		ridtracker app = injector.getInstance(ridtracker.class);
		Params params = injector.getInstance( Params.class );
		IAnalyzer ra = injector.getInstance( ridanalyzer.class );
		//Params paraa.pms = Params.getParams();
		//ra.prepareAnalyzer();
		app.run(args, params, ra);

	}

	private void run(String[] args, Params params, IAnalyzer ra) {
		if (args.length  != 2 ){
			log.error("Usage: ridtracker <rid> <schema>");	
			return;
		}
		if ( args[0].length() != 8){
			log.error("you did not enter an 8 character loan iq rid (ex. E84OCBHJ");
		}
		else {
			String rid = args[0];
			params.set_schema(args[1]);
			//TODO: Jodatime
			LocalDateTime startPoint = LocalDateTime.now();   // The current date and time			
            ra.prepareAnalyzer();
			ra.analyze(rid);//API_01
			
			LocalDateTime endPoint = LocalDateTime.now();     // The current date and time
			Duration timeToExecute = Duration.between(endPoint, startPoint);
			log.info("Execution took "+timeToExecute.getSeconds()+ " seconds");
			log.info("finished...");
		}
	}

}
