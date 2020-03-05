package uiauto.central;

import java.util.HashMap;
import java.util.Map;

import org.testng.ITestContext;

import uiauto.lib.enums.ConfigType;
import uiauto.lib.enums.PlatformType;
import uiauto.lib.utilities.Utility;
import uiauto.loader.DeviceLoader;

public class ContextConfiguration {
	private Map<ConfigType, String> configuration = new HashMap<ConfigType, String>();
	private String cName = null;
		
	private ContextConfiguration(String name, Map<ConfigType, String> centralConfig){
		this.setContextName(name);
		configuration.putAll(centralConfig);
	}
	
	public ContextConfiguration(Map<ConfigType, String> centralConfig){
		this("central", centralConfig);
	}
	
	public ContextConfiguration(ITestContext context, ContextConfiguration centralConfig) throws Exception{
		this.setContextName(context.getName());
		
		configuration.putAll(centralConfig.getConfigMap());
		configuration.put(ConfigType.CONTEXT_REPORT_DIR, Utility.joinPaths(this.getConfigValue(ConfigType.REPORT_DIR), context.getName()));
		configuration.put(ConfigType.SCREENSHOT_DIR, Utility.joinPaths(this.getConfigValue(ConfigType.CONTEXT_REPORT_DIR), "Screenshots"));
		
		Map<String,String> params = context.getCurrentXmlTest().getAllParameters();
		
		for(String key:params.keySet()){
			try{
				configuration.put(ConfigType.valueOf(key.replace("." , "_").toUpperCase()), params.get(key));
			} catch(IllegalArgumentException e) {
				throw new Exception(String.format("No enum constant in ConfigType.class for %s", key));
			}catch(Exception e){
				throw new Exception(String.format("Invalid testNG configuration file %s", e.getMessage()));
			}
		}
		if(configuration.get(ConfigType.PLATFORM_TYPE).equalsIgnoreCase(PlatformType.MOBILE.toString())) {
			String deviceDir = Utility.joinPaths(System.getProperty("user.dir"), "src", "test", "resources", "devices");
			configuration.putAll(DeviceLoader.getDeviceInformations(Utility.joinPaths(deviceDir, "Devices.json"),
					this.getConfigValue(ConfigType.DEVICENAME_TESTNG)));
		}
	}
	
	private Map<ConfigType, String> getConfigMap() {
		return this.configuration;
	}
	
	public String getConfigValue(ConfigType conf){
		return this.configuration.get(conf);
	}
		
	public String getContextName() {
		return cName;
	}

	private void setContextName(String cName) {
		this.cName = cName;
	}
}