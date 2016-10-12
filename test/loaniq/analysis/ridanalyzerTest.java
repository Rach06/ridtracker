package loaniq.analysis;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import loaniq.analysis.dummy.RidAnalyzerDummy;
import loaniq.utils.RidTrackerModule;

public class ridanalyzerTest {
	
	IAnalyzer ra  = new RidAnalyzerDummy(null);
	Vector<String> sql;

	@Before
	public void setUp() throws Exception {
		Injector injector = Guice.createInjector(new RidTrackerModule());
		IAnalyzer ra = injector.getInstance( RidAnalyzerDummy.class );
		
		ra.prepareAnalyzer();
		sql = ra.build_sql();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testPrepareAnalyzer() {
		fail("Not yet implemented");
	}

	@Test
	public final void testBuild_sql() {
		assert(sql.get(0).equals("SELECT DEA_PID_DEAL,DEA_PID_FACILITY,DEA_NME_DEAL FROM VLS_DEAL"));
	}

	@Test
	public final void testPopulate_tables() {
		fail("Not yet implemented");
	}

}
