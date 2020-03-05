package uiauto.extent;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import uiauto.central.ContextConfiguration;
import uiauto.lib.enums.ConfigType;
import uiauto.lib.utilities.Utility;

public class ExtentFactory {
	
	public static ExtentReports createReporter(ContextConfiguration cConf) {
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		
		ExtentHtmlReporter	 htmlReporter = new ExtentHtmlReporter(Utility.joinPaths(
				cConf.getConfigValue(ConfigType.CONTEXT_REPORT_DIR), 
				"TestReport_" + cConf.getContextName() +".html"));
		
				htmlReporter.loadXMLConfig(Utility.joinPaths(
				cConf.getConfigValue(ConfigType.RESOURCES_DIR), 
				"ExtentConfig",
				"ReportsConfig.xml"));
		
		ExtentReports extent = new ExtentReports();
		
		    extent.attachReporter(htmlReporter);
		    extent.setSystemInfo("Device OS", cConf.getConfigValue(ConfigType.MOBILE_DEVICE_PLATFORM));
		    extent.setSystemInfo("Execution Engineer", cConf.getConfigValue(ConfigType.EXECUTION_ENGR));
		    extent.setAnalysisStrategy(AnalysisStrategy.TEST);
		    
		    htmlReporter.config().setTheme(Theme.DARK);
		    htmlReporter.config().enableTimeline(false);
		    
	    return extent;
	}
}