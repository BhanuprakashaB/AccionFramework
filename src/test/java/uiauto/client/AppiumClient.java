package uiauto.client;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import uiauto.central.ContextConfiguration;
import uiauto.lib.enums.ConfigType;
import uiauto.lib.enums.MobilePlatformType;
import uiauto.lib.utilities.Utility;

public class AppiumClient implements Client{
	private AppiumDriver<MobileElement> driver = null;
	private ContextConfiguration conf = null;
	private DesiredCapabilities capabilities;
	private AppiumServer server;
	private MobilePlatformType platform;
		
	public AppiumClient(ContextConfiguration conf, AppiumServer server) throws Exception{
		this.server = server;
		this.conf = conf;
		this.platform = MobilePlatformType.valueOf(this.conf.getConfigValue(ConfigType.MOBILE_DEVICE_PLATFORM).toUpperCase());
	
		loadDriver();
		getDriver().manage().timeouts().implicitlyWait(Long.parseLong(conf.getConfigValue(ConfigType.WAIT_IMPLICIT)), TimeUnit.SECONDS);
		this.getDriver().get(this.conf.getConfigValue(ConfigType.SITE_URL));
	}

	private void loadDriver() throws Exception {
		processCommonConfig();
		try {
			switch(this.platform) {
			case ANDROID:
				processAndroidConfig();
				setDriver(new AndroidDriver<MobileElement>(this.server.getService(), capabilities));
				break;
			case IOS:
				processIOSConfig();
				setDriver(new IOSDriver<MobileElement>(this.server.getService(), capabilities));
				break;
			}
		} catch (Exception e) {
			throw new Exception("Failed to create Appium client session", e);				
		}
	}
		
	private void processCommonConfig(){
		capabilities = new DesiredCapabilities();
				
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, conf.getConfigValue(ConfigType.DEVICE_NAME));
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, Integer.parseInt(conf.getConfigValue(ConfigType.WAIT_EXPLICIT)));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,  conf.getConfigValue(ConfigType.PLATFORM_VERSION));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, conf.getConfigValue(ConfigType.MOBILE_DEVICE_PLATFORM));
		capabilities.setCapability(MobileCapabilityType.UDID, conf.getConfigValue(ConfigType.DEVICE_ID));
		capabilities.setCapability(CapabilityType.BROWSER_NAME, conf.getConfigValue(ConfigType.BROWSER_NAME));
	}
	
	private void processAndroidConfig(){
		capabilities.setCapability(AndroidMobileCapabilityType.CHROMEDRIVER_EXECUTABLE, conf.getConfigValue(ConfigType.CHROMEDRIVER_INSTALLED_PATH));
	}
	
	private void processIOSConfig(){
		capabilities.setCapability(IOSMobileCapabilityType.SHOW_XCODE_LOG, false);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
		capabilities.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, true);
		capabilities.setCapability(IOSMobileCapabilityType.START_IWDP, true);
	}
		
	public void quit() {
		if(this.getDriver() != null) {
			this.getDriver().quit();
		}
	}
	
	public AppiumDriver<MobileElement> getDriver() {
		return this.driver;
	}

	private void setDriver(AppiumDriver<MobileElement> driver) {
		this.driver = driver;
	}
	
	public MobilePlatformType getDevicePlatform() {
		return this.platform;
	}
	
	public String captureScreenForReport() throws IOException{
		String screenshotName = Utility.getDateTimeString("yyyy_MM_dd_HH_mm_ss_sss") + ".jpg";
		TakesScreenshot ts = (TakesScreenshot)getDriver();
		File tempLocation = ts.getScreenshotAs(OutputType.FILE);
		FileHandler.createDir(new File(conf.getConfigValue(ConfigType.SCREENSHOT_DIR)));
		String screenshotPath = Utility.joinPaths(
				conf.getConfigValue(ConfigType.SCREENSHOT_DIR), screenshotName);
		FileHandler.copy(tempLocation, new File(screenshotPath));
	
		//To add relative path for screenshots in html report
		String relativePath = Utility.joinPaths("./Screenshots/", screenshotName);
		return relativePath;
	}
}
