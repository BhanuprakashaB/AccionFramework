package uiauto.central;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.testng.ITestContext;

import com.aventstack.extentreports.ExtentReports;

import uiauto.extent.ExtentFactory;
import uiauto.lib.enums.ConfigType;
import uiauto.lib.utilities.Utility;


public enum SMASingleton {
	INSTANCE;
	private Map<String, ContextConfiguration> contextConfigs = new HashMap<String, ContextConfiguration>();
	private Map<String, ExtentReports> contextReporters = new HashMap<String, ExtentReports>();
	private static ContextConfiguration CENTRAL_CONF;
	
	public void init() throws Exception {
		loadCentralConfigFile();
	}
	
	public void loadCentralConfigFile() throws Exception{
		Properties config = new Properties();
		String propDir = Utility.joinPaths(
				System.getProperty("user.dir"), "src", "test", "resources", "lsac", "properties");
		
		FileInputStream fis = new FileInputStream(
				Utility.joinPaths(propDir, "config.properties"));
		config.load(fis);
		
		Map<ConfigType, String> cMap = new HashMap<ConfigType, String>();
		for(Entry<Object,Object> entry:config.entrySet()) {
			try {
				cMap.put(
						ConfigType.valueOf(((String)entry.getKey()).replace("." , "_").toUpperCase()),
						(String)entry.getValue());
			} catch(IllegalArgumentException e) {
				throw new Exception(String.format("No enum constant in ConfigType.class for %s", entry.getKey()));
			}
		}
		
		String reportFolder = Utility.getDateTimeString("yyyy_MM_dd_HH_mm_ss");
		String resourceDir = Utility.joinPaths(System.getProperty("user.dir"), "src", "test", "resources", "lsac");
		cMap.put(ConfigType.REPORT_DIR, Utility.joinPaths(
				System.getProperty("user.dir"), "target", "Reports", "lsac",
				reportFolder));
		cMap.put(ConfigType.RESOURCES_DIR, resourceDir);
		cMap.put(ConfigType.MAPS_DIR, Utility.joinPaths(resourceDir, "maps"));
		CENTRAL_CONF = new ContextConfiguration(cMap);
	}
	
	
	
	public synchronized void processTestContext(ITestContext context) throws Exception{
		String cName = context.getName();
		this.contextConfigs.put(cName, new ContextConfiguration(context, CENTRAL_CONF));
			
		if (!this.contextReporters.containsKey(cName)) {
			this.contextReporters.put(cName, ExtentFactory.createReporter(this.contextConfigs.get(cName)));
		}
	}
	
	public ExtentReports getReporter(ITestContext context) {
		return this.contextReporters.get(context.getName());
	}
	
	public ContextConfiguration getConfiguration(ITestContext context) {
		return this.contextConfigs.get(context.getName());
	}

	public ContextConfiguration getCentralConfig() {
		return CENTRAL_CONF;
	}
}
