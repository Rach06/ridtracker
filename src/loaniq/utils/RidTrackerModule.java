package loaniq.utils;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import loaniq.analysis.IAnalyzer;
import loaniq.analysis.ridanalyzer;


public class RidTrackerModule extends AbstractModule  {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		bindListener(Matchers.any(), new Log4JTypeListener());
		bind(IAnalyzer.class).to(ridanalyzer.class);
	}

}
