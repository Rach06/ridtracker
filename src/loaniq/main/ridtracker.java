package loaniq.main;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.logging.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import loaniq.analysis.IAnalyzer;
import loaniq.analysis.Params;
import loaniq.analysis.RidAnalyzer;
import loaniq.analysis.dummy.RidAnalyzerDummy;
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
		IAnalyzer ra = injector.getInstance( RidAnalyzer.class );
		//          ra = injector.getInstance( RidAnalyzerDummy.class );
		app.run(args, params, ra);

	}

	private void run(String[] args, Params params, IAnalyzer ra) {
		if (args.length  != 3 ){

			log.error("Usage: ridtracker <rid> <schema> <out file>");	
			return;
		}
		if ( args[0].length() != 8){
			log.error("you did not enter an 8 character loan iq rid (ex. E84OCBHJ");
		}
		else {
			String rid = args[0];
			params.set_schema(args[1]);
			params.setFile( args[2]);

			LocalDateTime startPoint = LocalDateTime.now();   // The current date and time	
			
            ra.prepareAnalyzer();
			ra.analyze(rid);//API_01
			
			LocalDateTime endPoint = LocalDateTime.now();     // The current date and time

			Duration timeToExecute = Duration.between(startPoint, endPoint );
			log.info("Execution took "+timeToExecute.getSeconds()+ " seconds");
			log.info("finished...see file "+params.getFileName());
		}
	}

}
