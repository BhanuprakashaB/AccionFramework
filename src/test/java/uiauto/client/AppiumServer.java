package uiauto.client;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.net.UrlChecker;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import uiauto.central.ContextConfiguration;
import uiauto.lib.enums.ConfigType;
import uiauto.lib.enums.MobilePlatformType;
import uiauto.lib.utilities.Utility;

public class AppiumServer {
	private AppiumDriverLocalService driverLocalService = null;
	private MobilePlatformType devicePlatform;
	private String UDID;
	private int timeout;
	
	public AppiumServer(ContextConfiguration conf) throws Exception {
		this.devicePlatform = MobilePlatformType.valueOf(conf.getConfigValue(ConfigType.MOBILE_DEVICE_PLATFORM).toUpperCase());
		this.UDID = conf.getConfigValue(ConfigType.DEVICE_ID);
		this.timeout = Integer.parseInt(conf.getConfigValue(ConfigType.WAIT_EXPLICIT));
		buildService(conf);
	}
	 
	public void start() throws Exception {
        this.driverLocalService.start();
   		waitUntilAppiumServerIsRunnning();
	}
	
 	public void stop() {
 		if(this.driverLocalService !=null) {
 			this.driverLocalService.stop();
 		}
 	}
 	
	public AppiumDriverLocalService getService() throws Exception{
		return this.driverLocalService;
	}
	
	private void buildService(ContextConfiguration conf) throws Exception {		
		String ipAddress = conf.getConfigValue(ConfigType.APPIUM_SERVER_IPADDRESS);
		String appiumJSPath = conf.getConfigValue(ConfigType.APPIUM_JSFILE_PATH);
		String nodePath = conf.getConfigValue(ConfigType.NODE_INSTALLED_PATH);

		File logFile = new File(Utility.joinPaths(
				conf.getConfigValue(ConfigType.CONTEXT_REPORT_DIR),
				"Appium_" + conf.getConfigValue(ConfigType.DEVICE_ID) + ".log"));
		
		AppiumServiceBuilder builder = new AppiumServiceBuilder()
		.withAppiumJS(new File(appiumJSPath))
		.usingDriverExecutable(new File(nodePath))
		.usingAnyFreePort()
		.withIPAddress(ipAddress)
		.withArgument(GeneralServerFlag.SESSION_OVERRIDE)
		.withLogFile(logFile)
		.withArgument(GeneralServerFlag.LOG_LEVEL, "error")
		.withArgument(GeneralServerFlag.LOCAL_TIMEZONE);
		
		switch(this.devicePlatform) {
		case ANDROID:
			addAndroidProperties(builder);
			break;
		case IOS:
			addIosProperties(builder);
			break;
		}

		this.driverLocalService = AppiumDriverLocalService.buildService(builder);			
	}
	   
	private void addAndroidProperties(AppiumServiceBuilder builder) throws Exception {
		builder.withArgument(AndroidServerFlag.BOOTSTRAP_PORT_NUMBER, Integer.toString(1234));
	}
	
	private void addIosProperties(AppiumServiceBuilder builder) {
		
	}
	
	private void waitUntilAppiumServerIsRunnning() throws Exception{
		final URL status = new URL(this.driverLocalService.getUrl() + "/sessions");
		try {
		    new UrlChecker().waitUntilAvailable(this.timeout, TimeUnit.SECONDS, status);
		} catch (UrlChecker.TimeoutException e) {
			throw new Exception(
					String.format("Unable to start Appium Server for deive ID: %s. Message: %s", this.UDID, e.getMessage()));
		}
	}
}
